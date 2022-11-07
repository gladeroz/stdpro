package app.config;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;

import org.slf4j.Logger;

import app.SpringFXMLLoader;
import enums.AreaPosition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Manages switching Scenes on the Primary Stage
 */
public class StageManager {

	private static final Logger LOG = getLogger(StageManager.class);
	private final Stage primaryStage;
	private final SpringFXMLLoader springFXMLLoader;
	private BorderPane rootLayout;

	public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
		this.springFXMLLoader = springFXMLLoader;
		this.primaryStage = stage;
	}

	public void initScene() throws IOException {
		rootLayout = springFXMLLoader.loadBorderPane("fxml/RootLayout.fxml", primaryStage);
		springFXMLLoader.loadAnchorPane(rootLayout, "fxml/AffichageLayout.fxml", AreaPosition.CENTER);
		show(rootLayout, "STD Pro");
	}

	private void show(final Parent rootnode, String title) {
		// Show the scene containing the root layout.
		primaryStage.setScene(new Scene(rootLayout));
		primaryStage.show();

		primaryStage.setResizable(Boolean.TRUE);
		primaryStage.setMinWidth(1280.0);
		primaryStage.setMinHeight(800.0);
	}

	public SpringFXMLLoader getSpringFXMLLoader() {
		return springFXMLLoader;
	}
}