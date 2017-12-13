package org.superbiz.game.msg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONMapper extends ObjectMapper {
    private  JSONMapper() {
    }

    public static JSONMapper getJSONMapper() {
        final JSONMapper mapper = new JSONMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
