package me.apexjcl.todomoro.entities;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;

import java.util.Date;

/**
 * Describes a Task stored in the server
 * <p>
 * // TODO: add last_posted_task to keep up to date the local database
 * <p>
 * Created by apex on 28/03/17.
 */
public class Task extends RealmObject {

    public String title;
    public String description;
    @SerializedName("due_date")
    public Date dueDate;
    @SerializedName("pomodoro_cycles")
    public Integer pomodoroCycles;
    @SerializedName("created_at")
    public Date createdAt;

}
