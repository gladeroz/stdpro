package utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import enums.LogLevel;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Logger {

	private static TextArea consoleLogscreen;

	private static Logger logger;

	public static Logger getInstance(){
		if(logger == null) {
			logger = new Logger();
		}
		return logger;
	}

	public void initLogger(TextArea consoleLogscreen) {		
		Console console = new Console();
		PrintStream ps = new PrintStream(console, true);
		System.setOut(ps);
		System.setErr(ps);
		System.err.flush();
		System.out.flush();
	}

	private class Console extends OutputStream {
		@Override
		public void write(final int i) throws IOException {
			consoleLogscreen.appendText(String.valueOf((char) i));
		}
	}

	public static void print(LogLevel level, Object mesg) {
		if(consoleLogscreen == null) return;
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					switch (level) {
					case INFO:
						info(mesg);
						break;
					case ERROR:
						error(mesg);
						break;
					case DEBUG:
						debug(mesg);
						break;
					case WARNING:
						warning(mesg);
						break;
					default:
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void warning(Object mesg) throws InterruptedException {
		consoleLogscreen.appendText("[WARNING] " + mesg + "\n");
	}

	private static void error(Object mesg) throws InterruptedException {
		consoleLogscreen.appendText("[ERROR] " + mesg + "\n");
	}

	private static void info(Object mesg) throws InterruptedException {
		consoleLogscreen.appendText("[INFO] " + mesg + "\n");
	}

	private static void debug(Object mesg) throws InterruptedException {
		consoleLogscreen.appendText("[DEBUG] " + mesg + "\n");
	}

	public TextArea getConsoleLogscreen() {
		return consoleLogscreen;
	}

	public static void setConsoleLogscreen(TextArea consoleLogscreen) {
		Logger.consoleLogscreen = consoleLogscreen;
	}
}
