package org.totalit.sbms.ClassImplements;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import org.totalit.sbms.R;

public class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    View vieww;
    EditText inputToner;

    public SpinnerOnItemSelectedListener(View vieww) {
        this.vieww = vieww;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        inputToner = vieww.findViewById(R.id.input_toner_type);
       String selected = parent.getItemAtPosition(position).toString();
       if(selected.startsWith("printe")){
           inputToner.setVisibility(View.VISIBLE);
       }
       else{
           inputToner.setVisibility(View.GONE);
       }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
