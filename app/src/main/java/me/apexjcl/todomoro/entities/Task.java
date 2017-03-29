package me.apexjcl.todomoro.entities;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.gson.annotations.SerializedName;
import io.realm.Realm;
import io.realm.RealmObject;
import me.apexjcl.todomoro.R;
import me.apexjcl.todomoro.retrofit.RetrofitInstance;
import me.apexjcl.todomoro.retrofit.services.TaskService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public class FetchTaskCallback implements Callback<List<Task>> {

        private ProgressBar progressBar;
        private Context context;

        public FetchTaskCallback(ProgressBar progressBar, Context context) {
            this.progressBar = progressBar;
            this.context = context;
        }

        @Override
        public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
            if (response.body() == null)
                Toast.makeText(context, R.string.noTasks, Toast.LENGTH_SHORT).show();
            else
                Task.save();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<List<Task>> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
