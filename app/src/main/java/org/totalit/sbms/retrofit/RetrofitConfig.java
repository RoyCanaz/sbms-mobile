package org.totalit.sbms.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static Retrofit retrofit = null;

    public static Retrofit retrofitConfig() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                  //.baseUrl("http://10.0.2.2:8090/sbms/rest/client/")
                  //.baseUrl("http://192.168.1.132:8090/sbms/rest/client/")
                  //  .baseUrl("http://192.168.1.203:8080/sbms/rest/client/")
                  // .baseUrl("http://197.211.212.86:8080/sbms/rest/client/")
                    .baseUrl("http://192.168.1.103:8080/sbms/rest/client/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
