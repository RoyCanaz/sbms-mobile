package org.totalit.sbms.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
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
import org.totalit.sbms.adapters.SerialNumberAdapter;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Stock;
import org.totalit.sbms.retrofit.ProductService;
import org.totalit.sbms.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductList extends AppCompatActivity implements SearchView.OnQueryTextListener{

    @BindView(R.id.recycler_product_list)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    AppDatabase mdb;

    ProductListAdapter productListAdapter;
    SharedPreferences sharedPreferences;
    String username;
    String password;
    Long categoryId;
    String categoryName;
    Long companyId;
    ProgressDialog progressDialog;
    List<Product> productsList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        mdb = AppDatabase.getFileDatabase(this);
        Intent i = getIntent();
        categoryId = i.getLongExtra("categoryId", 0);
        categoryName = i.getStringExtra("categoryName");
        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0);
        productsList = new ArrayList<>();
        getProductsListByCategory(companyId, categoryId, new ProductListListener() {
            @Override
            public void productList(List<Product> products) {
                productsList.addAll(products);
                productListAdapter  = new ProductListAdapter(products, username, password, getApplicationContext());
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(productListAdapter);
                productListAdapter.notifyDataSetChanged();

            }

            @Override
            public void product(Product product) {

            }
        });
        productListAdapter  = new ProductListAdapter(productsList, username, password, getApplicationContext());
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(categoryName);


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
        productListAdapter.updateList(products);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // onBackPressed();
      //  return true;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public void getProductsListByCategory(Long companyId, Long categoryId, final ProductListListener listener){

        progressDialog.setTitle("Loading. Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        ProductService productService = ServiceGenerator.createService(ProductService.class, username, password);
        Call<List<Product>> productsCall = productService.getProductsByCategory(companyId, categoryId);
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
