package com.example.guitarcenterapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.guitarcenterapp.AddProductActivity;
import com.example.guitarcenterapp.EditActivity;
import com.example.guitarcenterapp.Helpers.BitmapHelper;
import com.example.guitarcenterapp.Models.Product;
import com.example.guitarcenterapp.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>  {

    private final List<Product> productList;
    private final Context context;

    public Adapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.recyclerview_item, parent, false);

        TextView tvLayoutName = (TextView) view.findViewById(R.id.tvName);
        TextView tvLayoutBrand = (TextView) view.findViewById(R.id.tvBrand);
        TextView tvLayoutPrice = (TextView) view.findViewById(R.id.tvPrice);
        ImageView ivLayoutPhoto = (ImageView) view.findViewById(R.id.ivProduct);

        tvLayoutName.setText(productList.get(position).getName());
        tvLayoutBrand.setText(productList.get(position).getBrand());
        tvLayoutPrice.setText(String.valueOf(productList.get(position).getPrice()));
        ivLayoutPhoto.setImageBitmap(BitmapHelper.getBitmapFromPath(productList.get(position).getImagePath()));

        return rowView;
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

            itemView.setOnClickListener(this);
        }

        public void setData(Product product) {
            ivProduct.setImageBitmap(BitmapHelper.getBitmapFromPath(product.getImagePath()));
            tvName.setText(product.getName());
            tvBrand.setText(product.getBrand());
            tvPrice.setText("$" + String.valueOf(product.getPrice()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), EditActivity.class);
            Product product = productList.get(getLayoutPosition());
            intent.putExtra("ID", product.getId());
            v.getContext().startActivity(intent);
        }

    }

}