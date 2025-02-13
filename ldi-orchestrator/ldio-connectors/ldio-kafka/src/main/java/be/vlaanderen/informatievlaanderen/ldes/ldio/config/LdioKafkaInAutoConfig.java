package be.vlaanderen.informatievlaanderen.ldes.ldio.config;

import be.vlaanderen.informatievlaanderen.ldes.ldi.services.ComponentExecutor;
import be.vlaanderen.informatievlaanderen.ldes.ldi.types.LdiAdapter;
import be.vlaanderen.informatievlaanderen.ldes.ldio.LdioKafkaIn;
import be.vlaanderen.informatievlaanderen.ldes.ldio.auth.KafkaAuthStrategy;
import be.vlaanderen.informatievlaanderen.ldes.ldio.auth.SaslSslPlainConfigProvider;
import be.vlaanderen.informatievlaanderen.ldes.ldio.configurator.LdioInputConfigurator;
import be.vlaanderen.informatievlaanderen.ldes.ldio.types.LdioInput;
import be.vlaanderen.informatievlaanderen.ldes.ldio.valueobjects.ComponentProperties;
import io.micrometer.observation.ObservationRegistry;
import org.apache.jena.riot.Lang;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import static be.vlaanderen.informatievlaanderen.ldes.ldio.LdioKafkaIn.NAME;
import static be.vlaanderen.informatievlaanderen.ldes.ldio.config.KafkaInConfigKeys.*;
import static be.vlaanderen.informatievlaanderen.ldes.ldio.config.OrchestratorConfig.ORCHESTRATOR_NAME;

@Configuration
public class LdioKafkaInAutoConfig {

	@SuppressWarnings("java:S6830")
	@Bean(NAME)
	public LdioKafkaInConfigurator ldioConfigurator(ObservationRegistry observationRegistry) {
		return new LdioKafkaInConfigurator(observationRegistry);
	}

	public static class LdioKafkaInConfigurator implements LdioInputConfigurator {
		private final ObservationRegistry observationRegistry;

		public LdioKafkaInConfigurator(ObservationRegistry observationRegistry) {
			this.observationRegistry = observationRegistry;
		}

		@Override
		public LdioInput configure(LdiAdapter adapter, ComponentExecutor executor, ApplicationEventPublisher applicationEventPublisher, ComponentProperties config) {
			final String pipelineName = config.getPipelineName();
			final ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerConfig(config));
			final String[] topics = config.getProperty(TOPICS).split(",");
			final String contentType = config.getOptionalProperty(CONTENT_TYPE).orElse(Lang.NQUADS.getHeaderString());
			final LdioKafkaIn ldioKafkaIn = new LdioKafkaIn(pipelineName, executor, adapter, observationRegistry, applicationEventPublisher, consumerFactory, topics, contentType);
			ldioKafkaIn.start();
			return ldioKafkaIn;
		}

		@Override
		public boolean isAdapterRequired() {
			return true;
		}


		private Map<String, Object> getConsumerConfig(ComponentProperties config) {
			final Map<String, Object> props = new HashMap<>();
			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getProperty(BOOTSTRAP_SERVERS));
			props.put(ConsumerConfig.GROUP_ID_CONFIG,
					config.getOptionalProperty(GROUP_ID).orElse(defineUniqueGroupName(config)));
			props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
					config.getOptionalProperty(AUTO_OFFSET_RESET).orElse("earliest"));
			props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			props.putAll(isSecurityEnabled(config) ? getSecurityProperties(config) : Map.of());
			return props;
		}

		private String defineUniqueGroupName(ComponentProperties config) {
			return String.format("ldio-%s-%s", config.getProperty(ORCHESTRATOR_NAME), config.getPipelineName());
		}

		private boolean isSecurityEnabled(ComponentProperties config) {
			return config.getOptionalProperty(SECURITY_PROTOCOL)
					.map(KafkaAuthStrategy::from)
					.filter(KafkaAuthStrategy.SASL_SSL_PLAIN::equals)
					.isPresent();
		}

		private Map<String, Object> getSecurityProperties(ComponentProperties config) {
			final String user = config.getProperty(SASL_JAAS_USER);
			final String password = config.getProperty(SASL_JAAS_PASSWORD);
			return new SaslSslPlainConfigProvider().createSaslSslPlainConfig(user, password);
		}
	}
}
