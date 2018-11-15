package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Notes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotesService {
    @POST("notes")
    Call<Notes> sendNotes(@Body Notes notes);

    @GET("getNotes/{createdBy}")
    Call<List<Notes>> getNotesByCreatedBy(@Path("createdBy") int createdBy);
}
