package org.totalit.sbms.ClassImplements;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.totalit.sbms.domain.Rate;

import java.util.List;

public class RateSpinnerAdapter extends ArrayAdapter<Rate> {

    private Context context;
    private List<Rate> rateList;

    public RateSpinnerAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Rate> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.rateList = objects;
    }

    @Override
    public int getCount() {
        return rateList.size();
    }

    @Nullable
    @Override
    public Rate getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // return super.getView(position, convertView, parent);
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setTextSize(17);
        label.setPadding(5,5,5,5);

        label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        label.setText(rateList.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    //    return super.getDropDownView(position, convertView, parent);
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(rateList.get(position).getName());
        return label;
    }


}
