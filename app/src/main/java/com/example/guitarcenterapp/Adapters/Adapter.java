package com.example.guitarcenterapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.guitarcenterapp.Helpers.BitmapHelper;
import com.example.guitarcenterapp.Models.Product;
import com.example.guitarcenterapp.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>  {

    List<Product> productList;

    public Adapter(List<Product> productList) {
        this.productList = productList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        TextView tvBrand;
        TextView tvPrice;
        ImageView ivProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvBrand = (TextView) itemView.findViewById(R.id.tvBrand);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "You selected " +
                    productList.get(getLayoutPosition()).getBrand() + " " + productList.get(getLayoutPosition()).getName(), Toast.LENGTH_LONG).show();
        }

        public void setData(Product product) {
            ivProduct.setImageBitmap(BitmapHelper.getBitmapFromPath(product.getImagePath()));
            tvName.setText(product.getName());
            tvBrand.setText(product.getBrand());
            tvPrice.setText("$" + String.valueOf(product.getPrice()));
        }
    }

}