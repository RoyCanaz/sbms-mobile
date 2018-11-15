package org.totalit.sbms.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Repository.QuoteList;
import org.totalit.sbms.adapters.ListQuotesAdapter;
import org.totalit.sbms.adapters.SelectQuoteItemAdapter;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListQuotesActivity extends AppCompatActivity {

    @BindView(R.id.recycler_list_quotes)
    RecyclerView recyclerView;

    @BindView(R.id.fab_new_quote)
    FloatingActionButton fabNewQuote;

    RecyclerView.LayoutManager layoutManager;
    AppDatabase mdb;

    ListQuotesAdapter listQuotesAdapter;
    SharedPreferences sharedPreferences;
    String username;
    String password;
    Long companyId;
    ProgressDialog progressDialog;
    List<Quote> quotesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quotes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        mdb = AppDatabase.getFileDatabase(this);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0);

        try {
            quotesList = new QuoteList(mdb).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        listQuotesAdapter = new ListQuotesAdapter(quotesList, this, mdb, progressDialog);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter( listQuotesAdapter);
        fabNewQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                   //     .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), NewQuoteActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}
