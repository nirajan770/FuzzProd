package com.fuzzprod.api.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Nirajan on 10/20/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Item {

    public final String id;
    public final String type;
    public final String date;
    public final String data;

    @JsonCreator
    public Item(@JsonProperty("id")String id, @JsonProperty("type")String type, @JsonProperty("date")String date, @JsonProperty("data")String data){
        this.id=id;
        this.type=type;
        this.date=date;
        this.data=data;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }


    public String getData() {
        return data;
    }


}
