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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.OrderItemsAdapter;
import com.dqserv.config.Constants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.BillProductObject;
import com.dqserv.rest.OrderItemsObject;
import com.dqserv.widget.CustomItemClickListener;
import com.pos.printer.PrinterFunctions;
import com.pos.printer.PrinterFunctionsLAN;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    List<OrderItemsObject.Orders> results;
    OrderItemsAdapter orderItemsAdapter;
    RelativeLayout mProgressBar;
    LinearLayout mOfflineView;
    RecyclerView rv;
    String sTabeleId = "";
    Button btnCompleteOrder, btnCancelOrder;
    HashMap<String, OrderItemsObject.Orders> orderItems = new HashMap<String, OrderItemsObject.Orders>();
    public String companyName = "DigitalQ Information Services";
    public String addressLine1 = "#G2,C-Block";
    public String addressLine2 = "Hansavandhana Apartment";
    public String addressLine3 = "Naidu Shop Street";
    public String addressLine4 = "Radha Nagar, Chrompet";
    public String addressLine5 = "Chennai, India - 600 044";
    public String phonenumbers = "Phone: +91-(0)44-2265 1990";
    public String GSTNumber = "GST: 123456789012";
    public String thankyou = "Thank You !! Visit Again";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);

        sTabeleId = getIntent().getStringExtra("table_id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCompleteOrder = (Button) findViewById(R.id.order_items_btn_complete_order);
        btnCancelOrder = (Button) findViewById(R.id.order_items_btn_cancel_order);
        mProgressBar = (RelativeLayout) findViewById(R.id.order_items_rl_progress);
        mOfflineView = (LinearLayout) findViewById(R.id.order_items_rl_offline);
        rv = (RecyclerView) findViewById(R.id.order_items_recycler_view);

        results = new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
            mOfflineView.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<OrderItemsObject> call = apiService.getOrderedTableItems(Constants.AUTH_TOKEN, sTabeleId);
            call.enqueue(new Callback<OrderItemsObject>() {
                @Override
                public void onResponse(Call<OrderItemsObject> call, Response<OrderItemsObject> response) {
                    results.clear();
                    results = fetchResults(response);
                    if (results.size() > 0) {
                        for (int aIndex = 0; aIndex < results.size(); aIndex++) {
                            OrderItemsObject.Orders orderItemsObject = new OrderItemsObject.Orders();
                            orderItemsObject.setProductName(results.get(aIndex).getProductName());
                            orderItemsObject.setQuantity(results.get(aIndex).getQuantity());
                            orderItemsObject.setSubTotal(results.get(aIndex).getSubTotal());

                            orderItems.put(String.valueOf(aIndex), orderItemsObject);
                        }
                        orderItemsAdapter = new OrderItemsAdapter(results, new CustomItemClickListener() {
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
                        rv.setAdapter(orderItemsAdapter);
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<OrderItemsObject> call, Throwable t) {
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
                    Call<OrderItemsObject> call = apiService.getOrderedTableItems(Constants.AUTH_TOKEN, sTabeleId);
                    call.enqueue(new Callback<OrderItemsObject>() {
                        @Override
                        public void onResponse(Call<OrderItemsObject> call, Response<OrderItemsObject> response) {
                            results.clear();
                            results = fetchResults(response);
                            if (results.size() > 0) {
                                orderItemsAdapter = new OrderItemsAdapter(results, new CustomItemClickListener() {
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
                                rv.setAdapter(orderItemsAdapter);
                            }
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<OrderItemsObject> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    rv.setVisibility(View.GONE);
                    mOfflineView.setVisibility(View.VISIBLE);
                }
            }
        });

        btnCompleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printOrder(orderItems);
                orderItems.clear();
            }
        });

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //get Tables
    private List<OrderItemsObject.Orders> fetchResults(Response<OrderItemsObject> response) {
        OrderItemsObject billObj = response.body();
        return billObj.getOrders();
    }


    private void printOrder(HashMap<String, OrderItemsObject.Orders> orderItems) {
        int res = 0;
        if (WifiPrinterActivity.isLAN) {
            PrinterFunctionsLAN.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,
                    0, 0, 1, 0, 0, 0,
                    5, 1, companyName + "\n" + addressLine1 + "\n"
                            + addressLine2 + addressLine3 + "\n" + addressLine4 + "\n" + addressLine5
                            + "\n" + phonenumbers + "\n" + GSTNumber + "\n\n\n");
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm a").format(Calendar.getInstance().getTime());
            String space = "  ";
            PrinterFunctionsLAN.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,
                    0, 0, 0, 0, 0, 0,
                    0, 2, timeStamp + "\n");
            DecimalFormat format1 = new DecimalFormat("#.##");
            format1.setMinimumFractionDigits(2);
            for (Map.Entry entry : orderItems.entrySet()) {
                String key = entry.getKey().toString();
                OrderItemsObject.Orders orderItem = (OrderItemsObject.Orders) entry.getValue();
                PrinterFunctionsLAN.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,
                        0, 0, 0, 0, 0, 0,
                        5, 0, orderItem.getProductName());
                String space1 = addspace(0, ((space + "Qty" + space).length() - -(format1.format(orderItem.getQuantity())).length()));
                String space2 = addspace(0, ((space + "Price" + space).length() - -(format1.format(orderItem.getSalePrice())).length()));
                String space3 = addspace(0, ((space + "Amount" + space).length() - -(format1.format(orderItem.getSubTotal())).length()));
                PrinterFunctionsLAN.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,
                        0, 0, 0, 0, 0, 0,
                        0, 0, space1 + " " + space2 + " " + space3 + "\n");
            }
            PrinterFunctionsLAN.PreformCut(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings, 1);
        } else {
            res = PrinterFunctions.CheckStatus(
                    WifiPrinterActivity.portName,
                    WifiPrinterActivity.portSettings,
                    WifiPrinterActivity.value_StatusSpecified);
            if (res == 1) {
                PrinterFunctions.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings, 0, 0, 0, 0, 0, 0, 5, 0, "Welcome to DQPOS First 1");
            }
            if (res == 0) {
                PrinterFunctions.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings, 0, 0, 0, 0, 0, 0, 5, 0, "Welcome to DQPOS First 0");
            }
            if (res == 2) {
                PrinterFunctions.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings, 0, 0, 0, 0, 0, 0, 5, 0, "Welcome to DQPOS First 2");
            }
            PrinterFunctions.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings, 0, 0, 1, 0, 0, 0, 5, 0, "Welcome to DQPOS Common");
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            finish();
        } else {
            finish();
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
            if (!OrderItemsActivity.class.getSimpleName().equalsIgnoreCase("POS")) {
                startActivity(new Intent(this, POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_gallery) {
            if (!OrderItemsActivity.class.getSimpleName().equalsIgnoreCase("OrdersList")) {
                startActivity(new Intent(this, OrdersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_slideshow) {
            if (!OrderItemsActivity.class.getSimpleName().equalsIgnoreCase("BillActivity")) {
                startActivity(new Intent(this, BillActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_products) {
            if (!OrderItemsActivity.class.getSimpleName().equalsIgnoreCase("Products")) {
                startActivity(new Intent(this, Products.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_categories) {
            if (!OrderItemsActivity.class.getSimpleName().equalsIgnoreCase("Categories")) {
                startActivity(new Intent(this, Categories.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_tables) {
            if (!OrderItemsActivity.class.getSimpleName().equalsIgnoreCase("Tables")) {
                startActivity(new Intent(this, Tables.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_orders_list) {
            if (!OrderItemsActivity.class.getSimpleName().equalsIgnoreCase("OrdersList")) {
                startActivity(new Intent(this, OrdersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_printers) {
            if (!OrderItemsActivity.class.getSimpleName().equalsIgnoreCase("PrintersActivity")) {
                startActivity(new Intent(this, PrintersActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String addspace(int start, int end) {
        String space = "";
        for (int i = start; i < end; i++) {
            space += " ";
        }
        return space;
    }

    public String getdashline() {
        String line = "";
        for (int i = 0; i < 32; i++) {
            line += "-";
        }
        return line;
    }

    public int getRightPoint(String line) {
        double maxline = 32;
        double actline = line.length();
        int x = ((int) maxline) - ((int) actline);
        return x;
    }
}