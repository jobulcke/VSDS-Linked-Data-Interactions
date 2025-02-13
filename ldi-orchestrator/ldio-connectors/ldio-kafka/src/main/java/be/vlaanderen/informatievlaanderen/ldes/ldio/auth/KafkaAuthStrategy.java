package be.vlaanderen.informatievlaanderen.ldes.ldio.auth;

import be.vlaanderen.informatievlaanderen.ldes.ldio.exceptions.SecurityProtocolNotSupportedException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static be.vlaanderen.informatievlaanderen.ldes.ldio.config.KafkaInConfigKeys.SECURITY_PROTOCOL;

public enum KafkaAuthStrategy {

	NO_AUTH, SASL_SSL_PLAIN;

	public static KafkaAuthStrategy from(String s) {
		return Arrays.stream(values()).filter(val -> val.name().equals(StringUtils.upperCase(s))).findFirst().orElseThrow(() -> new SecurityProtocolNotSupportedException(SECURITY_PROTOCOL));
	}

}
