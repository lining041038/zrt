package antelope.workflow;

import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import antelope.springmvc.SpringUtils;
import antelope.workflow.cmd.GetPreStartVariablesCmd;
import antelope.workflow.cmd.RemovePreStartVariablesCmd;
import antelope.workflow.cmd.SetPreStartVariablesCmd;


@Service
public class WorkflowExtendedService extends ServiceImpl {
	
	public Map<String, Object> getPreStartProcessVariables(String business_sid) {
		RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) SpringUtils.getBean(RuntimeService.class);
	    return runtimeService.getCommandExecutor().execute(new GetPreStartVariablesCmd<Map<String, Object>>(business_sid));
	}
	
	public void setPreStartProcessVariables(String business_sid, Map<String, ? extends Object> variables) {
		RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) SpringUtils.getBean(RuntimeService.class);
	    runtimeService.getCommandExecutor().execute(new SetPreStartVariablesCmd(business_sid, variables));
	}
	
	public void removePreStartProcessVariables(String business_sid) {
		RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) SpringUtils.getBean(RuntimeService.class);
	    runtimeService.getCommandExecutor().execute(new RemovePreStartVariablesCmd(business_sid));
	}
}
