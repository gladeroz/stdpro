package app;


import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import app.config.StageManager;
import app.service.MainRepository;
import javafx.application.Application;
import javafx.stage.Stage;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JfxApp extends Application {

	protected ConfigurableApplicationContext springContext;
	protected StageManager stageManager;

	@Override
	public void init() {
		springContext = springBootApplicationContext();
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		stageManager = springContext.getBean(StageManager.class, primaryStage);
		MainRepository.setSpringContext(springContext);
		stageManager.initScene();
	}

	@Override
	public void stop() {
		springContext.close();
	}

	public static void main(String[] args) {
		launch(JfxApp.class, args);
	}

	private ConfigurableApplicationContext springBootApplicationContext() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(JfxApp.class);
		String[] args = getParameters().getRaw().toArray(String[]::new);
		return builder.run(args);
	}
}
