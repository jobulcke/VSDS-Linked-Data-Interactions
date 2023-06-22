package be.vlaanderen.informatievlaanderen.ldes.ldi;

import be.vlaanderen.informatievlaanderen.ldes.ldi.types.LdiTransformer;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class SparqlSelectTransformer implements LdiTransformer {
    private final Query query;

    public SparqlSelectTransformer(Query query) {
        this.query = query;
    }

    @Override
    public Model apply(Model linkedDataModel) {
        try (QueryExecution qexec = QueryExecutionFactory.create(query, linkedDataModel)) {
            ResultSet resultModel = qexec.execSelect();
            return resultModel.getResourceModel();
        }
    }
}
