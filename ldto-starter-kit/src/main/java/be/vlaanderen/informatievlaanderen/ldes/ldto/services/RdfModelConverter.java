package be.vlaanderen.informatievlaanderen.ldes.ldto.services;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFParserBuilder;
import org.springframework.http.MediaType;

import java.io.StringWriter;

import static java.util.Optional.ofNullable;
import static org.apache.jena.riot.RDFLanguages.TURTLE;
import static org.apache.jena.riot.RDFLanguages.nameToLang;

public class RdfModelConverter {
	private RdfModelConverter() {
	}

	public static Lang getLang(MediaType contentType) {
		if (contentType.equals(MediaType.TEXT_HTML))
			return TURTLE;
		return ofNullable(nameToLang(contentType.getType() + "/" + contentType.getSubtype()))
				.orElseGet(() -> ofNullable(nameToLang(contentType.getSubtype()))
						.orElseThrow());
	}

	public static Model fromString(final String content, final Lang lang) {
		return RDFParserBuilder.create().fromString(content).lang(lang).toModel();
	}

	public static String toString(final Model model, final Lang lang) {
		StringWriter stringWriter = new StringWriter();
		RDFDataMgr.write(stringWriter, model, lang);
		return stringWriter.toString();
	}
}