package me.apexjcl.todomoro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import net.danlew.android.joda.JodaTimeAndroid;
import retrofit2.Call;

import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.progressSpinner)
    ProgressBar spinner;

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

    private void loadMainFragment() {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, new DayViewFragment());
        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fab.setOnClickListener((View.OnClickListener) fragments.get(0));
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

                } else {
                    Toast.makeText(this, R.string.errorFetch, Toast.LENGTH_LONG).show();
                }
                toggleSpinner();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleSpinner() {
        if (spinner.getVisibility() == View.VISIBLE)
            spinner.setVisibility(View.GONE);
        else
            spinner.setVisibility(View.VISIBLE);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
