package antelope.workflow.cmd;

import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import antelope.workflow.PreStartProcessEntity;

public class SetPreStartVariablesCmd implements Command<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	protected String business_sid;
	  protected Map<String, ? extends Object> variables;

	public SetPreStartVariablesCmd(String business_sid, Map<String, ? extends Object> variables) {
		this.business_sid = business_sid;
		this.variables = variables;
	}

	public Void execute(CommandContext commandContext) {
		if (business_sid == null) {
			return null;
		}
		PreStartProcessEntity entity = new PreStartProcessEntity(business_sid);
		entity.setVariables(variables);
		return null;
	}
}
