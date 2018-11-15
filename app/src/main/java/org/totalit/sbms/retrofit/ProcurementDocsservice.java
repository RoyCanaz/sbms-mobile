package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.ProcumentDocs;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProcurementDocsservice {

    @POST("procurementDocs")
    Call<ProcumentDocs> sendProcurement(@Body ProcumentDocs procumentDocs);

    @GET("getProcurementDocs/{createdBy}")
    Call<List<ProcumentDocs>> getProcurementDocsByCreatedBy(@Path("createdBy") int createdBy);
}
