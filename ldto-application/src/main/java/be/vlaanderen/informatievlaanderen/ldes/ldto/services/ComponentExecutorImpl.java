package be.vlaanderen.informatievlaanderen.ldes.ldto.services;

import be.vlaanderen.informatievlaanderen.ldes.ldto.types.LdtoTransformer;
import be.vlaanderen.informatievlaanderen.ldes.ldto.types.LdtoOutput;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComponentExecutorImpl implements ComponentExecutor {

	private final ExecutorService executorService;
	private final List<LdtoTransformer> ldtoTransformers;
	private final List<LdtoOutput> ldtoOutputs;

	public ComponentExecutorImpl(List<LdtoTransformer> ldtoTransformers, List<LdtoOutput> ldtoOutputs) {
		this.executorService = Executors.newSingleThreadExecutor();
		this.ldtoTransformers = ldtoTransformers;
		this.ldtoOutputs = ldtoOutputs;
	}

	@Override
	public void transformLinkedData(final Model linkedDataModel) {
		executorService.execute(() -> {
			Model transformedLinkedDataModel = linkedDataModel;
			for (LdtoTransformer component: ldtoTransformers) {
				transformedLinkedDataModel = component.execute(transformedLinkedDataModel);
			}
			ldtoOutputs.parallelStream().forEach(ldtoOutput -> {
				ldtoOutput.sendLinkedData(linkedDataModel);
			});
		});
	}
}