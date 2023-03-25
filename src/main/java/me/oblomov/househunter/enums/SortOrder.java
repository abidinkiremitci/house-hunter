package me.oblomov.househunter.enums;

public enum SortOrder {
    DESC_DATE("sorteer-datum-af"),
    ASC_DATE("");

    private final String uriPath;

    SortOrder(String uriPath) {
        this.uriPath = uriPath;
    }

    public String getUriPath() {
        return uriPath;
    }
}
