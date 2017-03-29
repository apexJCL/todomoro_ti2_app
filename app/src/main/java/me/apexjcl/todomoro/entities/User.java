package me.apexjcl.todomoro.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import me.apexjcl.todomoro.rest.entities.Credentials;
import me.apexjcl.todomoro.utils.Constants;

/**
 * User object on Realm.
 * <p>
 * Also stores basic usage functions.
 * <p>
 * Created by apex on 28/03/17.
 */
public class User extends RealmObject {


    @Ignore
    public String password;

    public String username;
    public String email;
    public String firstName;
    public String lastName;

    public String JWToken;

    public static boolean checkSession(AppCompatActivity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(Constants.PREFS_SESSION, false);
    }

    /**
     * Logins a user if the credentials were alright and a JWT was received.
     * @param credentials
     * @param user
     * @param context
     * @return
     */
    public static boolean login(final Credentials credentials, final User user, Context context) {
        if (credentials.token == null)
            return false;
        Realm r = Realm.getDefaultInstance();
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User u = realm.createObject(User.class);
                u.username = user.username;
                u.JWToken = credentials.token;
                u.firstName = credentials.firstName;
                u.lastName = credentials.lastName;
                realm.copyToRealm(u);
            }
        });
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.PREFS_SESSION, true);
        editor.apply();
        return true;
    }

}
