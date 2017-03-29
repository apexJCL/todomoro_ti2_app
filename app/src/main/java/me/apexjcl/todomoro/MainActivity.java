package me.apexjcl.todomoro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import me.apexjcl.todomoro.entities.Task;
import me.apexjcl.todomoro.entities.User;
import me.apexjcl.todomoro.retrofit.RetrofitInstance;
import me.apexjcl.todomoro.retrofit.services.TaskService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        ButterKnife.bind(this);

        if (!User.checkSession(this)) {
            launchLogin();
            return;
        }

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    TaskService service = RetrofitInstance.createDebugService(TaskService.class, getApplicationContext());
                    service.fetchTasks(User.getJWT(getApplicationContext())).enqueue(new Callback<List<Task>>() {
                        @Override
                        public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                            if (response.body() == null)
                                return;
                            for (Task task : response.body())
                                Log.d("Response Task", task.title);
                        }

                        @Override
                        public void onFailure(Call<List<Task>> call, Throwable t) {
                            Log.e("Error from server", t.getMessage());
                            t.printStackTrace();
                            Log.d("End of error", "<<<<<<<<<<");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Launches login activity and finish current activity
     */
    private void launchLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                Log.d("menu", "Logout clicked");
                User.logout(this);
                launchLogin();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
