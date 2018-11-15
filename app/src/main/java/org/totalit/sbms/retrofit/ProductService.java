package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Supplier;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {

    @GET("product/category/{company}/{category}")
    Call<List<Product>> getProductsByCategory(@Path("company") Long company, @Path("category") Long category);

    @GET("product/all/{company}")
    Call<List<Product>> getAllProducts(@Path("company") Long company);

    @GET("product/id/{id}")
    Call<Product> getProductById(@Path("id") Long id);
}
