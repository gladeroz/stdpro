package controlleur;

import java.io.IOException;

import enums.AreaPosition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.ConfigCollection;
import utils.Yaml;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private MainController mc;
	private ConfigurationController cc;
	private ConfigCollection config;

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("STD Pro");
		
		config = new ConfigCollection();
		
		mc = new MainController(primaryStage);

		initRootLayout();
		
		config = Yaml.parseConfig();
		
		cc = new ConfigurationController(config, primaryStage);
		mc.setConfigurationController(cc);
		
		addArea("fxml/AffichageLayout.fxml", AreaPosition.CENTER);

		primaryStage.setResizable(Boolean.TRUE);
		primaryStage.setMinWidth(1280.0);
		primaryStage.setMinHeight(800.0);
	}

	private void addArea(String overviewStr, AreaPosition zone) throws IOException {
		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(overviewStr));
		loader.setController(cc);
		AnchorPane overview = (AnchorPane) loader.load();
		
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

	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("fxml/RootLayout.fxml"));
			loader.setController(mc);
			rootLayout = (BorderPane) loader.load();
			
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
