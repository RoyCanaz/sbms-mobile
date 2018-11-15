package org.totalit.sbms.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.R;
import org.totalit.sbms.activity.ScannerActivity;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.Stock;
import org.totalit.sbms.retrofit.ServiceGenerator;
import org.totalit.sbms.retrofit.StockService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerialNumberAdapter extends RecyclerView.Adapter<SerialNumberAdapter.ViewHolder> {

    ArrayList<Stock> stockList;
    String username;
    String password;
    Context context;


    public SerialNumberAdapter(ArrayList<Stock> stockList, String username, String password, Context context) {
                this.stockList = new ArrayList<>(stockList);
                this.username = username;
                this.password = password;
                this.context = context;
            }

    @NonNull
    @Override
    public SerialNumberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serial_number_row, parent, false);
         context = parent.getContext();
        return new SerialNumberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            holder.inputSerialNumber.setText(stockList.get(position).getSerialNumber().toString());
            final int stockId =  stockList.get(position).getId().intValue();
            holder.btnScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ScannerActivity.class);
                    intent.putExtra("stockId", stockId);
                    ((Activity) context).startActivityForResult(intent, 0);
                }
            });
            holder.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String serialNumber = holder.inputSerialNumber.getText()==null ? "nil" : holder.inputSerialNumber.getText().toString();
                    updateSerialNumber(stockId, serialNumber, holder, position);
                }
            });
    }
      public void updateSerialNumber(int id, String serialNumber, final ViewHolder holder, final int position){
          holder.btnSave.setEnabled(false);
          holder.progressBar.setVisibility(View.VISIBLE);
          Stock  stock = new Stock();
          stock.setId((long) id);
          stock.setSerialNumber(serialNumber);
          StockService stockService = ServiceGenerator.createService(StockService.class, username, password);
          Call<ResponseBody> bodyCall = stockService.updateSn(stock);
          bodyCall.enqueue(new Callback<ResponseBody>() {
              @Override
              public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                  if(response.isSuccessful()){
                      holder.btnSave.setEnabled(true);
                      holder.progressBar.setVisibility(View.GONE);
                      removeItem(holder, position);
                      Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                  }
              }

              @Override
              public void onFailure(Call<ResponseBody> call, Throwable t) {
                  holder.btnSave.setEnabled(true);
                  holder.progressBar.setVisibility(View.GONE);
                  Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
              }
          });
      }
      public void updateSerial(ArrayList<Stock> stockListUpdated){
        if(stockList!=null){
            stockList.clear();
            stockList.addAll(stockListUpdated);
        }
        else{
            stockList = stockListUpdated;
        }
          notifyDataSetChanged();
      }
        private void removeItem(ViewHolder holder, int position) {
            int newPosition = holder.getAdapterPosition();
            stockList.remove(newPosition);
            notifyItemRemoved(newPosition);
            notifyItemRangeChanged(newPosition, stockList.size());
        }

    @Override
    public int getItemCount() {
        return stockList.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progress_bar_sn)
        ProgressBar progressBar;

        @BindView(R.id.input_serial_number)
        EditText inputSerialNumber;

        @BindView(R.id.btn_scan)
        Button btnScan;

        @BindView(R.id.btn_save_serial_number)
        Button btnSave;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
