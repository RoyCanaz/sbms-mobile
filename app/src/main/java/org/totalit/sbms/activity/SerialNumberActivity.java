package org.totalit.sbms.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.adapters.SerialNumberAdapter;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.Stock;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SerialNumberActivity extends AppCompatActivity {

    @BindView(R.id.recycler_capture_serial_number)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    AppDatabase mdb;
    ArrayList<Stock> stockList;
    SerialNumberAdapter serialNumberAdapter;
    SharedPreferences sharedPreferences;
    String username;
    String password;
    String brand;
    String model;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_number_activity);
        ButterKnife.bind(this);
        mdb = AppDatabase.getFileDatabase(this);
        sharedPreferences = getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        Intent i = getIntent();
        model = i.getStringExtra("model");
        brand = i.getStringExtra("brand");
        getSupportActionBar().setTitle(brand+" / "+model);
        Bundle bundle = i.getExtras();
        stockList = bundle.getParcelableArrayList("StockList");
        serialNumberAdapter  = new SerialNumberAdapter(stockList, username, password, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(serialNumberAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode == CommonStatusCodes.SUCCESS){

                    if(data!=null) {
                        Barcode barcode = data.getParcelableExtra("barcode");
                        int stockId = data.getIntExtra("stockId", 0);
                        ArrayList<Stock> stocks = new ArrayList<>();
                        for (Stock s : stockList) {
                            if (s.getId() == stockId) {
                                s.setSerialNumber(barcode.displayValue);
                            }
                            stocks.add(s);
                        }
                        serialNumberAdapter.updateSerial(stocks);
                        Toast.makeText(this, barcode.displayValue, Toast.LENGTH_LONG).show();
                    }
                    else {

                        Toast.makeText(this, "No Result Found", Toast.LENGTH_LONG).show();
                    }

            }
        }

    }
}
