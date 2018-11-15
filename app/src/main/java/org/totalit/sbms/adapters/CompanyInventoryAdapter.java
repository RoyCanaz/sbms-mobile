package org.totalit.sbms.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.totalit.sbms.CallListerner.Listener;
import org.totalit.sbms.CallListerner.OnItemClick;
import org.totalit.sbms.HomeActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.activity.AddComputerActivity;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.retrofit.CategoryService;
import org.totalit.sbms.retrofit.ServiceGenerator;
import org.totalit.sbms.retrofit.StockService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.totalit.sbms.R.color.colorDanger;

public class CompanyInventoryAdapter extends RecyclerView.Adapter<CompanyInventoryAdapter.ViewHolder>  {
    List<Category> categories;
    String username;
    String password;
    private OnItemClick itemClick;
    public CompanyInventoryAdapter(List<Category> categories, String username, String password) {
        this.categories = categories;
        this.password = password;
        this.username = username;
    }

    @NonNull
    @Override
    public CompanyInventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.categoryName.setText(categories.get(position).getName());

           getAvailableStock(categories.get(position).getId(), new Listener() {
               @Override
               public void available(List<Integer> i) {
                   Double level = (i.get(0).doubleValue()/i.get(1).doubleValue())*100;
                   holder.stockAvailable.setText(String.valueOf(i.get(0)));
                   holder.stockPercent.setText(String.valueOf(level.intValue()));
                   holder.stockLevel.setProgress(level.intValue());

               }
           });
       holder.btnAddStock.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              Intent intent = new Intent(v.getContext(), AddComputerActivity.class);
               intent.putExtra("categoryName", categories.get(position).getName());
               Integer catId =  categories.get(position).getId();
              intent.putExtra("categoryId",catId.longValue());
               v.getContext().startActivity(intent);
              // Toast.makeText(v.getContext(), categories.get(position).getId()+"", Toast.LENGTH_SHORT).show();

           }
       });




    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
    private void getAvailableStock(int categoryId, final Listener listener) {
        StockService stockService = ServiceGenerator.createService(StockService.class, username, password);
        Call<List<Integer>> stockCall = stockService.getAvailableStock(categoryId);
      stockCall.enqueue(new Callback<List<Integer>>() {
          @Override
          public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
              if(response.isSuccessful()){
                  listener.available(response.body());
              }
          }

          @Override
          public void onFailure(Call<List<Integer>> call, Throwable t) {

          }
      });

    }
    public OnItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton btnAddStock;
        ProgressBar stockLevel;
        TextView categoryName;
        TextView stockAvailable;
        TextView stockPercent;
        Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setClickable(true);
            context = itemView.getContext().getApplicationContext();
            btnAddStock = itemView.findViewById(R.id.add_stock);
            stockLevel = itemView.findViewById(R.id.stock_level);
            categoryName = itemView.findViewById(R.id.category_name);
            stockAvailable = itemView.findViewById(R.id.available_stock);
            stockPercent = itemView.findViewById(R.id.stock_percent);
        }
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            if(itemClick!=null) {
                itemClick.onItemClicked(getAdapterPosition());

            }
        }
    }
}

