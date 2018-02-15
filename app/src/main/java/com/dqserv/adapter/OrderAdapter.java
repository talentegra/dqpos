package com.dqserv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dqserv.dqpos.R;
import com.dqserv.rest.OrderObject;
import com.dqserv.widget.CustomItemClickListener;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<OrderObject.Orders> ordersList;
    CustomItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPrice, tvQty, tvtotal;
        public ImageView ivDelete;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.row_order_tv_name);
            tvPrice = (TextView) view.findViewById(R.id.row_order_tv_price);
            tvQty = (TextView) view.findViewById(R.id.row_order_tv_qty);
            tvtotal = (TextView) view.findViewById(R.id.row_order_tv_total);
            ivDelete = (ImageView) view.findViewById(R.id.row_order_iv_delete);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.deleteViewOnClick(view, getAdapterPosition());
                }
            });

        }
    }


    public OrderAdapter(List<OrderObject.Orders> ordersList, CustomItemClickListener listener) {
        this.ordersList = ordersList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderObject.Orders oOrder = ordersList.get(position);
        holder.tvName.setText(oOrder.getProductName());
        holder.tvPrice.setText(oOrder.getSalePrice());
        holder.tvQty.setText(oOrder.getQuantity());
        holder.tvtotal.setText(oOrder.getSubTotal());
        holder.ivDelete.setTag(oOrder.getOrderId());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }
}