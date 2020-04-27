package utils;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javafx.scene.control.TextArea;

public class LoggerArea extends WriterAppender {
	
	public static TextArea logTextArea;
	
	@Override
    public void append(LoggingEvent loggingEvent) {
        final String logMessage = this.layout.format( loggingEvent );
        
        if(logTextArea != null) {
        	logTextArea.appendText( logMessage );
        } else {
        	System.out.println( logMessage );
        }
    }
}
