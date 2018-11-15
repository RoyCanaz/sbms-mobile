package org.totalit.sbms.retrofit;

import org.totalit.sbms.Sync.ClientDataMapper;
import org.totalit.sbms.Sync.ClientDataMapperX;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientRequestMapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClientService {
        @POST("addClient")
        Call<Client> sendClient(@Body Client client);

        @GET("getClient/{companyId}/{createdBy}")
        Call<List<Client>> getClientByCreatedBy(@Path("companyId")Long companyId, @Path("createdBy") int createdBy);

        @GET("getClientData/{companyId}/{createdBy}")
        Call<ClientRequestMapper> getClientData(@Path("companyId")Long companyId, @Path("createdBy") int createdBy);

        @POST("save_client/{companyId}")
        Call<ClientDataMapper> saveClient(@Body ClientDataMapper clientDataMapper, @Path("companyId")Long companyId);

        @POST("save_clientx/{companyId}")
        Call<ClientDataMapperX> clientDatax(@Body ClientDataMapperX clientDataMapperx, @Path("companyId")Long companyId);


}
