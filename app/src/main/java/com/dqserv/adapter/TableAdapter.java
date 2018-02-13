package com.dqserv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dqserv.dqpos.R;
import com.dqserv.rest.TableObject;
import com.dqserv.widget.CustomItemClickListener;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.MyViewHolder> {

    private List<TableObject.Tables> tablesList;
    CustomItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTableName;

        public MyViewHolder(View view) {
            super(view);
            tvTableName = (TextView) view.findViewById(R.id.row_table_tv_name);
        }
    }


    public TableAdapter(List<TableObject.Tables> tablesList, CustomItemClickListener listener) {
        this.tablesList = tablesList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_table, parent, false);
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
        TableObject.Tables oTable = tablesList.get(position);
        holder.itemView.setTag(oTable.getTableId());
        holder.tvTableName.setText(oTable.getTableName());
    }

    @Override
    public int getItemCount() {
        return tablesList.size();
    }
}