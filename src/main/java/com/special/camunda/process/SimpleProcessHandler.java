package com.special.camunda.process;

import com.special.camunda.request.ProcessRequest;
import com.special.camunda.request.TaskRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/simpleProcessHandler")
public interface SimpleProcessHandler {

	@ApiOperation(value = "流程初始化", notes = "流程初始化")
	@RequestMapping(value = "/simpleInitProcess", method = RequestMethod.POST)
	public List<TaskDto> simpleInitProcess(@RequestBody ProcessRequest processRequest) throws Exception;

	@ApiOperation(value = "查找历史任务", notes = "根据流程定义Key查找历史任务记录")
	@RequestMapping(value = "/simpleGetHisTasks/{processDefKey}", method = RequestMethod.POST)
	public List<HistoricTaskInstance> simpleGetHisTasks(
            @ApiParam(name = "processDefKey", value = "流程定义Key", required = true) @PathVariable String processDefKey)
			throws Exception;

	@ApiOperation(value = "查找运行任务", notes = "根据流程定义Key查找运行任务")
	@RequestMapping(value = "/simpleGetTaskIds/{processDefKey}", method = RequestMethod.POST)
	public List<TaskDto> simpleGetTaskIds(
            @ApiParam(name = "processDefKey", value = "流程定义Key", required = true) @PathVariable String processDefKey)
			throws Exception;

	@ApiOperation(value = "流程审批", notes = "流程审批")
	@RequestMapping(value = "/simpleApproveProcess", method = RequestMethod.POST)
	public List<TaskDto>  simpleApproveProcess(@RequestBody TaskRequest taskRequest, HttpServletRequest request) throws Exception;

}
