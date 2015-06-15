package antelope.interfaces.components.supportclasses;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import antelope.interfaces.components.BaseUIController;
import antelope.interfaces.components.BaseUIOptions;

public class MultipleDatagridsOptions extends BaseUIOptions{

	public MultipleDatagridsOptions(BaseUIController controller) {
		super(controller);
	}

	public Map<String, SingleDatagridOptionsExtended> singleDatagridOptionMap = new LinkedHashMap<String, SingleDatagridOptionsExtended>();
	
	@Override
	public void initOptions() {
		Set<Entry<String, SingleDatagridOptionsExtended>> entries = singleDatagridOptionMap.entrySet();
		for (Entry<String, SingleDatagridOptionsExtended> entry : entries) {
			entry.getValue().initOptions();
		}
	}
}
