package org.totalit.sbms.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Repository.QuoteList;
import org.totalit.sbms.Utils.TwoDecimalPlaceUtil;
import org.totalit.sbms.activity.ListQuotesActivity;
import org.totalit.sbms.domain.Quote;
import org.totalit.sbms.retrofit.QuoteService;
import org.totalit.sbms.retrofit.ServiceGenerator;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class ListQuotesAdapter extends RecyclerView.Adapter<ListQuotesAdapter.ViewHolder> {
    List<Quote> quoteList;
    Context context;
    AppDatabase mdb;
    ProgressDialog progressDialog;
    String username;
    String password;
    SharedPreferences sharedPreferences;

    public ListQuotesAdapter(List<Quote> quoteList, Context context, AppDatabase mdb,  ProgressDialog progressDialog) {
        this.quoteList = quoteList;
        this.context = context;
        this.mdb = mdb;
        this.progressDialog = progressDialog;
        sharedPreferences = context.getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
    }

    @NonNull
    @Override
    public ListQuotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
        return new ListQuotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.quoteUuid.setText(quoteList.get(position).getQuoteUuid());
            holder.quoteDate.setText(quoteList.get(position).getDateCreated());
            holder.numberOfitems.setText(String.valueOf(quoteList.get(position).getNumOfItems()));
            int stat = quoteList.get(position).getLastSendMailStatus();
            if(stat==1) {
                holder.quoteStatus.setText("SENT");
                holder.quoteStatus.setTextColor(context.getResources().getColor(R.color.colorLight));
            }
            else{
                holder.quoteStatus.setText("NOT SENT");
                holder.quoteStatus.setTextColor(Color.RED);
            }
            holder.subTotal.setText(quoteList.get(position).getTotal().toString());
            holder.vat.setText(TwoDecimalPlaceUtil.formatDouble(quoteList.get(position).getVat()));
            holder.total.setText(quoteList.get(position).getTotalIncVat().toString());
            holder.cardViewQuote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                  //  dialog.
                }
            });
            holder.quoteMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Quote quote = quoteList.get(position);
                    PopupMenu popupMenu = new PopupMenu(context, holder.quoteMenu);
                    popupMenu.inflate(R.menu.menu_quote);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.quote_send:
                                    sendQuote((long)quote.getId(), v);
                                    break;
                                case R.id.quote_edit:
                                    //Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                                    break;

                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }

            });
    }

    @Override
    public int getItemCount() {
        return quoteList.size();

    }
    public void sendQuote(final Long quoteId, final View v){
        progressDialog.setTitle("Sending. Please wait.....");
        progressDialog.setCancelable(true);
        progressDialog.show();
        QuoteService quoteService = ServiceGenerator.createService(QuoteService.class, username, password);
        retrofit2.Call<Quote> quoteCall = quoteService.sendQuote(quoteId);
        quoteCall.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(retrofit2.Call<Quote> call, Response<Quote> response) {
                if(response.isSuccessful()){
                    final Quote quote = response.body();
                    progressDialog.dismiss();
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            Quote q = mdb.quoteDao().getQuote(quoteId);
                            q.setLastSendMailStatus(quote.getLastSendMailStatus());
                            q.setDateCreated(quote.getDateCreated());
                            mdb.quoteDao().updateQuote(q);

                        }
                    });
                    try {
                        swapData();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Send Successfully", Toast.LENGTH_SHORT).show();

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(context, "Sending Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Quote> call, Throwable t) {
                Log.e("Send Error", t.getMessage()+"");
                //Toast.makeText(context, t.getMessage()+"", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(v, t.getMessage()+"", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendQuote(quoteId, view);
                            }
                        });

                snackbar.show();
                progressDialog.dismiss();
            }
        });
    }
    public void swapData() throws ExecutionException, InterruptedException {
        quoteList.clear();
        List<Quote> quotes = new QuoteList(mdb).execute().get();
        quoteList.addAll(quotes);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder   {

        @BindView(R.id.quote_date)
        TextView quoteDate;
        @BindView(R.id.quote_status)
        TextView quoteStatus;
        @BindView(R.id.quote_num_of_items)
        TextView numberOfitems;
        @BindView(R.id.quote_uuid)
        TextView quoteUuid;
        @BindView(R.id.quote_vat)
        TextView vat;
        @BindView(R.id.quote_sub_total)
        TextView subTotal;
        @BindView(R.id.quote_total)
        TextView total;
        @BindView(R.id.quote_menu)
        ImageButton quoteMenu;
        @BindView(R.id.card_quote)
        CardView cardViewQuote;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            quoteUuid.setPaintFlags(quoteUuid.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }


    }
}
