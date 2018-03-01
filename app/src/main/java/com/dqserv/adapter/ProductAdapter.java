package com.dqserv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dqserv.dqpos.R;
import com.dqserv.rest.ProductObject;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<ProductObject.Products> productsList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductCode, tvProductName, tvProductPrice;

        public MyViewHolder(View view) {
            super(view);
            tvProductCode = (TextView) view.findViewById(R.id.row_product_code);
            tvProductName = (TextView) view.findViewById(R.id.row_product_name);
            tvProductPrice = (TextView) view.findViewById(R.id.row_product_price);
        }
    }


    public ProductAdapter(Context mContext, List<ProductObject.Products> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductObject.Products oProduct = productsList.get(position);
        holder.tvProductCode.setText("Product Code: " + oProduct.getProductCode());
        holder.tvProductName.setText("Product Name: " + oProduct.getProductName());
        holder.tvProductPrice.setText("Product Price: " + mContext.getString(R.string.rs_symbol) + " " + oProduct.getSalePrice());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}