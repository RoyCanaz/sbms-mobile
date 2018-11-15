package org.totalit.sbms.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.totalit.sbms.CallListerner.ProductListListener;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.TwoDecimalPlaceUtil;
import org.totalit.sbms.activity.AddQuoteItemsActivity;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Rate;
import org.totalit.sbms.domain.temp.QuoteItemTemp;
import org.totalit.sbms.domain.temp.QuoteTemp;
import org.totalit.sbms.retrofit.ProductService;
import org.totalit.sbms.retrofit.ServiceGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQuoteItemsAdapter extends RecyclerView.Adapter<AddQuoteItemsAdapter.ViewHolder>  {

    List<QuoteItemTemp> quoteItems;
    Context context;
    AppDatabase mdb;
    Long quoteId;
    String username;
    String password;
    SharedPreferences sharedPreferences;
    Rate rat;
    Activity activity;


    public AddQuoteItemsAdapter(List<QuoteItemTemp> quoteItems, Context context, final AppDatabase mdb, Long quoteId, Activity activity) {
        this.quoteItems = quoteItems;
        this.context = context;
        this.mdb = mdb;
        this.quoteId = quoteId;
        this.activity = activity;

        sharedPreferences = context.getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                rat = mdb.rateDao().getActiveRate(true);
            }
        });
    }

    @NonNull
    @Override
    public AddQuoteItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_quote_item, parent, false);
        return new AddQuoteItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Long productId = Long.valueOf(quoteItems.get(position).getProductId());
        holder.textQuantity.setText(quoteItems.get(position).getQuantity().toString());

                getProduct(holder.cardView, holder.progressBar, holder.linearLayout, holder.textError, productId, new ProductListListener() {
                    @Override
                    public void productList(List<Product> productList) {

                    }

                    @Override
                    public void product(final Product product) {
                        holder.brand.setText(product.getBrand());
                        holder.model.setText(product.getModel());
                        holder.available.setText(product.getAvailableStock().toString());
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                if(rat!=null){
                                    double rp = rat.getRate().doubleValue() * product.getSellingPrice();
                                    double up = rp/1.15;
                                    double lp = up*quoteItems.get(position).getQuantity();
                                    String unitPrice = String.format("%.2f", up);
                                    String linePrice = String.format("%.2f", lp);
                                    holder.unitPrice.setText("$ "+unitPrice);
                                    holder.linePrice.setText("$ "+linePrice);
                                }
                            }
                        });

                    }
                });

                holder.addQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String up = holder.unitPrice.getText().toString();
                        String lp = holder.linePrice.getText().toString();

                        String qty = holder.textQuantity.getText().toString();
                        int totalQty = Integer.parseInt(qty)+1;

                        String unitPric = up.replace("$", "");
                        String linePrice =  lp.replace("$", "");
                        Double totalLinePrice = Double.parseDouble(unitPric)+Double.parseDouble(linePrice);
                        holder.linePrice.setText(TwoDecimalPlaceUtil.formatDouble(totalLinePrice));
                        holder.textQuantity.setText(String.valueOf(totalQty));
                     //   notifyDataSetChanged();
                        addQuantity( quoteItems.get(position).getQuoteId(), quoteItems.get(position).getProductId(), unitPric);
                    }
                });
                holder.subtractQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String up = holder.unitPrice.getText().toString();
                        String lp = holder.linePrice.getText().toString();

                        String qty = holder.textQuantity.getText().toString();
                        int totalQty = Integer.parseInt(qty)-1;
                        String unitPric = up.replace("$", "");
                        String linePrice =  lp.replace("$", "");
                        Double totalLinePrice = Double.parseDouble(linePrice)-Double.parseDouble(unitPric);
                        holder.linePrice.setText(TwoDecimalPlaceUtil.formatDouble(totalLinePrice));
                        holder.textQuantity.setText(String.valueOf(totalQty));
                       // notifyDataSetChanged();
                        subtractQuantity( quoteItems.get(position).getQuoteId(), quoteItems.get(position).getProductId(), unitPric);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return quoteItems.size();
    }
    public void getProduct(final CardView cardView, final ProgressBar progressBar, final LinearLayout linearLayout, final TextView textView, Long id, final ProductListListener listener){
        linearLayout.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        ProductService productService = ServiceGenerator.createService(ProductService.class, username, password);
        Call<Product> productCall = productService.getProductById(id);
        productCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()){

                    listener.product(response.body());
                    linearLayout.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                linearLayout.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    /**
     * @param quoteId Quote Id
     * @param productId Product Id for the current quote
     * Add quantity when imagebutton up pressed
     */
    public void addQuantity(final Long quoteId, final Long productId, final String textUnitPrice ){
        final AddQuoteItemsActivity a = (AddQuoteItemsActivity) activity;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                QuoteItemTemp itemTemp = mdb.quoteItemTempDao().getQuoteItemByQuoteAndProduct(quoteId, productId);
                itemTemp.setQuantity(itemTemp.getQuantity()+1L);
                mdb.quoteItemTempDao().updateQuoteItemTemp(itemTemp);

                Double unitPrice = Double.parseDouble(textUnitPrice);
                QuoteTemp quoteTemp = mdb.quoteTempDao().getQuoteTemp(itemTemp.getQuoteId().intValue());

                Double totalAmt = quoteTemp.getTotal()+unitPrice;
                Double vat = totalAmt * 0.15;
                Double totalIncVat = vat + totalAmt;
                quoteTemp.setTotal(TwoDecimalPlaceUtil.toDecimal(totalAmt));
                quoteTemp.setVat(TwoDecimalPlaceUtil.toDecimal(vat));
                quoteTemp.setTotalIncVat(TwoDecimalPlaceUtil.toDecimal(totalIncVat));
                quoteTemp.setNumOfItems(quoteTemp.getNumOfItems()+1);
                Log.e("Items", ""+quoteTemp.getNumOfItems());
                mdb.quoteTempDao().updateQuoteTemp(quoteTemp);

                Log.e("total", totalAmt.toString());
                Log.e("vat", vat.toString());
                a.setTotals(TwoDecimalPlaceUtil.formatDouble(totalAmt), TwoDecimalPlaceUtil.formatDouble(vat), TwoDecimalPlaceUtil.formatDouble(totalIncVat), a);
            }
        });

    }

    /**
     * @param quoteId Quote Id
     * @param productId Product Id for the current quote
     * Subtract quantity when imagebutton up pressed
     */
    public void subtractQuantity(final Long quoteId, final Long productId, final String textUnitPrice ){
        final AddQuoteItemsActivity a = (AddQuoteItemsActivity) activity;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                QuoteItemTemp itemTemp = mdb.quoteItemTempDao().getQuoteItemByQuoteAndProduct(quoteId, productId);
                itemTemp.setQuantity(itemTemp.getQuantity()-1L);
                mdb.quoteItemTempDao().updateQuoteItemTemp(itemTemp);

                Double unitPrice = Double.parseDouble(textUnitPrice);
                QuoteTemp quoteTemp = mdb.quoteTempDao().getQuoteTemp(itemTemp.getQuoteId().intValue());

                Double totalAmt = quoteTemp.getTotal()-unitPrice;
                Double vat = totalAmt * 0.15;
                Double totalIncVat = vat + totalAmt;
                quoteTemp.setTotal(TwoDecimalPlaceUtil.toDecimal(totalAmt));
                quoteTemp.setVat(TwoDecimalPlaceUtil.toDecimal(vat));
                quoteTemp.setTotalIncVat(TwoDecimalPlaceUtil.toDecimal(totalIncVat));
                quoteTemp.setNumOfItems(quoteTemp.getNumOfItems()-1);
                mdb.quoteTempDao().updateQuoteTemp(quoteTemp);

                Log.e("total", totalAmt.toString());
                Log.e("vat", vat.toString());
                a.setTotals(TwoDecimalPlaceUtil.formatDouble(totalAmt), TwoDecimalPlaceUtil.formatDouble(vat), TwoDecimalPlaceUtil.formatDouble(totalIncVat), a);
            }
        });


    }





    public class ViewHolder extends RecyclerView.ViewHolder   {

        @BindView(R.id.text_item_brand)
        TextView brand;

        @BindView(R.id.text_item_model)
        TextView model;

        @BindView(R.id.text_item_available)
        TextView available;

        @BindView(R.id.text_unit_price)
        TextView unitPrice;

        @BindView(R.id.text_line_price)
        TextView linePrice;

        @BindView(R.id.text_quantity)
        TextView textQuantity;

        @BindView(R.id.btn_add)
        ImageButton addQuantity;

        @BindView(R.id.btn_subtract)
        ImageButton subtractQuantity;

        @BindView(R.id.progress_bar_quote)
        ProgressBar progressBar;

        @BindView(R.id.card_view_qoute_item)
        CardView cardView;

        @BindView(R.id.linear_layout_progress_bar)
        LinearLayout linearLayout;

        @BindView(R.id.text_error)
        TextView textError;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            brand.setPaintFlags(brand.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            model.setPaintFlags(model.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        }


    }
}
