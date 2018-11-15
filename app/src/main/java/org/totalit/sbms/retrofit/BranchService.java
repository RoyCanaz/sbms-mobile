package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BranchService {
    @POST("branch")
    Call<Branch> sendBranch(@Body Branch branch);

    @GET("getBranch/{createdBy}")
    Call<List<Branch>> getBranchByCreatedBy(@Path("createdBy") int createdBy);
}
