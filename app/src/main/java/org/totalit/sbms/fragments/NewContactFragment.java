package org.totalit.sbms.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Contact;

public class NewContactFragment extends Fragment {
    EditText inputFirstName, inputLastName, inputAddress, inputOfficePhone, inputMobilePhone, inputEmail, inputJobPosition, inputDepartment, inputGender;
    Button savecontactbtn;
    TextView nameClient;
    AppDatabase mdb;
    int clientId;
    int uuid;
    String clientName;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_client_contact, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        nameClient = view.findViewById(R.id.title_add_contact_client_name);
        nameClient.setText(clientName);

        savecontactbtn = view.findViewById(R.id.save_contact_btn);
        inputFirstName = view.findViewById(R.id.add_contact_input_firstname);
        inputLastName = view.findViewById(R.id.add_contact_input_lastname);
        inputOfficePhone = view.findViewById(R.id.add_contact_input_officephone);
        inputMobilePhone = view.findViewById(R.id.add_contact_input_phone);
        inputEmail = view.findViewById(R.id.add_contact_input_email);
        inputJobPosition = view.findViewById(R.id.add_contact_input_jobposition);
        inputDepartment = view.findViewById(R.id.add_contact_input_department);
        savecontactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact(v);
            }
        });

        return view;
    }
    public void saveContact(View v){
        final Contact c = new Contact();
        c.setSyncStatus(SyncStatus.NOT_SEND);
        c.setFirstName(inputFirstName.getText().toString());
        c.setLastName(inputLastName.getText().toString());
        c.setOfficePhone(inputOfficePhone.getText().toString());
        c.setMobilePhone(inputMobilePhone.getText().toString());
        c.setEmail(inputEmail.getText().toString());
        c.setJobPosition(inputJobPosition.getText().toString());
        c.setDepartment(inputDepartment.getText().toString());
        c.setCreatedBy(sharedPreferences.getInt(LoginActivity.idPref, 0));
        if(uuid==0) {
            c.setClientId(clientId);
        }
        else{
            c.setClientId(uuid);
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.contactDao().insertContact(c);

            }
        });
        Toast.makeText(getActivity().getApplicationContext(), "Contact Saved successfully", Toast.LENGTH_LONG).show();
        clearFields();

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putInt("clientId", clientId);
        bundle.putInt("uuid", uuid);
        bundle.putString("clientName", clientName);

        fragment = new ClientContactsFragment();
        fragment.setArguments(bundle);



        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
    public void clearFields(){
        inputFirstName.setText("");
        inputLastName.setText("");
        inputDepartment.setText("");
        inputJobPosition.setText("");
        inputMobilePhone.setText("");
        inputOfficePhone.setText("");
        inputEmail.setText("");

    }
}

