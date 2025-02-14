package be.jobulcke.ldes.ldio.config;

public class RabbitMqConfigKeys {

	private RabbitMqConfigKeys() {
	}

	public static final String RABBITMQ_HOST = "host";
	public static final String RABBITMQ_PORT = "port";
	public static final String RABBITMQ_USERNAME = "username";
	public static final String RABBITMQ_PASSWORD = "password";
	public static final String RABBITMQ_QUEUES = "queues";
	public static final String RABBITMQ_EXCHANGE = "exchange";
	public static final String RABBITMQ_ROUTING_KEY = "routingKey";

	public static final int DEFAULT_PORT = 5672;
	public static final String VIRTUAL_HOST = "/";
}
