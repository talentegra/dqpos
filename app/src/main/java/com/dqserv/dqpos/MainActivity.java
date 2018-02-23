package com.dqserv.dqpos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dqserv.connection.DBConstants;
import com.dqserv.connection.DataBaseHelper;
import com.dqserv.rest.ProductObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<ProductObject.Products> resultProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultProducts = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getProductsFromLocal();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_pos) {
            getProductsFromLocal();
            if (resultProducts.size() > 0) {
                if (!MainActivity.class.getSimpleName().equalsIgnoreCase("POS")) {
                    startActivity(new Intent(this, POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            } else {
                Toast.makeText(getApplicationContext(), "Sync Products", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_gallery) {
            getProductsFromLocal();
            if (resultProducts.size() > 0) {
                if (!MainActivity.class.getSimpleName().equalsIgnoreCase("Orders")) {
                    startActivity(new Intent(this, Orders.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            } else {
                Toast.makeText(getApplicationContext(), "Sync Products", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_slideshow) {
            if (!MainActivity.class.getSimpleName().equalsIgnoreCase("BillActivity")) {
                startActivity(new Intent(this, BillActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_manage) {
            if (!MainActivity.class.getSimpleName().equalsIgnoreCase("Categories")) {
                startActivity(new Intent(this, Categories.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_products) {
            if (!MainActivity.class.getSimpleName().equalsIgnoreCase("Products")) {
                startActivity(new Intent(this, Products.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_categories) {
            if (!MainActivity.class.getSimpleName().equalsIgnoreCase("Categories")) {
                startActivity(new Intent(this, Categories.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_tables) {
            if (!MainActivity.class.getSimpleName().equalsIgnoreCase("Tables")) {
                startActivity(new Intent(this, Tables.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getProductsFromLocal() {
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM products");

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
                    newProduct.setProductCost(cursor.getString(cursor.getColumnIndex("sale_price")));
                    newProduct.setCategoryId(cursor.getString(cursor.getColumnIndex("category_id")));

                    resultProducts.add(newProduct);
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
}