package org.totalit.sbms.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.totalit.sbms.R;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Stock;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    List<Product> productList;
    String username;
    String password;
    Context context;

    public ProductListAdapter(List<Product> productList, String username, String password, Context context) {
        this.productList = productList;
        this.username = username;
        this.password = password;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.brand.setText(productList.get(position).getBrand().toString());
        holder.model.setText(productList.get(position).getModel().toString());
        holder.description.setText(productList.get(position).getDescription().toString());
        holder.productCode.setText(productList.get(position).getProductCode()==null ?  "" : productList.get(position).getProductCode().toString());
        holder.monthYear.setText(productList.get(position).getMonthYear()==null? "No Warranty" : productList.get(position).getMonthYear().toString());
        holder.warranty.setText(productList.get(position).getWarranty().toString());
        holder.delivered.setText(productList.get(position).getQuantityDelivered().toString());
        holder.available.setText(productList.get(position).getAvailableStock().toString());
        holder.unitPrice.setText(String.valueOf(productList.get(position).getUnitPrice()));

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

        @BindView(R.id.text_brand)
        TextView brand;
        @BindView(R.id.text_model)
        TextView model;
        @BindView(R.id.text_description)
        TextView description;
        @BindView(R.id.text_month_year)
        TextView monthYear;
        @BindView(R.id.text_warranty)
        TextView warranty;
        @BindView(R.id.text_delivered)
        TextView delivered;
        @BindView(R.id.text_available_stock)
        TextView available;
        @BindView(R.id.text_product_code)
        TextView productCode;
        @BindView(R.id.text_unit_price)
        TextView unitPrice;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            brand.setPaintFlags(brand.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            model.setPaintFlags(model.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        }


    }
}
