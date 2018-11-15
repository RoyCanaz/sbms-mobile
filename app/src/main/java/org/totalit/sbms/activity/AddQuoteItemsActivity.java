package org.totalit.sbms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.TwoDecimalPlaceUtil;
import org.totalit.sbms.adapters.AddQuoteItemsAdapter;
import org.totalit.sbms.adapters.SelectQuoteItemAdapter;
import org.totalit.sbms.domain.Quote;
import org.totalit.sbms.domain.QuoteItem;
import org.totalit.sbms.domain.QuoteMapper;
import org.totalit.sbms.domain.QuoteMapperR;
import org.totalit.sbms.domain.temp.QuoteItemTemp;
import org.totalit.sbms.domain.temp.QuoteTemp;
import org.totalit.sbms.retrofit.QuoteService;
import org.totalit.sbms.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddQuoteItemsActivity extends AppCompatActivity {

    @BindView(R.id.text_quote_sub_total)
    TextView textSubTotal;

    @BindView(R.id.text_quote_vat)
    TextView textVat;

    @BindView(R.id.text_quote_total)
    TextView textTotal;

    @BindView(R.id.fab_add_quote_item)
    FloatingActionButton fab;

    @BindView(R.id.recylce_view_quote_items)
    RecyclerView recyclerView;

    AddQuoteItemsAdapter addQuoteItemsAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<QuoteItemTemp> quoteItems;
    AppDatabase mdb;
    Long quoteId;

    SharedPreferences sharedPreferences;
    String username;
    String password;
    Long companyId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quote_items);
        ButterKnife.bind(this);
        mdb = AppDatabase.getFileDatabase(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0);
        progressDialog = new ProgressDialog(this);
        Intent i = getIntent();
        quoteId = i.getLongExtra("quoteId", 0);
        quoteItems  = new ArrayList<>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                QuoteTemp quoteTemp = mdb.quoteTempDao().getQuoteTemp(quoteId.intValue());




                textSubTotal.setText(TwoDecimalPlaceUtil.formatDouble(quoteTemp.getTotal()));
                textVat.setText(TwoDecimalPlaceUtil.formatDouble(quoteTemp.getVat()));
                textTotal.setText(TwoDecimalPlaceUtil.formatDouble(quoteTemp.getTotalIncVat()));

                List<QuoteItemTemp> items = mdb.quoteItemTempDao().getQuoteItemByQuote(quoteId);
                quoteItems.addAll(items);
                addQuoteItemsAdapter  = new AddQuoteItemsAdapter(quoteItems, getApplication(), mdb, quoteId, AddQuoteItemsActivity.this);
                layoutManager = new LinearLayoutManager(getApplicationContext());

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(addQuoteItemsAdapter );
                addQuoteItemsAdapter .notifyDataSetChanged();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectQuoteItemsActivity.class);
                intent.putExtra("quoteId", quoteId);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_quote_menu, menu);

        return true;
    }

    public void processQuote(){
        progressDialog.setTitle("Sending. Please wait.....");
        progressDialog.setCancelable(true);
        progressDialog.show();
        List<QuoteItemTemp> quoteItemTemps = null;
        QuoteTemp quoteTemp = null;
        try {
            quoteItemTemps = new QuoteItems(mdb, quoteId).execute().get();
            quoteTemp = new Quot(mdb, quoteId).execute().get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        Log.e("Contact Id", "..."+quoteTemp.getContact());
        Log.e("Client Id", "..."+quoteTemp.getClient());
        Log.e("Bank", "..."+quoteTemp.getBankingDetail());
        QuoteMapper quoteMapper = new QuoteMapper();
        quoteMapper.setQuote(quoteTemp);
        quoteMapper.setQuoteItem(quoteItemTemps);
        QuoteService quoteService = ServiceGenerator.createService(QuoteService.class, username, password);
        Log.e("Quote Json", ServiceGenerator.gson.toJson(quoteMapper));
        Call<QuoteMapperR> quoteCall = quoteService.newQuote(quoteMapper, companyId.intValue());

        quoteCall.enqueue(new Callback<QuoteMapperR>() {
            @Override
            public void onResponse(Call<QuoteMapperR> call, Response<QuoteMapperR> response) {
                if (response.isSuccessful()){
                    QuoteMapperR mapper = response.body();
                    Log.e("Quote Json", ServiceGenerator.gson.toJson(mapper.getQuote()));
                    saveQuote(mapper.getQuote(), mapper.getQuoteItem());
                    int qId = mapper.getQuote().getId();
                    sendQuote((long) qId);
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Quote Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuoteMapperR> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Quote Error", t.getMessage()+"");
                Snackbar snackbar = Snackbar
                        .make(textSubTotal, ""+t.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                  processQuote();
                            }
                        });

                snackbar.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.action_send) {
            processQuote();
        }
        if(id== android.R.id.home){
            try {
                showDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return super.onOptionsItemSelected(menuItem);
    }

    public void showDialog() throws Exception
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Cancel Quote : Yes/No");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                Intent intent = new Intent(getApplicationContext(), NewQuoteActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void setTotals(final String total, final String vat, final String totalIncVat, Activity activity){

activity.runOnUiThread(new Runnable() {
    @Override
    public void run() {
        textTotal.setText(totalIncVat);
        textVat.setText(vat);
        textSubTotal.setText(total);
    }
});


    }



    public void saveQuote(final org.totalit.sbms.domain.Quote quote, final List<QuoteItem> quoteItems){
          AsyncTask.execute(new Runnable() {
              @Override
              public void run() {
                  Log.e("Saving Quote", "..............");
                  mdb.quoteDao().insertQuote(quote);
                  for(QuoteItem item : quoteItems) {
                      mdb.quoteItemDao().insertQuoteItem(item);
                      Log.e("Quote item", "QuoteItem Id"+item.getId());
                  }
              }
          });
    }
    public void sendQuote(final Long quoteId){
        QuoteService quoteService = ServiceGenerator.createService(QuoteService.class, username, password);
       Call<Quote> quoteCall = quoteService.sendQuote(quoteId);
       quoteCall.enqueue(new Callback<org.totalit.sbms.domain.Quote>() {
           @Override
           public void onResponse(Call<org.totalit.sbms.domain.Quote> call, Response<Quote> response) {
               if(response.isSuccessful()){
                   progressDialog.dismiss();
                   final Quote quote = response.body();
                   AsyncTask.execute(new Runnable() {
                       @Override
                       public void run() {

                           Quote q = mdb.quoteDao().getQuote(quoteId);
                           q.setLastSendMailStatus(quote.getLastSendMailStatus());
                           q.setDateCreated(quote.getDateCreated());
                           mdb.quoteDao().updateQuote(q);

                       }
                   });

                   Toast.makeText(getApplicationContext(), "Send Successfully", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(getApplicationContext(),ListQuotesActivity.class);
                   startActivity(intent);
                   finish();
               }
               else{
                   progressDialog.dismiss();
                   Toast.makeText(getApplicationContext(), "Sending Failed", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<org.totalit.sbms.domain.Quote> call, Throwable t) {
               Log.e("Send Error", t.getMessage()+"");
               Snackbar snackbar = Snackbar
                       .make(textVat, ""+t.getMessage(), Snackbar.LENGTH_INDEFINITE)
                       .setAction("Retry", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               sendQuote(quoteId);
                           }
                       });

               snackbar.show();

               progressDialog.dismiss();
             /*  Intent intent = new Intent(getApplicationContext(), ListQuotesActivity.class);
               startActivity(intent);
               finish();*/


           }
       });
    }

    public class QuoteItems extends AsyncTask<Void, Void, List<QuoteItemTemp>> {
        List<QuoteItemTemp> quoteItemTemps;
        Long quoteId;
        AppDatabase mdb;
        public QuoteItems(AppDatabase mdb, Long quoteId) {
            this.mdb = mdb;
            this.quoteId = quoteId;
            quoteItemTemps = new ArrayList<>();
        }

        @Override
        protected List<QuoteItemTemp> doInBackground(Void... voids) {
            quoteItemTemps = mdb.quoteItemTempDao().getQuoteItemByQuote(quoteId);
            return quoteItemTemps;
        }
    }

    public class Quot extends AsyncTask<Void, Void, QuoteTemp> {
        QuoteTemp quoteTemp;
        Long quoteId;
        AppDatabase mdb;
        public Quot(AppDatabase mdb, Long quoteId) {
            this.mdb = mdb;
            this.quoteId = quoteId;

        }

        @Override
        protected QuoteTemp doInBackground(Void... voids) {
            Log.e("QuoteId...", quoteId+"");
            quoteTemp = mdb.quoteTempDao().getQuoteTemp(quoteId.intValue());
            return quoteTemp;
        }
    }




}
