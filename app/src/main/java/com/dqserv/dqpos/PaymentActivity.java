package com.dqserv.dqpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mProgressBar = (RelativeLayout) findViewById(R.id.payment_rl_progress);
        rv = (RecyclerView) findViewById(R.id.payment_recycler_view);

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
