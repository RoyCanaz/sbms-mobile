package org.totalit.sbms.adapters;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.HomeActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.fragments.AddClientFragment;
import org.totalit.sbms.fragments.ClientHome;
import org.totalit.sbms.fragments.ClientManagementHomeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {
    List<Client> clients;
    AppDatabase mdb;




    public ClientAdapter(List<Client> clients, AppDatabase mdb) {
        this.clients = clients;
        this.mdb = mdb;
    }

    @NonNull
    @Override
    public ClientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clients_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.ViewHolder holder, int position) {
      //  holder.id.setText( String.valueOf(clients.get(position).getId()));
      /*  if(clients.get(position).getRealId()==0){
          holder.id.setText( String.valueOf(clients.get(position).getId()));
        }
        else{
          holder.id.setText(String.valueOf(clients.get(position).getRealId()));
        }
        */
        holder.id.setText( String.valueOf(clients.get(position).getId()));
        holder.realId.setText(String.valueOf(clients.get(position).getRealId()));
        holder.name.setText(clients.get(position).getName());
        holder.descr.setText(clients.get(position).getDescription() );
        int syncStatus = clients.get(position).getSyncStatus();
        if(syncStatus == SyncStatus.NOT_SEND){
              holder.imageSync.setImageResource(R.mipmap.fail);
        }
        else {
            holder.imageSync.setImageResource(R.mipmap.ok);
        }

    }

    public void swapList() throws ExecutionException, InterruptedException {
        if(clients!=null) {
            clients.clear();
        }
        List<Client>  clientList = new ArrayList<>();
        clientList = new ClientManagementHomeFragment.LoadClientList(mdb).execute().get();
     if(!clientList.isEmpty()) {
         List<Client> clients = new ArrayList<>();
         clients.addAll(clientList);
     }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return clients.size();

    }
    public void filterList(List<Client> filteredList){
       clients = filteredList;
       notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView id;
        public TextView realId;
        public TextView name;
        public TextView descr;
        public ImageView imageSync;

        public ViewHolder(final View itemView) {

            super(itemView);


            itemView.setOnClickListener(this);
            id =  itemView.findViewById(R.id.client_db_id);
            realId = itemView.findViewById(R.id.client_db_real_id);
            name = itemView.findViewById(R.id.client_name);
            descr = itemView.findViewById(R.id.client_desciption);
            imageSync = itemView.findViewById(R.id.image_sync);

        }
        private void navigateToAddInventory(View view){

        }

        @Override
        public void onClick(View v) {
            HomeActivity homeActivity = (HomeActivity)v.getContext();

           TextView tv =  v.findViewById(R.id.client_name);
           TextView clientId = v.findViewById(R.id.client_db_id);
            TextView realClientId = v.findViewById(R.id.client_db_real_id);
            int clintId = Integer.parseInt(clientId.getText().toString());
            int realCId = Integer.parseInt(realClientId.getText().toString());
            String clientname = tv.getText().toString();
          //  int m = getAdapterPosition();
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            bundle.putString("clientFullName", clientname);
            bundle.putInt("clientDbId", clintId);
            bundle.putInt("uuid", realCId);
            fragment = new ClientHome();
            fragment.setArguments(bundle);
            ((HomeActivity) homeActivity).replaceFragement(fragment);


          //  homeActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commitAllowingStateLoss();

            Toast.makeText(v.getContext(), String.valueOf(realCId), Toast.LENGTH_SHORT).show();
        }
    }

}