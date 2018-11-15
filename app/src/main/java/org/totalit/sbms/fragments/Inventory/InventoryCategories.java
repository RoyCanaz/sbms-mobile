package org.totalit.sbms.fragments.Inventory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.totalit.sbms.CallListerner.OnItemClick;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Repository.ListCategory;
import org.totalit.sbms.activity.ProductList;
import org.totalit.sbms.adapters.ClientAdapter;
import org.totalit.sbms.adapters.CompanyInventoryAdapter;
import org.totalit.sbms.domain.Category;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class InventoryCategories extends Fragment implements OnItemClick {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AppDatabase mdb;
    CompanyInventoryAdapter adapter;
    List<Category>  categoryList;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.company_inventory, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        try {
            categoryList = categoryList = new ListCategory(mdb).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(categoryList!=null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            recyclerView = v.findViewById(R.id.recylce_view_company_inventory);
            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new CompanyInventoryAdapter(categoryList, sharedPreferences.getString(LoginActivity.usernamePref, ""), sharedPreferences.getString(LoginActivity.passwordPref, ""));
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            adapter.setItemClick(this);
        }

        return v;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), ProductList.class);
        intent.putExtra("categoryName", categoryList.get(position).getName());
        Integer catId =  categoryList.get(position).getId();
        intent.putExtra("categoryId",catId.longValue());
        startActivity(intent);
        //getActivity().finish();
    }
}
