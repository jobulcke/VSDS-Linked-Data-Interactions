package be.vlaanderen.informatievlaanderen.ldes.ldio.config.auth;

import be.vlaanderen.informatievlaanderen.ldes.ldio.auth.KafkaAuthStrategy;
import be.vlaanderen.informatievlaanderen.ldes.ldio.exceptions.SecurityProtocolNotSupportedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KafkaAuthStrategyTest {

	@ParameterizedTest
	@EnumSource(KafkaAuthStrategy.class)
	void shouldReturnAValueForExistingAuthStrategies(KafkaAuthStrategy authStrategy) {
		assertThat(KafkaAuthStrategy.from(authStrategy.name())).isEqualTo(authStrategy);
	}

	@Test
	void shouldReturnEmptyOptionalForNonExistingAuthStrategies() {
		assertThatThrownBy(() -> KafkaAuthStrategy.from("nonExisting"))
				.isInstanceOf(SecurityProtocolNotSupportedException.class);
	}

}