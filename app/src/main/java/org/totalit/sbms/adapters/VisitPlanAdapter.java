package org.totalit.sbms.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.HomeActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.SendAndReceive.GetClientById;
import org.totalit.sbms.SendAndReceive.GetClientByRealId;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.VisitPlan;
import org.totalit.sbms.fragments.ClientHome;
import org.totalit.sbms.fragments.UpdateVisitFragment;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VisitPlanAdapter extends RecyclerView.Adapter<VisitPlanAdapter.ViewHolder> {

    List<VisitPlan> planList;
    AppDatabase mdb;

    public VisitPlanAdapter(List<VisitPlan> planList, AppDatabase mdb) {
        this.planList = planList;
        this.mdb = mdb;
    }

    @NonNull
    @Override
    public VisitPlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_visit_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Client clientByRealId = null;

        try {
            //clientById = new GetClientById(mdb).execute(planList.get(position).getClientId()).get();
            clientByRealId = new GetClientByRealId(mdb).execute(planList.get(position).getClientId()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
          holder.id.setText(String.valueOf(planList.get(position).getId()));
          holder.realId.setText(String.valueOf(planList.get(position).getRealId()));
          holder.clientName.setText(clientByRealId==null ? "" : clientByRealId.getName());

           holder.dateOfVisit.setText(planList.get(position).getDateOfVisit());
           holder.status.setText(planList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView dateOfVisit, clientName, status, id, realId;


        public ViewHolder(final View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            dateOfVisit =  itemView.findViewById(R.id.date_of_visit_plan);
            status = itemView.findViewById(R.id.status_of_visit_plan);
            clientName = itemView.findViewById(R.id.client_name_visit_plan);
           id = itemView.findViewById(R.id.visit_plan_id);
           realId = itemView.findViewById(R.id.visit_plan_real_id);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Hello", Toast.LENGTH_SHORT).show();
          HomeActivity homeActivity = (HomeActivity)v.getContext();
            TextView name =  v.findViewById(R.id.client_name_visit_plan);
           TextView vId = v.findViewById(R.id.visit_plan_id);
           TextView realVisitId = v.findViewById(R.id.visit_plan_real_id);
            int vid = Integer.parseInt(vId.getText().toString());
            int realVId = Integer.parseInt(realVisitId.getText().toString());

            if(realVId == 0){
                Toast.makeText(v.getContext(), "Sync Data First!!!", Toast.LENGTH_SHORT).show();
            }
            else {
                String clientname = name.getText().toString();
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                bundle.putString("clientName", clientname);
                bundle.putInt("clientDbId", vid);
                bundle.putInt("uuid", realVId);
                fragment = new UpdateVisitFragment();
                fragment.setArguments(bundle);
                ((HomeActivity) homeActivity).replaceFragement(fragment);
            }


        }
    }
}
