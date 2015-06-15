package antelope.dbdeploy.scripts;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antelope.dbdeploy.exceptions.UnrecognisedFilenameException;

public class FilenameParser {
	private final Pattern pattern;

	public FilenameParser() {
		pattern = Pattern.compile("(\\d+).*");
	}

	public long extractIdFromFilename(String filename) throws UnrecognisedFilenameException {
		Matcher matches = pattern.matcher(filename);
		if (!matches.matches() || matches.groupCount() != 1)
			throw new UnrecognisedFilenameException("Could not extract a change script number from filename: " + filename);
		
		return Long.parseLong(matches.group(1));
	 }

}
