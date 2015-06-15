package antelope.dbdeploy.scripts;


import java.util.Collections;
import java.util.List;

import antelope.dbdeploy.AvailableChangeScriptsProvider;
import antelope.dbdeploy.exceptions.DuplicateChangeScriptException;


public class ChangeScriptRepository implements AvailableChangeScriptsProvider {

	private final List<ChangeScript> scripts;

	@SuppressWarnings("unchecked")
	public ChangeScriptRepository(List<ChangeScript> scripts) throws DuplicateChangeScriptException {
		this.scripts = scripts;

		Collections.sort(this.scripts);
		
		checkForDuplicateIds(scripts);
	}
	
	private void checkForDuplicateIds(List<ChangeScript> scripts) throws DuplicateChangeScriptException {
		long lastId = -1;
		
		for (ChangeScript script : scripts) {
			if (script.getId() == lastId) {
				throw new DuplicateChangeScriptException("There is more than one change script with number " + lastId);
			}
			
			lastId = script.getId();
		}
		
	}

	public List<ChangeScript> getOrderedListOfDoChangeScripts() {
		return Collections.unmodifiableList(scripts);
	}

	public List<ChangeScript> getAvailableChangeScripts() {
		return getOrderedListOfDoChangeScripts();
	}
}
