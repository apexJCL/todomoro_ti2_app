package me.apexjcl.todomoro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import me.apexjcl.todomoro.entities.Task;
import me.apexjcl.todomoro.entities.User;
import me.apexjcl.todomoro.fragments.DayViewFragment;
import me.apexjcl.todomoro.fragments.NewTaskFragment;
import me.apexjcl.todomoro.fragments.TodoFragment;
import net.danlew.android.joda.JodaTimeAndroid;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FloatingActionButton.OnClickListener,
        FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.loadingSpinner)
    ProgressBar spinner;

    @BindView(R.id.content_fragment)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        JodaTimeAndroid.init(this);
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        if (!User.checkSession(this)) {
            launchLogin();
            return;
        }

        ButterKnife.bind(this);

        // Load stuff
        User u = User.currentUser(getApplicationContext());
        // Meants preference was true where there was no actually a user
        if (u == null) {
            User.logout(this);
            launchLogin();
            return;
        }

        TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawerUsername);
        TextView fullName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawerFullname);
        username.setText(u.username);
        fullName.setText(u.getFullname());

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        loadMainFragment();

        fab.setOnClickListener(this);
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                toggleSpinner();
                Call<List<Task>> refresh = Task.refresh(this);
                if (refresh != null) {
                    refresh.enqueue(new Callback<List<Task>>() {
                        @Override
                        public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                            if (response.body() == null) {
                                toggleSpinner();
                                return;
                            }
                            Task.reloadFromServer(response.body());
//                            DayViewFragment dayViewFragment = (DayViewFragment) getSupportFragmentManager().findFragmentByTag(DayViewFragment.TAG);
//                            dayViewFragment.updateWeekview(Task.fetchAll());
                            toggleSpinner();
                        }

                        @Override
                        public void onFailure(Call<List<Task>> call, Throwable t) {
                            toggleSpinner();
                            Toast.makeText(getApplicationContext(), R.string.serverError, Toast.LENGTH_SHORT).show();
                            Log.d("Error", t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(this, R.string.errorFetch, Toast.LENGTH_LONG).show();
                    toggleSpinner();
                }
                break;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggleSpinner() {
        if (spinner.getVisibility() == View.VISIBLE) {
            spinner.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            spinner.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fab.setImageResource(R.drawable.ic_add);

        // Check current fragment
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_fragment);

        switch (id) {
            case R.id.nav_dayView:
            case R.id.nav_weekView:
                if (!(f instanceof DayViewFragment))
                    loadMainFragment();
                DayViewFragment dayViewFragment = (DayViewFragment) getSupportFragmentManager().findFragmentByTag(DayViewFragment.TAG);
                if (dayViewFragment != null)
                    dayViewFragment.getWeekView().setNumberOfVisibleDays(id == R.id.nav_dayView ? 1 : 7);
                break;
            case R.id.nav_todo:
                launchTodoFragment();
                break;
            case R.id.nav_logout:
                User.logout(this);
                launchLogin();
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchTodoFragment() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_fragment);
        if (f instanceof TodoFragment)
            return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, new TodoFragment());
        ft.commit();
        ft.addToBackStack(null);
    }

    private void loadMainFragment() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_fragment);
        if (f instanceof DayViewFragment)
            return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, new DayViewFragment(), DayViewFragment.TAG);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        fab.setImageResource(R.drawable.ic_done);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, new NewTaskFragment());
        ft.addToBackStack(null);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        ft.commit();
    }

    @Override
    public void onBackStackChanged() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_fragment);
        fab.setOnClickListener((View.OnClickListener) f);
        if (f instanceof NewTaskFragment)
            fab.setImageResource(R.drawable.ic_done);
        else if (f instanceof DayViewFragment || f instanceof TodoFragment)
            fab.setImageResource(R.drawable.ic_add);
    }
}
