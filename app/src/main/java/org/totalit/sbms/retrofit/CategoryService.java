package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {

    @GET("category/{company}")
    Call<List<Category>> getCategories(@Path("company") int company);

}
