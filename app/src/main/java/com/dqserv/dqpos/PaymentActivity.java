package com.dqserv.dqpos;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.PaymentAdapter;
import com.dqserv.config.Constants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.PaymentObject;
import com.dqserv.widget.CustomItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    List<PaymentObject.Orders> results;
    PaymentAdapter paymentAdapter;
    RelativeLayout mProgressBar;
    RecyclerView rv;
    Button btnPayment, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mProgressBar = (RelativeLayout) findViewById(R.id.payment_rl_progress);
        rv = (RecyclerView) findViewById(R.id.payment_recycler_view);
        btnPayment = (Button) findViewById(R.id.payment_btn_payment);
        btnCancel = (Button) findViewById(R.id.payment_btn_cancel);

        results = new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<PaymentObject> call = apiService.getOrderById(Constants.AUTH_TOKEN,
                    getIntent().getStringExtra("order_sale_id"));
            call.enqueue(new Callback<PaymentObject>() {
                @Override
                public void onResponse(Call<PaymentObject> call, Response<PaymentObject> response) {
                    results.clear();
                    results = fetchResults(response);
                    if (results.size() > 0) {
                        paymentAdapter = new PaymentAdapter(results, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                            }

                            @Override
                            public void deleteViewOnClick(View v, int position) {

                            }
                        });
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        rv.setLayoutManager(linearLayoutManager);
                        rv.setItemAnimator(new DefaultItemAnimator());
                        rv.setAdapter(paymentAdapter);
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<PaymentObject> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog paymentDialog = new Dialog(PaymentActivity.this, R.style.AppTheme);
                paymentDialog.setContentView(R.layout.layout_payment_popup);

                TextView tvTotalItems = (TextView) paymentDialog
                        .findViewById(R.id.popup_payment_tv_total_items);
                TextView tvPayble = (TextView) paymentDialog
                        .findViewById(R.id.popup_payment_tv_total_payable);
                TextView tvPaying = (TextView) paymentDialog
                        .findViewById(R.id.popup_payment_tv_total_paying);
                TextView tvBalance = (TextView) paymentDialog
                        .findViewById(R.id.popup_payment_tv_balance);

                Button btnClose = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_close);

                Button btnSubmit = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_submit);


                tvTotalItems.setText(getIntent().getStringExtra("total_items").toString().equalsIgnoreCase("null")
                        ? "0" : getIntent().getStringExtra("total_items"));
                tvPayble.setText(getIntent().getStringExtra("grand_total").toString().equalsIgnoreCase("null")
                        ? "0" : getIntent().getStringExtra("grand_total"));

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        paymentDialog.dismiss();
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                paymentDialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), BillActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    //get Tables
    private List<PaymentObject.Orders> fetchResults(Response<PaymentObject> response) {
        PaymentObject paymentObj = response.body();
        return paymentObj.getOrders();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), BillActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
