package se.jg.mashup.integration.description.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Query {

    private Map<String, Page> pages;

    public Query() {
        pages = new HashMap<>();
    }
}
