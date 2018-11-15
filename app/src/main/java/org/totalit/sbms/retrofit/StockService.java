package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Stock;
import org.totalit.sbms.domain.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StockService {

    @GET("stock/available/{categoryId}")
    Call<List<Integer>> getAvailableStock(@Path("categoryId") int categoryId);

    @GET("stock/suppliers/{company}")
    Call<List<Supplier>> getSuppliers(@Path("company") int company);

    @GET("stock/brands")
    Call<ArrayList<String>> getBrands();

    @POST("stock/addproduct")
    Call<List<Stock>> addProduct(@Body Product product);

    @POST("stock/updateSn")
    Call<ResponseBody> updateSn(@Body Stock stock);
}
