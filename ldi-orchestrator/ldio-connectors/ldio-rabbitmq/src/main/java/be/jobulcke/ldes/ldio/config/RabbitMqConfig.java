package be.jobulcke.ldes.ldio.config;


import be.vlaanderen.informatievlaanderen.ldes.ldio.pipeline.creation.valueobjects.ComponentProperties;
import org.apache.jena.riot.Lang;

import static be.jobulcke.ldes.ldio.config.RabbitMqConfigKeys.*;

public class RabbitMqConfig {
	private final ComponentProperties config;

	public RabbitMqConfig(ComponentProperties config) {
		this.config = config;
	}

	public String[] getQueues() {
		return config.getProperty(RABBITMQ_QUEUES).split(",");
	}

	public String getHost() {
		return config.getProperty(RABBITMQ_HOST);
	}

	public int getPort() {
		return config.getOptionalInteger(RABBITMQ_PORT).orElse(DEFAULT_PORT);
	}

	public String getUsername() {
		return config.getProperty(RABBITMQ_USERNAME);
	}

	public String getPassword() {
		return config.getProperty(RABBITMQ_PASSWORD);
	}

	public String getVirtualHost() {
		return VIRTUAL_HOST;
	}

	public String getContentType() {
		return config.getOptionalProperty("contentType").orElse(Lang.NQ.getHeaderString());
	}

}
