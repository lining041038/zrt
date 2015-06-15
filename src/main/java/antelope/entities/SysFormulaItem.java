package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统公式表，储存由公式编辑器生成的公式，一般用于富文本编辑框添加公式图片
 * @author lining
 * @since 2013-12-5
 */
@Entity
@Table(name="SYS_FORMULA_LATEX")
public class SysFormulaItem {
	
	@Id
	public String sid;
	public String latex;
}
