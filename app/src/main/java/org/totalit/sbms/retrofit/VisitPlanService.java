package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Notes;
import org.totalit.sbms.domain.VisitPlan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VisitPlanService {
    @POST("visitPlan")
    Call<VisitPlan> sendVisitPlan(@Body VisitPlan visitPlan);

    @GET("getVisitPlan/{createdBy}")
    Call<List<VisitPlan>> getVisitPlanByCreatedBy(@Path("createdBy") int createdBy);
}
