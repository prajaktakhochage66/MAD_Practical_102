package com.example.quickbite;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class    ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public ProductAdapter(com.example.quickbite.mainactivity mainactivity, List<ProductModel> productList) {
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvQuantity.setText("Quantity: " + product.getQuantity());
        holder.tvExpDate.setText("Expires: " + product.getExpDate());

        String status = calculateStatus(product.getExpDate());
        holder.tvStatus.setText("Status: " + status);

        if (status.equals("Expired")) {
            holder.tvStatus.setTextColor(Color.RED);
        } else if (status.equals("Near Expiry")) {
            holder.tvStatus.setTextColor(Color.parseColor("#FFA500")); // Orange
        } else {
            holder.tvStatus.setTextColor(Color.GREEN);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditProductActivity.class);
            intent.putExtra("id", product.getId());
            intent.putExtra("name", product.getName());
            intent.putExtra("quantity", product.getQuantity());
            intent.putExtra("mfgDate", product.getMfgDate());
            intent.putExtra("expDate", product.getExpDate());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private String calculateStatus(String expDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date expDate = sdf.parse(expDateStr);
            Date today = new Date();
            
            // Remove time components for pure date comparison
            Calendar calToday = Calendar.getInstance();
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            calToday.set(Calendar.MILLISECOND, 0);
            
            Calendar calExp = Calendar.getInstance();
            calExp.setTime(expDate);
            calExp.set(Calendar.HOUR_OF_DAY, 0);
            calExp.set(Calendar.MINUTE, 0);
            calExp.set(Calendar.SECOND, 0);
            calExp.set(Calendar.MILLISECOND, 0);

            long diffInMillies = calExp.getTimeInMillis() - calToday.getTimeInMillis();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diffInDays < 0) return "Expired";
            if (diffInDays <= 4) return "Near Expiry";
            return "Fresh";
        } catch (ParseException e) {
            return "Unknown";
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpDate, tvStatus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvExpDate = itemView.findViewById(R.id.tvItemExpDate);
            tvStatus = itemView.findViewById(R.id.tvItemStatus);
        }
    }
}
