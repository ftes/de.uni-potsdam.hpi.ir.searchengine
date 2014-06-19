package de.hpi.krestel.mySearchEngine.util;

public class Log {	
	public static enum Level {
		DEBUG, ERROR
	}
	
	public static Level LEVEL = Level.DEBUG;
	
	public static void log(Level level, String msg) {
		if (level.compareTo(LEVEL) >= 0) {
			System.out.println(msg);
		}
	}
}
