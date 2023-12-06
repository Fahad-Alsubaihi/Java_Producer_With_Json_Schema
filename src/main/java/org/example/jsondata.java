package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
public class jsondata {
    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;
    @JsonProperty("email")
    public String email;

    public jsondata(int id, String name,String email) {
        this.id = id;
        this.name = name;
        this.email=email;

    }
    public jsondata() {}


    public String toString() {
        return "{\"ID\":" + id + ", \"Name\":\"" + name + "\"+\"Email\":\"" + email + "\"}";
    }


}