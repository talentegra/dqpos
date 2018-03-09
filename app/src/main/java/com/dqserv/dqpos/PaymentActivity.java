package com.dqserv.dqpos;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.POSD.controllers.PrinterController;
import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.PaymentAdapter;
import com.dqserv.config.Constants;
import com.dqserv.connection.DBConstants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.BillProductObject;
import com.dqserv.rest.PaymentObject;
import com.dqserv.rest.SaleObject;
import com.dqserv.widget.CustomItemClickListener;

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

public class PaymentActivity extends AppCompatActivity {

    List<PaymentObject.Orders> results;
    HashMap<String, BillProductObject> billproducts = new HashMap<String, BillProductObject>();

    PaymentAdapter paymentAdapter;
    RelativeLayout mProgressBar;
    LinearLayout mOfflineView, llButtons;
    RecyclerView rv;
    Button btnPayment, btnCancel;

    double billcount = 1;
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
        setContentView(R.layout.activity_payment);
        mProgressBar = (RelativeLayout) findViewById(R.id.payment_rl_progress);
        mOfflineView = (LinearLayout) findViewById(R.id.payment_rl_offline);
        llButtons = (LinearLayout) findViewById(R.id.payment_ll_buttons);
        rv = (RecyclerView) findViewById(R.id.payment_recycler_view);
        btnPayment = (Button) findViewById(R.id.payment_btn_payment);
        btnCancel = (Button) findViewById(R.id.payment_btn_cancel);

