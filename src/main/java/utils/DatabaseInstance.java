package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DatabaseInstance {

	// private field that refers to the object
	private static Connection connexion;

	private DatabaseInstance() throws SQLException, JsonParseException, JsonMappingException, IOException {
		// constructor of the SingletonExample class
		initDatabase();
	}

	public static Connection getInstance() throws JsonParseException, JsonMappingException, IOException {
		// write code that allows us to create only one object
		// access the object as per our need
		if(connexion == null) {
			try {
				initDatabase();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connexion;
	}

	public static PreparedStatement getStatement(String query) throws SQLException, JsonParseException, JsonMappingException, IOException {
		return getInstance().prepareStatement(query);
	}

	private static void initDatabase() throws SQLException, JsonParseException, JsonMappingException, IOException {
		DatabaseConfig config = DatabaseConfig.parseFile("configuration/database.yml");
		connexion = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
	}

	public static void resetConnexion() {
		if(connexion != null) {
			try {
				connexion.close();
			} catch (SQLException e) {}
		}
		connexion = null;
	}
}
