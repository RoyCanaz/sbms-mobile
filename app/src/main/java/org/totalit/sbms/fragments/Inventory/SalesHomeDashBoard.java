package org.totalit.sbms.fragments.Inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.totalit.sbms.R;
import org.totalit.sbms.activity.NewQuoteActivity;
import org.totalit.sbms.activity.ProductList;
import org.totalit.sbms.fragments.ClientManagementHomeFragment;
import org.totalit.sbms.fragments.VisitPlanFragment;
import org.totalit.sbms.fragments.quotes.QuoteDashboard;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesHomeDashBoard extends Fragment implements View.OnClickListener {

    @BindView(R.id.card_client_management)
    CardView cardClientMan;
    @BindView(R.id.card_visit_plans)
    CardView cardVisitPlan;
    @BindView(R.id.card_invoices)
    CardView cardInvoice;
    @BindView(R.id.card_quotes)
    CardView cardQuotes;
    @BindView(R.id.card_inventory)
    CardView cardInventory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_home, container, false);
        ButterKnife.bind(this, view);
        cardClientMan.setOnClickListener(this);
        cardVisitPlan.setOnClickListener(this);
        cardInvoice.setOnClickListener(this);
        cardQuotes.setOnClickListener(this);
        cardInventory.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        int item = v.getId();
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if(item==cardClientMan.getId()){
            fragment = new ClientManagementHomeFragment();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Client Management");
        }
        else if(item == cardVisitPlan.getId()){
            fragment = new VisitPlanFragment();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Client Visit Plan");
        }
        else if(item == cardInvoice.getId()){

        }
        else if(item == cardQuotes.getId()){
            fragment = new QuoteDashboard();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Quotes");
        }
        else if(item == cardInventory.getId()){
            fragment = new InventoryCategories();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Inventory");
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Intent not found", Toast.LENGTH_SHORT).show();
        }
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    }
}
