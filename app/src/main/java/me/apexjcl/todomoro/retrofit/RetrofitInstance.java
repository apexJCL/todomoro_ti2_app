package me.apexjcl.todomoro.retrofit;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Generates services based on prestablished configuration
 * Created by apex on 28/03/17.
 */
public class RetrofitInstance {

    private static String BASE_ENDPOINT_URL = "/rest/v1/";
    private static final String PREFS_NAME = "TodomoroRetrofitPrefs";


    /**
     * Creates a service for the given class
     *
     * @param serviceClass
     * @param context
     * @param <S>
     * @return
     * @throws Exception
     */
    public static <S> S createService(Class<S> serviceClass, Context context) throws Exception {
        // TODO generate config screen
//        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        String baseUrl = preferences.getString("baseUrl", "");
//        if (baseUrl.length() == 0)
//            throw new Exception("No IP declared");
        String baseUrl = "192.168.100.7:81";
        String url = String.format("http://%s%s", baseUrl, BASE_ENDPOINT_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createDebugService(Class<S> serviceClass, Context context) throws Exception {
        // Build debugger
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        String baseUrl = "192.168.100.7:81";
        String url = String.format("http://%s%s", baseUrl, BASE_ENDPOINT_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(serviceClass);
    }

}
