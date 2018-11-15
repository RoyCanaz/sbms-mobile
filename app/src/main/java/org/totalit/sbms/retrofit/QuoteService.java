package org.totalit.sbms.retrofit;

import org.totalit.sbms.domain.Bank;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Quote;
import org.totalit.sbms.domain.QuoteMapper;
import org.totalit.sbms.domain.QuoteMapperR;
import org.totalit.sbms.domain.Rate;
import org.totalit.sbms.domain.temp.QuoteTemp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuoteService {

    @GET("quote/rates/{company}")
    Call<List<Rate>> getRate(@Path("company") int company);
    @GET("quote/bank/{company}")
    Call<List<Bank>> getBank(@Path("company") int company);

    @POST("quote/new_quote/{company}")
    Call<QuoteMapperR> newQuote(@Body QuoteMapper quoteMapper, @Path("company") int company);

    @POST("quote/send_quote/{quoteId}")
    Call<Quote> sendQuote(@Path("quoteId") Long quoteId);
}
