package antelope.dbdeploy;


import java.util.List;

import antelope.dbdeploy.scripts.ChangeScript;

public interface AvailableChangeScriptsProvider {
	List<ChangeScript> getAvailableChangeScripts();
}
