package org.totalit.sbms.retrofit;


import org.totalit.sbms.domain.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    /*@POST("login")
    Call<User> login(@Body User user);*/

   // @GET("login")
  //  Call<User> login(@Query("userName") String userName);
   @GET("login/{userName}")
   Call<User> login(@Path("userName")String userName);
}
