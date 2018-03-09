package com.dqserv.dqpos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.TableAdapter;
import com.dqserv.config.Constants;
import com.dqserv.connection.DBConstants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.BillObject;
import com.dqserv.rest.TableObject;
import com.dqserv.widget.CustomItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POS extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<TableObject.Tables> results;
    TableAdapter tableAdapter;
    RelativeLayout mProgressBar;
    RecyclerView rv;
    List<Integer> onlineTableIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (RelativeLayout) findViewById(R.id.tables_rl_progress);
        rv = (RecyclerView) findViewById(R.id.pos_recycler_view);

        results = new ArrayList<>();
        onlineTableIds = new ArrayList<>();
        getOnlineTablesFromLocal();

        if (ConnectivityReceiver.isConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<TableObject> call = apiService.getTables
                    (Constants.AUTH_TOKEN);
            call.enqueue(new Callback<TableObject>() {
                @Override
                public void onResponse(Call<TableObject> call, Response<TableObject> response) {
                    results.clear();
                    fetchResults(response);
                    if (results.size() > 0) {
                        tableAdapter = new TableAdapter(results, onlineTableIds, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                String[] aTableValues = v.getTag().toString().split("\\|");
                                startActivity(new Intent(getApplicationContext(), Orders.class)
                                        .putExtra("param_table_id", aTableValues[0])
                                        .putExtra("param_table_name", aTableValues[1])
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }

                            @Override
                            public void deleteViewOnClick(View v, int position) {

                            }
                        });
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                        rv.setLayoutManager(gridLayoutManager);
                        rv.setItemAnimator(new DefaultItemAnimator());
                        rv.setAdapter(tableAdapter);
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<TableObject> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            results.clear();
            getTablesFromLocal();
            if (results.size() > 0) {
                tableAdapter = new TableAdapter(results, onlineTableIds, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        String[] aTableValues = v.getTag().toString().split("\\|");
                        startActivity(new Intent(getApplicationContext(), Orders.class)
                                .putExtra("param_table_id", aTableValues[0])
                                .putExtra("param_table_name", aTableValues[1])
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }

                    @Override
                    public void deleteViewOnClick(View v, int position) {

                    }
                });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                rv.setLayoutManager(gridLayoutManager);
                rv.setItemAnimator(new DefaultItemAnimator());
                rv.setAdapter(tableAdapter);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            if (!POS.class.getSimpleName().equalsIgnoreCase("POS")) {
                startActivity(new Intent(this, POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_gallery) {
            if (!POS.class.getSimpleName().equalsIgnoreCase("Orders")) {
                startActivity(new Intent(this, Orders.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_slideshow) {
            if (!POS.class.getSimpleName().equalsIgnoreCase("BillActivity")) {
                startActivity(new Intent(this, BillActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_products) {
            if (!POS.class.getSimpleName().equalsIgnoreCase("Products")) {
                startActivity(new Intent(this, Products.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_categories) {
            if (!POS.class.getSimpleName().equalsIgnoreCase("Categories")) {
                startActivity(new Intent(this, Categories.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_tables) {
            if (!POS.class.getSimpleName().equalsIgnoreCase("Tables")) {
                startActivity(new Intent(this, Tables.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_orders_list) {
            if (!POS.class.getSimpleName().equalsIgnoreCase("OrdersList")) {
                startActivity(new Intent(this, OrdersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //get Tables
    private void fetchResults(Response<TableObject> response) {
        TableObject tableObj = response.body();
        saveTables(tableObj.getTables());
    }

    private void saveTables(List<TableObject.Tables> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int tableIndex = 0; tableIndex < items.size(); tableIndex++) {
            try {
                results.add(items.get(tableIndex));
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

    public void getTablesFromLocal() {
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM tables");

        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    TableObject.Tables newTable = new TableObject.Tables();
                    newTable.setTableId(cursor.getString(cursor.getColumnIndex("table_id")));
                    newTable.setTableName(cursor.getString(cursor.getColumnIndex("table_name")));

                    results.add(newTable);
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

    public void getOnlineTablesFromLocal() {
        onlineTableIds.clear();
        if (ConnectivityReceiver.isConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<BillObject> call = apiService.getOrders(Constants.AUTH_TOKEN);
            call.enqueue(new Callback<BillObject>() {
                @Override
                public void onResponse(Call<BillObject> call, Response<BillObject> response) {
                    mProgressBar.setVisibility(View.GONE);
                    BillObject billObj = response.body();
                    for (int tIndex = 0; tIndex < billObj.getOrders().size(); tIndex++) {
                        onlineTableIds.add(Integer.parseInt(billObj.getOrders().get(tIndex).getTableId()));
                    }
                    String ORDERS_SELECT_QUERY = String.format("SELECT table_id FROM orders");

                    //Open the database
                    String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
                    SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                            SQLiteDatabase.OPEN_READWRITE);
                    Cursor cursor = myDataBase.rawQuery(ORDERS_SELECT_QUERY, null);
                    try {
                        if (cursor.moveToFirst()) {
                            do {
                                onlineTableIds.add(cursor.getInt(0));
                            } while (cursor.moveToNext());
                        }
                    } catch (Exception e) {
                        Log.d("LocalResponse", "Error while trying to get posts from database");
                    } finally {
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                        String ORDERS_PRINT_SELECT_QUERY = String.format("SELECT table_id FROM order_print");

                        //Open the database
                        String myPathSecond = DBConstants.DB_PATH + DBConstants.DB_NAME;
                        SQLiteDatabase myDataBaseSecond = SQLiteDatabase.openDatabase(myPathSecond, null,
                                SQLiteDatabase.OPEN_READWRITE);
                        Cursor cursorSecond = myDataBaseSecond.rawQuery(ORDERS_PRINT_SELECT_QUERY, null);
                        try {
                            if (cursorSecond.moveToFirst()) {
                                do {
                                    onlineTableIds.add(cursorSecond.getInt(0));
                                } while (cursorSecond.moveToNext());
                            }
                        } catch (Exception e) {
                            Log.d("LocalResponse", "Error while trying to get posts from database");
                        } finally {
                            if (cursorSecond != null && !cursorSecond.isClosed()) {
                                cursorSecond.close();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<BillObject> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            String ORDERS_SELECT_QUERY = String.format("SELECT table_id FROM orders");

            //Open the database
            String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
            SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = myDataBase.rawQuery(ORDERS_SELECT_QUERY, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        onlineTableIds.add(cursor.getInt(0));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d("LocalResponse", "Error while trying to get posts from database");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                String ORDERS_PRINT_SELECT_QUERY = String.format("SELECT table_id FROM order_print");

                //Open the database
                String myPathSecond = DBConstants.DB_PATH + DBConstants.DB_NAME;
                SQLiteDatabase myDataBaseSecond = SQLiteDatabase.openDatabase(myPathSecond, null,
                        SQLiteDatabase.OPEN_READWRITE);
                Cursor cursorSecond = myDataBaseSecond.rawQuery(ORDERS_PRINT_SELECT_QUERY, null);
                try {
                    if (cursorSecond.moveToFirst()) {
                        do {
                            onlineTableIds.add(cursorSecond.getInt(0));
                        } while (cursorSecond.moveToNext());
                    }
                } catch (Exception e) {
                    Log.d("LocalResponse", "Error while trying to get posts from database");
                } finally {
                    if (cursorSecond != null && !cursorSecond.isClosed()) {
                        cursorSecond.close();
                    }
                }
            }
        }
    }
}