package com.dqserv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dqserv.dqpos.R;
import com.dqserv.rest.PrinterObject;
import com.dqserv.rest.PrinterObject;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class PrinterAdapter extends RecyclerView.Adapter<PrinterAdapter.MyViewHolder> {

    private List<PrinterObject.Printers> printersList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPrinterName, tvPrinterIP, tvPrinterPort;

        public MyViewHolder(View view) {
            super(view);
            tvPrinterName = (TextView) view.findViewById(R.id.row_printer_name);
            tvPrinterIP = (TextView) view.findViewById(R.id.row_printer_ip_address);
            tvPrinterPort = (TextView) view.findViewById(R.id.row_printer_port);
        }
    }


    public PrinterAdapter(Context mContext, List<PrinterObject.Printers> printersList) {
        this.mContext = mContext;
        this.printersList = printersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_printer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PrinterObject.Printers oPrinter = printersList.get(position);
        holder.tvPrinterName.setText("PrinterName: " + oPrinter.getTitle());
        holder.tvPrinterIP.setText("IP Address: " + oPrinter.getIpAddress());
        holder.tvPrinterPort.setText("Port: " + oPrinter.getPort());
    }

    @Override
    public int getItemCount() {
        return printersList.size();
    }
}