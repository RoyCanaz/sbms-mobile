package org.totalit.sbms.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.totalit.sbms.R;
import org.totalit.sbms.domain.ClientInventory;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    List<ClientInventory> clientInvent;

    public InventoryAdapter(List<ClientInventory> clientInvent) {
        this.clientInvent = clientInvent;
    }

    @NonNull
    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.ViewHolder holder, int position) {
             holder.id.setText(String.valueOf(clientInvent.get(position).getId()));
             holder.brand.setText(String.valueOf(clientInvent.get(position).getCategory()));
             holder.model.setText(clientInvent.get(position).getModel());
             holder.description.setText(clientInvent.get(position).getDescription());
             holder.quantity.setText(String.valueOf(clientInvent.get(position).getQuantity()));
             holder.needMaintenence.setText(clientInvent.get(position).getNeedMaintenence());

    }

    @Override
    public int getItemCount() {
        return clientInvent.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView id, brand, model, description, quantity, needMaintenence;

        public ViewHolder(final View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            id = itemView.findViewById(R.id.client_inventory_id);
            brand = itemView.findViewById(R.id.client_inventory_brand);
            model = itemView.findViewById(R.id.client_inventory_model);
            description = itemView.findViewById(R.id.client_inventory_description);
            quantity = itemView.findViewById(R.id.client_inventory_quantity);
            needMaintenence = itemView.findViewById(R.id.client_inventory_need_maintence);



        }

        @Override
        public void onClick(View v) {

        }
    }
}
