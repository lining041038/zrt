package antelope.workflow.cmd;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import antelope.workflow.PreStartProcessEntity;

public class GetPreStartVariablesCmd<T> implements Command<Map<String, Object>>, Serializable {

	private static final long serialVersionUID = 1L;
	protected String business_sid;
	protected Collection<String> variableNames;
	protected boolean isLocal;

	public GetPreStartVariablesCmd(String business_sid) {
		this.business_sid = business_sid;
	}

	public Map<String, Object> execute(CommandContext commandContext) {
		if (business_sid == null) {
			return new HashMap<String, Object>();
		}
		PreStartProcessEntity entity = new PreStartProcessEntity(business_sid);
		return entity.getVariables();
	}
}
