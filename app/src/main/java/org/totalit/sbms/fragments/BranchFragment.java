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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Branch;

public class BranchFragment extends Fragment {
    private TextInputLayout inputLayoutName, inputLayoutAddress, inputLayoutDescription;
    private EditText inputname, inputDescr, inputaddress;
    private Button saveBranch, goNext;
    private int clientId;
    SharedPreferences sharedPreferences;

    AppDatabase mdb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branch, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientId = this.getArguments().getInt("clientId");
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);

        inputLayoutName = view.findViewById(R.id.input_layout_branch_name);
        inputLayoutDescription = view.findViewById(R.id.input_layout_branch_descr);
        inputLayoutAddress = view.findViewById(R.id.input_layout_branch_address);
        inputname = view.findViewById(R.id.input_branch_name);
        inputDescr = view.findViewById(R.id.input_branch_descr);
        inputaddress = view.findViewById(R.id.input_branch_address);
        saveBranch = view.findViewById(R.id.saveBranch);
        goNext = view.findViewById(R.id.goNextBtn);
        saveBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBranchCall(v);
            }
        });
        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddRequiredDocs(v);
            }
        });

        return view;
    }

    private void saveBranchCall(View view){
        final Branch branch = new Branch();
        branch.setRealId(0);
        branch.setSyncStatus(SyncStatus.NOT_SEND);
        branch.setBranch(inputname.getText().toString());
        branch.setDescription(inputDescr.getText().toString());
        branch.setAddress(inputaddress.getText().toString());
        branch.setClientId(clientId);
        branch.setCreatedBy(sharedPreferences.getInt(LoginActivity.idPref, 1));
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.branchDao().insertBranch(branch);

            }
        });
        Toast.makeText(getActivity().getApplicationContext(), "Branch Saved successfully", Toast.LENGTH_LONG).show();
        clearFields();
    }
    private void navigateToAddRequiredDocs(View view){
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        fragment = new ProcumentDocsFragment();
        bundle.putInt("clientId", clientId);
        fragment.setArguments(bundle);

        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
    private void clearFields(){
        inputname.setText("");
        inputDescr.setText("");
        inputaddress.setText("");
    }
}
