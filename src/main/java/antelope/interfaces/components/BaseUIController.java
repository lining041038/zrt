package antelope.interfaces.components;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import antelope.interfaces.OptionsProvider;
import antelope.springmvc.BaseController;
import antelope.utils.JSONObject;

public abstract class BaseUIController extends BaseController implements OptionsProvider{
	
	public abstract Object getOptions(HttpServletRequest req) throws SQLException, Exception;
	
	@RequestMapping("/getOptionsByRequest")
	public void getOptionsByRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		BaseUIOptions opts = (BaseUIOptions) getOptions(req);
		opts.initOptions();
		print(new JSONObject(opts, true), res);
	}
}
