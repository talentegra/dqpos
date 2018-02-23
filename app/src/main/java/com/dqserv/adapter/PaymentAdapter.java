package com.dqserv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dqserv.dqpos.R;
import com.dqserv.rest.BillObject;
import com.dqserv.rest.PaymentObject;
import com.dqserv.widget.CustomItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

    private List<PaymentObject.Orders> paymentList;
    CustomItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductID;

        public MyViewHolder(View view) {
            super(view);
            tvProductID = (TextView) view.findViewById(R.id.row_payment_product_id);
        }
    }


    public PaymentAdapter(List<PaymentObject.Orders> paymentList, CustomItemClickListener listener) {
        this.paymentList = paymentList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_payment, parent, false);
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
        PaymentObject.Orders oPayment = paymentList.get(position);
        holder.tvProductID.setText(oPayment.getProductId());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }
}