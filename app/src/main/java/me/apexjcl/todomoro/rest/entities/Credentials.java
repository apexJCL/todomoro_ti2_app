package me.apexjcl.todomoro.rest.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by apex on 28/03/17.
 */
public class Credentials {

    public String token;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    public String username;

}
