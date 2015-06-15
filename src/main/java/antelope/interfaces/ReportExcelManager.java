package antelope.interfaces;

import java.io.IOException;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.WriteException;

/**
 * 报表excel管理者接口，处理报表excel相关操作
 * @author lining
 */
public interface ReportExcelManager {
	
	/**
	 * 获取excel导入模板
	 * @return
	 */
	public void exportExcelTemplate(OutputStream os) throws IOException, WriteException;
	
	/**
	 * 对excel模板进行验证, 返回验证结果是否正确
	 * @param workbook
	 * @return
	 */
	public boolean validateExcel(Workbook workbook);
}
