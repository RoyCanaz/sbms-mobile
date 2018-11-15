package org.totalit.sbms.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.totalit.sbms.ClassImplements.DatabaseQuery;
import org.totalit.sbms.ClassImplements.SpinnerOnItemSelectedListener;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Repository.ListCategory;
import org.totalit.sbms.SendAndReceive.GetCategoryId;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.ClientInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientInventoryFragment extends Fragment {

    private TextInputLayout inputLayoutBrand, inputLayoutModel, inputLayoutToner;
    private EditText inputBrand, inputModel, inputQuantity, inputDescrpition, inputToner;
    private Button saveInventoryBtn, goNext;
    private Spinner inputBrandSpinner, inputNeedMaSpinner;
    AppDatabase mdb;
    int clientId;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_client_inventory, container, false);
         mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientId = this.getArguments().getInt("clientId");
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        inputLayoutModel = view.findViewById(R.id.input_layout_model);
      //  inputBrand = view.findViewById(R.id.brand_spinner);
        inputModel = view.findViewById(R.id.input_model);
        inputQuantity = view.findViewById(R.id.input_quantity);
        inputDescrpition = view.findViewById(R.id.input_description_inventory);
        saveInventoryBtn = view.findViewById(R.id.saveInventory);
        inputToner = view.findViewById(R.id.input_toner_type);
        goNext = view.findViewById(R.id.goNext_btn_in_iventory);
        inputBrandSpinner = view.findViewById(R.id.brand_spinner);
        inputLayoutToner = view.findViewById(R.id.input_layout_toner_type);
      //  ArrayList<Category> categor = getCategories();
        ArrayAdapter adapter = null;
        try {
            adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, getCategories());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        inputBrandSpinner.setAdapter(adapter);
        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToClientManagement(v);
            }
        });
       // categoryListener(view)
        inputBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sel = parent.getItemAtPosition(position).toString();
                if (sel.startsWith("Prin")){
                    inputLayoutToner.setVisibility(View.VISIBLE);
                    inputToner.setVisibility(View.VISIBLE);
                }
                else{
                    inputLayoutToner.setVisibility(View.GONE);
                    inputToner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        needmaintListener(view);
        saveInventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveInventory(v);
                } catch (ExecutionException |InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
  /*  private void categoryListener(View view){
      //  inputBrandSpinner = view.findViewById(R.id.brand_spinner);
        inputBrandSpinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener(view));
    }*/
    private void needmaintListener(View view){
        inputNeedMaSpinner = view.findViewById(R.id.inventory_spinner_need_mantain);
        inputNeedMaSpinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener(view));
    }
    private void saveInventory(View v) throws ExecutionException, InterruptedException {
        final ClientInventory c = new ClientInventory();
        String catName = inputBrandSpinner.getSelectedItem().toString();
        AsyncTask<String, Void, Integer >  getId = new GetCategoryId(mdb).execute(catName);
        int catId = getId.get();
        c.setSyncStatus(SyncStatus.NOT_SEND);
        c.setCategory(catId);
        c.setModel(inputModel.getText().toString());
        c.setQuantity(Integer.parseInt(inputQuantity.getText() == null ? "0" : inputQuantity.getText().toString()));
        c.setDescription(inputDescrpition.getText().toString());
        c.setNeedMaintenence(inputNeedMaSpinner.getSelectedItem().toString());
        c.setTonerType(inputToner.getText().toString());
        c.setClientId(clientId);
        c.setCreatedBy(sharedPreferences.getInt(LoginActivity.idPref, 0));
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.clientInventoryDao().insertUser(c);
            }
        });
        Toast.makeText(getActivity().getApplicationContext(), "Inventory Saved successfully", Toast.LENGTH_LONG).show();
        clearFields();
    }


    private ArrayList<String> getCategories() throws ExecutionException, InterruptedException {
         List<Category> categoryList = new ListCategory(mdb).execute().get();
         ArrayList<String> categoryArrayList = new ArrayList<>();
         for(Category category : categoryList){
             categoryArrayList.add(category.getName());
         }
         return categoryArrayList;
    }


    private void navigateToClientManagement(View view) {
        Fragment fragment = null;
        fragment = new ClientManagementHomeFragment();
        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
    public void clearFields(){
        inputModel.setText("");
        inputQuantity.setText("");
        inputDescrpition.setText("");
    }



}
