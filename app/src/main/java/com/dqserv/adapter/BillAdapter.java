package com.dqserv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dqserv.dqpos.R;
import com.dqserv.rest.BillObject;
import com.dqserv.widget.CustomItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    private List<BillObject.Orders> billList;
    CustomItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvTableName, tvTotalItems, tvGrandTotal;

        public MyViewHolder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.row_bill_tv_date);
            tvTableName = (TextView) view.findViewById(R.id.row_bill_tv_table_name);
            tvTotalItems = (TextView) view.findViewById(R.id.row_bill_tv_total_items);
            tvGrandTotal = (TextView) view.findViewById(R.id.row_bill_tv_grand_total);
        }
    }


    public BillAdapter(List<BillObject.Orders> billList, CustomItemClickListener listener) {
        this.billList = billList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bill, parent, false);
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
        BillObject.Orders oBill = billList.get(position);
        String displayValue ="";
        try {
            // Get date from string
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormatter.parse(oBill.getDate());

            // Get time from date
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            displayValue = timeFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvDate.setText(displayValue);
        holder.tvTableName.setText(oBill.getTableName());
        holder.tvTotalItems.setText(oBill.getTotalItems() == null ? "0" : oBill.getTotalItems());
        holder.tvGrandTotal.setText(oBill.getGrandTotal() == null ? "0.0" : oBill.getGrandTotal());
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }
}