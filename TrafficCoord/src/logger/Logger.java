package logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

	private static Logger instance;
	private final boolean VERBOSE = false;

	public Logger() {
	}

	public static synchronized Logger getInstance() {
		if (Logger.instance == null) {
			Logger.instance = new Logger();
		}
		return Logger.instance;
	}

	public void log(String msg) {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		System.out.println(sdf.format(cal.getTime()) + "\t\t" + msg);

	}

	public void logLine() {

		System.out.println("======================================================================================================");
		System.out.println();
	}

	public void write(String msg) {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if (VERBOSE) {
			System.out.println(sdf.format(cal.getTime()) + "\t->\t" + msg);
		}
	}

	public void read(String msg) {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if (VERBOSE) {
			System.out.println(sdf.format(cal.getTime()) + "\t<-\t" + msg);
		}
	}
}
