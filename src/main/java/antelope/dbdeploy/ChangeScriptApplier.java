package antelope.dbdeploy;

import java.util.List;

import antelope.dbdeploy.scripts.ChangeScript;

public interface ChangeScriptApplier {
	void apply(List<ChangeScript> changeScript);
}
