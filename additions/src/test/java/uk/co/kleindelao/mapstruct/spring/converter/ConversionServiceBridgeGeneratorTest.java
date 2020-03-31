package uk.co.kleindelao.mapstruct.spring.converter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.assertj.core.api.BDDAssertions.then;

import com.squareup.javapoet.ClassName;
import java.io.IOException;
import java.io.StringWriter;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class ConversionServiceBridgeGeneratorTest {
  private static final Clock FIXED_CLOCK =
      Clock.fixed(
          ZonedDateTime.of(2020, 3, 29, 15, 21, 34, (int) (236 * Math.pow(10, 6)), ZoneId.of("Z"))
              .toInstant(),
          ZoneId.of("Z"));
  private final ConversionServiceBridgeGenerator generator =
      new ConversionServiceBridgeGenerator(FIXED_CLOCK);

  @Test
  void shouldGenerateMatchingOutput() throws IOException {
    // Given
    final ConversionServiceBridgeDescriptor descriptor = new ConversionServiceBridgeDescriptor();
    descriptor.setBridgeClassName(
        ClassName.get(
            ConversionServiceBridgeGeneratorTest.class.getPackage().getName(),
            "ConversionServiceBridge"));
    descriptor.setFromToMappings(
        singletonList(Pair.of(ClassName.get("test", "Car"), ClassName.get("test", "CarDto"))));
    final StringWriter outputWriter = new StringWriter();

    // When
    generator.writeConversionServiceBridge(descriptor, outputWriter);

    // Then
    then(outputWriter.toString())
        .isEqualToIgnoringWhitespace(resourceToString("/ConversionServiceBridge.java", UTF_8));
  }
}
