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

import org.totalit.sbms.ClassImplements.SpinnerOnItemSelectedListener;
import org.totalit.sbms.ClassImplements.SpinnerSelected;
import org.totalit.sbms.Database.AbstractDb;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.Database.InstantiateDb;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.DateUtil;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.Utils.Validations;
import org.totalit.sbms.domain.Client;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class AddClientFragment extends Fragment {

    private EditText inputName, inputEmail, inputDescription, inputPhone, inputAddress, inputWebsite;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutDescription, inputLayoutPhone, inputLayoutAddress, inputLayoutWebsite;
    private Button saveClient;
    private Spinner branchSpinner;
    String companyOrIndividual;
    RadioGroup clientType;
    RadioButton radioCom, radioInd;
    SharedPreferences sharedPreferences;

    AppDatabase mdb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addclients,container, false);

            mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
            sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);


            inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
            inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
            inputLayoutDescription = (TextInputLayout) view.findViewById(R.id.input_layout_description);
            inputLayoutPhone = (TextInputLayout) view.findViewById(R.id.input_layout_phone);
            inputLayoutAddress = (TextInputLayout) view.findViewById(R.id.input_layout_address);
            inputLayoutWebsite = (TextInputLayout) view.findViewById(R.id.input_layout_website);
            inputName = (EditText)view.findViewById(R.id.input_name);
            inputEmail = (EditText)view.findViewById(R.id.input_email);
            inputDescription = (EditText)view.findViewById(R.id.input_description);
            inputPhone = (EditText)view.findViewById(R.id.input_phone);
            inputAddress = (EditText)view.findViewById(R.id.input_address);
            inputWebsite = (EditText)view.findViewById(R.id.input_website);

           clientType = view.findViewById(R.id.client_type_radio_group);
           saveClient = (Button)view.findViewById(R.id.saveClientBtn);
           radioCom = view.findViewById(R.id.radio_company);
           radioInd = view.findViewById(R.id.radio_individual);
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

        return view;
    }



    private void branchListenerr(View view){
        branchSpinner = view.findViewById(R.id.branch_spinner);
        branchSpinner.setOnItemSelectedListener(new SpinnerSelected());
    }

    private void saveClientClicked(View view) throws ExecutionException, InterruptedException {
        if ( !validateRadio() | !validateName() | !validateEmail() ) {
            return;
        }

       else {
            Date dateNow = new Date();
            final Client client = new Client();
            client.setRealId(0);
            client.setSyncStatus(SyncStatus.NOT_SEND);
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
            int i = new  GetClientId(mdb).execute().get();
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            bundle.putInt("clientId", i);
            if (branchSpinner.getSelectedItem().equals("Yes")) {

                fragment = new BranchFragment();
                fragment.setArguments(bundle);

            } else {
                fragment = new ProcumentDocsFragment();
                fragment.setArguments(bundle);
            }


            if (fragment != null) {
                saveClient.setEnabled(false);
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
        if(inputEmail.getText().toString().isEmpty()){
            return true;
        }

        String email = inputEmail.getText().toString().trim();

        if (!Validations.isValidEmail(email)) {
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
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;

            }
        }
    }
    public class GetClientId extends AsyncTask<Void, Void, Integer> {
        int id = 0;
        AppDatabase mdb;

        public GetClientId(AppDatabase mdb) {
            this.mdb = mdb;
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            id = mdb.clientRepository().getMaxId();

            return id;
        }


    }
}