        results = new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mOfflineView.setVisibility(View.GONE);
            llButtons.setVisibility(View.VISIBLE);
            rv.setVisibility(View.VISIBLE);
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
        } else {
            mOfflineView.setVisibility(View.VISIBLE);
            llButtons.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
        }

        mOfflineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mOfflineView.setVisibility(View.GONE);
                    llButtons.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
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
                } else {
                    mOfflineView.setVisibility(View.VISIBLE);
                    llButtons.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                }
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] nPayning = {0};
                final float[] nBalance = {0f};
                final Dialog paymentDialog = new Dialog(PaymentActivity.this, R.style.AppTheme);
                paymentDialog.setContentView(R.layout.layout_payment_popup);

                final RelativeLayout mPopupProgressBar = (RelativeLayout) paymentDialog
                        .findViewById(R.id.popup_payment_rl_progress);
                final TextView tvTotalItems = (TextView) paymentDialog
                        .findViewById(R.id.popup_payment_tv_total_items);
                final TextView tvPayble = (TextView) paymentDialog
                        .findViewById(R.id.popup_payment_tv_total_payable);
                final TextView tvPaying = (TextView) paymentDialog
                        .findViewById(R.id.popup_payment_tv_total_paying);
                final TextView tvBalance = (TextView) paymentDialog
                        .findViewById(R.id.popup_payment_tv_balance);

                Button btnClose = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_close);
                Button btnClear = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_clear);
                Button btnSubmit = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_submit);

                Button btn1Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_one_rs);
                Button btn2Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_two_rs);
                Button btn5Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_five_rs);
                Button btn10Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_ten_rs);
                Button btn20Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_twenty_rs);
                Button btn50Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_fifty_rs);
                Button btn100Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_hundred_rs);
                Button btn200Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_two_hundred_rs);
                Button btn500Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_five_hundred_rs);
                Button btn2000Rs = (Button) paymentDialog
                        .findViewById(R.id.popup_payment_btn_two_thousand_rs);


                tvTotalItems.setText(getIntent().getStringExtra("total_items").toString().equalsIgnoreCase("null")
                        ? "0" : getIntent().getStringExtra("total_items"));
                tvPayble.setText(getIntent().getStringExtra("grand_total").toString().equalsIgnoreCase("null")
                        ? "0" : getIntent().getStringExtra("grand_total"));

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = 0;
                        nBalance[0] = 0f;
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                        paymentDialog.dismiss();
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Float.parseFloat(tvBalance.getText().toString()) < 0.0
                                || Float.parseFloat(tvPaying.getText().toString()) == 0.0) {
                            Toast.makeText(getApplicationContext(), "Please pay fill amount", Toast.LENGTH_SHORT).show();
                        } else {
                            if (ConnectivityReceiver.isConnected()) {
                                mPopupProgressBar.setVisibility(View.VISIBLE);
                                ApiInterface apiService =
                                        ApiClient.getClient().create(ApiInterface.class);
                                JSONObject objOrder = null;
                                JSONArray jsonArray = new JSONArray();
                                for (int aIndex = 0; aIndex < results.size(); aIndex++) {
                                    objOrder = new JSONObject();
                                    try {
                                        objOrder.put("total", tvPayble.getText().toString());
                                        objOrder.put("grand_total", tvPayble.getText().toString());
                                        objOrder.put("table_name", getIntent().getStringExtra("table_name"));
                                        objOrder.put("total_items", tvTotalItems.getText().toString());
                                        objOrder.put("date", currentdateTimeInString());
                                        objOrder.put("product_id", results.get(aIndex).getProductId());
                                        objOrder.put("quantity", results.get(aIndex).getQuantity());
                                        objOrder.put("unit_price", results.get(aIndex).getUnitPrice());
                                        objOrder.put("net_unit_price", results.get(aIndex).getUnitPrice());
                                        objOrder.put("real_unit_price", results.get(aIndex).getUnitPrice());
                                        objOrder.put("subtotal", results.get(aIndex).getSubtotal());
                                        objOrder.put("product_code", results.get(aIndex).getProductCode());
                                        objOrder.put("product_name", results.get(aIndex).getProductName());
                                        objOrder.put("sale_price", results.get(aIndex).getUnitPrice());
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    jsonArray.put(objOrder);
                                    BillProductObject billProduct = new BillProductObject();
                                    billProduct.setProductName(results.get(aIndex).getProductName());
                                    billProduct.setAmount(Double.parseDouble(results.get(aIndex).getSubtotal()));
                                    billProduct.setPrice(Double.parseDouble(results.get(aIndex).getUnitPrice()));
                                    billProduct.setQuantity(Double.parseDouble(results.get(aIndex).getQuantity()));

                                    billproducts.put(String.valueOf(aIndex), billProduct);
                                }
                                Call<SaleObject> call = apiService.addSalesData(Constants.AUTH_TOKEN,
                                        getIntent().getStringExtra("order_sale_id"), jsonArray);
                                call.enqueue(new Callback<SaleObject>() {
                                    @Override
                                    public void onResponse(Call<SaleObject> call, Response<SaleObject> response) {
                                        nPayning[0] = 0;
                                        nBalance[0] = 0f;
                                        tvPaying.setText(String.valueOf(nPayning[0]));
                                        tvBalance.setText(String.valueOf(nBalance[0]));
                                        mPopupProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Succes your Sale.", Toast.LENGTH_SHORT).show();
                                        saveSalesPrintTable(results);
                                        saveSalesPrintItemsTable(results);
                                        deleteAllOrders();
                                        printBill(billproducts);
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), BillActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }

                                    @Override
                                    public void onFailure(Call<SaleObject> call, Throwable t) {
                                        mPopupProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Somthing went wrong, please try again...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Check internet connection.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                btn1Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn2Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn5Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn10Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn20Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn50Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn100Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn200Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn500Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });
                btn2000Rs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = nPayning[0] + Integer.parseInt(view.getTag().toString());
                        nBalance[0] = nPayning[0] - Float.parseFloat(tvPayble.getText().toString());
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
                    }
                });

                btnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nPayning[0] = 0;
                        nBalance[0] = 0f;
                        tvPaying.setText(String.valueOf(nPayning[0]));
                        tvBalance.setText(String.valueOf(nBalance[0]));
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

    private void saveSalesPrintTable(List<PaymentObject.Orders> items) {
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        try {
            String insertSQL = "INSERT OR REPLACE INTO sales \n" +
                    "(sale_id, sale_date, table_id, grand_total, total_items, status)\n" +
                    "VALUES \n" +
                    "(" + items.get(0).getOrderSaleId() + ", " +
                    "'" + currentdateTimeInString() + "', " +
                    "" + getTableId(getIntent().getStringExtra("table_name")) + ", " +
                    "'" + getIntent().getStringExtra("grand_total") + "', " +
                    "" + getIntent().getStringExtra("total_items") + ", " +
                    "" + 1 + ");";
            myDataBase.execSQL(insertSQL);
        } catch (Exception ex) {
            Log.e("Error", "Problem in Adding Product." + ex.getMessage());
            ex.printStackTrace();
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }

    private void saveSalesPrintItemsTable(List<PaymentObject.Orders> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int oIndex = 0; oIndex < items.size(); oIndex++) {
            try {
                String insertItemsSQL = "INSERT OR REPLACE INTO sale_items \n" +
                        "(sale_id, product_id, product_code, product_name, quantity, sale_price, subtotal, product_id_table_id)\n" +
                        "VALUES \n" +
                        "(" + items.get(oIndex).getOrderSaleId() + ", " +
                        "'" + items.get(oIndex).getProductId() + "', " +
                        "'" + items.get(oIndex).getProductCode() + "', " +
                        "'" + items.get(oIndex).getProductName() + "', " +
                        "" + items.get(oIndex).getQuantity() + ", " +
                        "'" + items.get(oIndex).getUnitPrice() + "', " +
                        "" + (Float.parseFloat(items.get(oIndex).getUnitPrice()) * Float.parseFloat(items.get(oIndex).getQuantity())) + ", " +
                        "'" + items.get(oIndex).getProductId() + "_" + getTableId(getIntent().getStringExtra("table_name")) + "');";
                myDataBase.execSQL(insertItemsSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }

    private void deleteAllOrders() {
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        try {
            if (results.size() > 0) {
                String deleteOrderSql = "DELETE FROM order_print_items WHERE order_id=" + results.get(0).getOrderSaleId() + "";
                myDataBase.execSQL(deleteOrderSql);
                String deleteOrderItemsSql = "DELETE FROM order_print WHERE order_id=" + results.get(0).getOrderSaleId() + "";
                myDataBase.execSQL(deleteOrderItemsSql);
            }
        } catch (Exception ex) {
            Log.e("Error", "Problem in Adding Product." + ex.getMessage());
            ex.printStackTrace();
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }

    private int getTableId(String sTableName) {
        int nTableId = 0;
        String POSTS_SELECT_QUERY = String.format("SELECT table_id FROM tables WHERE " +
                "table_name = '" + sTableName + "'");

        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                nTableId = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.d("LocalResponse", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return nTableId;
    }


    private void printBill(HashMap<String, BillProductObject> billproducts) {
        try {
            PrinterController printerController = PrinterController.getInstance();

            // connect printer
            printerController.PrinterController_Open();

            int centerpoint = 0;

            PrinterController.getInstance().PrinterController_PrinterLanguage(1);
            printerController.PrinterController_Linefeed();

            if (companyName.trim().length() != 0) {
                centerpoint = getCenterPoint(companyName.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + companyName));
                printerController.PrinterController_Linefeed();
            }
            if (addressLine1.trim().length() != 0) {
                centerpoint = getCenterPoint(addressLine1.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + addressLine1));
                printerController.PrinterController_Linefeed();
            }
            if (addressLine2.trim().length() != 0) {
                centerpoint = getCenterPoint(addressLine2.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + addressLine2));
                printerController.PrinterController_Linefeed();
            }
            if (addressLine3.trim().length() != 0) {
                centerpoint = getCenterPoint(addressLine3.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + addressLine3));
                printerController.PrinterController_Linefeed();
            }
            if (addressLine4.trim().length() != 0) {
                centerpoint = getCenterPoint(addressLine4.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + addressLine4));
                printerController.PrinterController_Linefeed();
            }
            if (addressLine5.trim().length() != 0) {
                centerpoint = getCenterPoint(addressLine5.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + addressLine5));
                printerController.PrinterController_Linefeed();
            }
            if (phonenumbers.trim().length() != 0) {
                centerpoint = getCenterPoint(phonenumbers.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + phonenumbers));
                printerController.PrinterController_Linefeed();
            }
            if (GSTNumber.trim().length() != 0) {
                centerpoint = getCenterPoint(GSTNumber.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + GSTNumber));
                printerController.PrinterController_Linefeed();
            }

            printerController.PrinterController_Print(print(getdashline()));
            printerController.PrinterController_Linefeed();
            //Bill Numbers
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm a").format(Calendar.getInstance().getTime());
            printerController.PrinterController_Print(print("Date Time: " + timeStamp));
            printerController.PrinterController_Linefeed();
            printerController.PrinterController_Print(print("Bill No: " + (int) billcount));
            printerController.PrinterController_Linefeed();

            printerController.PrinterController_Print(print(getdashline()));
            printerController.PrinterController_Linefeed();

            String space = "  ";
            printerController.PrinterController_Print(print("Sno " + "Name" + space + "Qty" + space + "Price" + space + "Amount" + space));
            printerController.PrinterController_Linefeed();
            printerController.PrinterController_Print(print(getdashline()));
            printerController.PrinterController_Linefeed();

            double total = 0;
            double totalqty = 0;
            int count = 1;
            DecimalFormat format1 = new DecimalFormat("#.##");
            format1.setMinimumFractionDigits(2);
            for (Map.Entry entry : billproducts.entrySet()) {
                String key = entry.getKey().toString();
                BillProductObject billProduct = (BillProductObject) entry.getValue();
                total += billProduct.getAmount();
                totalqty += billProduct.getQuantity();
                printerController.PrinterController_Print(print(count + "  " + billProduct.getProductName()));
                printerController.PrinterController_Linefeed();

                String space1 = addspace(0, (("Sno Name" + space).length()));
                String space2 = addspace(0, (("Qty" + space).length() - -(format1.format(billProduct.getQuantity())).length()));
                String space3 = addspace(0, (("Price" + space).length() - -(format1.format(billProduct.getPrice())).length()));
                String space4 = addspace(0, (("Amount" + space).length() - -(format1.format(billProduct.getAmount())).length()));


                printerController.PrinterController_Print(print(space1 + format1.format(billProduct.getQuantity()) + " " + format1.format(billProduct.getPrice()) + " " + format1.format(billProduct.getAmount())));
                printerController.PrinterController_Linefeed();
                count += 1;
            }

            double sgst = 2.5;
            double cgst = 2.5;

            double sgstamount = total * (2.5 / 100);
            double cgstamount = total * (2.5 / 100);

            printerController.PrinterController_Print(print(getdashline()));
            printerController.PrinterController_Linefeed();


            printerController.PrinterController_Print(print("CGST(2.5%) " + format1.format(cgstamount)));
            printerController.PrinterController_Linefeed();
            printerController.PrinterController_Print(print("SGST(2.5%) " + format1.format(sgstamount)));
            printerController.PrinterController_Linefeed();

            printerController.PrinterController_Print(print(getdashline()));
            printerController.PrinterController_Linefeed();

            printerController.PrinterController_Print(print("Total Qty " + totalqty));
            printerController.PrinterController_Print(print(addspace(("Total Qty " + totalqty).length(), getRightPoint("Total Amount")) + format1.format(total + sgstamount + cgstamount)));
            printerController.PrinterController_Linefeed();
            printerController.PrinterController_Print(print(getdashline()));
            if (companyName.trim().length() != 0) {
                centerpoint = getCenterPoint(thankyou.trim());
                printerController.PrinterController_Print(print(addspace(0, centerpoint) + thankyou));
                printerController.PrinterController_Linefeed();
            }
            printerController.PrinterController_Linefeed();


            billcount += 1;

            results.clear();
            billproducts.clear();

            //Printer cloase
            printerController.PrinterController_Close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Print problem" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public int getCenterPoint(String line) {
        double maxline = 32;
        int center = (int) (maxline / 2);
        int centerlength = (int) (line.length() / 2);
        int x = center - centerlength;
        return x;
    }

    public byte[] print(String line) {
        return new StringBuffer(line).reverse().toString().getBytes();
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
