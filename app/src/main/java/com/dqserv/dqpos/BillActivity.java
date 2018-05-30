package com.dqserv.dqpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.BillAdapter;
import com.dqserv.config.Constants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.BillObject;
import com.dqserv.widget.CustomItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<BillObject.Orders> results;
    BillAdapter billAdapter;
    RelativeLayout mProgressBar;
    LinearLayout mOfflineView;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (RelativeLayout) findViewById(R.id.bill_rl_progress);
        mOfflineView = (LinearLayout) findViewById(R.id.bill_rl_offline);
        rv = (RecyclerView) findViewById(R.id.bill_recycler_view);

        results = new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
            mOfflineView.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<BillObject> call = apiService.getOrders(Constants.AUTH_TOKEN);
            call.enqueue(new Callback<BillObject>() {
                @Override
                public void onResponse(Call<BillObject> call, Response<BillObject> response) {
                    results.clear();
                    results = fetchResults(response);
                    if (results.size() > 0) {
                        billAdapter = new BillAdapter(results, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                String aBillData[] = v.getTag().toString().split("\\|");
                                finish();
                                startActivity(new Intent(getApplicationContext(),
                                        PaymentActivity.class)
                                        .putExtra("order_sale_id", aBillData[0])
                                        .putExtra("total_items", aBillData[1])
                                        .putExtra("grand_total", aBillData[2])
                                        .putExtra("table_name", aBillData[3])
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }

                            @Override
                            public void deleteViewOnClick(View v, int position) {

                            }
                        });
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        rv.setLayoutManager(linearLayoutManager);
                        rv.setItemAnimator(new DefaultItemAnimator());
                        rv.setAdapter(billAdapter);
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<BillObject> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            mOfflineView.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mOfflineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected()) {
                    mOfflineView.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<BillObject> call = apiService.getOrders(Constants.AUTH_TOKEN);
                    call.enqueue(new Callback<BillObject>() {
                        @Override
                        public void onResponse(Call<BillObject> call, Response<BillObject> response) {
                            results.clear();
                            results = fetchResults(response);
                            if (results.size() > 0) {
                                billAdapter = new BillAdapter(results, new CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        String aBillData[] = v.getTag().toString().split("\\|");
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),
                                                PaymentActivity.class)
                                                .putExtra("order_sale_id", aBillData[0])
                                                .putExtra("total_items", aBillData[1])
                                                .putExtra("grand_total", aBillData[2])
                                                .putExtra("table_name", aBillData[3])
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }

                                    @Override
                                    public void deleteViewOnClick(View v, int position) {

                                    }
                                });
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                        getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                rv.setLayoutManager(linearLayoutManager);
                                rv.setItemAnimator(new DefaultItemAnimator());
                                rv.setAdapter(billAdapter);
                            }
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<BillObject> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    rv.setVisibility(View.GONE);
                    mOfflineView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //get Tables
    private List<BillObject.Orders> fetchResults(Response<BillObject> response) {
        BillObject billObj = response.body();
        return billObj.getOrders();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class).
                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_pos) {
            // Handle the camera action
            if (!BillActivity.class.getSimpleName().equalsIgnoreCase("POS")) {
                startActivity(new Intent(this, POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_gallery) {
            if (!BillActivity.class.getSimpleName().equalsIgnoreCase("Orders")) {
                startActivity(new Intent(this, OrdersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_slideshow) {
            if (!BillActivity.class.getSimpleName().equalsIgnoreCase("BillActivity")) {
                startActivity(new Intent(this, BillActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_products) {
            if (!BillActivity.class.getSimpleName().equalsIgnoreCase("Products")) {
                startActivity(new Intent(this, Products.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_categories) {
            if (!BillActivity.class.getSimpleName().equalsIgnoreCase("Categories")) {
                startActivity(new Intent(this, Categories.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_tables) {
            if (!BillActivity.class.getSimpleName().equalsIgnoreCase("Tables")) {
                startActivity(new Intent(this, Tables.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_orders_list) {
            if (!BillActivity.class.getSimpleName().equalsIgnoreCase("OrdersList")) {
                startActivity(new Intent(this, OrdersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_printers) {
            if (!BillActivity.class.getSimpleName().equalsIgnoreCase("PrintersActivity")) {
                startActivity(new Intent(this, PrintersActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}