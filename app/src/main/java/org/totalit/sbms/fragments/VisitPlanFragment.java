package org.totalit.sbms.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.SendAndReceive.GetVisitsByCreatedBy;
import org.totalit.sbms.adapters.NotesAdapter;
import org.totalit.sbms.adapters.VisitPlanAdapter;
import org.totalit.sbms.domain.VisitPlan;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VisitPlanFragment extends Fragment {
    AppDatabase mdb;
    FloatingActionButton addClientVisit;
    RecyclerView recyclerView;
    VisitPlanAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<VisitPlan> visitPlans;
    SharedPreferences sharedPreferences;
    int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_visits_fragment, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(LoginActivity.idPref, 0);
        try {
            visitPlans = new GetVisitsByCreatedBy(mdb).execute(userId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        addClientVisit = view.findViewById(R.id.fab_new_visit_plan);
        recyclerView = view.findViewById(R.id.recycler_visit_plan);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VisitPlanAdapter(visitPlans, mdb);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        addClientVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new AddVisitPlan();
                openFragment(fragment);
            }
        });
        return view;

    }
    private void openFragment(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }
}
