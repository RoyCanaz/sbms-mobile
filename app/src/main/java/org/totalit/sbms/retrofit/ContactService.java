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

public interface ContactService {
    @POST("contact")
    Call<Contact> sendContacts(@Body Contact contact);

    @GET("getContact/{createdBy}")
    Call<List<Contact>> getContactByCreatedBy(@Path("createdBy") int createdBy);
}
