package com.dqserv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dqserv.dqpos.R;
import com.dqserv.rest.ProductObject;
import com.dqserv.widget.CustomItemClickListener;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class ProductByCategoryAdapter extends RecyclerView.Adapter<ProductByCategoryAdapter.MyViewHolder> {

    private List<ProductObject.Products> productsList;
    CustomItemClickListener listener;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button btnProduct;

        public MyViewHolder(View view) {
            super(view);
            btnProduct = (Button) view.findViewById(R.id.row_orders_product_btn_product);
        }
    }


    public ProductByCategoryAdapter(Context mContext, List<ProductObject.Products> productsList, CustomItemClickListener listener) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_orders_product, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductObject.Products oProduct = productsList.get(position);
        holder.btnProduct.setText(oProduct.getProductName() + "\n"
                + mContext.getString(R.string.rs_symbol) + " " + oProduct.getProductCost());
        holder.itemView.setTag(oProduct.getProductId());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}