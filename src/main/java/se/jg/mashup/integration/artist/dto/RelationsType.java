package se.jg.mashup.integration.artist.dto;

public enum RelationsType {
    wikidata, wikipedia;

    public static boolean isOfType(String type) {
        return wikidata.name().equalsIgnoreCase(type) || wikipedia.name().equalsIgnoreCase(type);
    }

    public static boolean isOfType(Relation relation) {
        return isOfType(relation.getType());
    }
}
