package be.vlaanderen.informatievlaanderen.ldes.ldi.rdf.formatter;

import org.apache.jena.atlas.web.MediaType;
import org.apache.jena.riot.Lang;

import java.util.Map;
import java.util.Objects;

import static org.apache.jena.riot.RDFLanguages.nameToLang;

public class LdiRdfWriterProperties {
	public final static String RDF_WRITER = "rdfwriter";
	public final static String CONTENT_TYPE = "contenttype";
	public final static String FRAME_TYPE = "jsonldframetype";
	private final Map<String, String> properties;
	private Lang lang;

	public LdiRdfWriterProperties() {
		this.properties = Map.of();
	}

	public LdiRdfWriterProperties(Map<String, String> properties) {
		this.properties = properties;
		setLangFromProperties();
	}

	public LdiRdfWriterProperties withLang(Lang lang) {
		this.lang = lang;
		return this;
	}

	public Lang getLang() {
		return lang;
	}

	private void setLangFromProperties() {
		if (properties.containsKey(CONTENT_TYPE)) {
			this.lang = nameToLang(MediaType.createFromContentType(properties.get(CONTENT_TYPE)).getContentTypeStr());
		} else {
			this.lang = Lang.TTL;
		}
	}

	// JSON-LD

	public String getFrameType() {
		return properties.get(Objects.requireNonNull(FRAME_TYPE));
	}
}