package org.totalit.sbms.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.totalit.sbms.HomeActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.domain.Contact;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>  {
    List<Contact> contacts;

    public ContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        holder.id.setText(String.valueOf(contacts.get(position).getId()));
        holder.firstName.setText(contacts.get(position).getFirstName());
        holder.lastName.setText(contacts.get(position).getLastName());
        holder.email.setText(contacts.get(position).getEmail());
        holder.mobilePhone.setText(contacts.get(position).getMobilePhone());
        holder.officePhone.setText(contacts.get(position).getOfficePhone());
        holder.department.setText(contacts.get(position).getDepartment());
        holder.jobposition.setText(contacts.get(position).getJobPosition());

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView id;
        public TextView firstName, lastName, email;
        public TextView mobilePhone,officePhone;
        public TextView department, jobposition;


        public ViewHolder(final View itemView) {

            super(itemView);


            itemView.setOnClickListener(this);
            id =  itemView.findViewById(R.id.client_contact_id);
            firstName = itemView.findViewById(R.id.client_contact_firstname);
            lastName= itemView.findViewById(R.id.client_contact_lastname);
            email = itemView.findViewById(R.id.client_contact_email);
            mobilePhone = itemView.findViewById(R.id.client_contact_mobile_phone);
            officePhone = itemView.findViewById(R.id.client_contact_office_phone);
            department = itemView.findViewById(R.id.client_contact_department);
            jobposition = itemView.findViewById(R.id.client_contact_job_position);


        }

        @Override
        public void onClick(View v) {

        }
    }
}
