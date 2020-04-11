package se.jg.mashup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
@EnableAsync // Could fiddle with executor settings to ramp up performance but default settings is working fine
@ConfigurationPropertiesScan("se.jg.mashup.config")
public class MashupApplication {

	public static void main(String[] args) {
		startApplication(args);
	}

	public static void startApplication(String[] args) {
		SpringApplication app = new SpringApplication(MashupApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		ConfigurableApplicationContext configurableApplicationContext = app.run(args);
		Environment env = configurableApplicationContext.getEnvironment();
		log.info(getInfoText(env.getProperty("server.port")));
	}

	private static String getInfoText(String port) {
		StringBuilder sb = new StringBuilder();
		sb.append("Access URLs:\n----------------------------------------------------------\n\t")
				.append("Local: \t\t\thttp://localhost:")
				.append(port).append("/mashup")
				.append("\n\tSwagger-ui: \thttp://localhost:").append(port).append("/mashup/swagger-ui.html\n")
				.append("----------------------------------------------------------");
		return sb.toString();
	}

}
