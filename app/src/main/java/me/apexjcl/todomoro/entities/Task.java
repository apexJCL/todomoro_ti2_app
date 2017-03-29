package me.apexjcl.todomoro.entities;

import android.content.Context;
import com.google.gson.annotations.SerializedName;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import me.apexjcl.todomoro.retrofit.RetrofitInstance;
import me.apexjcl.todomoro.retrofit.services.TaskService;
import retrofit2.Call;

import java.util.Date;
import java.util.List;

/**
 * Describes a Task stored in the server
 * <p>
 * // TODO: add last_posted_task to keep up to date the local database
 * <p>
 * Created by apex on 28/03/17.
 */
public class Task extends RealmObject {

    @PrimaryKey
    public int id;
    public String title;
    public String description;
    @SerializedName("due_date")
    public Date dueDate;
    @SerializedName("pomodoro_cycles")
    public Integer pomodoroCycles;
    @SerializedName("created_at")
    public Date createdAt;


    private static void save(final List<Task> tasks) {
        Realm r = Realm.getDefaultInstance();
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Task.class);
                for (Task task : tasks) {
                    Task t = realm.createObject(Task.class);
                    t.id = task.id;
                    t.title = task.title;
                    t.description = task.description;
                    t.createdAt = task.createdAt;
                    t.dueDate = task.dueDate;
                    t.pomodoroCycles = task.pomodoroCycles;
                    realm.copyToRealm(t);
                }
            }
        });
    }

    /**
     * Returns a call for the tasks, so it can be handled by the controller and toggle progressBar according
     * to the events
     *
     * @param context
     * @return
     */
    public static Call<List<Task>> refresh(Context context) {
        try {
            TaskService taskService = RetrofitInstance.createDebugService(TaskService.class, context);
            return taskService.fetchTasks(User.getJWT(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void reloadFromServer(final List<Task> tasks) {
        Realm r = Realm.getDefaultInstance();
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Task.class);
                for (Task task : tasks) {
                    Task t = realm.createObject(Task.class);
                    t.title = task.title;
                    t.description = task.description;
                    t.pomodoroCycles = task.pomodoroCycles;
                    t.dueDate = task.dueDate;
                    t.createdAt = task.createdAt;
                    realm.copyToRealm(t);
                }
            }
        });
    }

    public static List<Task> fetchAll() {
        Realm r = Realm.getDefaultInstance();
        return r.where(Task.class).findAll();
    }
}
