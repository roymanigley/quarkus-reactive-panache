package ch.bytecrowd.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CustomOffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {

    public CustomOffsetDateTimeDeserializer() {
        super(OffsetDateTime.class);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String isoOffsetDateTimeString = jsonParser.readValueAs(String.class);
        return OffsetDateTime.from(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(isoOffsetDateTimeString)
        ).withOffsetSameInstant(ZoneOffset.UTC);
    }
}
