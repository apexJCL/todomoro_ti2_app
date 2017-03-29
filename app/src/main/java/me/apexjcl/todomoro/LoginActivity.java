package me.apexjcl.todomoro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import me.apexjcl.todomoro.entities.User;
import me.apexjcl.todomoro.rest.entities.Credentials;
import me.apexjcl.todomoro.retrofit.RetrofitInstance;
import me.apexjcl.todomoro.retrofit.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    @BindView(R.id.usernameInput)
    EditText usernameInput;

    @BindView(R.id.passwordInput)
    EditText passwordInput;

    @BindView(R.id.loginData)
    LinearLayout loginData;

    @BindView(R.id.progressSpinner)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Realm.init(this);
    }

    @OnClick(R.id.loginButton)
    void login() {
        if (!usernameCheck())
            return;

        if (!passwordCheck())
            return;

        toggleProgressbar();
        // Login
        try {
            UserService userService = RetrofitInstance.createDebugService(UserService.class, this);
            final User u = new User();
            u.username = usernameInput.getText().toString();
            u.password = passwordInput.getText().toString();
            userService.login(u).enqueue(new Callback<Credentials>() {
                @Override
                public void onResponse(Call<Credentials> call, Response<Credentials> response) {
                    if (User.login(response.body(), u, getApplicationContext())) {
                        launchMain();
                        return;
                    }
                    toggleProgressbar();
                    Toast.makeText(getApplicationContext(), getString(R.id.badCredentials), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Credentials> call, Throwable t) {
                    toggleProgressbar();
                    Toast.makeText(getApplicationContext(), getString(R.id.contactServerError), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            toggleProgressbar();
            e.printStackTrace();
        }
    }

    private void launchMain() {
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private boolean usernameCheck() {
        if (usernameInput.getText().length() == 0) {
            usernameInput.setError(getString(R.string.emptyUsername));
            return false;
        }
        return true;
    }

    private boolean passwordCheck() {
        if (passwordInput.getText().length() == 0) {
            passwordInput.setError(getString(R.string.emptyPassword));
            return false;
        }

        if (passwordInput.getText().length() < 6) {
            passwordInput.setError(getString(R.string.passwordTooShort));
            return false;
        }

        return true;
    }

    private void toggleProgressbar() {
        switch (progressBar.getVisibility()) {
            case View.GONE:
                loginData.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case View.VISIBLE:
                progressBar.setVisibility(View.GONE);
                loginData.setVisibility(View.VISIBLE);
                break;
        }
    }

}
