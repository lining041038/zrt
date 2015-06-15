package antelope.dbdeploy;


import java.util.List;

public interface AppliedChangesProvider {
	List<Long> getAppliedChanges();
}
