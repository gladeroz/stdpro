package app;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import app.config.StageManager;
import app.repository.gims.TraitementGimsRepository;
import app.repository.odr.TraitementOdrRepository;
import app.service.MainRepository;
import javafx.application.Application;
import javafx.stage.Stage;

@SpringBootApplication
public class JfxApp extends Application {

	@Autowired TraitementGimsRepository gims;

	@Autowired TraitementOdrRepository odr;

	protected ConfigurableApplicationContext springContext;
	protected StageManager stageManager;

	@Override
	public void init() throws Exception {
		springContext = springBootApplicationContext();
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		stageManager = springContext.getBean(StageManager.class, primaryStage);
		MainRepository.setSpringContext(springContext);
		stageManager.initScene();
	}

	@Override
	public void stop() throws Exception {
		springContext.stop();
	}

	public static void main(String[] args) {
		launch(JfxApp.class, args);
	}

	private ConfigurableApplicationContext springBootApplicationContext() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(JfxApp.class);
		String[] args = getParameters().getRaw().stream().toArray(String[]::new);
		return builder.run(args);
	}
}
