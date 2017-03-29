package me.apexjcl.todomoro.retrofit.services;

import me.apexjcl.todomoro.entities.Task;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

/**
 * Created by apex on 28/03/17.
 */
public interface TaskService {

    @GET("task")
    Call<List<Task>> fetchTasks(@Query("token") String token);

}
