package controlleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.ResourceBundle;

import enums.Job;
import enums.LogLevel;
import enums.Options;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.ConfigCollection;
import model.ConfigItem;
import utils.Logger;
import utils.Traitement;
import utils.Yaml;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ConfigurationController implements Initializable {

	@FXML private Accordion configuration;

	@FXML private Button executeTraitement;
	@FXML private Button exportConfButton;
	@FXML private Button importConfButton;

	@FXML private GridPane gridComptage;
	@FXML private GridPane gridSuffixe;

	@FXML public TextArea LogArea;

	private ConfigCollection config;

	private Stage stage;

	public ConfigurationController(ConfigCollection config, Stage stage) {
		super();
		this.config = config;
		this.stage = stage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Logger.setConsoleLogscreen(LogArea);
		
		createSectionAccordion(Job.COMPTAGE_PDF, gridComptage, config.getConfigComptagePdf());
		createSectionAccordion(Job.SUFFIX_PREFIX, gridSuffixe, config.getConfigSuffixPrefix());

		executeTraitement.setOnAction(this::handleExecuteButtonAction);
		exportConfButton.setOnAction(this::defaultSaveButtonAction);
		importConfButton.setOnAction(this::defaultLoadButtonAction);

		configuration.autosize();
	}  

	@FXML
	private void handleExecuteButtonAction(ActionEvent event){
		Traitement.setConfig(config);
		Traitement.doJob();
	}

	@FXML
	private void handleClearMenuAction(ActionEvent event){
		LogArea.clear();
	}

	@FXML
	private void defaultLoadButtonAction(ActionEvent event) {
		ConfigCollection cc = Yaml.loadConfig(stage);

		createSectionAccordion(Job.COMPTAGE_PDF, gridComptage, cc.getConfigComptagePdf());
		createSectionAccordion(Job.SUFFIX_PREFIX, gridSuffixe, cc.getConfigSuffixPrefix());
		
		config = cc;
	}

	@FXML
	private void defaultSaveButtonAction(ActionEvent event){
		Logger.print(LogLevel.DEBUG, "SAUVEGARDE EN COURS DE "+ Job.COMPTAGE_PDF);

		Collection<ConfigItem> cc = config.getSpecificConfig(Job.COMPTAGE_PDF);

		for(Node node : gridComptage.getChildren()) {
			if(node instanceof TextField) {
				String[] id = ((TextField) node).getId().split("#");

				for(ConfigItem c : cc) {
					String value = ((TextField) node).getText();
					if(c.getId().equals(Integer.valueOf(id[2])) && !c.getValue().equals(value)) {
						Logger.print(LogLevel.DEBUG, "[Nom de la configuration : " + c.getLabel() + " | Ancienne valeur : "+ c.getValue() + " | Nouvelle valeur : " + value + "]");
						c.setValue(value);
					}
				}
			}
		}

		config.setConfigComptagePdf(cc);

		Logger.print(LogLevel.DEBUG, "FIN DE LA SAUVEGARDE DE "+ Job.COMPTAGE_PDF + "\n");

		Logger.print(LogLevel.DEBUG, "SAUVEGARDE EN COURS DE "+ Job.SUFFIX_PREFIX);

		cc = config.getSpecificConfig(Job.SUFFIX_PREFIX);

		for(Node node : gridSuffixe.getChildren()) {
			if(node instanceof TextField) {
				String[] id = ((TextField) node).getId().split("#");

				for(ConfigItem c : cc) {
					String value = ((TextField) node).getText();
					if(c.getId().equals(Integer.valueOf(id[2])) && !c.getValue().equals(value)) {
						Logger.print(LogLevel.DEBUG, "[Nom de la configuration : " + c.getLabel() + " | Ancienne valeur : "+ c.getValue() + " | Nouvelle valeur : " + value + "]");
						c.setValue(value);
					}
				}
			}
		}

		config.setConfigComptagePdf(cc);

		Logger.print(LogLevel.DEBUG, "FIN DE LA SAUVEGARDE DE "+ Job.SUFFIX_PREFIX + "\n");

		try {
			Yaml.saveConfig(config, stage);
		} catch (FileNotFoundException | URISyntaxException e) {
			System.err.println(e);
		}
	}

	private void createSectionAccordion(Job job, GridPane grid, Collection<ConfigItem> children) {
		
		grid.getChildren().clear();
		
		for (ConfigItem child : children) {
		
			Label l = new Label((child.getMandatory()) ? child.getLabel() : child.getLabel()+"*");
			GridPane.setConstraints(l, 0, child.getId());
			grid.getChildren().add(l);

			switch (child.getType()) {
			case PATH:
				//Defining the Name text field
				final TextField name = new TextField();
				name.setPromptText((child.getMandatory()) ? child.getLabel() : "[Optionnel] " + child.getLabel());
				//name.setPrefColumnCount(25);
				name.setText(child.getValue());
				name.setId("INPUT#" + child.getConfigName() + "#" + child.getId());

				GridPane.setConstraints(name, 1, child.getId());
				grid.getChildren().add(name);

				//Defining the Submit button
				Button spath = new Button("...");
				GridPane.setConstraints(spath, 2, child.getId());
				grid.getChildren().add(spath);

				spath.setOnAction(e -> createFileChooserEvent(name, child));
				break;

			case DATE_TIME :
				DatePicker f = createDatePickerEvent();
				GridPane.setConstraints(f, 1, child.getId());
				grid.getChildren().add(f);
				break;

			default:
				//Defining the Name text field
				final TextField def = new TextField();
				def.setPromptText(child.getLabel());
				//name.setPrefColumnCount(25);
				def.setText(child.getValue());
				def.setId("INPUT#" + child.getConfigName() + "#" + child.getId());

				GridPane.setConstraints(def, 1, child.getId());
				grid.getChildren().add(def);

				break;
			}
		}
	}

	private DatePicker createDatePickerEvent() {
		// Setting a particular date value in the class constructor
		DatePicker checkInDatePicker = new DatePicker(LocalDate.of(1998, 10, 8));

		// Setting a particular date value by using the setValue method
		checkInDatePicker.setValue(LocalDate.of(1998, 10, 8));

		// Setting the minimum date available in the calendar
		checkInDatePicker.setValue(LocalDate.MIN);

		// Setting the maximum date available in the calendar
		checkInDatePicker.setValue(LocalDate.MAX);

		// Setting the current date
		checkInDatePicker.setValue(LocalDate.now());

		checkInDatePicker.autosize();

		return checkInDatePicker;
	}

	private void createFileChooserEvent(TextField name, ConfigItem child) {
		if(child.getOptions().contains(Options.FILECHOOSER_DIRECTORY)) {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Open directory for pdf treatment");

			File directoy = directoryChooser.showDialog(stage);
			if (directoy != null) {
				name.setText(directoy.getAbsolutePath());
			}
		}

		if(child.getOptions().contains(Options.FILECHOOSER_FILE)) {
			FileChooser fileChooser = new FileChooser();
			File file = null;
			
			if (child.getOptions().contains(Options.FILECHOOSER_SAVE)) {
				file = fileChooser.showSaveDialog(stage);
			}else {
				file = fileChooser.showOpenDialog(stage);
			}
			
			if (file != null) {
				name.setText(file.getAbsolutePath());
				child.setValue(file.getAbsolutePath());
			}
		}
	}
}