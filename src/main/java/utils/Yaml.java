package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ConfigCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

public class Yaml {
	
	private static Logger logger = Logger.getLogger(Yaml.class);
	private static String path = "configuration/config.json";
	private static ConfigCollection config;

	public static ConfigCollection parseConfig() {
		return parseConfig(path, true);
	}

	public static ConfigCollection parseConfig(String p, Boolean ressources) {
		if(p == null) {
			logger.error("Valeur invalide (path: "+ p + ")");
			return null;
		}

		setPath(p);

		logger.info("Debut de la lecture de la configuration "+ path);

		try{
			ConfigCollection cc = null;
			if(ressources) {
				cc = JsonService.getInstance().readValue(Yaml.class.getResource(path), ConfigCollection.class);
			} else {
				cc = JsonService.getInstance().readValue(new File(path), ConfigCollection.class);
			}
			logger.info("Fin de la lecture de la configuration "+ path);
			setConfig(cc);
			return cc;
		}catch (IOException e){
			logger.error(e);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void printConfig(ConfigCollection config) {
		logger.info("Affichage de la configuration");

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(JsonService.getInstance().writeValueAsString(config));
			logger.info(gson.toJson(je));
		} catch (JsonProcessingException e) {
			logger.error(e);
		}

		logger.info("Fin de l'affichage de la configuration");
	}

	public static void saveConfig(ConfigCollection cc, Stage stage) throws FileNotFoundException, URISyntaxException, UnsupportedEncodingException {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(stage);
		
		if(file == null) return;
		
		PrintWriter pwriter = new PrintWriter(file.getPath(), java.nio.charset.StandardCharsets.UTF_8.name());

		if (file != null) {
			try {
				ObjectWriter writer = JsonService.getInstance().writer(new DefaultPrettyPrinter());
				writer.writeValue(pwriter, cc);
			} catch (IOException e) {
				logger.error(e);
			} 
		}
	}

	public static ConfigCollection loadConfig(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);

		if (file != null) {
			return parseConfig(file.getAbsolutePath(), false);
		} else {
			return parseConfig();
		}
	}

	public static ConfigCollection getConfig() {
		return config;
	}

	public static void setConfig(ConfigCollection config) {
		Yaml.config = config;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		Yaml.path = path;
	}
}
