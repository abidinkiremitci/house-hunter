package me.oblomov.househunter.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "@context",
        "@type",
        "name",
        "url",
        "itemListOrder",
        "itemListElement"
})
public class HouseList {

    @JsonProperty("@context")
    private String context;
    @JsonProperty("@type")
    private List<String> type = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("itemListOrder")
    private String itemListOrder;
    @JsonProperty("itemListElement")
    private List<ItemListElement> itemListElement = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public HouseList() {
    }

    @JsonProperty("@context")
    public String getContext() {
        return context;
    }

    @JsonProperty("@context")
    public void setContext(String context) {
        this.context = context;
    }

    @JsonProperty("@type")
    public List<String> getType() {
        return type;
    }

    @JsonProperty("@type")
    public void setType(List<String> type) {
        this.type = type;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("itemListOrder")
    public String getItemListOrder() {
        return itemListOrder;
    }

    @JsonProperty("itemListOrder")
    public void setItemListOrder(String itemListOrder) {
        this.itemListOrder = itemListOrder;
    }

    @JsonProperty("itemListElement")
    public List<ItemListElement> getItemListElement() {
        return itemListElement;
    }

    @JsonProperty("itemListElement")
    public void setItemListElement(List<ItemListElement> itemListElement) {
        this.itemListElement = itemListElement;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}