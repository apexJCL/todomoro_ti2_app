package me.apexjcl.todomoro.entities;

import android.content.Context;
import com.google.gson.annotations.SerializedName;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import me.apexjcl.todomoro.fragments.NewTaskFragment;
import me.apexjcl.todomoro.retrofit.RetrofitInstance;
import me.apexjcl.todomoro.retrofit.services.TaskService;
import retrofit2.Call;

import java.util.Calendar;
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

    public String title;
    public String description;
    @SerializedName("due_date")
    public Date dueDate;
    @SerializedName("pomodoro_cycles")
    public Integer pomodoroCycles = 0;
    public boolean done;
    @SerializedName("created_at")
    public Date createdAt;

    @Ignore
    public Integer year;
    @Ignore
    public Integer month;
    @Ignore
    public Integer dayOfMonth;
    @Ignore
    public Integer hour;
    @Ignore
    public Integer minutes;


    private static void save(final List<Task> tasks) {
        Realm r = Realm.getDefaultInstance();
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Task.class);
                for (Task task : tasks) {
                    Task t = realm.createObject(Task.class);
                    t.title = task.title;
                    t.description = task.description;
                    t.createdAt = task.createdAt;
                    t.dueDate = task.dueDate;
                    t.pomodoroCycles = task.pomodoroCycles;
                    t.done = task.done;
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

    public void setDueDate(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public void create() {

    }

    public void save() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        this.dueDate = new Date(c.getTimeInMillis());
        Realm r = Realm.getDefaultInstance();
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task t = realm.createObject(Task.class);
                t.dueDate = dueDate;
                t.title = title;
                t.description = description;
                t.done = false;
                t.createdAt = new Date();
                t.pomodoroCycles = 0;
                realm.copyToRealm(t);
            }
        });
    }

    public static void save(final Task task, NewTaskFragment fragment) {
        Realm r = Realm.getDefaultInstance();
        r.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task t = realm.createObject(Task.class);
                t.title = task.title;
                t.description = task.description;
                t.pomodoroCycles = task.pomodoroCycles;
                t.done = task.done;
                Calendar c = Calendar.getInstance();
                c.set(task.year, task.month, task.year);
                c.set(Calendar.HOUR_OF_DAY, task.hour);
                c.set(Calendar.MINUTE, task.minutes);
                t.dueDate = c.getTime();
                realm.copyToRealm(t);
            }
        }, fragment, fragment);
    }
}
