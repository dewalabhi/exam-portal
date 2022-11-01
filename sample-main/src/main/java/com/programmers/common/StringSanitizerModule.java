package com.programmers.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@Component
public class StringSanitizerModule extends SimpleModule {

    public StringSanitizerModule() {
        addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Nullable
            @Override
            public String deserialize(@NotNull JsonParser parser, DeserializationContext context) throws IOException {
                final String value = parser.getValueAsString();
                return StringUtils.isBlank(value) ? null : value.trim();
            }
        });
    }
}
