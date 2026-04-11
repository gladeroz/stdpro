package utils;

import java.io.Serializable;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import javafx.scene.control.TextArea;

@Plugin(name = "TextAreaAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class LoggerService extends AbstractAppender {

	public static TextArea logTextArea;

	protected LoggerService(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout, false, null);
	}

	@PluginFactory
	public static LoggerService createAppender(
			@PluginAttribute("name") String name,
			@PluginElement("Filter") Filter filter,
			@PluginElement("Layout") Layout<? extends Serializable> layout) {
		return new LoggerService(name, filter, layout);
	}

	@Override
	public void append(LogEvent event) {
		final String logMessage = new String(getLayout().toByteArray(event));

		javafx.application.Platform.runLater(() -> {
			if (logTextArea != null) {
				logTextArea.appendText(logMessage);
			} else {
				System.out.print(logMessage);
			}
		});
	}
}
