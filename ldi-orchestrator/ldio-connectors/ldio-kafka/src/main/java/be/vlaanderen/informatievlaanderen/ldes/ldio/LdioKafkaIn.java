package be.vlaanderen.informatievlaanderen.ldes.ldio;

import be.vlaanderen.informatievlaanderen.ldes.ldi.services.ComponentExecutor;
import be.vlaanderen.informatievlaanderen.ldes.ldi.types.LdiAdapter;
import be.vlaanderen.informatievlaanderen.ldes.ldio.types.LdioInput;
import io.micrometer.observation.ObservationRegistry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

public class LdioKafkaIn extends LdioInput implements MessageListener<String, String> {
	public static final String NAME = "Ldio:KafkaIn";
	private static final Logger log = LoggerFactory.getLogger(LdioKafkaIn.class);
	private final KafkaMessageListenerContainer<String, String> container;
	private final String fallbackContentType;


	/**
	 * Creates a LdiInput with its Component Executor and LDI Adapter
	 *
	 * @param executor                  Instance of the Component Executor. Allows the LDI Input to pass
	 *                                  data on the pipeline
	 * @param adapter                   Instance of the LDI Adapter. Facilitates transforming the input
	 *                                  data to a linked data model (RDF).
	 */
	public LdioKafkaIn(String pipelineName, ComponentExecutor executor, LdiAdapter adapter, ObservationRegistry observationRegistry,
	                   ApplicationEventPublisher applicationEventPublisher, ConsumerFactory<String, String> consumerFactory, String[] topics,  String fallbackContentType) {
		super(NAME, pipelineName, executor, adapter, observationRegistry, applicationEventPublisher);
		this.fallbackContentType = fallbackContentType;
		ContainerProperties containerProps = new ContainerProperties(topics);
		containerProps.setMessageListener(this);
		this.container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
	}

	@Override
	public void start() {
		super.start();
		container.start();
	}

	@Override
	protected void resume() {
		container.resume();
	}

	@Override
	protected void pause() {
		container.pause();
	}


	@Override
	public void shutdown() {
		container.pause();
	}


	@Override
	public void onMessage(ConsumerRecord<String, String> data) {
		final String contentType = getContentType(data.headers());
		final var content = LdiAdapter.Content.of(data.value(), contentType);
		log.atDebug().log("Incoming kafka message: {}", content);
		processInput(content);
	}

	private String getContentType(Headers headers) {
		final Header contentTypeOnHeader = headers.lastHeader("content-type");
		return contentTypeOnHeader == null ? fallbackContentType : new String(contentTypeOnHeader.value());
	}
}
