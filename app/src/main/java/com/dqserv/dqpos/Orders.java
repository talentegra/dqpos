package com.dqserv.dqpos;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.POSD.controllers.PrinterController;
import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.OrderAdapter;
import com.dqserv.adapter.ProductByCategoryAdapter;
import com.dqserv.config.Constants;
import com.dqserv.connection.DBConstants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.CategoryObject;
import com.dqserv.rest.OrderObject;
import com.dqserv.rest.ProductObject;
import com.dqserv.rest.ResponseOrderObject;
import com.dqserv.widget.CustomItemClickListener;
import com.pos.printer.PrinterFunctions;
import com.pos.printer.PrinterFunctionsLAN;

import org.json.JSONArray;
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

//import com.mocoo.hang.rtprinter.driver.Contants;
//import com.mocoo.hang.rtprinter.driver.HsWifiPrintDriver;

public class Orders extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;
    static Context mContext;
    static TabLayout tabLayout;
    static String sTableId = "", sTableName = "";
    static List<OrderObject.Orders> resultOrders;
    static List<ProductObject.Products> resultProductsFromCategory;
    static List<CategoryObject.Categories> resultCategories;
    RelativeLayout rlOrders;
    FrameLayout rlPagerProducts;
    static LinearLayout llOrderHeader, llOrderFooter;
    static RecyclerView rvOrders;
    static OrderAdapter orderAdapter = null;
    static TextView tvTotal;
    static int total = 0;
    static int quantity = 0;
    static long currentOrderID = 0;
    static Button btnOrderComplete, btnOrderCancel, btnOrderConfirm;
    static RelativeLayout mProgressBar;
    HashMap<String, OrderObject.Orders> confirmOrderItems = new HashMap<String, OrderObject.Orders>();
    public String companyName = "DigitalQ Information Services";
    public String addressLine1 = "#G2,C-Block";
    public String addressLine2 = "Hansavandhana Apartment";
    public String addressLine3 = "Naidu Shop Street";
    public String addressLine4 = "Radha Nagar, Chrompet";
    public String addressLine5 = "Chennai, India - 600 040";
    public String phonenumbers = "Contact No: +91 044-2265 1990";
    public String GSTNumber = "GST No: 123456789012345";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getIntent() != null) {
            sTableId = getIntent().hasExtra("param_table_id") ?
                    getIntent().getStringExtra("param_table_id") : "";
            sTableName = getIntent().hasExtra("param_table_name") ?
                    getIntent().getStringExtra("param_table_name") : "";
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            loadCategories(ConnectivityReceiver.isConnected());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        //    ConnStateHandler connStateHandler = new ConnStateHandler();
        //   hsWifiPrintDriver.getInstance().setHandler(connStateHandler);

        mContext = Orders.this;
        total = 0;
        quantity = 0;
        currentOrderID = 0;
        resultOrders = new ArrayList<>();
        resultProductsFromCategory = new ArrayList<>();
        resultCategories = new ArrayList<>();

        if (getIntent() != null) {
            sTableId = getIntent().hasExtra("param_table_id") ?
                    getIntent().getStringExtra("param_table_id") : "";
            sTableName = getIntent().hasExtra("param_table_name") ?
                    getIntent().getStringExtra("param_table_name") : "";
        }

        int tabLayoutHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen._63sdp), getResources().getDisplayMetrics());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mProgressBar = (RelativeLayout) findViewById(R.id.orders_rl_progress);
        llOrderHeader = (LinearLayout) findViewById(R.id.orders_recycler_view_header);
        llOrderFooter = (LinearLayout) findViewById(R.id.orders_recycler_view_footer);
        rvOrders = (RecyclerView) findViewById(R.id.orders_recycler_view);
        rlOrders = (RelativeLayout) findViewById(R.id.orders_rl_listing);
        tvTotal = (TextView) findViewById(R.id.orders_tv_total);
        btnOrderComplete = (Button) findViewById(R.id.orders_btn_order_complete);
        btnOrderCancel = (Button) findViewById(R.id.orders_btn_order_cancel);
        btnOrderConfirm = (Button) findViewById(R.id.orders_btn_order_confirm);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            AppBarLayout.LayoutParams rlOrdersParams = (AppBarLayout.LayoutParams)
                    rlOrders.getLayoutParams();
            rlOrdersParams.width = displayMetrics.widthPixels;
            rlOrdersParams.height = ((displayMetrics.heightPixels - (tabLayoutHeight * 2)) * 40) / 100;

            rlPagerProducts = (FrameLayout) findViewById(R.id.orders_rl_pager_products);
            CoordinatorLayout.LayoutParams rlPagerProductsParams = (CoordinatorLayout.LayoutParams)
                    rlPagerProducts.getLayoutParams();
            rlPagerProductsParams.width = displayMetrics.widthPixels;
            rlPagerProductsParams.height = ((displayMetrics.heightPixels - (tabLayoutHeight * 2)) * 60) / 100;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(sTableName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        if (!sTableId.equalsIgnoreCase("")) {
            getOrders(sTableId, currentOrderID);
        } else {
            toolbar.setTitle("No Table Selected");
        }

        loadCategories(ConnectivityReceiver.isConnected());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        btnOrderComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultOrders.size() > 0) {
                    if (ConnectivityReceiver.isConnected()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        ApiInterface apiService =
                                ApiClient.getClient().create(ApiInterface.class);
                        JSONObject objOrder = null;
                        JSONArray jsonArray = new JSONArray();
                        for (int aIndex = 0; aIndex < resultOrders.size(); aIndex++) {
                            objOrder = new JSONObject();
                            try {
                                objOrder.put("order_sale_id", String.valueOf(resultOrders.get(aIndex).getOrderId()));
                                objOrder.put("total", String.valueOf(total));
                                objOrder.put("grand_total", String.valueOf(total));
                                objOrder.put("table_name", sTableName);
                                objOrder.put("total_items", String.valueOf(quantity));
                                objOrder.put("date", currentdateTimeInString());
                                objOrder.put("product_id", resultOrders.get(aIndex).getProductId());
                                objOrder.put("quantity", resultOrders.get(aIndex).getQuantity());
                                objOrder.put("unit_price", resultOrders.get(aIndex).getSalePrice());
                                objOrder.put("net_unit_price", resultOrders.get(aIndex).getSalePrice());
                                objOrder.put("real_unit_price", resultOrders.get(aIndex).getSalePrice());
                                objOrder.put("subtotal", resultOrders.get(aIndex).getSubTotal());
                                objOrder.put("product_code", resultOrders.get(aIndex).getProductCode());
                                objOrder.put("product_name", resultOrders.get(aIndex).getProductName());
                                objOrder.put("sale_price", resultOrders.get(aIndex).getSalePrice());
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonArray.put(objOrder);
                        }
                        Call<ResponseOrderObject> call = apiService.addOrders(Constants.AUTH_TOKEN, sTableId,
                                jsonArray);
                        call.enqueue(new Callback<ResponseOrderObject>() {
                            @Override
                            public void onResponse(Call<ResponseOrderObject> call, Response<ResponseOrderObject> response) {
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Succes your order.", Toast.LENGTH_SHORT).show();
                                saveOrderPrintTable(resultOrders);
                                saveOrderPrintItemsTable(resultOrders);
                                deleteAllOrders();
                                unregisterControls();
                                getOrders(sTableId, currentOrderID);
                                finish();
                                startActivity(new Intent(getApplicationContext(), POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }

                            @Override
                            public void onFailure(Call<ResponseOrderObject> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Somthing went wrong, please try again...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Succes your order.", Toast.LENGTH_SHORT).show();
                        saveOrderPrintTable(resultOrders);
                        saveOrderPrintItemsTable(resultOrders);
                        deleteAllOrders();
                        unregisterControls();
                        getOrders(sTableId, currentOrderID);
                        finish();
                        startActivity(new Intent(getApplicationContext(), POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                } else {
                    try {
                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.layout_popup,
                                (ViewGroup) findViewById(R.id.popup_cancel_order));
                        final PopupWindow pw = new PopupWindow(layout, (int) mContext.getResources().getDimension(R.dimen._200sdp),
                                (int) mContext.getResources().getDimension(R.dimen._180sdp), true);
                        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                        TextView msg = (TextView) layout.findViewById(R.id.message_popup);
                        msg.setText("No Orders!");
                        Button close = (Button) layout.findViewById(R.id.close_popup);
                        close.setVisibility(View.GONE);
                        Button accept = (Button) layout.findViewById(R.id.accept_popup);
                        accept.setText("Ok");
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pw.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sTableId.equalsIgnoreCase("")) {
                    try {
                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.layout_popup,
                                (ViewGroup) findViewById(R.id.popup_cancel_order));
                        final PopupWindow pw = new PopupWindow(layout, (int) mContext.getResources().getDimension(R.dimen._200sdp),
                                (int) mContext.getResources().getDimension(R.dimen._180sdp), true);
                        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                        Button close = (Button) layout.findViewById(R.id.close_popup);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pw.dismiss();
                            }
                        });
                        Button accept = (Button) layout.findViewById(R.id.accept_popup);
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Cancel your order.", Toast.LENGTH_SHORT).show();
                                deleteAllOrders();
                                unregisterControls();
                                getOrders(sTableId, currentOrderID);
                                pw.dismiss();
                                finish();
                                startActivity(new Intent(getApplicationContext(), POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        });

        btnOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultOrders.size() > 0) {
                    if (ConnectivityReceiver.isConnected()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        ApiInterface apiService =
                                ApiClient.getClient().create(ApiInterface.class);
                        JSONObject objOrder = null;
                        JSONArray jsonArray = new JSONArray();
                        for (int aIndex = 0; aIndex < resultOrders.size(); aIndex++) {
                            objOrder = new JSONObject();
                            try {
                                objOrder.put("order_sale_id", String.valueOf(resultOrders.get(aIndex).getOrderId()));
                                objOrder.put("total", String.valueOf(total));
                                objOrder.put("grand_total", String.valueOf(total));
                                objOrder.put("table_name", sTableName);
                                objOrder.put("total_items", String.valueOf(quantity));
                                objOrder.put("date", currentdateTimeInString());
                                objOrder.put("product_id", resultOrders.get(aIndex).getProductId());
                                objOrder.put("quantity", resultOrders.get(aIndex).getQuantity());
                                objOrder.put("unit_price", resultOrders.get(aIndex).getSalePrice());
                                objOrder.put("net_unit_price", resultOrders.get(aIndex).getSalePrice());
                                objOrder.put("real_unit_price", resultOrders.get(aIndex).getSalePrice());
                                objOrder.put("subtotal", resultOrders.get(aIndex).getSubTotal());
                                objOrder.put("product_code", resultOrders.get(aIndex).getProductCode());
                                objOrder.put("product_name", resultOrders.get(aIndex).getProductName());
                                objOrder.put("sale_price", resultOrders.get(aIndex).getSalePrice());
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonArray.put(objOrder);


                            OrderObject.Orders confirmOrderItemsObject = new OrderObject.Orders();
                            confirmOrderItemsObject.setProductName(resultOrders.get(aIndex).getProductName());
                            confirmOrderItemsObject.setQuantity(resultOrders.get(aIndex).getQuantity());

                            confirmOrderItems.put(String.valueOf(aIndex), confirmOrderItemsObject);
                        }
                        Call<ResponseOrderObject> call = apiService.addOrders(Constants.AUTH_TOKEN, sTableId,
                                jsonArray);
                        call.enqueue(new Callback<ResponseOrderObject>() {
                            @Override
                            public void onResponse(Call<ResponseOrderObject> call, Response<ResponseOrderObject> response) {
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Succes your order.", Toast.LENGTH_SHORT).show();
                                saveOrderPrintTable(resultOrders);
                                saveOrderPrintItemsTable(resultOrders);
                                deleteAllOrders();
                                unregisterControls();
                                getOrders(sTableId, currentOrderID);
                                printOrder(confirmOrderItems, response.body().getOrderId());
                                finish();
                                startActivity(new Intent(getApplicationContext(), POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }

                            @Override
                            public void onFailure(Call<ResponseOrderObject> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Somthing went wrong, please try again...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Succes your order.", Toast.LENGTH_SHORT).show();
                        saveOrderPrintTable(resultOrders);
                        saveOrderPrintItemsTable(resultOrders);
                        deleteAllOrders();
                        unregisterControls();
                        getOrders(sTableId, currentOrderID);
                        finish();
                        startActivity(new Intent(getApplicationContext(), POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                } else {
                    try {
                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.layout_popup,
                                (ViewGroup) findViewById(R.id.popup_cancel_order));
                        final PopupWindow pw = new PopupWindow(layout, (int) mContext.getResources().getDimension(R.dimen._200sdp),
                                (int) mContext.getResources().getDimension(R.dimen._180sdp), true);
                        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                        TextView msg = (TextView) layout.findViewById(R.id.message_popup);
                        msg.setText("No Orders!");
                        Button close = (Button) layout.findViewById(R.id.close_popup);
                        close.setVisibility(View.GONE);
                        Button accept = (Button) layout.findViewById(R.id.accept_popup);
                        accept.setText("Ok");
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pw.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /* private void printOrder() {
         if (hsWifiPrintDriver.IsNoConnection()) {
             Toast.makeText(getApplicationContext(),
                     "Connect you wifi printer.", Toast.LENGTH_SHORT).show();
         } else {
             hsWifiPrintDriver.printString("dqpos Order Bill1");
             hsWifiPrintDriver.WIFI_Write("dqpos Order Bill2");
         }
     }
  */
    private void printOrder(HashMap<String, OrderObject.Orders> confirmItems, String sOrderNo) {
        StringBuilder sbPrintData = new StringBuilder();
        int centerpoint = 0;
        int rightpoint = 0;
        String OrderNumber = "Order No: " + sOrderNo;
        if (companyName.trim().length() != 0) {
            centerpoint = getCenterPoint(companyName.trim());
            sbPrintData.append(addspace(0, centerpoint) + companyName + "\n");
        }
        if (addressLine1.trim().length() != 0) {
            centerpoint = getCenterPoint(addressLine1.trim());
            //  sbPrintData.append(addspace(0, centerpoint) + addressLine1 + "\n");
        }
        if (addressLine2.trim().length() != 0) {
            centerpoint = getCenterPoint(addressLine2.trim());
            //sbPrintData.append(addspace(0, centerpoint) + addressLine2 + "\n");
        }
    /*    if (addressLine3.trim().length() != 0) {
            centerpoint = getCenterPoint(addressLine3.trim());
         //   sbPrintData.append(addspace(0, centerpoint) + addressLine3 + "\n");
        }
        if (addressLine4.trim().length() != 0) {
            centerpoint = getCenterPoint(addressLine4.trim());
           // sbPrintData.append(addspace(0, centerpoint) + addressLine4 + "\n");
        }
      */
        if (addressLine5.trim().length() != 0) {
            centerpoint = getCenterPoint(addressLine5.trim());
            //   sbPrintData.append(addspace(0, centerpoint) + addressLine5 + "\n");
        }
        if (phonenumbers.trim().length() != 0) {
            centerpoint = getCenterPoint(phonenumbers.trim());
            // sbPrintData.append(addspace(0, centerpoint) + phonenumbers + "\n");
        }
        if (GSTNumber.trim().length() != 0) {
            centerpoint = getCenterPoint(GSTNumber.trim());
            // sbPrintData.append(addspace(0, centerpoint) + GSTNumber + "\n");
        }


        sbPrintData.append("\n\n" + getdashline() + "\n");
      /*  if (OrderNumber.trim().length() != 0) {
            rightpoint = getRightPoint(OrderNumber.trim());
             sbPrintData.append(addspace(0, rightpoint) + OrderNumber + "\n");
        }
*/
        sbPrintData.append(addspace(0, centerpoint) + "Order No: " + sOrderNo + "\n");
        sbPrintData.append(getdashline() + "\n\n");
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm a").format(Calendar.
                getInstance().getTime());
        sbPrintData.append("Date Time: " + timeStamp + "\n\n");

        sbPrintData.append("Table Name: " + sTableName + "\n");
        sbPrintData.append(getdashline() + "\n");

        String space = "  ";
        sbPrintData.append("Sno " + "Name" + space + "                               Qty" + "\n");
        sbPrintData.append(getdashline() + "\n");

        int count = 1;
        DecimalFormat format1 = new DecimalFormat("#.##");
        format1.setMinimumFractionDigits(2);
        for (Map.Entry entry : confirmItems.entrySet()) {
            OrderObject.Orders orderItem = (OrderObject.Orders) entry.getValue();
            sbPrintData.append(count + "  " + orderItem.getProductName() + "\n");
            String space1 = addspace(0, (("Sno Name" + space).length()));
            sbPrintData.append(space1 + "                              " + (format1.format(Double.
                    parseDouble(orderItem.getQuantity()))) + "\n");
            count += 1;
        }
        Log.e("Check", sbPrintData.toString());
        try {
            int resLAN = 0;
            resLAN = PrinterFunctionsLAN.PortDiscovery(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings);
            if (resLAN == 0) {
                PrinterFunctionsLAN.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,
                        0, 0, 1, 0, 0, 0,
                        5, 0, sbPrintData.toString());
                PrinterFunctionsLAN.PreformCut(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,
                        1);
            } else {
                Toast.makeText(getApplicationContext(), "No Printer Available", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception ea) {
            ea.printStackTrace();
        }
        confirmItems.clear();
    }
    private void unregisterControls() {
        quantity = 0;
        total = 0;
        currentOrderID = 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            unregisterControls();
            finish();
            startActivity(new Intent(getApplicationContext(), POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        RecyclerView rv = null;
        ProductByCategoryAdapter productByCategoryAdapter = null;

        public PlaceholderFragment() {
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                if (rv != null && (getArguments().getInt(ARG_SECTION_NUMBER) != 0
                        || getArguments().getInt(ARG_SECTION_NUMBER) == tabLayout.getTabCount() - 1)) {
                    resultProductsFromCategory.clear();
                    getProductsFromLocalByCategoryId(resultCategories.get(getArguments().getInt(ARG_SECTION_NUMBER)).getCategoryId());
                    if (resultProductsFromCategory.size() > 0) {
                        productByCategoryAdapter = new ProductByCategoryAdapter(mContext, resultProductsFromCategory,
                                new CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        if (!sTableId.equalsIgnoreCase("")) {
                                            saveOrderTable(v.getTag().toString());
                                            getOrders(sTableId, currentOrderID);
                                            updateOrder(currentOrderID);
                                        } else {
                                            Toast.makeText(getActivity(), "No Table Selected", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void deleteViewOnClick(View v, int position) {

                                    }
                                });
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                        rv.setLayoutManager(gridLayoutManager);
                        rv.setItemAnimator(new DefaultItemAnimator());
                        rv.setAdapter(productByCategoryAdapter);
                    }
                }
            }
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_orders_categories, container, false);
            rv = (RecyclerView) rootView.findViewById(R.id.orders_categories_recycler_view_products);
            if (getArguments().getInt(ARG_SECTION_NUMBER) == tabLayout.getSelectedTabPosition()) {
                resultProductsFromCategory.clear();
                getProductsFromLocalByCategoryId(resultCategories.get(getArguments().getInt(ARG_SECTION_NUMBER)).getCategoryId());
                productByCategoryAdapter = new ProductByCategoryAdapter(mContext, resultProductsFromCategory,
                        new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                if (!sTableId.equalsIgnoreCase("")) {
                                    saveOrderTable(v.getTag().toString());
                                    getOrders(sTableId, currentOrderID);
                                    updateOrder(currentOrderID);
                                } else {
                                    Toast.makeText(getActivity(), "No Table Selected", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void deleteViewOnClick(View v, int position) {

                            }
                        });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                rv.setLayoutManager(gridLayoutManager);
                rv.setItemAnimator(new DefaultItemAnimator());
                rv.setAdapter(productByCategoryAdapter);
            }

            return rootView;
        }
    }

    private void loadCategories(boolean isConnected) {
        if (isConnected) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<CategoryObject> call = apiService.getCategories
                    (Constants.AUTH_TOKEN);
            call.enqueue(new Callback<CategoryObject>() {
                @Override
                public void onResponse(Call<CategoryObject> call, Response<CategoryObject> response) {
                    if (response.body().getCategories().size() > 0) {
                        resultCategories.clear();
                        fetchResults(response);
                        for (int catTabIndex = 0; catTabIndex < resultCategories.size(); catTabIndex++) {
                            tabLayout.addTab(tabLayout.newTab().setText(resultCategories.get(catTabIndex).getCategoryName()));
                        }
                        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                                tabLayout.getTabCount());
                        mViewPager.setOffscreenPageLimit(1);
                        mViewPager.setAdapter(mSectionsPagerAdapter);
                    }
                }

                @Override
                public void onFailure(Call<CategoryObject> call, Throwable t) {
                    resultCategories.clear();
                    getCategoriesFromLocal();
                }
            });
        } else {
            resultCategories.clear();
            getCategoriesFromLocal();
        }
    }

    //get Tables
    private void fetchResults(Response<CategoryObject> response) {
        CategoryObject categoryObject = response.body();
        saveCategories(categoryObject.getCategories());
    }

    private void saveCategories(List<CategoryObject.Categories> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int tableIndex = 0; tableIndex < items.size(); tableIndex++) {
            try {
                resultCategories.add(items.get(tableIndex));
                String insertSQL = "INSERT OR REPLACE INTO categories \n" +
                        "(category_id, category_code, category_name)\n" +
                        "VALUES \n" +
                        "(" + items.get(tableIndex).getCategoryId() + ", " +
                        "'" + items.get(tableIndex).getCategoryCode() + "', " +
                        "'" + items.get(tableIndex).getCategoryName() + "');";

                myDataBase.execSQL(insertSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }

    private void getCategoriesFromLocal() {
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM categories");

        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    CategoryObject.Categories newCategory = new CategoryObject.Categories();
                    newCategory.setCategoryId(cursor.getString(cursor.getColumnIndex("category_id")));
                    newCategory.setCategoryCode(cursor.getString(cursor.getColumnIndex("category_code")));
                    newCategory.setCategoryName(cursor.getString(cursor.getColumnIndex("category_name")));


                    resultCategories.add(newCategory);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("LocalResponse", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        for (int catTabIndex = 0; catTabIndex < resultCategories.size(); catTabIndex++) {
            tabLayout.addTab(tabLayout.newTab().setText(resultCategories.get(catTabIndex).getCategoryName()));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private static void getProductsFromLocalByCategoryId(String idCategory) {
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM products WHERE category_id = " + idCategory + "");

        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ProductObject.Products newProduct = new ProductObject.Products();
                    newProduct.setProductId(cursor.getString(cursor.getColumnIndex("product_id")));
                    newProduct.setProductCode(cursor.getString(cursor.getColumnIndex("product_code")));
                    newProduct.setProductName(cursor.getString(cursor.getColumnIndex("product_name")));
                    newProduct.setSalePrice(cursor.getString(cursor.getColumnIndex("sale_price")));
                    newProduct.setCategoryId(cursor.getString(cursor.getColumnIndex("category_id")));

                    resultProductsFromCategory.add(newProduct);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("LocalResponse", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    private static void getOrders(String idTable, long sOrderID) {
        resultOrders.clear();
        String POSTS_SELECT_QUERY = "select * from order_items LEFT JOIN orders ON " +
                "order_items.order_id=orders.order_id where orders.table_id = " + idTable + "";

        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToLast()) {
                do {
                    OrderObject.Orders newOrder = new OrderObject.Orders();
                    newOrder.setOrderId(cursor.getString(cursor.getColumnIndex("order_id")));
                    newOrder.setProductId(cursor.getString(cursor.getColumnIndex("product_id")));
                    newOrder.setProductCode(cursor.getString(cursor.getColumnIndex("product_code")));
                    newOrder.setProductName(cursor.getString(cursor.getColumnIndex("product_name")));
                    newOrder.setSalePrice(cursor.getString(cursor.getColumnIndex("sale_price")));
                    newOrder.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                    newOrder.setSubTotal(cursor.getString(cursor.getColumnIndex("subtotal")));

                    resultOrders.add(newOrder);
                } while (cursor.moveToPrevious());
            }
        } catch (Exception e) {
            Log.d("LocalResponse", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        if (resultOrders.size() > 0) {
            llOrderHeader.setVisibility(View.VISIBLE);
            llOrderFooter.setVisibility(View.VISIBLE);
            orderAdapter = new OrderAdapter(resultOrders, new CustomItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {

                }

                @Override
                public void deleteViewOnClick(View v, int position) {
                    String aIDs[] = v.getTag().toString().split("\\|");
                    deleteOrder(aIDs[0], aIDs[1]);
                    getOrders(sTableId, currentOrderID);
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                    LinearLayoutManager.VERTICAL, false);
            rvOrders.setLayoutManager(linearLayoutManager);
            rvOrders.setNestedScrollingEnabled(true);
            rvOrders.setItemAnimator(new DefaultItemAnimator());
            rvOrders.setAdapter(orderAdapter);
            quantity = 0;
            total = 0;
            for (int priceIndex = 0; priceIndex < resultOrders.size(); priceIndex++) {
                quantity += (Integer.parseInt(resultOrders.get(priceIndex).getQuantity()));
                total += (Integer.parseInt(resultOrders.get(priceIndex).getSalePrice()) *
                        Integer.parseInt(resultOrders.get(priceIndex).getQuantity()));
                tvTotal.setText(total + "");
            }
        } else {
            llOrderHeader.setVisibility(View.GONE);
            llOrderFooter.setVisibility(View.GONE);
        }
    }

    private static void saveOrderTable(String sProductId) {
        boolean isAlreadyAdded = false;
        long lastInsertedOrderId = 0, lastInsertedOrderItemsId = 0;
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        if (resultOrders.size() > 0) {
            for (int productIndex = 0; productIndex < resultOrders.size(); productIndex++) {
                if (resultOrders.get(productIndex).getProductId().equalsIgnoreCase(sProductId)) {
                    isAlreadyAdded = true;
                    break;
                } else {
                    isAlreadyAdded = false;
                }
            }
        } else {
            isAlreadyAdded = false;
        }
        try {
            if (isAlreadyAdded) {
                int old_quantity = 0;
                String query = "select order_items.order_id, quantity, order_items.order_item_id from order_items LEFT JOIN orders ON " +
                        "order_items.order_id=orders.order_id where orders.table_id = " + sTableId + " " +
                        "AND order_items.product_id= " + sProductId + "";
                Cursor cursor = myDataBase.rawQuery(query, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        lastInsertedOrderId = cursor.getLong(0);
                        old_quantity = cursor.getInt(1) + 1;
                        lastInsertedOrderItemsId = cursor.getLong(2);
                        currentOrderID = lastInsertedOrderId;
                    }
                } catch (Exception e) {
                    Log.d("LocalResponse", "Error while trying to get posts from database");
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }

                String POSTS_SELECT_QUERY = String.format("SELECT * FROM products " +
                        "WHERE product_id=" + sProductId + "");
                Cursor pCursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
                try {
                    if (pCursor.moveToFirst()) {
                        do {
                            String insertItemsSQL = "INSERT OR REPLACE INTO order_items \n" +
                                    "(order_item_id, order_id, product_id, product_code, product_name, quantity, sale_price, subtotal)\n" +
                                    "VALUES \n" +
                                    "(" + lastInsertedOrderItemsId + ", " +
                                    "" + lastInsertedOrderId + ", " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_id")) + "', " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_code")) + "', " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_name")) + "', " +
                                    "" + old_quantity + ", " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("sale_price")) + "', " +
                                    "" + (Integer.parseInt(pCursor.getString(pCursor.getColumnIndex("sale_price"))) * old_quantity) + ");";
                            myDataBase.execSQL(insertItemsSQL);
                        } while (pCursor.moveToNext());
                    }
                } catch (Exception e) {
                    Log.d("LocalResponse", "Error while trying to get posts from database");
                } finally {
                    if (pCursor != null && !pCursor.isClosed()) {
                        pCursor.close();
                    }
                }
            } else {
                String queryOrder = "SELECT order_id from orders WHERE table_id=" + sTableId + "";
                Cursor cursorOrder = myDataBase.rawQuery(queryOrder, null);
                try {
                    if (cursorOrder != null && cursorOrder.moveToFirst()) {
                        lastInsertedOrderId = cursorOrder.getLong(0);
                        currentOrderID = lastInsertedOrderId;
                    } else {
                        currentOrderID = 0;
                    }
                } catch (Exception e) {
                    Log.d("LocalResponse", "Error while trying to get posts from database");
                } finally {
                    if (cursorOrder != null && !cursorOrder.isClosed()) {
                        cursorOrder.close();
                    }
                }
                if (currentOrderID == 0) {
                    String insertSQL = "INSERT OR REPLACE INTO orders \n" +
                            "(order_date, table_id, grand_total, total_items, status)\n" +
                            "VALUES \n" +
                            "('" + currentdateTimeInString() + "', " +
                            "" + sTableId + ", " +
                            "'" + total + "', " +
                            "" + quantity + ", " +
                            "" + 1 + ");";
                    myDataBase.execSQL(insertSQL);
                    String query = "SELECT order_id from orders " +
                            "order by order_id DESC limit 1";
                    Cursor cursor = myDataBase.rawQuery(query, null);
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            lastInsertedOrderId = cursor.getLong(0);
                            currentOrderID = lastInsertedOrderId;
                        }
                    } catch (Exception e) {
                        Log.d("LocalResponse", "Error while trying to get posts from database");
                    } finally {
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }

                String POSTS_SELECT_QUERY = String.format("SELECT * FROM products " +
                        "WHERE product_id=" + sProductId + "");
                Cursor pCursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
                try {
                    if (pCursor.moveToFirst()) {
                        do {
                            String insertItemsSQL = "INSERT OR REPLACE INTO order_items \n" +
                                    "(order_id, product_id, product_code, product_name, quantity, sale_price, subtotal, product_id_table_id)\n" +
                                    "VALUES \n" +
                                    "(" + lastInsertedOrderId + ", " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_id")) + "', " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_code")) + "', " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_name")) + "', " +
                                    "" + 1 + ", " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("sale_price")) + "', " +
                                    "" + (Integer.parseInt(pCursor.getString(pCursor.getColumnIndex("sale_price"))) * 1) + ", " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_id")) + "_" + sTableId + "');";

                            myDataBase.execSQL(insertItemsSQL);
                        } while (pCursor.moveToNext());
                    }
                } catch (Exception e) {
                    Log.d("LocalResponse", "Error while trying to get posts from database");
                } finally {
                    if (pCursor != null && !pCursor.isClosed()) {
                        pCursor.close();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Error", "Problem in Adding Product." + ex.getMessage());
            ex.printStackTrace();
        }
        myDataBase.close();
    }


    private static void updateOrder(long sOrderId) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        String updateSQL = "UPDATE orders SET order_date = '" + currentdateTimeInString() + "' " +
                "AND grand_total = '" + total + "' AND total_items = " + quantity + " " +
                "WHERE order_id = " + sOrderId + " AND table_id = " + sTableId + "";
        try {
            myDataBase.execSQL(updateSQL);
        } catch (Exception ex) {
            Log.e("Error", "Problem in Adding Product." + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private static void deleteOrder(String sOrderId, String sProductId) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        try {
            String deleteOrderSql = "DELETE FROM order_items WHERE order_id=" + sOrderId + " " +
                    "AND product_id = " + sProductId + "";
            myDataBase.execSQL(deleteOrderSql);

            String query = "SELECT * FROM order_items WHERE order_id = " + sOrderId + "";
            Cursor cursor = myDataBase.rawQuery(query, null);
            try {
                if (cursor != null && cursor.getCount() == 0) {
                    String deleteOrderItemsSql = "DELETE FROM orders WHERE order_id=" + sOrderId + " " +
                            "AND table_id = " + sTableId + "";
                    myDataBase.execSQL(deleteOrderItemsSql);
                }
            } catch (Exception e) {
                Log.d("LocalResponse", "Error while trying to get posts from database");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } catch (Exception ex) {
            Log.e("Error", "Problem in Adding Product." + ex.getMessage());
            ex.printStackTrace();
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }

    private static void deleteAllOrders() {

        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        try {
            if (resultOrders.size() > 0) {
                String deleteOrderSql = "DELETE FROM order_items WHERE order_id=" + resultOrders.get(0).getOrderId() + "";
                myDataBase.execSQL(deleteOrderSql);
                String deleteOrderItemsSql = "DELETE FROM orders WHERE order_id=" + resultOrders.get(0).getOrderId() + "";
                myDataBase.execSQL(deleteOrderItemsSql);
            }
        } catch (Exception ex) {
            Log.e("Error", "Problem in Adding Product." + ex.getMessage());
            ex.printStackTrace();
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public SectionsPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return mNumOfTabs;
        }
    }

    public static String currentdateTimeInString() {
        String currentDate = null;
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentDate = df.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    private void saveOrderPrintTable(List<OrderObject.Orders> items) {
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        try {
            String insertSQL = "INSERT OR REPLACE INTO order_print \n" +
                    "(order_id, order_date, table_id, grand_total, total_items, status)\n" +
                    "VALUES \n" +
                    "(" + items.get(0).getOrderId() + ", " +
                    "'" + currentdateTimeInString() + "', " +
                    "" + sTableId + ", " +
                    "'" + total + "', " +
                    "" + quantity + ", " +
                    "" + 1 + ");";
            myDataBase.execSQL(insertSQL);
        } catch (Exception ex) {
            Log.e("Error", "Problem in Adding Product." + ex.getMessage());
            ex.printStackTrace();
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }

    private void saveOrderPrintItemsTable(List<OrderObject.Orders> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int oIndex = 0; oIndex < items.size(); oIndex++) {
            try {
                String insertItemsSQL = "INSERT OR REPLACE INTO order_print_items \n" +
                        "(order_id, product_id, product_code, product_name, quantity, sale_price, subtotal, product_id_table_id)\n" +
                        "VALUES \n" +
                        "(" + items.get(oIndex).getOrderId() + ", " +
                        "'" + items.get(oIndex).getProductId() + "', " +
                        "'" + items.get(oIndex).getProductCode() + "', " +
                        "'" + items.get(oIndex).getProductName() + "', " +
                        "" + items.get(oIndex).getQuantity() + ", " +
                        "'" + items.get(oIndex).getSalePrice() + "', " +
                        "" + (Integer.parseInt(items.get(oIndex).getSalePrice()) * Integer.parseInt(items.get(oIndex).getQuantity())) + ", " +
                        "'" + items.get(oIndex).getProductId() + "_" + sTableId + "');";
                myDataBase.execSQL(insertItemsSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        unregisterControls();
        finish();
        startActivity(new Intent(getApplicationContext(), POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    /*
    private class ConnStateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            switch (data.getInt("flag")) {
                /*The FLAG_STATE_CHANGE indicat that connection state will change when the Handler received message*/
    //            case Contants.FLAG_STATE_CHANGE:
                    /*This Case indicate the current connection state that include four state and you can find a state from the Contants Class
                    UNCONNECTED、
                    CONNECTED_BY_BLUETOOTH、
                    CONNECTED_BY_USB、
                    CONNECTED_BY_WIFI	)*/
      /*              int state = data.getInt("state");
                    //As for receiving state , you can write corresponding Code in here
                    break;
                   /*The FLAG_FAIL_CONNECT indicat that connection is Failed when the Handler received message*/

        /*        case Contants.FLAG_FAIL_CONNECT:
                    //As for receiving state , you can write corresponding Code in here
                    break;
                    /*The FLAG_FAIL_CONNECT indicat that connection is Successful when the Handler received message*/

          /*      case Contants.FLAG_SUCCESS_CONNECT:
                    //As for receiving state , you can write corresponding Code in here
                    connectWifi(GlobalApplication.wifiIpPref.getString("wifi_ip", "192.168.1.1"),
                            GlobalApplication.wifiPortPref.getInt("wifi_port", 80));
                    break;
            }
        }
    }

    private void connectWifi(final String ip, final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hsWifiPrintDriver = HsWifiPrintDriver.getInstance();
                hsWifiPrintDriver.WIFISocket(ip, port);
            }
        }).start();
    }
    */
    //bluetooth printer

    public int getCenterPoint(String line) {
        double maxline = 48;
        int center = (int) (maxline / 2);
        int centerlength = (int) (line.length() / 2);
        int x = center - centerlength;
        return x;
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
        for (int i = 0; i < 48; i++) {
            line += "-";
        }
        return line;
    }

    public int getRightPoint(String line) {
        double maxline = 48;
        double actline = line.length();
        int x = ((int) maxline) - ((int) actline);
        return x;
    }

    private void TcrPrintOrder(HashMap<String, OrderObject.Orders> confirmItems, String sOrderNo) {
        StringBuilder sbPrintData = new StringBuilder();
        int centerpoint = 0;
        int rightpoint = 0;
        String OrderNumber = "Order No: " + sOrderNo;

        try {
            int printerStatus = 0;
            PrinterController printerController = PrinterController.getInstance();

            // connect printer
            printerStatus =  printerController.PrinterController_Open();
            Toast.makeText(getApplicationContext(), "Printer Status: " + printerStatus, Toast.LENGTH_SHORT).show();
            if (printerStatus==0) {


                PrinterController.getInstance().PrinterController_PrinterLanguage(1);
                printerController.PrinterController_Linefeed();

                if (companyName.trim().length() != 0) {
                    centerpoint = getCenterPoint(companyName.trim());
                    printerController.PrinterController_Print(print(addspace(0, centerpoint) + companyName));
                    printerController.PrinterController_Linefeed();

                }
                if (addressLine1.trim().length() != 0) {
                    centerpoint = getCenterPoint(addressLine1.trim());
                    //  sbPrintData.append(addspace(0, centerpoint) + addressLine1 + "\n");
                }
                if (addressLine2.trim().length() != 0) {
                    centerpoint = getCenterPoint(addressLine2.trim());
                    //sbPrintData.append(addspace(0, centerpoint) + addressLine2 + "\n");
                }

                if (addressLine5.trim().length() != 0) {
                    centerpoint = getCenterPoint(addressLine5.trim());
                    //   sbPrintData.append(addspace(0, centerpoint) + addressLine5 + "\n");
                }
                if (phonenumbers.trim().length() != 0) {
                    centerpoint = getCenterPoint(phonenumbers.trim());
                    // sbPrintData.append(addspace(0, centerpoint) + phonenumbers + "\n");
                }
                if (GSTNumber.trim().length() != 0) {
                    centerpoint = getCenterPoint(GSTNumber.trim());
                    // sbPrintData.append(addspace(0, centerpoint) + GSTNumber + "\n");
                }

                printerController.PrinterController_Print(print(getdashline()));
                printerController.PrinterController_Linefeed();

                printerController.PrinterController_Print(print(addspace(0, centerpoint) + "Order No: " + sOrderNo + "\n"));

                printerController.PrinterController_Print(print(getdashline() + "\n"));

                String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm a").format(Calendar.
                        getInstance().getTime());
                printerController.PrinterController_Print(print("Date Time: " + timeStamp + "\n\n"));

                printerController.PrinterController_Print(print("Table Name: " + sTableName + "\n"));
                printerController.PrinterController_Print(print(getdashline() + "\n"));

                String space = "  ";
                printerController.PrinterController_Print(print("Sno " + "Name" + space + "                 Qty \n"));
                printerController.PrinterController_Print(print(getdashline() + "\n"));

                int count = 1;
                DecimalFormat format1 = new DecimalFormat("#.##");
                format1.setMinimumFractionDigits(2);
                for (Map.Entry entry : confirmItems.entrySet()) {
                    OrderObject.Orders orderItem = (OrderObject.Orders) entry.getValue();
                    printerController.PrinterController_Print(print(count + "  " + orderItem.getProductName() + "\n"));
                    //String space1 = addspace(0, (("Sno Name" + space).length()));
                    //printerController.PrinterController_Print(print(space1 + "               " + (format1.format(orderItem.getQuantity())) + "\n"));
                    String space1 = addspace(0, (("Sno Name" + space).length()));
                    printerController.PrinterController_Print(print(space1 + "               " + (format1.format(Double.
                            parseDouble(orderItem.getQuantity()))) + "\n"));
                    count += 1;
                }
                printerController.PrinterController_Linefeed();
                printerController.PrinterController_Linefeed();
                printerController.PrinterController_Linefeed();
                printerController.PrinterController_Linefeed();


            } else {
                Toast.makeText(getApplicationContext(), "No Printer Available", Toast.LENGTH_SHORT).show();
                // PrinterFunctions.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,0, 0, 1, 0, 0, 5, 0, "Welcome to DQPOS Common");
            }
        } catch (Exception ea) {
            ea.printStackTrace();
        }
        confirmItems.clear();
    }


    public byte[] print(String line) {
        return new StringBuffer(line).reverse().toString().getBytes();
    }

}