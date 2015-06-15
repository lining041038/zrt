package antelope.workflow;

import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.impl.persistence.entity.VariableScopeImpl;


/**
 * 流程启动前变量存储服务
 * 非线程安全服务，每次需要使用新实例
 * @author lining
 * @since 2012-11-27
 */
public class PreStartProcessEntity extends VariableScopeImpl {

	private static final long serialVersionUID = 1L;
	
	private String business_id;

	public PreStartProcessEntity(String business_id) {
		this.business_id = business_id;
	}

	@Override
	protected List<VariableInstanceEntity> loadVariableInstances() {
	    return Context
	    	      .getCommandContext()
	    	      .getVariableInstanceManager()
	    	      .findVariableInstancesByExecutionId("A" + business_id);
	}

	/**
	 * 获取父级变量范围,不需要实现
	 */
	@Override
	protected VariableScopeImpl getParentVariableScope() {
		return null;
	}

	@Override
	protected void initializeVariableInstanceBackPointer(VariableInstanceEntity variableInstance) {
		variableInstance.setExecutionId("A" + business_id);
		variableInstance.setProcessInstanceId("A" + business_id);
	}

}
