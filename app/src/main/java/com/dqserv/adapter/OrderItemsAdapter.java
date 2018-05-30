package com.dqserv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dqserv.dqpos.R;
import com.dqserv.rest.OrderItemsObject;
import com.dqserv.widget.CustomItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.MyViewHolder> {

    private List<OrderItemsObject.Orders> orderItemsList;
    CustomItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvTableName, tvTotalItems, tvGrandTotal;

        public MyViewHolder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.row_order_items_tv_date);
            tvTableName = (TextView) view.findViewById(R.id.row_order_items_tv_table_name);
            tvTotalItems = (TextView) view.findViewById(R.id.row_order_items_tv_total_items);
            tvGrandTotal = (TextView) view.findViewById(R.id.row_order_items_tv_grand_total);
        }
    }


    public OrderItemsAdapter(List<OrderItemsObject.Orders> orderItemsList, CustomItemClickListener listener) {
        this.orderItemsList = orderItemsList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order_items, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(viewItem);
        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderItemsObject.Orders oBill = orderItemsList.get(position);
        holder.tvTableName.setText(oBill.getProductName());
        holder.tvTotalItems.setText(oBill.getQuantity() == null ? "0" : oBill.getQuantity());
        holder.tvGrandTotal.setText(oBill.getSubTotal() == null ? "0.0" : oBill.getSubTotal());
    }

    @Override
    public int getItemCount() {
        return orderItemsList.size();
    }
}