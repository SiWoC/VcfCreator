package nl.siwoc.navigation.utils;

public class ParseException extends Exception {

	public ParseException(String message) {
		super(message);
	}

	/**
	 * if parsing a file seems to succeed but the content is not supported
	 * like
	 * - multiple routes
	 * - Grid waypoints in stead of LatLon
	 */
	private static final long serialVersionUID = 4817192529272470286L;

	
}
