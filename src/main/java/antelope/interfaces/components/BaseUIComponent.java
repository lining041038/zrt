package antelope.interfaces.components;

import javax.servlet.http.HttpServletRequest;

import antelope.interfaces.OptionsProvider;
import antelope.springmvc.BaseComponent;

public abstract class BaseUIComponent extends BaseComponent implements OptionsProvider{
	
	public abstract Object getOptions(HttpServletRequest req);
}
