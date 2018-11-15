package org.totalit.sbms.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.ClassImplements.SpinnerOnItemSelectedListener;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.ClientInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewInventoryFragment extends Fragment {

    private TextInputLayout inputLayoutBrand, inputLayoutModel, inputLayoutToner;
    private EditText inputBrand, inputModel, inputQuantity, inputDescrpition, inputToner;
    private Button saveInventoryBtn, goNext;
    private Spinner inputBrandSpinner, inputNeedMaSpinner;
    private TextView clientNameHeading;
    AppDatabase mdb;
    int clientId;
    int uuid;
    String clientName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_client_inventory, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");
         clientNameHeading = view.findViewById(R.id.title_add_inventory_client_name);
         clientNameHeading.setText(clientName);
        inputLayoutModel = view.findViewById(R.id.input_layout_model);
        inputModel = view.findViewById(R.id.input_model);
        inputQuantity = view.findViewById(R.id.input_quantity);
        inputDescrpition = view.findViewById(R.id.input_description_inventory);
        saveInventoryBtn = view.findViewById(R.id.saveInventory);
        inputBrandSpinner = view.findViewById(R.id.brand_spinner_client);
        inputToner = view.findViewById(R.id.input_toner_type);
        inputLayoutToner = view.findViewById(R.id.input_layout_toner_type);
       // categoryListener(view);
        ArrayAdapter adapter = null;
        try {
            adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, getCategories());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        inputBrandSpinner.setAdapter(adapter);
        needmaintListener(view);
        saveInventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInventory(v);
            }
        });
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
        return view;
    }
    /*
    private void categoryListener(View view){
        inputBrandSpinner = view.findViewById(R.id.brand_spinner_client);
        inputBrandSpinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener(view));
    }*/
    private void needmaintListener(View view){
        inputNeedMaSpinner = view.findViewById(R.id.inventory_spinner_need_mantain);
        inputNeedMaSpinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener(view));
    }
    private ArrayList<String> getCategories() throws ExecutionException, InterruptedException {
        List<Category> categoryList = new CategoryNames(mdb).execute().get();
        ArrayList<String> categoryArrayList = new ArrayList<>();
        for(Category category : categoryList){
            categoryArrayList.add(category.getName());
        }
        return categoryArrayList;
    }
    private void saveInventory(View v){
        final ClientInventory c = new ClientInventory();
        c.setSyncStatus(SyncStatus.NOT_SEND);
        c.setCategory(1);
        c.setModel(inputModel.getText().toString());
        c.setQuantity(Integer.parseInt(inputQuantity.getText() == null ? "0" : inputQuantity.getText().toString()));
        c.setDescription(inputDescrpition.getText().toString());
        c.setNeedMaintenence(inputNeedMaSpinner.getSelectedItem().toString());
        c.setTonerType(inputToner.getText().toString());
        if(uuid==0) {
            c.setClientId(clientId);
        }
        else{
            c.setClientId(uuid);
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.clientInventoryDao().insertUser(c);
            }
        });
        Toast.makeText(getActivity().getApplicationContext(), "Inventory Saved successfully", Toast.LENGTH_LONG).show();
        clearFields();

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putInt("clientId", clientId);
        bundle.putInt("uuid", uuid);
        bundle.putString("clientName", clientName);

        fragment = new ClientInventoriesFragment();
        fragment.setArguments(bundle);



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
    public class CategoryNames extends AsyncTask<Void, Void, List<Category>> {
        List<Category> catNames = new ArrayList<>();
        AppDatabase mdb;


        public CategoryNames(AppDatabase mdb) {
            this.mdb = mdb;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            catNames = mdb.categoryDao().getNames();
            return catNames;
        }

    }
}
