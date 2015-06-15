package antelope.interfaces.components;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.components.supportclasses.StatsChartgridOptions;


/**
 * 统计图表通用组件可选项
 * @author lining
 * @since 2012-9-11
 */
public abstract class StatsChartgrid extends BaseUIController {
	
	
	@RequestMapping("/getData")
	public abstract void getData(String key, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception;
	
	
	@Override
	public abstract StatsChartgridOptions getOptions(HttpServletRequest req);
	
}
