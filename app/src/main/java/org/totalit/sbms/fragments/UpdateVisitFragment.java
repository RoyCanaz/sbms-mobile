package org.totalit.sbms.fragments;

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
import org.totalit.sbms.R;
import org.totalit.sbms.SendAndReceive.GetVisitByUuid;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.VisitPlan;

import java.util.concurrent.ExecutionException;

public class UpdateVisitFragment extends Fragment {

    TextView clientName;
    EditText visitResult;
    Button updateBtn;
    int uuid;
    int clientDbId;
    String name;
    AppDatabase mdb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_visit_update_status, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
           clientName = view.findViewById(R.id.title_visit_client_name);
           visitResult = view.findViewById(R.id.visit_result);
           updateBtn = view.findViewById(R.id.update_visit_btn);
        uuid = this.getArguments().getInt("uuid");
        clientDbId = this.getArguments().getInt("clientDbId");
        name = this.getArguments().getString("clientName");
        clientName.setText(name);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateClientVisit();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
    public void updateClientVisit() throws ExecutionException, InterruptedException {
        final VisitPlan vp = new GetVisitByUuid(mdb).execute(uuid).get();
        vp.setVisitResult(visitResult.getText().toString());
        vp.setSyncStatus(SyncStatus.NOT_SEND);
        vp.setStatus("Done");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.visitPlanDao().updateVisitPlan(vp);
            }
        });
        Toast.makeText(getActivity().getApplicationContext(), "Visit Done", Toast.LENGTH_LONG).show();
        Fragment fragment = null;
        fragment = new VisitPlanFragment();

        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

    }
}
