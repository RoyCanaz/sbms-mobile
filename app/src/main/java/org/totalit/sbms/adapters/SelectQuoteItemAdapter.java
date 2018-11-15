package org.totalit.sbms.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.TwoDecimalPlaceUtil;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Rate;
import org.totalit.sbms.domain.temp.QuoteItemTemp;
import org.totalit.sbms.domain.temp.QuoteTemp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectQuoteItemAdapter extends RecyclerView.Adapter<SelectQuoteItemAdapter.ViewHolder> {

    List<Product> productList;
    Context context;
    AppDatabase mdb;
    Long quoteId;


    public SelectQuoteItemAdapter(List<Product> productList, Context context, AppDatabase mdb, Long quoteId) {
        this.productList = productList;
        this.context = context;
        this.mdb = mdb;
        this.quoteId = quoteId;


    }



    @NonNull
    @Override
    public SelectQuoteItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_quote_item, parent, false);
        return new SelectQuoteItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.brand.setText(productList.get(position).getBrand().toString());
        holder.model.setText(productList.get(position).getModel().toString());
        holder.available.setText(productList.get(position).getAvailableStock().toString());
      //  AsyncTask.execute(new Runnable() {
       //     @Override
       //     public void run() {
                Rate rat = mdb.rateDao().getActiveRate(true);

                if(rat!=null){
                    double rp = rat.getRate().doubleValue() * productList.get(position).getSellingPrice();
                    double up = rp/1.15;
                    String unitPrice = String.format("%.2f", up);
                    holder.unitPrice.setText("$"+unitPrice);
                }
                QuoteItemTemp quoteItem = mdb.quoteItemTempDao().getQuoteItemByQuoteAndProduct(quoteId, (long) productList.get(position).getId());
                if(quoteItem!=null) {
                    holder.checkBoxItem.setChecked(true);
                }
       //     }
    //    });
       holder.checkBoxItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(holder.checkBoxItem.isChecked()){
                   final QuoteItemTemp qi = new QuoteItemTemp();
                   qi.setProductId((long) productList.get(position).getId());
                   qi.setQuoteId(quoteId);
                   qi.setQuantity(1L);

                   //Adding QuoteItem after checkbox click
                   AsyncTask.execute(new Runnable() {
                       @Override
                       public void run() {
                           Log.e("Getting", quoteId+"---"+productList.get(position).getId());
                           String un = holder.unitPrice.getText().toString();
                           Double unitP  = Double.parseDouble(un.replace("$", ""));
                           QuoteItemTemp quoteItem = mdb.quoteItemTempDao().getQuoteItemByQuoteAndProduct(quoteId, (long) productList.get(position).getId());
                           if(quoteItem==null) {
                               mdb.quoteItemTempDao().insertQuoteItemTemp(qi);

                               QuoteTemp qt = mdb.quoteTempDao().getQuoteTemp(quoteId.intValue());
                               Double totalAmt =  qt.getTotal() + unitP;
                               Double vat = totalAmt * 0.15;
                               Double totalIncVat = vat + totalAmt;
                               int numberOfItems = qt.getNumOfItems() + 1;
                               qt.setNumOfItems(numberOfItems);
                               qt.setTotal(TwoDecimalPlaceUtil.toDecimal(totalAmt));
                               qt.setVat(TwoDecimalPlaceUtil.toDecimal(vat));
                               qt.setTotalIncVat(TwoDecimalPlaceUtil.toDecimal(totalIncVat));
                               mdb.quoteTempDao().updateQuoteTemp(qt);

                               Log.e("Nu Quote item", "ID::"+quoteId);


                           }
                       }
                   });

                   Toast.makeText(v.getContext(), "Added", Toast.LENGTH_SHORT).show();
               }
               else {
                   AsyncTask.execute(new Runnable() {
                       @Override
                       public void run() {

                           //Deleting QuoteItem after checkbox click
                           QuoteItemTemp quoteItem = mdb.quoteItemTempDao().getQuoteItemByQuoteAndProduct(quoteId, (long) productList.get(position).getId());
                           int quantity = quoteItem.getQuantity().intValue();

                           Log.e("Deleting", quoteId+"---"+productList.get(position).getId());
                           String un = holder.unitPrice.getText().toString();
                           Double unitP  = Double.parseDouble(un.replace("$", ""));
                           mdb.quoteItemTempDao().deleteQuoteItem(quoteId, (long) productList.get(position).getId());

                           QuoteTemp qt = mdb.quoteTempDao().getQuoteTemp(quoteId.intValue());
                           int numberOfItems = qt.getNumOfItems()-quantity;
                           Double totalAmt =  qt.getTotal() - unitP;
                           Double vat = totalAmt * 0.15;
                           Double totalIncVat = vat + totalAmt;
                           QuoteTemp quoteTemp = new QuoteTemp();
                           quoteTemp.setId(quoteId.intValue());
                           quoteTemp.setTotal(TwoDecimalPlaceUtil.toDecimal(totalAmt));
                           quoteTemp.setVat(TwoDecimalPlaceUtil.toDecimal(vat));
                           quoteTemp.setTotalIncVat(TwoDecimalPlaceUtil.toDecimal(totalIncVat));
                           quoteTemp.setNumOfItems(numberOfItems);
                            mdb.quoteTempDao().updateQuoteTemp(quoteTemp);
                           Log.e("Nu Quote item", "ID::"+quoteId);
                       }
                   });
                   Toast.makeText(v.getContext(), "Removed", Toast.LENGTH_SHORT).show();
               }

           }
       });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public void updateList(List<Product> products ){
        productList = new ArrayList<>();
        productList.addAll(products);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder   {

        @BindView(R.id.text_item_brand)
        TextView brand;
        @BindView(R.id.text_item_model)
        TextView model;
        @BindView(R.id.text_item_available)
        TextView available;
        @BindView(R.id.text_item_unit_price)
        TextView unitPrice;
        @BindView(R.id.checkbox_item)
        CheckBox checkBoxItem;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            brand.setPaintFlags(brand.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            model.setPaintFlags(model.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        }


    }
}
