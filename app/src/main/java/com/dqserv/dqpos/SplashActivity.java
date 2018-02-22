package com.dqserv.dqpos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.config.Constants;
import com.dqserv.connection.DBConstants;
import com.dqserv.connection.DataBaseHelper;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.CategoryObject;
import com.dqserv.rest.ProductObject;
import com.dqserv.rest.TableObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    LinearLayout llOnline, llOffline;
    TextView tvMessage;
    ApiInterface apiService;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            DataBaseHelper database = new DataBaseHelper(SplashActivity.this);
            database.createDataBase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        llOnline = (LinearLayout) findViewById(R.id.splash_online_view);
        llOffline = (LinearLayout) findViewById(R.id.splash_offline_view);
        tvMessage = (TextView) findViewById(R.id.splash_tv_message);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        if (!hasPermissions(this, PERMISSIONS)) {
            tvMessage.setTag("error");
            ActivityCompat.requestPermissions(this, PERMISSIONS, 100);
        } else {
            try {
                DataBaseHelper database = new DataBaseHelper(SplashActivity.this);
                database.createDataBase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            loadData();
        }

        llOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvMessage.getTag().toString().equalsIgnoreCase("on")) {
                    loadData();
                } else {
                    if (!hasPermissions(SplashActivity.this, PERMISSIONS)) {
                        tvMessage.setTag("error");
                        ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, 100);
                    } else {
                        try {
                            DataBaseHelper database = new DataBaseHelper(SplashActivity.this);
                            database.createDataBase();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        loadData();
                    }
                }
            }
        });
    }

    private void loadData() {
        if (ConnectivityReceiver.isConnected()) {
            if (getProductsFromLocal()) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
            } else {
                llOnline.setVisibility(View.VISIBLE);
                llOffline.setVisibility(View.GONE);
                loadProducts();
            }
        } else {
            if (getProductsFromLocal()) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
            } else {
                llOnline.setVisibility(View.GONE);
                llOffline.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean getProductsFromLocal() {
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM products");

        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
        if (cursor.getCount() > 0) {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return true;
        } else {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return false;
        }
    }

    private void loadProducts() {
        llOnline.setVisibility(View.VISIBLE);
        Call<ProductObject> call = apiService.getProducts
                (Constants.AUTH_TOKEN);
        call.enqueue(new Callback<ProductObject>() {
            @Override
            public void onResponse(Call<ProductObject> call, Response<ProductObject> response) {
                ProductObject productObj = response.body();
                saveProducts(productObj.getProducts());
                llOnline.setVisibility(View.GONE);
                loadTables();
            }

            @Override
            public void onFailure(Call<ProductObject> call, Throwable t) {
                llOnline.setVisibility(View.GONE);
            }
        });
    }

    private void saveProducts(List<ProductObject.Products> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int productIndex = 0; productIndex < items.size(); productIndex++) {
            try {
                String insertSQL = "INSERT OR REPLACE INTO products \n" +
                        "(product_id, product_code, product_name, sale_price, category_id, active)\n" +
                        "VALUES \n" +
                        "(" + items.get(productIndex).getProductId() + ", " +
                        "'" + items.get(productIndex).getProductCode() + "', " +
                        "'" + items.get(productIndex).getProductName() + "', " +
                        "'" + items.get(productIndex).getProductCost() + "', " +
                        "" + items.get(productIndex).getCategoryId() + "," +
                        "" + 1 + ");";

                myDataBase.execSQL(insertSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();

        Log.e("Success", "Products Successfully Added.");
    }

    private void loadTables() {
        llOnline.setVisibility(View.VISIBLE);
        Call<TableObject> call = apiService.getTables
                (Constants.AUTH_TOKEN);
        call.enqueue(new Callback<TableObject>() {
            @Override
            public void onResponse(Call<TableObject> call, Response<TableObject> response) {
                TableObject tableObj = response.body();
                saveTables(tableObj.getTables());
                llOnline.setVisibility(View.GONE);
                loadCategories();
            }

            @Override
            public void onFailure(Call<TableObject> call, Throwable t) {
                llOnline.setVisibility(View.GONE);
            }
        });
    }

    private void saveTables(List<TableObject.Tables> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int tableIndex = 0; tableIndex < items.size(); tableIndex++) {
            try {
                String insertSQL = "INSERT OR REPLACE INTO tables \n" +
                        "(table_id, table_name)\n" +
                        "VALUES \n" +
                        "(" + items.get(tableIndex).getTableId() + ", " +
                        "'" + items.get(tableIndex).getTableName() + "');";
                myDataBase.execSQL(insertSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }

    private void loadCategories() {
        llOnline.setVisibility(View.VISIBLE);
        Call<CategoryObject> call = apiService.getCategories
                (Constants.AUTH_TOKEN);
        call.enqueue(new Callback<CategoryObject>() {
            @Override
            public void onResponse(Call<CategoryObject> call, Response<CategoryObject> response) {
                CategoryObject categoryObject = response.body();
                saveCategories(categoryObject.getCategories());
                llOnline.setVisibility(View.GONE);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
            }

            @Override
            public void onFailure(Call<CategoryObject> call, Throwable t) {
                llOnline.setVisibility(View.GONE);
            }
        });
    }

    private void saveCategories(List<CategoryObject.Categories> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int tableIndex = 0; tableIndex < items.size(); tableIndex++) {
            try {
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


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length <= 0) {
                llOnline.setVisibility(View.GONE);
                llOffline.setVisibility(View.VISIBLE);
                tvMessage.setTag("error");
                tvMessage.setText("User interaction was cancelled.");
            } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                llOnline.setVisibility(View.GONE);
                llOffline.setVisibility(View.VISIBLE);
                tvMessage.setTag("error");
                tvMessage.setText("Permission was denied, but is needed for core functionality.");
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tvMessage.setTag("on");
                try {
                    DataBaseHelper database = new DataBaseHelper(SplashActivity.this);
                    database.createDataBase();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                loadData();
            }
        }
    }
}
