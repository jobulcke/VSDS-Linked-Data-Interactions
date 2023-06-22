package be.vlaanderen.informatievlaanderen.ldes.ldi;

import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SparqlConstructTransformerTest {

    private final static Model initModel = ModelFactory.createDefaultModel();

    private final static String constructQuery = """
            CONSTRUCT {
              <http://transformed-quad/> <http://test/> "Transformed data"
            }
            WHERE { ?s ?p ?o }
            """;

    private final Statement originalData = initModel.createStatement(
            initModel.createResource("http://data-from-source/"),
            initModel.createProperty("http://test/"),
            "Source data!");

    private final Statement transformedData = initModel.createStatement(
            initModel.createResource("http://transformed-quad/"),
            initModel.createProperty("http://test/"),
            "Transformed data");

    @Test
    void when_executeTransform_ExpectTransformedModel() {
        SparqlConstructTransformer sparqlConstructTransformer = new SparqlConstructTransformer(
                QueryFactory.create(constructQuery), false);

        Model model = ModelFactory.createDefaultModel().add(originalData);

        model = sparqlConstructTransformer.apply(model);

        assertTrue(model.contains(transformedData));
        assertFalse(model.contains(originalData));

    }

    @Test
    void when_executeTransform_includeOriginal_ExpectTransformedModelWithOriginal() {
        SparqlConstructTransformer sparqlConstructTransformer = new SparqlConstructTransformer(
                QueryFactory.create(constructQuery), true);

        Model model = ModelFactory.createDefaultModel().add(originalData);

        model = sparqlConstructTransformer.apply(model);

        assertTrue(model.contains(transformedData));
        assertTrue(model.contains(originalData));

    }

    @Test
    void test2() {
        SparqlConstructTransformer sparqlConstructTransformer = new SparqlConstructTransformer(
                QueryFactory.create(
                        "PREFIX ns0: <https://w3id.org/tree#>\n" +
                                "SELECT ?collection ?member \n" +
                                "WHERE\n" +
                                "  { ?collection ns0:member ?member ." +
                                "  }"
                ), false);

        Model model = RDFParser.fromString("{\n" +
                "    \"@graph\": [\n" +
                "        {\n" +
                "            \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/works/3775922\",\n" +
                "            \"https://gipod.vlaanderen.be/ns/gipod#gipodId\": {\n" +
                "                \"@value\": \"3775922\",\n" +
                "                \"@type\": \"http://www.w3.org/2001/XMLSchema#integer\"\n" +
                "            },\n" +
                "            \"https://data.vlaanderen.be/ns/mobiliteit#Inname.heeftGevolg\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/mobility-hindrances/10228651/628\"\n" +
                "            },\n" +
                "            \"@type\": \"https://data.vlaanderen.be/ns/mobiliteit#Werk\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/mobility-hindrances/10228651/628\",\n" +
                "            \"http://www.w3.org/ns/prov#generatedAtTime\": {\n" +
                "                \"@value\": \"2020-12-28T09:36:43.353Z\",\n" +
                "                \"@type\": \"http://www.w3.org/2001/XMLSchema#dateTime\"\n" +
                "            },\n" +
                "            \"http://www.w3.org/ns/adms#identifier\": {\n" +
                "                \"@id\": \"_:b5\"\n" +
                "            },\n" +
                "            \"http://purl.org/dc/terms/isVersionOf\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/mobility-hindrances/10228651\"\n" +
                "            },\n" +
                "            \"http://purl.org/dc/terms/modified\": {\n" +
                "                \"@value\": \"2020-12-11T13:41:56.48Z\",\n" +
                "                \"@type\": \"http://www.w3.org/2001/XMLSchema#dateTime\"\n" +
                "            },\n" +
                "            \"https://gipod.vlaanderen.be/ns/gipod#gipodId\": {\n" +
                "                \"@value\": \"10228651\",\n" +
                "                \"@type\": \"http://www.w3.org/2001/XMLSchema#integer\"\n" +
                "            },\n" +
                "            \"http://purl.org/dc/terms/created\": {\n" +
                "                \"@value\": \"2020-12-11T13:31:13.777Z\",\n" +
                "                \"@type\": \"http://www.w3.org/2001/XMLSchema#dateTime\"\n" +
                "            },\n" +
                "            \"https://data.vlaanderen.be/ns/mobiliteit#Inname.status\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/taxonomies/statuses/a411c53e-db33-436a-9bb9-d62d535b661d\"\n" +
                "            },\n" +
                "            \"http://www.w3.org/ns/adms#versionNotes\": {\n" +
                "                \"@language\": \"nl-be\",\n" +
                "                \"@value\": \"MobilityHindranceWasImportedFromLegacy\"\n" +
                "            },\n" +
                "            \"@type\": \"https://data.vlaanderen.be/ns/mobiliteit#Mobiliteitshinder\",\n" +
                "            \"https://data.vlaanderen.be/ns/mobiliteit#beheerder\": {\n" +
                "                \"@id\": \"_:b4\"\n" +
                "            },\n" +
                "            \"http://purl.org/dc/elements/1.1/creator\": {\n" +
                "                \"@id\": \"_:b0\"\n" +
                "            },\n" +
                "            \"https://data.vlaanderen.be/ns/mobiliteit#periode\": {\n" +
                "                \"@id\": \"_:b2\"\n" +
                "            },\n" +
                "            \"https://data.vlaanderen.be/ns/mobiliteit#zone\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/mobility-hindrances/10228651/zones/37b715e6-3d02-4e53-baee-260e71f832ed\"\n" +
                "            },\n" +
                "            \"http://purl.org/dc/elements/1.1/contributor\": {\n" +
                "                \"@id\": \"_:b3\"\n" +
                "            },\n" +
                "            \"http://purl.org/dc/terms/description\": {\n" +
                "                \"@value\": \"9100 Sint-Niklaas, Meesterstraat 157: Plaatsen container (m.u.v. van een bureelcontainer)\",\n" +
                "                \"@type\": \"http://www.w3.org/1999/02/22-rdf-syntax-ns#langString\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"_:b0\",\n" +
                "            \"@type\": \"http://www.w3.org/ns/org#Organization\",\n" +
                "            \"http://purl.org/dc/terms/isVersionOf\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/organisations/fedab33f-792a-029c-9b34-9d9cfe7d6245\"\n" +
                "            },\n" +
                "            \"http://www.w3.org/2004/02/skos/core#prefLabel\": {\n" +
                "                \"@value\": \"EagleBe Smartcity\",\n" +
                "                \"@type\": \"http://www.w3.org/1999/02/22-rdf-syntax-ns#langString\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"_:b1\",\n" +
                "            \"@type\": \"http://www.w3.org/ns/locn#Geometry\",\n" +
                "            \"http://www.opengis.net/ont/geosparql#asWKT\": {\n" +
                "                \"@value\": \"<http://www.opengis.net/def/crs/EPSG/9.9.1/31370> POLYGON ((135966.110558159 208430.189657898, 135967.369198941 208428.635365975, 135972.03207471 208432.411288322, 135970.773433928 208433.965580245, 135966.110558159 208430.189657898))\",\n" +
                "                \"@type\": \"http://www.opengis.net/ont/geosparql#wktLiteral\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"_:b2\",\n" +
                "            \"@type\": \"http://data.europa.eu/m8g/PeriodOfTime\",\n" +
                "            \"http://data.europa.eu/m8g/endTime\": {\n" +
                "                \"@value\": \"2020-12-10T19:00:00Z\",\n" +
                "                \"@type\": \"http://www.w3.org/2001/XMLSchema#dateTime\"\n" +
                "            },\n" +
                "            \"http://data.europa.eu/m8g/startTime\": {\n" +
                "                \"@value\": \"2020-12-05T05:00:00Z\",\n" +
                "                \"@type\": \"http://www.w3.org/2001/XMLSchema#dateTime\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"_:b3\",\n" +
                "            \"@type\": \"http://www.w3.org/ns/org#Organization\",\n" +
                "            \"http://purl.org/dc/terms/isVersionOf\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/organisations/fedab33f-792a-029c-9b34-9d9cfe7d6245\"\n" +
                "            },\n" +
                "            \"http://www.w3.org/2004/02/skos/core#prefLabel\": {\n" +
                "                \"@value\": \"EagleBe Smartcity\",\n" +
                "                \"@type\": \"http://www.w3.org/1999/02/22-rdf-syntax-ns#langString\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/mobility-hindrances/10228651/zones/37b715e6-3d02-4e53-baee-260e71f832ed\",\n" +
                "            \"@type\": \"https://data.vlaanderen.be/ns/mobiliteit#Zone\",\n" +
                "            \"http://www.w3.org/ns/locn#geometry\": {\n" +
                "                \"@id\": \"_:b1\"\n" +
                "            },\n" +
                "            \"https://data.vlaanderen.be/ns/mobiliteit#Zone.type\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/taxonomies/zonetypes/0fb72ef7-6ac9-4a70-b295-a30ea215d250\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/taxonomies/zonetypes/0fb72ef7-6ac9-4a70-b295-a30ea215d250\",\n" +
                "            \"http://www.w3.org/2004/02/skos/core#prefLabel\": {\n" +
                "                \"@language\": \"nl-be\",\n" +
                "                \"@value\": \"HinderZone\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"_:b4\",\n" +
                "            \"@type\": \"http://www.w3.org/ns/org#Organization\",\n" +
                "            \"http://purl.org/dc/terms/isVersionOf\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/organisations/fedab33f-792a-029c-9b34-9d9cfe7d6245\"\n" +
                "            },\n" +
                "            \"http://www.w3.org/2004/02/skos/core#prefLabel\": {\n" +
                "                \"@value\": \"EagleBe Smartcity\",\n" +
                "                \"@type\": \"http://www.w3.org/1999/02/22-rdf-syntax-ns#langString\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"_:b5\",\n" +
                "            \"@type\": \"http://www.w3.org/ns/adms#Identifier\",\n" +
                "            \"http://www.w3.org/2004/02/skos/core#notation\": {\n" +
                "                \"@value\": \"10228651\",\n" +
                "                \"@type\": \"https://gipod.vlaanderen.be/ns/gipod#gipodId\"\n" +
                "            },\n" +
                "            \"http://www.w3.org/ns/adms#schemaAgency\": {\n" +
                "                \"@language\": \"nl-be\",\n" +
                "                \"@value\": \"https://gipod.vlaanderen.be\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/taxonomies/statuses/a411c53e-db33-436a-9bb9-d62d535b661d\",\n" +
                "            \"http://www.w3.org/2004/02/skos/core#prefLabel\": {\n" +
                "                \"@language\": \"nl-be\",\n" +
                "                \"@value\": \"Onbekend\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/ldes/mobility-hindrances\",\n" +
                "            \"https://w3id.org/tree#member\": {\n" +
                "                \"@id\": \"https://private-api.gipod.beta-vlaanderen.be/api/v1/mobility-hindrances/10228651/628\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}").lang(Lang.JSONLD).toModel();

        model = sparqlConstructTransformer.apply(model);

        System.out.println();

    }
}
