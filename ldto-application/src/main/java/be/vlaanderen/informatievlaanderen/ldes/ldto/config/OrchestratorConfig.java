package be.vlaanderen.informatievlaanderen.ldes.ldto.config;

import be.vlaanderen.informatievlaanderen.ldes.ldto.types.ComponentDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "orchestrator")
public class OrchestratorConfig {
	private ComponentDefinition input;
	private List<ComponentDefinition> transformers;
	private List<ComponentDefinition> outputs;

	public ComponentDefinition getInput() {
		return input;
	}

	public void setInput(ComponentDefinition input) {
		this.input = input;
	}

	public List<ComponentDefinition> getTransformers() {
		return transformers;
	}

	public void setTransformers(List<ComponentDefinition> transformers) {
		this.transformers = transformers;
	}

	public List<ComponentDefinition> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<ComponentDefinition> outputs) {
		this.outputs = outputs;
	}

	@Override public String toString() {
		return "OrchestratorConfig{" +
				"input=" + input +
				", transformers=" + transformers +
				", outputs=" + outputs +
				'}';
	}
}