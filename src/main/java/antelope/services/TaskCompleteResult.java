package antelope.services;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;


public class TaskCompleteResult {
	private ProcessInstance processInstance;
	private String prevTaskid;
	private List<Task> nexttasks;
	private List<Task> copytotasks = new ArrayList<Task>();
	TaskCompleteResult(String prevTaskId, ProcessInstance processInstance, List<Task> nexttasks, List<Task> copytotasks) {
		this.prevTaskid = prevTaskId ;
		this.processInstance = processInstance;
		this.nexttasks = nexttasks;
		this.copytotasks = copytotasks;
	}
	TaskCompleteResult(String prevTaskId, ProcessInstance processInstance, List<Task> nexttasks) {
		this.prevTaskid = prevTaskId ;
		this.processInstance = processInstance;
		this.nexttasks = nexttasks;
	}
	
	public String getProc_inst_id_() {
		return processInstance.getProcessInstanceId();
	}
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	public List<Task> getNexttasks() {
		return nexttasks;
	}

	public String getPrevTaskid() {
		return prevTaskid;
	}

	void setNexttasks(List<Task> nexttasks) {
		this.nexttasks = nexttasks;
	}
	public List<Task> getCopytotasks() {
		if (copytotasks == null)
			return new ArrayList<Task>();
		return copytotasks;
	}
}
