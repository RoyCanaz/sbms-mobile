package org.totalit.sbms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.SendAndReceive.ClientData;
import org.totalit.sbms.Sync.PullService;
import org.totalit.sbms.SyncData.SyncAll;
import org.totalit.sbms.SyncTasks.SyncSendBeforeDelete;
import org.totalit.sbms.SyncTasks.UpdateClientTask;
import org.totalit.sbms.SyncTasks.Wrapper;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.fragments.ClientManagementHomeFragment;
import org.totalit.sbms.fragments.FragmentTakePicture;
import org.totalit.sbms.fragments.Inventory.InventoryCategories;
import org.totalit.sbms.fragments.Inventory.SalesHomeDashBoard;
import org.totalit.sbms.fragments.VisitPlanFragment;
import org.totalit.sbms.retrofit.BranchService;
import org.totalit.sbms.retrofit.CategoryService;
import org.totalit.sbms.retrofit.ClientInventoryService;
import org.totalit.sbms.retrofit.ClientService;
import org.totalit.sbms.retrofit.ContactService;
import org.totalit.sbms.retrofit.ProcurementDocsservice;
import org.totalit.sbms.retrofit.RetrofitConfig;
import org.totalit.sbms.retrofit.ServiceGenerator;
import org.totalit.sbms.retrofit.UserService;


import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView recyclerView;
    //RecyclerView.Adapter adapter;

    RecyclerView.LayoutManager layoutManager;
    List<Client> clients;
    public TextView email, companyName;
    public TextView fullName;
    SharedPreferences sharedPreferences;
    AppDatabase mdb;
    ProgressDialog progressDialog;


    Long companyId;
    String username;
    String password;
    String companyN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navBar);

        View navHeaderView = navigationView.getHeaderView(0);
        drawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);
        sharedPreferences = getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);

        email = navHeaderView.findViewById(R.id.email_user);
        fullName = navHeaderView.findViewById(R.id.fullName_user);
        companyName = navHeaderView.findViewById(R.id.com_name);
        progressDialog = new ProgressDialog(this);
        mdb = AppDatabase.getFileDatabase(this);
        if (sharedPreferences.contains(LoginActivity.usernamePref)) {
            username = sharedPreferences.getString(LoginActivity.usernamePref, "");
            String fullname = sharedPreferences.getString(LoginActivity.fullNamePref, "");
            companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0l);
            username = sharedPreferences.getString(LoginActivity.usernamePref, "");
            password = sharedPreferences.getString(LoginActivity.passwordPref, "");
            companyN = sharedPreferences.getString(LoginActivity.companyNamePref, "");
            email.setText(username);
            fullName.setText(fullname);
            companyName.setText(companyN);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mdb = AppDatabase.getFileDatabase(this);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        drawerLayout.closeDrawers();

                        int id = menuItem.getItemId();
                        Fragment fragment = null;
                        Bundle bundle = new Bundle();
                        if (id == R.id.menu_sales) {
                            fragment = new SalesHomeDashBoard();
                            getSupportActionBar().setTitle("Sales Dashboard");

                        }

                     /*  if (id == R.id.menu_clientManagement) {
                          fragment = new ClientManagementHomeFragment();
                           getSupportActionBar().setTitle("Client Management");

                        }

                        if (id == R.id.menu_clientVisitPlan) {
                            fragment = new VisitPlanFragment();
                            getSupportActionBar().setTitle("Client Visit Plan");

                        }
                        if (id == R.id.menu_stock) {
                            fragment = new InventoryCategories();
                            getSupportActionBar().setTitle("Inventory");
                        }
                        */
                        if (id == R.id.totalitweb) {
                            bundle.putString("url", "http://10.0.2.2:8090/login");
                            // bundle.putString("url", "https://www.facebook.com");
                            fragment = new org.totalit.sbms.fragments.WebViewFragment();
                            fragment.setArguments(bundle);
                        }
                        if (fragment != null) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.frame_layout, fragment);
                            ft.addToBackStack(null);
                            ft.commit();

                        }

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.layout_drawer);
                        drawer.closeDrawer(GravityCompat.START);

                        return true;
                    }
                });

        salesDashboard();
    }


    public void salesDashboard(){
        Fragment fragment = null;
        fragment = new SalesHomeDashBoard();
        getSupportActionBar().setTitle("Sales Dashboard");
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
    public void clientMana(){
        Fragment fragment = null;
        fragment = new ClientManagementHomeFragment();
        getSupportActionBar().setTitle("Client Management");
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    AsyncTask<Void, Void, Void> asyncTask;

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (actionBarDrawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_sync) {

            PullService pullService = new PullService(mdb, this, progressDialog);
            try {
                pullService.sendClientData();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Fragment fragment = null;
            fragment = new ClientManagementHomeFragment();
            getSupportActionBar().setTitle("Client Management");
            replaceFragement(fragment);
        }

        if (id == R.id.action_gallery) {
            Fragment fragment = new FragmentTakePicture();
            getSupportActionBar().setTitle("Media");
            replaceFragement(fragment);
            return true;
        }
        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void replaceFragement(Fragment fragment) {
        // linearLayoutHome.setVisibility(View.GONE);

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
            //  getSupportActionBar().setTitle("new title");
        }
    }


    private void syncClientData() throws ExecutionException, InterruptedException {
        List<Client> clients = new ClientData(mdb).execute().get();
        if (clients.size() != 0 || !clients.isEmpty()) {
            ClientService clientService = ServiceGenerator.createService(ClientService.class, username, password);
            for (final Client client : clients) {
                client.setCompanyId(companyId);

                Call<Client> clientCall = clientService.sendClient(client);
                clientCall.enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, retrofit2.Response<Client> response) {

                        if (response.isSuccessful()) {

                            Client cl = new Client();
                            cl.setId(client.getId());
                            cl.setRealId(response.body().getRealId());
                            cl.setSyncStatus(SyncStatus.SEND);
                            cl.setClientType(client.getClientType());
                            cl.setName(client.getName());
                            cl.setEmail(client.getEmail());
                            cl.setDescription(client.getDescription());
                            cl.setPhone(client.getPhone());
                            cl.setBranch(client.getBranch());
                            cl.setWebsite(client.getWebsite());
                            updateClient(cl);

                        } else {
                            Log.e("Error Code", String.valueOf(response.code()));
                            Log.e("Error Body", response.errorBody().contentType().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {
                        Log.d("LoginActivity", "onFailure" + t.getMessage());
                    }
                });
            }
        } else {
            Log.e("Syscn Before Delete", "Syncing Other.......................");
            AsyncTask<Void, Void, Void> asyncTask = new SyncSendBeforeDelete(mdb, progressDialog, this, sharedPreferences.getInt(LoginActivity.idPref, 0));
            asyncTask.execute();
        }
    }

    public void messages(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void updateClient(Client client) {
        int userId = sharedPreferences.getInt(LoginActivity.idPref, 0);
        AsyncTask<Void, Void, Wrapper> asyncTask = new UpdateClientTask(mdb, client, progressDialog, this, userId);
        asyncTask.execute();
    }
/*
    public void syncClientDetails(){
        SyncAll all = new SyncAll(mdb);
        try {
            all.syncBranches();
            all.syncClientInventory();
            all.syncContacts();
            all.syncProcurementDocs();
            all.syncNotes();
            all.syncVisitPlan();
            Toast.makeText(this, "Sync Successful", Toast.LENGTH_LONG).show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error During Sync", Toast.LENGTH_LONG).show();
        }

    }

*/
}
