package controlleur;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import enums.Job;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import utils.Yaml;

public class MainController implements Initializable {
	@FXML private MenuItem closeMenu;

	@FXML private MenuItem clearMenu;

	@FXML private MenuItem printConfig;

	@FXML private CheckMenuItem tCompt;
	@FXML private CheckMenuItem tSuffix;
	@FXML private CheckMenuItem tOcr;
	@FXML private CheckMenuItem tSendMail;
	@FXML private CheckMenuItem tExtractZone;
	@FXML private CheckMenuItem tCodeBarre;

	private Stage primaryStage;

	private ConfigurationController cc;

	public MainController(Stage primaryStage) {
		super();
		this.primaryStage = primaryStage;
	}

	@Override
	public void initialize(URL a, ResourceBundle b) {
		closeMenu.setOnAction(this::handleCloseMenuAction);
		clearMenu.setOnAction(this::handleClearMenuAction);
		printConfig.setOnAction(this::handlePrintConfigMenuAction);

		primaryStage.setOnCloseRequest(evt -> {
			// prevent window from closing
			evt.consume();

			// execute own shutdown procedure
			shutdown();
		});
	}

	@FXML
	private void handleCloseMenuAction(ActionEvent event){
		ExecutorService executor = ConfigurationController.getExecutor();
		if(executor != null) {
			executor.shutdownNow();
		}

		((Stage)primaryStage.getScene().getWindow()).close();
		System.exit(0);
	}

	private void shutdown() {
		handleCloseMenuAction(null);
	}

	@FXML
	private void handleClearMenuAction(ActionEvent event){
		cc.LogArea.clear();
	}

	@FXML
	private void handlePrintConfigMenuAction(ActionEvent event){
		Yaml.printConfig(Yaml.getConfig());
	}

	@FXML
	private void validateTraitement(ActionEvent event){
		tCompt.setSelected(false);
		tSuffix.setSelected(false);
		tOcr.setSelected(false);
		tSendMail.setSelected(false);
		tExtractZone.setSelected(false);
		tCodeBarre.setSelected(false);

		CheckMenuItem item = (CheckMenuItem)event.getSource();

		if(item.equals(tCompt)) {
			cc.setJob(Job.COMPTAGE_PDF);
		} else if (item.equals(tSuffix)) {
			cc.setJob(Job.SUFFIX_PREFIX);
		} else if (item.equals(tOcr)) {
			cc.setJob(Job.OCR);
		} else if(item.equals(tSendMail)) {
			cc.setJob(Job.SEND_MAIL);
		} else if(item.equals(tExtractZone)) {
			cc.setJob(Job.EXTRACT_ZONE);
		} else if(item.equals(tCodeBarre)) {
			cc.setJob(Job.CODE_BARRE);
		}

		item.setSelected(true);
	}

	public void setConfigurationController(ConfigurationController cc) {
		this.cc = cc;
	}
}
