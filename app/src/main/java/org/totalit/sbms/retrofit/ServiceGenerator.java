package org.totalit.sbms.retrofit;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.totalit.sbms.Utils.DateDeserializer;
import org.totalit.sbms.Utils.DateSerializer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

   public static final String API_BASE_URL = "http://192.168.142.79:8084/vimbika/rest/client/";
  // public static final String API_BASE_URL = "https://vimbika.net/vimbika/rest/client/";
 //public static final String API_BASE_URL = "http://192.168.1.189:8084/vimbika/rest/client/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    //For timeout
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS).build();

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .registerTypeAdapter(Date.class, new DateSerializer())
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                     .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {

        return createService(serviceClass, null, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null, null);
    }
    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
                if (!TextUtils.isEmpty(authToken)) {
                    AuthenticationInterceptor interceptor =
                            new AuthenticationInterceptor(authToken);

                    if (!httpClient.interceptors().contains(interceptor)) {
                        httpClient.addInterceptor(interceptor);

                        builder.client(httpClient.build());
                        retrofit = builder.build();
                    }
                }

              return retrofit.create(serviceClass);
    }
}
