package com.special.camunda.process;

import com.alibaba.fastjson.JSON;
import com.special.camunda.request.ProcessRequest;
import com.special.camunda.request.TaskRequest;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.webapp.impl.security.auth.AuthenticationService;
import org.camunda.bpm.webapp.impl.security.auth.Authentications;
import org.camunda.bpm.webapp.impl.security.auth.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimpleProcessHandlerImpl implements SimpleProcessHandler {
	private static final Logger logger = LoggerFactory.getLogger(SimpleProcessHandlerImpl.class);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private ProcessEngine processEngine;

	@Autowired
	private IdentityService identityService;

	@Override
	public List<TaskDto> simpleInitProcess(ProcessRequest processRequest) throws Exception {
		String processInstanceId = null;
		List<TaskDto> resultList = new ArrayList<TaskDto>();

		// 流程初始化
		ProcessInstance processInstance = null;
		// 流程初始化
		if (StringUtils.isNotBlank(processRequest.getProcessDefKey())) {
			processInstance = runtimeService.startProcessInstanceByKey(processRequest.getProcessDefKey());
		} else {
			processInstance = runtimeService.startProcessInstanceById(processRequest.getProcessDefId());
		}
		// 创建成功
		if (processInstance != null && StringUtils.isNotBlank(processInstance.getId())) {
			processInstanceId = processInstance.getId();
			resultList = simpleGetTasks(processInstanceId);
		} else {
			throw new Exception("创建流程实例失败：");
		}
		return resultList;
	}

	@Override
	public List<HistoricTaskInstance> simpleGetHisTasks(String processDefKey) throws Exception {
		List<HistoricTaskInstance> resultList = new ArrayList<HistoricTaskInstance>();
		resultList = historyService.createHistoricTaskInstanceQuery().processDefinitionKey(processDefKey).list();
		return resultList;
	}

	@Override
	public List<TaskDto> simpleGetTaskIds(String processDefKey) throws Exception {
		List<TaskDto> resultList = new ArrayList<TaskDto>();
		List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(processDefKey).list();
		for (Task task : taskList) {
			TaskDto dto = new TaskDto();
			dto = TaskDto.fromEntity(task);
			resultList.add(dto);
		}
		return resultList;
	}

	@Override
	public List<TaskDto> simpleApproveProcess(TaskRequest taskRequest, HttpServletRequest request)
			throws Exception {
		AuthenticationService authenticationService = new AuthenticationService();
		String engineName = processEngine.getName();
		UserAuthentication authentication = (UserAuthentication) authenticationService.createAuthenticate(engineName,
				taskRequest.getUserId(), null, null);
		logger.info("authentication--------->" + authentication.getName());
		Authentications.revalidateSession(request, authentication);
		identityService.setAuthenticatedUserId(authentication.getName());
		List<TaskDto> taskList = new ArrayList<TaskDto>();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables = taskRequest.getVariables();
		Map<String, Object> localVariables = new HashMap<String, Object>();
		localVariables = taskRequest.getLocalVariables();

		runtimeService.setVariables(taskRequest.getProcessInstId(), localVariables);
		if(StringUtils.isNoneBlank(taskRequest.getToActId())){
			taskService.complete(taskRequest.getTaskId(), variables);
			taskService.createComment(taskRequest.getTaskId(), taskRequest.getProcessInstId(), "重新提交流程");
			ActivityInstance tree = runtimeService.getActivityInstance(taskRequest.getProcessInstId());
			runtimeService
				.createProcessInstanceModification(taskRequest.getProcessInstId())
				.cancelActivityInstance(getInstanceIdForActivity(tree, tree.getActivityId()))
				.startBeforeActivity(taskRequest.getToActId())
				.execute();
		}else{
			taskService.createComment(taskRequest.getTaskId(), taskRequest.getProcessInstId(), "审批流程");
			taskService.complete(taskRequest.getTaskId(), variables);
			
		}
		
		taskList = simpleGetTasks(taskRequest.getProcessInstId());
		if (taskList != null && taskList.size() == 1) {
			taskService.setAssignee(taskList.get(0).getId(), taskRequest.getNextUserId());
		}
		taskList = simpleGetTasks(taskRequest.getProcessInstId());
		return taskList;
	}

	@Override
	public void mandualApproveProcess(TaskRequest taskRequest) throws Exception {
		Map<String, Object> variables = taskRequest.getVariables();
		variables.put("userId","all");
		List<TaskDto> taskDtoList = simpleGetTasks(taskRequest.getProcessInstId());
		String taskId = "";
		for (TaskDto taskDto : taskDtoList) {
			if(taskRequest.getProcessInstId().equals(taskDto.getProcessInstanceId())){
				taskId = taskDto.getId();
			}
		}
		taskService.createComment(taskId, taskRequest.getProcessInstId(), "执行手工流程");
		taskService.complete(taskId, variables);

	}

	public List<TaskDto> simpleGetTasks(String processInstId) throws Exception {
		List<TaskDto> resultList = new ArrayList<TaskDto>();
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstId).list();
		for (Task task : taskList) {
			TaskDto dto =  TaskDto.fromEntity(task);
			resultList.add(dto);
		}
		return resultList;
	}

	public String getInstanceIdForActivity(ActivityInstance activityInstance, String activityId) {
		ActivityInstance instance = getChildInstanceForActivity(activityInstance, activityId);
		if (instance != null) {
			return instance.getId();
		}
		return null;
	}

	public ActivityInstance getChildInstanceForActivity(ActivityInstance activityInstance, String activityId) {
		if (activityId.equals(activityInstance.getActivityId())) {
			return activityInstance;
		}

		for (ActivityInstance childInstance : activityInstance.getChildActivityInstances()) {
			ActivityInstance instance = getChildInstanceForActivity(childInstance, activityId);
			if (instance != null) {
				return instance;
			}
		}

		return null;
	}
}
