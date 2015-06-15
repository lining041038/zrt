package antelope.workflow.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import antelope.workflow.PreStartProcessEntity;

public class RemovePreStartVariablesCmd implements Command<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	protected String business_sid;

	public RemovePreStartVariablesCmd(String business_sid) {
		this.business_sid = business_sid;
	}

	public Void execute(CommandContext commandContext) {
		if (business_sid == null) {
			return null;
		}
		PreStartProcessEntity entity = new PreStartProcessEntity(business_sid);
		entity.removeVariables();
		return null;
	}
}
