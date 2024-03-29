package com.special.camunda.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * 流程提交参数对象
 * @author xusen
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="PscCommonProcessRequest-流程提交参数对象")
public class ProcessRequest {
	@ApiModelProperty(value="流程定义ID")
	private String	processDefId;
	
	@ApiModelProperty(value="流程定义Key")
	private String	processDefKey;
	
	public String getProcessDefKey() {
		return processDefKey;
	}
	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}
	@ApiModelProperty(value="启动者")
	private String	starter;
	
	public String getStarter() {
		return starter;
	}
	public void setStarter(String starter) {
		this.starter = starter;
	}
	@ApiModelProperty(value="流程标题")
	private	String	title;
	
	@ApiModelProperty(value="外部业务系统数据主键标识值")
	private String processBusinessKey;
	
	@ApiModelProperty(value="流程变量键值对")
	private Map<String, Object> variables;
   
	public static String REJECT_TO_START ="1";
	public static String REJECT_TO_LAST ="2";
	public static String REJECT_TO_TARGET ="3";
   
	public static String WITHDRAW_TO_START ="1";
	public static String WITHDRAW_TO_TARGET ="2";
	
	public String getProcessDefId() {
		return processDefId;
	}
	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProcessBusinessKey() {
		return processBusinessKey;
	}
	public void setProcessBusinessKey(String processBusinessKey) {
		this.processBusinessKey = processBusinessKey;
	}
	public Map<String, Object> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
}
