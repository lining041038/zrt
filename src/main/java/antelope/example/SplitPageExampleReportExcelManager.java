package antelope.example;

import java.io.IOException;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.springframework.stereotype.Component;

import antelope.interfaces.ReportExcelManager;


/**
 * 
 * @author lining
 */
@Component("splitpageexcelmanager")
public class SplitPageExampleReportExcelManager implements ReportExcelManager {

	@Override
	public void exportExcelTemplate(OutputStream os) throws IOException, WriteException {
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		WritableSheet sheet = workbook.createSheet("fsd", 0);
		sheet.addCell(new Label(0,0,"地方地方撒"));
		workbook.write();
		workbook.close();
	}

	@Override
	public boolean validateExcel(Workbook workbook) {
		System.out.println(workbook.getSheet(0).getCell(0, 0));
		// return true;
		return true;
	}

}
