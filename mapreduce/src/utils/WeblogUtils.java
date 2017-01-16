package utils;

import java.io.File;

/**
 * WeblogUtils
 * @author gengwuli
 *
 */
public class WeblogUtils {
	
	/**
	 * Delete a file folder
	 * @param file
	 */
	public static void deleteDir(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			for (File subfile : file.listFiles()) {
				deleteDir(subfile);
			}
		}
		file.delete();
	}
}
