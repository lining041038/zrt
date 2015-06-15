package antelope.interfaces.components.supportclasses;

/**
 * 表单域
 * @author lining
 * @since 2012-7-14
 */
public class FormField {
	public String label;
	public String field;
	
	/**
	 * 当为枚举类型时，此属性保存获取枚举类型的数据域
	 */
	public String enumXmlField;
	
	/**验证信息
		name: 'required', // 表单验证名称，用于属性值的名称如 required=true
		name: 'maxlength2', // 由于maxlength已经被浏览器的默认行为占用，此处使用maxlength2来代替
		name: 'num',
		name: 'int',
		name: 'after',
		name: 'notBefore',
	 */
	public String validate = "";
	public FormField(String field, String label) {
		this.label = label;
		this.field = field;
	}
	
	public FormField(String field, String label, String validate) {
		this.label = label;
		this.field = field;
		this.validate = validate;
	}
	public FormField(String field, String label, String validate, String enumXmlField) {
		this.label = label;
		this.field = field;
		this.validate = validate;
		this.enumXmlField = enumXmlField;
	}
}
