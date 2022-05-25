package utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

public class DatabaseConfig{
	private String url;

	private String user;

	private String password;
	
	public DatabaseConfig(String url, String user, String password) {
        this.setUrl(url);
        this.setUser(user);
        this.setPassword(password);
    }
	
	public DatabaseConfig() {}

	public static DatabaseConfig parseFile(String path) throws JsonParseException, JsonMappingException, IOException{
		// Instantiating a new ObjectMapper as a YAMLFactory
		ObjectMapper om = new ObjectMapper(new YAMLFactory());

		// Mapping the employee from the YAML file to the Employee class
		DatabaseConfig config = om.readValue(DatabaseConfig.class.getResource(path), DatabaseConfig.class);
		return config;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
