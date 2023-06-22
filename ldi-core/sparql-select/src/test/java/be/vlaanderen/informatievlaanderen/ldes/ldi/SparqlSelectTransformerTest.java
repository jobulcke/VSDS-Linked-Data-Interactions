package be.vlaanderen.informatievlaanderen.ldes.ldi;

import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParserBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SparqlSelectTransformerTest {

    @Test
    void when_executeTransform_ExpectTransformedModel() throws URISyntaxException, IOException {
        SparqlSelectTransformer sparqlSelectTransformer = new SparqlSelectTransformer(
                QueryFactory.create("PREFIX ns0: <https://w3id.org/tree#>\n" +
                        "SELECT ?collection ?member \n" +
                        "WHERE\n" +
                        "  { ?collection ns0:member ?member ." +
                        "  }"));

        Model model = readLdesMemberFromFile("gipod-member.jsonld");

        model = sparqlSelectTransformer.apply(model);

        System.out.println();

    }

    private Model readLdesMemberFromFile(String fileName)
            throws URISyntaxException, IOException {
        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI());

        return getModel(Files.lines(Paths.get(file.toURI())).collect(Collectors.joining()), Lang.NQUADS);
    }

    private Model getModel(String s, Lang lang) {
        return RDFParserBuilder.create()
                .fromString(s).lang(lang)
                .toModel();
    }

}