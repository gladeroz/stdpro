package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import enums.LogLevel;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ConfigCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

public class Yaml {

	private static String path = "configuration/config.json";

	private static ConfigCollection config;

	public static ConfigCollection parseConfig() {
		return parseConfig(path, true);
	}

	public static ConfigCollection parseConfig(String p, Boolean ressources) {
		if(p == null) {
			Logger.print(LogLevel.ERROR, "Valeur invalide (path: "+ p + ")");
			return null;
		}

		setPath(p);

		Logger.print(LogLevel.INFO, "Debut de la lecture de la configuration "+ path);

		try{
			ConfigCollection cc = null;
			if(ressources) {
				cc = Mapper.getInstance().readValue(Yaml.class.getResource(path), ConfigCollection.class);
			} else {
				cc = Mapper.getInstance().readValue(new File(path), ConfigCollection.class);
			}
			Logger.print(LogLevel.INFO, "Fin de la lecture de la configuration "+ path);
			setConfig(cc);
			return cc;
		}catch (IOException e){
			System.err.println(e);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void printConfig(ConfigCollection config) {
		Logger.print(LogLevel.INFO, "Affichage de la configuration");

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(Mapper.getInstance().writeValueAsString(config));
			Logger.print(LogLevel.INFO, gson.toJson(je));
		} catch (JsonProcessingException e) {
			System.err.println(e);
		}

		Logger.print(LogLevel.INFO, "Fin de l'affichage de la configuration");
	}

	public static void saveConfig(ConfigCollection cc, Stage stage) throws FileNotFoundException, URISyntaxException {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(stage);
		PrintWriter pwriter = new PrintWriter(file.getPath());

		if (file != null) {
			try {
				ObjectWriter writer = Mapper.getInstance().writer(new DefaultPrettyPrinter());
				writer.writeValue(pwriter, cc);
			} catch (IOException e) {
				System.err.println(e);
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
