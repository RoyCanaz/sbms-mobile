package org.totalit.sbms.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.totalit.sbms.CallListerner.BankListener;
import org.totalit.sbms.CallListerner.BrandListener;
import org.totalit.sbms.CallListerner.RateListener;
import org.totalit.sbms.ClassImplements.BankSpinnerAdapter;
import org.totalit.sbms.ClassImplements.RateSpinnerAdapter;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.ProductValidations;
import org.totalit.sbms.domain.Bank;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Rate;
import org.totalit.sbms.domain.Supplier;
import org.totalit.sbms.domain.temp.QuoteTemp;
import org.totalit.sbms.fragments.quotes.QuoteDashboard;
import org.totalit.sbms.retrofit.QuoteService;
import org.totalit.sbms.retrofit.ServiceGenerator;
import org.totalit.sbms.retrofit.StockService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewQuoteActivity extends AppCompatActivity  {

    @BindView(R.id.card_not_found)
    CardView notFound;
    @BindView(R.id.btn_create_quote)
    Button btnCreate;

    @BindView(R.id.quote_spinner_rates)
    SearchableSpinner selectRate;
    @BindView(R.id.quote_spinner_bank)
    SearchableSpinner selectBank;
    @BindView(R.id.quote_spinner_client)
    SearchableSpinner selectClient;
    @BindView(R.id.quote_spinner_contact)
    SearchableSpinner selectContact;
    @BindView(R.id.quote_text_bank)
    TextView textBank;
    @BindView(R.id.quote_text_rate)
    TextView textRate;

    SharedPreferences sharedPreferences;
    String username;
    String password;
    Long companyId;
    AppDatabase mdb;
       RateSpinnerAdapter rateSpinnerAdapter;
       BankSpinnerAdapter bankSpinnerAdapter;
    ArrayAdapter clientAdapter;
    ArrayAdapter contactAdapter;
    Rate rate = null;
    Bank bank = null;
    Long quoteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quote);
        ButterKnife.bind(this);

        mdb = AppDatabase.getFileDatabase(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0);

        selectRate.setOnItemSelectedListener(onItemRateSelectedListener);
        selectBank.setOnItemSelectedListener(onItemBankSelectedListener);
        selectClient.setOnItemSelectedListener(onItemClientSelectedListener);
        selectContact.setOnItemSelectedListener(onItemContactSelectedListener);
        populateArray();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Quote");
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuote();
            }
        });
    }

    public void getRates(final RateListener rateListener){
        QuoteService quoteService = ServiceGenerator.createService(QuoteService.class, username, password);
        Call<List<Rate>> rateCall = quoteService.getRate(companyId.intValue());
       rateCall.enqueue(new Callback<List<Rate>>() {
           @Override
           public void onResponse(Call<List<Rate>> call, final Response<List<Rate>> response) {
               if(response.isSuccessful()){
                   final List<Rate> rateList = response.body();
                   btnCreate.setEnabled(true);
                   AsyncTask.execute(new Runnable() {
                       @Override
                       public void run() {
                           for (Rate r : rateList){
                               mdb.rateDao().insertRate(r);
                           }
                       }
                   });
                   rateListener.rates(response.body());
               }
           }

           @Override
           public void onFailure(Call<List<Rate>> call, Throwable t) {
               btnCreate.setEnabled(false);
               Snackbar snackbar = Snackbar
                       .make(selectBank, ""+t.getMessage(), Snackbar.LENGTH_INDEFINITE)
                       .setAction("Retry", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               populateArray();
                           }
                       });

               snackbar.show();
           }
       });
    }
    public void getBank(final BankListener bankListener){
        QuoteService quoteService = ServiceGenerator.createService(QuoteService.class, username, password);
        Call<List<Bank>>  bankCall = quoteService.getBank(companyId.intValue());
        bankCall.enqueue(new Callback<List<Bank>>() {
            @Override
            public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                if (response.isSuccessful()) {
                    Log.e("Bank :::", ServiceGenerator.gson.toJson(response.body()));
                    final List<Bank> bankList = response.body();
                    btnCreate.setEnabled(true);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (Bank b : bankList) {
                                mdb.bankDao().insertBank(b);
                            }
                        }
                    });


                    bankListener.getActiveBank(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Bank>> call, Throwable t) {
                btnCreate.setEnabled(false);
                Snackbar snackbar = Snackbar
                        .make(selectBank, ""+t.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               populateArray();
                            }
                        });

                snackbar.show();
            }
        });
    }
    public void getClients(){

          final List<Client>  clientList = new ArrayList<>();
          AsyncTask.execute(new Runnable() {
              @Override
              public void run() {
                  List<Client> list = mdb.clientRepository().getAllSynced();
                  clientList.addAll(list);
              }
          });
        clientAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, clientList);
        selectClient.setAdapter(clientAdapter);




    }
    public void populateArray(){
        getRates(new RateListener() {
            @Override
            public void rates(List<Rate> rateList) {
                for (Rate r : rateList) {
                    if(r.getActive()){
                        textRate.setText(r.getName()+" [ "+r.getRate()+" ]");
                    }
                }
                rateSpinnerAdapter = new RateSpinnerAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, rateList);
                selectRate.setAdapter(rateSpinnerAdapter);
            }
        });


         getBank(new BankListener() {
             @Override
             public void getActiveBank(List<Bank> bankList) {
                 for (Bank b : bankList) {
                     if(b.getActive()){
                         textBank.setText(b.getBank());
                     }
                 }
                 bankSpinnerAdapter = new BankSpinnerAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, bankList);
                 selectBank.setAdapter(bankSpinnerAdapter);
             }
         });
        getClients();

    }

    private SearchableSpinner.OnItemSelectedListener onItemRateSelectedListener = new SearchableSpinner.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final Rate rat = (Rate) parent.getItemAtPosition(position);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Rate r = new Rate();
                    r.setId(rat.getId());
                    r.setRate(rat.getRate());

                    r.setActive(true);
                    Log.e("ID", rat.getId()+"");
                    mdb.rateDao().updateRate(r);
                    List<Rate> list = mdb.rateDao().getRates();
                    for (Rate rr : list){
                        if(rr.getId()!=rat.getId()){
                            rr.setActive(false);
                            mdb.rateDao().updateRate(rr);
                            Log.e("Rate Update", "Success");
                        }
                    }
                }
            });

            textRate.setText(selectRate.getSelectedItem().toString());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private SearchableSpinner.OnItemSelectedListener onItemBankSelectedListener = new SearchableSpinner.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final Bank bank = (Bank) parent.getItemAtPosition(position);
            bank.setActive(true);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    mdb.bankDao().updateBank(bank);
                    List<Bank> list = mdb.bankDao().getBanks();
                    for (Bank b : list){
                        if(b.getId()!=bank.getId()){
                            b.setActive(false);
                            mdb.bankDao().updateBank(b);
                            Log.e("Bank Update", "Success");
                        }
                    }
                }
            });
            textBank.setText(selectBank.getSelectedItem().toString());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private SearchableSpinner.OnItemSelectedListener onItemClientSelectedListener = new SearchableSpinner.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Client client = (Client) parent.getItemAtPosition(position);
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            Toast.makeText(getApplicationContext(), client.getName(), Toast.LENGTH_SHORT).show();
            getContacts(client.getRealId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private SearchableSpinner.OnItemSelectedListener onItemContactSelectedListener = new SearchableSpinner.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Contact contact = (Contact) parent.getItemAtPosition(position);
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

            if(contact.getEmail()==null || contact.getEmail().isEmpty() || contact.getEmail().length()<5){
                notFound.setVisibility(View.VISIBLE);
                btnCreate.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Invalid Contact", Toast.LENGTH_SHORT).show();
            }
            else{
                notFound.setVisibility(View.GONE);
                btnCreate.setEnabled(true);
                Toast.makeText(getApplicationContext(), contact.getEmail(), Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void getContacts(final int clientId){
      final List<Contact> contactList = new ArrayList<>();
      AsyncTask.execute(new Runnable() {
          @Override
          public void run() {
              List<Contact>  contacts = mdb.contactDao().getAllSyncedAndClient(clientId);
              contactList.addAll(contacts);
          }
      });
        contactAdapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, contactList);
        selectContact.setAdapter(contactAdapter);
    }

    /**
     * Button OnClick
     * @param
     */

    public void createQuote(){

      //  if( !ProductValidations.setSpinnerError(selectClient, getApplicationContext()) || !ProductValidations.setSpinnerError(selectContact, getApplicationContext()) ){
      ///      return;
     //   }
        if(selectClient.getSelectedItem()==null){
            Toast.makeText(this, "Client Required", Toast.LENGTH_SHORT).show();
            return;
        }

        if(selectContact.getSelectedItem()==null){
            Toast.makeText(this, "Contact Required", Toast.LENGTH_SHORT).show();
            return;
        }
        Client client = (Client) selectClient.getSelectedItem();
        Contact contact = (Contact)selectContact.getSelectedItem();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                rate = mdb.rateDao().getActiveRate(true);
                bank = mdb.bankDao().getActiveBank(true);
            }
        });
        if(rate !=null && bank!=null && client!=null && contact!=null){
           final QuoteTemp qt = new QuoteTemp();
            qt.setBankingDetail((long)bank.getId());
            qt.setClient((long)client.getRealId());
            qt.setCurrency((long)rate.getId());
            qt.setNumOfItems(0);
            qt.setTotal(0.0);
            qt.setVat(0.0);
            qt.setTotalIncVat(0.0);
            qt.setCompany(companyId);
            qt.setContact((long)contact.getRealId());

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Long qid =  mdb.quoteTempDao().insertQuoteTemp(qt);
                     int max = mdb.quoteTempDao().getMaxId();
                     quoteId = (long)max;
                     Log.e("IDS QUOTE", "Return::"+qid+"Max::"+quoteId);
                    Log.e("QuoteTemp Id", quoteId+"");
                    Intent intent = new Intent(getApplicationContext(), AddQuoteItemsActivity.class);
                    intent.putExtra("quoteId", quoteId);
                    startActivity(intent);
                    finish();

                }
            });


        }
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
