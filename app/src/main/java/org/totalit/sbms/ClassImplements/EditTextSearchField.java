package org.totalit.sbms.ClassImplements;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import org.totalit.sbms.adapters.ClientAdapter;
import org.totalit.sbms.domain.Client;

import java.util.ArrayList;
import java.util.List;

public class EditTextSearchField implements TextWatcher {
    List<Client> clients;
    ClientAdapter adapter;

    public EditTextSearchField(List<Client> clients, ClientAdapter adapter) {
        this.clients = clients;
        this.adapter = adapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        filter(s.toString());

    }
    public void filter(String s){
        List<Client> filteredList = new ArrayList<>();
        for(Client client : clients){

            if(client.getName().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(client);
            }
        }
      adapter.filterList(filteredList);

    }
}
