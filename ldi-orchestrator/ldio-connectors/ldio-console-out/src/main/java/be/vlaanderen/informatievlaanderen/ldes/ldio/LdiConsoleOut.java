package be.vlaanderen.informatievlaanderen.ldes.ldio;

import be.vlaanderen.informatievlaanderen.ldes.ldi.types.LdiOutput;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static org.apache.jena.riot.Lang.TURTLE;
import static org.apache.jena.riot.RDFLanguages.nameToLang;

public class LdiConsoleOut implements LdiOutput {
	public static final String CONTENT_TYPE = "content-type";
	public static final Lang DEFAULT_OUTPUT_LANG = Lang.NQUADS;
	private final Logger LOGGER = LoggerFactory.getLogger(LdiConsoleOut.class);

	private Lang outputLanguage = DEFAULT_OUTPUT_LANG;

	public LdiConsoleOut(Map<String, String> config) {
		if (config.containsKey(CONTENT_TYPE)) {
			outputLanguage = getLang(Objects.requireNonNull(MediaType.valueOf(config.get(CONTENT_TYPE))));
		}
	}

	@Override
	public void sendLinkedData(Model linkedDataModel) {
		LOGGER.info(toString(linkedDataModel, outputLanguage));
	}

	public static Lang getLang(MediaType contentType) {
		if (contentType.equals(MediaType.TEXT_HTML))
			return TURTLE;
		return ofNullable(nameToLang(contentType.getType() + "/" + contentType.getSubtype()))
				.orElseGet(() -> ofNullable(nameToLang(contentType.getSubtype()))
						.orElseThrow());
	}

	public static String toString(final Model model, final Lang lang) {
		StringWriter stringWriter = new StringWriter();
		RDFDataMgr.write(stringWriter, model, lang);
		return stringWriter.toString();
	}
}
