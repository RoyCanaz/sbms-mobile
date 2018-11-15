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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.totalit.sbms.ClassImplements.SpinnerSelected;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.Utils.Validations;
import org.totalit.sbms.domain.Client;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class FragmentEditClientDetails extends Fragment {
    private EditText inputName, inputEmail, inputDescription, inputPhone, inputAddress, inputWebsite;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutDescription, inputLayoutPhone, inputLayoutAddress, inputLayoutWebsite;
    private Button saveClient;
    private Spinner branchSpinner;
    String companyOrIndividual;
    RadioGroup clientType;
    RadioButton radioCom, radioInd;
    SharedPreferences sharedPreferences;
    int clientDbId;
    int uuid;
    String clientName;
    AppDatabase mdb;
    Client client;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_client_details,container, false);

        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        clientDbId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");

        inputLayoutName = (TextInputLayout) view.findViewById(R.id.edit_input_layout_name);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.edit_input_layout_email);
        inputLayoutDescription = (TextInputLayout) view.findViewById(R.id.edit_input_layout_description);
        inputLayoutPhone = (TextInputLayout) view.findViewById(R.id.edit_input_layout_phone);
        inputLayoutAddress = (TextInputLayout) view.findViewById(R.id.edit_input_layout_address);
        inputLayoutWebsite = (TextInputLayout) view.findViewById(R.id.edit_input_layout_website);
        inputName = (EditText)view.findViewById(R.id.edit_input_name);
        inputEmail = (EditText)view.findViewById(R.id.edit_input_email);
        inputDescription = (EditText)view.findViewById(R.id.edit_input_description);
        inputPhone = (EditText)view.findViewById(R.id.edit_input_phone);
        inputAddress = (EditText)view.findViewById(R.id.edit_input_address);
        inputWebsite = (EditText)view.findViewById(R.id.edit_input_website);

        clientType = view.findViewById(R.id.edit_client_type_radio_group);
        saveClient = (Button)view.findViewById(R.id.edit_saveClientBtn);
        radioCom = view.findViewById(R.id.edit_radio_company);
        radioInd = view.findViewById(R.id.edit_radio_individual);
        radioCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyOrIndividual = radioCom.getText().toString();
            }
        });
        radioInd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyOrIndividual = radioInd.getText().toString();
            }
        });
        branchListenerr(view);
        saveClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveClientClicked(view);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        setValues();
        return view;
    }



    private void branchListenerr(View view){
        branchSpinner = view.findViewById(R.id.edit_branch_spinner);
        branchSpinner.setOnItemSelectedListener(new SpinnerSelected());
    }

    private void setValues(){
        Toast.makeText(getActivity().getApplicationContext(), String.valueOf(uuid), Toast.LENGTH_LONG).show();
       try {
            client = new LoadClient(mdb, clientDbId, uuid).execute().get();

          if(client.getClientType().equalsIgnoreCase("Company")){
         //  if( client.getClientType()==null){
                companyOrIndividual = "Company";
                radioCom.setChecked(true);
            }
            else{
                companyOrIndividual = "Individual";
                radioInd.setChecked(true);
            }
            if(client.getBranch()==null){
                branchSpinner.setSelection(0);
            }
            else if(client.getBranch().equals("Yes")){
                branchSpinner.setSelection(1);
            }
            else {
                branchSpinner.setSelection(0);
            }
            inputName.setText(client.getName()==null ? "" : client.getName());
            inputDescription.setText(client.getDescription()==null ? "" : client.getDescription());
            inputAddress.setText(client.getAddress()==null ? "" : client.getAddress());
            inputEmail.setText(client.getEmail()==null ? "" : client.getEmail());
            inputWebsite.setText(client.getWebsite()==null ? "" : client.getWebsite());
            inputPhone.setText(client.getPhone()==null ? "" : client.getPhone());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void saveClientClicked(View view) throws ExecutionException, InterruptedException {
        if ( !validateRadio() | !validateName() | !validateEmail() ) {
            return;
        }

        else {
           // Date dateNow = new Date();
            final Client client = new Client();
            client.setId(clientDbId);
            client.setSyncStatus(SyncStatus.NOT_SEND);
            client.setRealId(uuid);
            client.setName(inputName.getText().toString());
            client.setCreatedBy(sharedPreferences.getInt(LoginActivity.idPref, 0));
            client.setClientType(companyOrIndividual);
            client.setAddress(inputAddress.getText().toString());
            client.setBranch(branchSpinner.getSelectedItem().toString());
            client.setDescription(inputDescription.getText().toString());
            client.setEmail(inputEmail.getText().toString());
            client.setPhone(inputPhone.getText().toString());
            client.setWebsite(inputWebsite.getText().toString());
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    mdb.clientRepository().insertUser(client);

                }
            });

            Toast.makeText(getActivity().getApplicationContext(), "Client Saved successfully", Toast.LENGTH_LONG).show();

            Fragment fragment = null;
            Bundle bundle = new Bundle();
            bundle.putInt("clientId", clientDbId);
            bundle.putInt("uuid", uuid);
            bundle.putString("clientName", inputName.getText().toString());

                fragment = new ClientProfileFragment();
                fragment.setArguments(bundle);



            if (fragment != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateRadio(){
        if(clientType.getCheckedRadioButtonId()==-1){
            Toast.makeText(getActivity().getApplicationContext(), "Please Select Client Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !Validations.isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_input_name:
                    validateName();
                    break;
                case R.id.edit_input_email:
                    validateEmail();
                    break;

            }
        }
    }

    public  class LoadClient extends AsyncTask<Void, Void, Client> {

        AppDatabase mdb;
        int id;
        int uuid;

        public LoadClient(AppDatabase mdb, int id, int uuid) {
            this.id = id;
            this.mdb = mdb;
            this.uuid = uuid;
        }

        @Override
        protected Client doInBackground(Void... voids) {

          /*  Client client = mdb.clientRepository().findOneById(id);

            Client cl = mdb.clientRepository().findOneByRealId(id);
            if(client==null){
                return cl;
            }
            else {
                return client;

            }*/
            Client client = mdb.clientRepository().findOneByIdAndUuid(id, uuid);
            return client;
        }


    }

}
