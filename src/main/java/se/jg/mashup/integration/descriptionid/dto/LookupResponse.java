package se.jg.mashup.integration.descriptionid.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LookupResponse {

    private Map<String, LookupEntity> entities;

    public LookupResponse() {
        entities = new HashMap<>();
    }
}
