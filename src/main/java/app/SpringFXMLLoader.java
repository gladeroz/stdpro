package app;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.controller.ConfigurationController;
import app.controller.MainController;
import app.model.ConfigCollection;
import enums.AreaPosition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.Yaml;

/**
 * Will load the FXML hierarchy as specified in the load method and register
 * Spring as the FXML Controller Factory. Allows Spring and Java FX to coexist
 * once the Spring Application context has been bootstrapped.
 */
@Component
public class SpringFXMLLoader {
	@Autowired ApplicationContext context;

	private ConfigCollection config;
	private ConfigurationController cc;

	public void loadAnchorPane(BorderPane rootLayout, String fxmlPath, AreaPosition zone) throws IOException {      
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		loader.setController(cc);
		AnchorPane overview = loader.load();
		switch (zone) {
		case CENTER:
			rootLayout.setCenter(overview);
			break;
		case LEFT:
			rootLayout.setLeft(overview);
			break;
		case RIGHT:
			rootLayout.setRight(overview);
			break;
		default:
			break;
		}
	}

	public BorderPane loadBorderPane(String fxmlPath, Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		loader.setControllerFactory(context::getBean); //Spring now FXML Controller Factory

		MainController mc = new MainController();
		mc.setPrimaryStage(primaryStage);

		config = new ConfigCollection();

		config = Yaml.parseConfig();

		cc = new ConfigurationController();
		cc.setConfig(config);
		cc.setStage(primaryStage);
		mc.setConfigurationController(cc);

		loader.setController(mc);

		return loader.load();
	}
}
