package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ClientInventoryService {

    @POST("clientInventory")
    Call<ClientInventory> sendClientInventory(@Body ClientInventory clientInventory);

    @GET("getClientInventory/{createdBy}")
    Call<List<ClientInventory>> getClientInventoryByCreatedBy(@Path("createdBy") int createdBy);

}
