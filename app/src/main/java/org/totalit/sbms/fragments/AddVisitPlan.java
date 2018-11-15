package org.totalit.sbms.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.SendAndReceive.GetClientId;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.VisitPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AddVisitPlan extends Fragment {
    private EditText dateOfVisit;
    private Spinner clientNameSpinner;
    private Button saveVisitPlan;
    SharedPreferences sharedPreferences;
    AppDatabase mdb;
    Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_client_visit_plan, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        dateOfVisit = view.findViewById(R.id.date_of_visit);

        clientNameSpinner = view.findViewById(R.id.clients_spinner);
        saveVisitPlan = view.findViewById(R.id.save_visit_plan_btn);
        calendar = Calendar.getInstance();
        ArrayAdapter adapter = null;
        try {
            adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, getClientNames());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        clientNameSpinner.setAdapter(adapter);
        saveVisitPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveClientVisit(v);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        dateOfVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfVisit.setText(sdf.format(calendar.getTime()));
    }


    public void saveClientVisit(View view) throws ExecutionException, InterruptedException {
        final VisitPlan visitPlan = new VisitPlan();
        Date date = new Date();
        String cName = clientNameSpinner.getSelectedItem().toString();
        AsyncTask<String, Void, Integer >  getId = new GetClientId(mdb).execute(cName);
        int cId = getId.get();
        if(cId==0){
            Toast.makeText(getActivity().getApplicationContext(), "Sync Data First....!!!", Toast.LENGTH_LONG).show();

        }else{
            visitPlan.setClientId(cId);
            visitPlan.setSyncStatus(SyncStatus.NOT_SEND);
            visitPlan.setDateOfVisit(dateOfVisit.getText().toString());
            visitPlan.setCreatedBy(sharedPreferences.getInt(LoginActivity.idPref, 0));
            visitPlan.setDateCreated(date.toString());
            visitPlan.setStatus("Not Done");
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    mdb.visitPlanDao().insertVisitPlan(visitPlan);
                }
            });
            Toast.makeText(getActivity().getApplicationContext(), "Visit Plan Created successfully", Toast.LENGTH_LONG).show();
            dateOfVisit.setText("");
        }


    }

    private ArrayList<String> getClientNames() throws ExecutionException, InterruptedException {
        List<Client> clientList = new ClientNames(mdb).execute().get();
        ArrayList<String> clientArrayList = new ArrayList<>();
        for(Client client : clientList){
            clientArrayList.add(client.getName());
        }
        return clientArrayList;
    }

    public class ClientNames extends AsyncTask<Void, Void, List<Client> > {
        List<Client> clientNames = new ArrayList<>();
        AppDatabase mdb;


        public ClientNames(AppDatabase mdb) {
            this.mdb = mdb;
        }

        @Override
        protected List<Client> doInBackground(Void... voids) {
            clientNames = mdb.clientRepository().getNames();
            return clientNames;
        }

    }
}
