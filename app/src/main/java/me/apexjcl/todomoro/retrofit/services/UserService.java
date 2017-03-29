package me.apexjcl.todomoro.retrofit.services;

import me.apexjcl.todomoro.entities.User;
import me.apexjcl.todomoro.rest.entities.Credentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * REST API Service for user
 * <p>
 * Created by apex on 28/03/17.
 */
public interface UserService {

    @POST("user/login")
    Call<Credentials> login(@Body User user);

}
