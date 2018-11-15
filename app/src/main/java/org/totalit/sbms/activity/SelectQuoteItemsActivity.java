package org.totalit.sbms.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.totalit.sbms.CallListerner.ProductListListener;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.adapters.ProductListAdapter;
import org.totalit.sbms.adapters.SelectQuoteItemAdapter;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.retrofit.ProductService;
import org.totalit.sbms.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectQuoteItemsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.recycler_select_quote_item)
    RecyclerView recyclerView;

    @BindView(R.id.fab_accept_items)
    FloatingActionButton fab;

    RecyclerView.LayoutManager layoutManager;
    AppDatabase mdb;

    SelectQuoteItemAdapter selectQuoteItemAdapter;
    SharedPreferences sharedPreferences;
    String username;
    String password;
    Long companyId;
    ProgressDialog progressDialog;
    List<Product> productsList;
    Long quoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_quote_items);
        ButterKnife.bind(this);
        mdb = AppDatabase.getFileDatabase(this);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0);
        productsList = new ArrayList<>();

        Intent i = getIntent();
        quoteId = i.getLongExtra("quoteId", 0);

        getAllProducts(companyId, new ProductListListener() {
            @Override
            public void productList(List<Product> products) {
                productsList.addAll(products);
                selectQuoteItemAdapter  = new SelectQuoteItemAdapter(products, getApplicationContext(), mdb, quoteId);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter( selectQuoteItemAdapter);
                selectQuoteItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void product(Product product) {

            }
        });

        selectQuoteItemAdapter  = new SelectQuoteItemAdapter(productsList, getApplicationContext(), mdb, quoteId);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter( selectQuoteItemAdapter);
        selectQuoteItemAdapter.notifyDataSetChanged();
        getSupportActionBar().setTitle("Select Quote Items");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddQuoteItemsActivity.class);
                intent.putExtra("quoteId", quoteId);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String searchText = newText.toLowerCase();
        List<Product> products = new ArrayList<>();
        for(Product product : productsList){
            if(product.getBrand().toLowerCase().contains(searchText)){
                products.add(product);
            }
            else if (product.getModel().toLowerCase().contains(searchText)){
                products.add(product);
            }
            else if(product.getProductCode().toLowerCase().contains(searchText)){
                products.add(product);
            }
        }
        selectQuoteItemAdapter.updateList(products);
        return true;

    }
    public void getAllProducts(Long companyId, final ProductListListener listener){

        progressDialog.setTitle("Loading. Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        ProductService productService = ServiceGenerator.createService(ProductService.class, username, password);
        Call<List<Product>> productsCall = productService.getAllProducts(companyId);
        productsCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    Log.e("Product Data",ServiceGenerator.gson.toJson(response.body()));
                    progressDialog.dismiss();
                    listener.productList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Product List Err", t.getMessage()+"");
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
