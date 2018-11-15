package org.totalit.sbms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.SendAndReceive.BranchData;
import org.totalit.sbms.SendAndReceive.ClientData;
import org.totalit.sbms.SendAndReceive.ClientInventoryData;
import org.totalit.sbms.SendAndReceive.ContactsData;
import org.totalit.sbms.SendAndReceive.ProcurementDocsData;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.domain.Supplier;
import org.totalit.sbms.domain.User;
import org.totalit.sbms.retrofit.BranchService;
import org.totalit.sbms.retrofit.CategoryService;
import org.totalit.sbms.retrofit.ClientInventoryService;
import org.totalit.sbms.retrofit.ClientService;
import org.totalit.sbms.retrofit.ContactService;
import org.totalit.sbms.retrofit.ProcurementDocsservice;
import org.totalit.sbms.retrofit.RetrofitConfig;
import org.totalit.sbms.retrofit.ServiceGenerator;
import org.totalit.sbms.retrofit.StockService;
import org.totalit.sbms.retrofit.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http2.Http2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.HTTP;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputLayoutUsername, inputLayoutPwd;
    private TextView userName, companyName;
    private TextView password;
    private Button loginBtn;
    private CheckBox rememberMe;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String loginPref = "loginPref";
    public static final String usernamePref = "username";
    public static final String passwordPref = "password";
    public static final String activePref = "active";
    public static final String fullNamePref = "fullName";
    public static final String companyIdPref = "company";
    public static final String companyNamePref = "companyName";
    public static final String idPref = "userId";
    public static final String rolePref = "role";

    AppDatabase mdb;
   // UserService userService;
   // CategoryService categoryService;

   // Retrofit retrofit;
    List<Client> clients;
    List<Category> categoryList;
    List<Supplier> supplierList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mdb = AppDatabase.getFileDatabase(this);
        sharedPreferences = getSharedPreferences(loginPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        inputLayoutUsername = findViewById(R.id.input_layout_username);
        inputLayoutPwd = findViewById(R.id.input_layout_pwd);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        companyName = findViewById(R.id.company_name);
        loginBtn = findViewById(R.id.loginBtn);
        rememberMe = findViewById(R.id.remember_me);
        progressDialog = new ProgressDialog(this);

        if(sharedPreferences.contains(usernamePref)){
            userName.setText(sharedPreferences.getString(usernamePref, ""));
            password.setText(sharedPreferences.getString(passwordPref, ""));
            companyName.setText(sharedPreferences.getString(companyNamePref, ""));
            rememberMe.setChecked(true);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    login();

            }
        });

    }



    private void getCategories(String usern, String pass, int company){

         categoryList = new ArrayList<>();
        CategoryService categoryService = ServiceGenerator.createService(CategoryService.class, usern, pass);
        Call <List<Category>> categoryCall = categoryService.getCategories(company);
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, final Response<List<Category>> response) {
                if(response.isSuccessful()){
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            categoryList = response.body();
                            for (Category cat : categoryList) {
                                mdb.categoryDao().insertCategory(cat);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("LoginActivity", "onFailure Categories" + t.getMessage());
               // Toast.makeText(LoginActivity.this, "Error encounted : NetWork Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getSuppliers(String usern, String pass, int company){

        supplierList = new ArrayList<>();
        StockService stockService = ServiceGenerator.createService(StockService.class, usern, pass);
        final Call <List<Supplier>> supplierCall = stockService.getSuppliers(company);
       supplierCall.enqueue(new Callback<List<Supplier>>() {
           @Override
           public void onResponse(Call<List<Supplier>> call, final Response<List<Supplier>> response) {
                    if(response.isSuccessful()){
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                supplierList = response.body();
                              for (Supplier supplier : supplierList){
                                  mdb.supplierDao().insertSupplier(supplier);
                              }

                            }
                        });

                    }
           }

           @Override
           public void onFailure(Call<List<Supplier>> call, Throwable t) {

           }
       });

    }

    private void login() {
        final String username = userName.getText().toString();
        final String pwd = password.getText().toString();
        progressDialog.setTitle("Signing in. Please wait.....");
        progressDialog.setCancelable(true);
        progressDialog.show();
       // final User user = new User();
      //  user.setUserName(username);
      //  user.setPassword(pwd);

        UserService userService = ServiceGenerator.createService(UserService.class, username, pwd);
        Call<User> userCall = userService.login(username);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()){
                    progressDialog.hide();
                /*    User responseUser = new User();
                    responseUser.setActive(response.body().getActive());
                    responseUser.setFirstName(response.body().getFirstName());
                    responseUser.setLastName(response.body().getLastName());
                    responseUser.setUserName(response.body().getUserName());
                    responseUser.setPassword(pwd);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            mdb.userRepository().insertUser(user);
                        }
                    });*/

                    String fullName = response.body().getFirstName() + " " + response.body().getLastName();
                    if(rememberMe.isChecked()) {
                        editor.putBoolean("saveCredentials", true);
                    }
                    else{
                        editor.putBoolean("saveCredentials", false);
                    }
                    editor.putInt(idPref, response.body().getId());
                    editor.putString(usernamePref, response.body().getUserName());
                    editor.putString(passwordPref, pwd);
                    editor.putBoolean(activePref, response.body().getActive());
                    editor.putString(fullNamePref, fullName);
                    editor.putLong(companyIdPref, response.body().getCompanyId());
                    editor.putString(companyNamePref, response.body().getCompanyName());
                    editor.putString(rolePref, response.body().getRole());
                    editor.commit();
                    getCategories(username, pwd, response.body().getCompanyId());
                    getSuppliers(username, pwd, response.body().getCompanyId());
                    Toast.makeText(LoginActivity.this, "Login Successful !!!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                    startActivity(intent);

                }else{
                    progressDialog.hide();
                    Toast.makeText(LoginActivity.this, "Incorrect Username/Password", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Data", ServiceGenerator.gson.toJson(call));
                Log.e("OnFailure", t.getMessage()+"");
                progressDialog.hide();
                 if(sharedPreferences.contains(usernamePref)){
                     loginUsingSharedPreferences();
                 }
                 else{
                     Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                 }


            }
        });
    }
    public void loginUsingSharedPreferences(){
        String username = userName.getText().toString();
        String pwd = password.getText().toString();
        if(username.equals(sharedPreferences.getString(usernamePref, "")) && pwd.equals(sharedPreferences.getString(passwordPref, ""))){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(LoginActivity.this, "Incorrect Username/Password", Toast.LENGTH_LONG).show();
        }
    }

}




