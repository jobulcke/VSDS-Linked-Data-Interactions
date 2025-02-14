package be.jobulcke.ldes.ldio.config;

import be.jobulcke.ldes.ldio.LdioRabbitMqIn;
import be.vlaanderen.informatievlaanderen.ldes.ldi.services.ComponentExecutor;
import be.vlaanderen.informatievlaanderen.ldes.ldi.types.LdiAdapter;
import be.vlaanderen.informatievlaanderen.ldes.ldio.pipeline.creation.LdioInput;
import be.vlaanderen.informatievlaanderen.ldes.ldio.pipeline.creation.LdioInputConfigurator;
import be.vlaanderen.informatievlaanderen.ldes.ldio.pipeline.creation.LdioObserver;
import be.vlaanderen.informatievlaanderen.ldes.ldio.pipeline.creation.valueobjects.ComponentProperties;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LdioRabbitMqInAutoConfig {

	@SuppressWarnings("java:S6830")
	@Bean(LdioRabbitMqIn.NAME)
	public LdioInputConfigurator ldioRabbitMqInConfigurator(ObservationRegistry observationRegistry) {
		return new LdioRabbitMqInConfigurator(observationRegistry);
	}

	public static final class LdioRabbitMqInConfigurator implements LdioInputConfigurator {
		private final ObservationRegistry observationRegistry;


		public LdioRabbitMqInConfigurator(ObservationRegistry observationRegistry) {
			this.observationRegistry = observationRegistry;
		}

		@Override
		public LdioInput configure(LdiAdapter adapter, ComponentExecutor executor, ApplicationEventPublisher eventPublisher, ComponentProperties properties) {
			final RabbitMqConfig config = new RabbitMqConfig(properties);
			final LdioObserver observer = LdioObserver.register(LdioRabbitMqIn.NAME, properties.getPipelineName(), observationRegistry);
			final SimpleMessageListenerContainer container = messageListenerContainer(config);
			final LdioRabbitMqIn ldioRabbitMqIn = new LdioRabbitMqIn(executor, adapter, observer, eventPublisher, container, config.getContentType());
			ldioRabbitMqIn.start();
			return ldioRabbitMqIn;
		}

		@Override
		public boolean isAdapterRequired() {
			return true;
		}

		SimpleMessageListenerContainer messageListenerContainer(RabbitMqConfig config) {
			final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory(config));
			container.setQueueNames(config.getQueues());
			return container;
		}

		ConnectionFactory connectionFactory(RabbitMqConfig config) {
			final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
			connectionFactory.setHost(config.getHost());
			connectionFactory.setPort(config.getPort());
			connectionFactory.setUsername(config.getUsername());
			connectionFactory.setPassword(config.getPassword());
			connectionFactory.setVirtualHost(config.getVirtualHost());
			return connectionFactory;
		}
	}
}
