package controlleur;

import java.net.URL;
import java.util.ResourceBundle;

import enums.Job;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import utils.Traitement;
import utils.Yaml;

public class MainController implements Initializable {
	@FXML private MenuItem closeMenu;
	
	@FXML private MenuItem clearMenu;
	
	@FXML private MenuItem printConfig;
	
	@FXML private CheckMenuItem tCompt;
	
	@FXML private CheckMenuItem tSuffix;
	
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
	}
	
	@FXML
	private void handleCloseMenuAction(ActionEvent event){
		((Stage)primaryStage.getScene().getWindow()).close();
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
		
		CheckMenuItem item = (CheckMenuItem)event.getSource();
		
		if(item.equals(tCompt)) {
			Traitement.setAction(Job.COMPTAGE_PDF);
		} else if (item.equals(tSuffix)) {
			Traitement.setAction(Job.SUFFIX_PREFIX);
		}
		
		item.setSelected(true);
	}
	
	public void setConfigurationController(ConfigurationController cc) {
		this.cc = cc;
	}
}
