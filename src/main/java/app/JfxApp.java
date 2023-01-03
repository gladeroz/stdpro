package app;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import app.config.StageManager;
import app.repository.TraitementRepository;
import app.service.MainRepository;
import javafx.application.Application;
import javafx.stage.Stage;

@SpringBootApplication
@EntityScan("app.entity")
@EnableJpaRepositories("app.repository")
public class JfxApp extends Application {

	@Autowired TraitementRepository t;

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
