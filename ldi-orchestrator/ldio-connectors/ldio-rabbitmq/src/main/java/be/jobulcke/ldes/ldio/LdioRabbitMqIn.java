package be.jobulcke.ldes.ldio;

import be.vlaanderen.informatievlaanderen.ldes.ldi.services.ComponentExecutor;
import be.vlaanderen.informatievlaanderen.ldes.ldi.types.LdiAdapter;
import be.vlaanderen.informatievlaanderen.ldes.ldio.pipeline.creation.LdioInput;
import be.vlaanderen.informatievlaanderen.ldes.ldio.pipeline.creation.LdioObserver;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.context.ApplicationEventPublisher;

public class LdioRabbitMqIn extends LdioInput implements MessageListener {
	public static final String NAME = "Ldio:RabbitMqIn";
	private final AbstractMessageListenerContainer container;
	private final String defaultContentType;

	/**
	 * Creates a LdiInput with its Component Executor and LDI Adapter
	 *
	 * @param executor                  Instance of the Component Executor. Allows the LDI Input to pass
	 *                                  data on the pipeline
	 * @param adapter                   Instance of the LDI Adapter. Facilitates transforming the input
	 *                                  data to a linked data model (RDF).
	 * @param ldioObserver
	 * @param applicationEventPublisher
	 */
	public LdioRabbitMqIn(ComponentExecutor executor, LdiAdapter adapter, LdioObserver ldioObserver, ApplicationEventPublisher applicationEventPublisher, AbstractMessageListenerContainer container, String defaultContentType) {
		super(executor, adapter, ldioObserver, applicationEventPublisher);
		this.defaultContentType = defaultContentType;
		container.setMessageListener(this);
		this.container = container;

	}

	@Override
	public void start() {
		super.start();
		container.start();
	}

	@Override
	public void shutdown() {
		container.shutdown();
	}

	@Override
	protected void resume() {
		if(!container.isRunning()) {
			container.stop();
		}
	}

	@Override
	protected void pause() {
		if(container.isRunning()) {
			container.stop();
		}
	}

	@Override
	public void onMessage(Message message) {
		final var data = new String(message.getBody());
		processInput(new LdiAdapter.Content(data, getContentType(message)));
	}

	private String getContentType(Message message) {
		return message.getMessageProperties().getContentType() != null ? message.getMessageProperties().getContentType() : defaultContentType;
	}
}
