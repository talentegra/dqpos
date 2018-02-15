package com.dqserv.dqpos;

import android.content.Context;
import android.content.Intent;
import android.database.AbstractCursor;
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
import android.view.Menu;
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
import com.dqserv.widget.CustomItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    static List<ProductObject.Products> resultProducts;
    static List<OrderObject.Orders> resultOrders;
    static List<ProductObject.Products> resultProductsFromCategory;
    static List<CategoryObject.Categories> resultCategories;
    RelativeLayout rlOrders;
    FrameLayout rlPagerProducts;
    static LinearLayout llOrderHeader, llOrderFooter;
    static RecyclerView rvOrders;
    static OrderAdapter orderAdapter = null;
    static TextView tvTotal;
    static Button btnOrderComplete, btnOrderCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mContext = Orders.this;
        resultProducts = new ArrayList<>();
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

        llOrderHeader = (LinearLayout) findViewById(R.id.orders_recycler_view_header);
        llOrderFooter = (LinearLayout) findViewById(R.id.orders_recycler_view_footer);
        rvOrders = (RecyclerView) findViewById(R.id.orders_recycler_view);
        rlOrders = (RelativeLayout) findViewById(R.id.orders_rl_listing);
        tvTotal = (TextView) findViewById(R.id.orders_tv_total);
        btnOrderComplete = (Button) findViewById(R.id.orders_btn_order_complete);
        btnOrderCancel = (Button) findViewById(R.id.orders_btn_order_cancel);

        AppBarLayout.LayoutParams rlOrdersParams = (AppBarLayout.LayoutParams)
                rlOrders.getLayoutParams();
        rlOrdersParams.width = displayMetrics.widthPixels;
        rlOrdersParams.height = ((displayMetrics.heightPixels - (tabLayoutHeight * 2)) * 40) / 100;

        rlPagerProducts = (FrameLayout) findViewById(R.id.orders_rl_pager_products);
        CoordinatorLayout.LayoutParams rlPagerProductsParams = (CoordinatorLayout.LayoutParams)
                rlPagerProducts.getLayoutParams();
        rlPagerProductsParams.width = displayMetrics.widthPixels;
        rlPagerProductsParams.height = ((displayMetrics.heightPixels - (tabLayoutHeight * 2)) * 50) / 100;

        FrameLayout.LayoutParams orderCompleteParams = (FrameLayout.LayoutParams)
                btnOrderComplete.getLayoutParams();
        orderCompleteParams.width = displayMetrics.widthPixels / 2;

        FrameLayout.LayoutParams orderCancelParams = (FrameLayout.LayoutParams)
                btnOrderCancel.getLayoutParams();
        orderCancelParams.width = displayMetrics.widthPixels / 2;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(sTableName);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        if (!sTableId.equalsIgnoreCase("")) {
            getOrders(sTableId);
        } else {
            toolbar.setTitle("No Table Selected");
        }
        loadCategories(ConnectivityReceiver.isConnected());
        loadProducts(ConnectivityReceiver.isConnected());

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        btnOrderComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.layout_popup,
                            (ViewGroup) findViewById(R.id.popup_cancel_order));
                    final PopupWindow pw = new PopupWindow(layout, (int) mContext.getResources().getDimension(R.dimen._200sdp),
                            (int) mContext.getResources().getDimension(R.dimen._180sdp), true);
                    pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    pw.setFocusable(false);
                    pw.setOutsideTouchable(false);
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
                            deleteAllOrders();
                            pw.dismiss();
                            getOrders(sTableId);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        RecyclerView rv;
        ProductByCategoryAdapter productByCategoryAdapter = null;

        public PlaceholderFragment() {
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                if (rv != null && (getArguments().getInt(ARG_SECTION_NUMBER) != 0
                        || getArguments().getInt(ARG_SECTION_NUMBER) != tabLayout.getTabCount() - 1)) {
                    resultProductsFromCategory.clear();
                    getProductsFromLocalByCategoryId(resultCategories.get(getArguments().getInt(ARG_SECTION_NUMBER)).getCategoryId());
                    if (resultProductsFromCategory.size() > 0) {
                        productByCategoryAdapter = new ProductByCategoryAdapter(resultProductsFromCategory,
                                new CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        if (!sTableId.equalsIgnoreCase("")) {
                                            saveOrderTable(v.getTag().toString());
                                            getOrders(sTableId);
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
                if (resultProductsFromCategory.size() > 0) {
                    productByCategoryAdapter = new ProductByCategoryAdapter(resultProductsFromCategory,
                            new CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    if (!sTableId.equalsIgnoreCase("")) {
                                        saveOrderTable(v.getTag().toString());
                                        getOrders(sTableId);
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
        saveTables(categoryObject.getCategories());
    }

    private void saveTables(List<CategoryObject.Categories> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int tableIndex = 0; tableIndex < items.size(); tableIndex++) {
            try {
                resultCategories.add(items.get(tableIndex));
                String insertSQL = "INSERT OR REPLACE INTO categories \n" +
                        "(category_id, category_name)\n" +
                        "VALUES \n" +
                        "('" + items.get(tableIndex).getCategoryId() + "', " +
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


    private void loadProducts(boolean isConnected) {
        if (isConnected) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ProductObject> call = apiService.getProducts
                    (Constants.AUTH_TOKEN);
            call.enqueue(new Callback<ProductObject>() {
                @Override
                public void onResponse(Call<ProductObject> call, Response<ProductObject> response) {
                    if (response.body().getProducts().size() > 0) {
                        resultProducts.clear();
                        fetchProductResults(response);
                    }
                }

                @Override
                public void onFailure(Call<ProductObject> call, Throwable t) {
                    resultProducts.clear();
                    getProductsFromLocal();
                }
            });
        } else {
            resultProducts.clear();
            getProductsFromLocal();
        }
    }

    //get Tables
    private void fetchProductResults(Response<ProductObject> response) {
        ProductObject productObject = response.body();
        saveProductTables(productObject.getProducts());
    }

    private void saveProductTables(List<ProductObject.Products> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int tableIndex = 0; tableIndex < items.size(); tableIndex++) {
            try {
                resultProducts.add(items.get(tableIndex));
                String insertSQL = "INSERT OR REPLACE INTO products \n" +
                        "(product_id, product_code, product_name, sale_price, category_id)\n" +
                        "VALUES \n" +
                        "('" + items.get(tableIndex).getProductId() + "', " +
                        "'" + items.get(tableIndex).getProductCode() + "', " +
                        "'" + items.get(tableIndex).getProductName() + "', " +
                        "'" + items.get(tableIndex).getProductCost() + "', " +
                        "'" + items.get(tableIndex).getCategoryId() + "');";

                myDataBase.execSQL(insertSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();

        Log.e("Success", "Tables Successfully Added.");
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
                    newProduct.setProductCost(cursor.getString(cursor.getColumnIndex("sale_price")));
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


    private static void getOrders(String idTable) {
        resultOrders.clear();
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM orders " +
                "INNER JOIN order_items ON orders.order_id = order_items.order_id " +
                "WHERE orders.table_id=" + idTable + "");

        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    OrderObject.Orders newOrder = new OrderObject.Orders();
                    newOrder.setOrderId(cursor.getString(cursor.getColumnIndex("order_id")));
                    newOrder.setProductId(cursor.getString(cursor.getColumnIndex("product_id")));
                    newOrder.setProductName(cursor.getString(cursor.getColumnIndex("product_name")));
                    newOrder.setSalePrice(cursor.getString(cursor.getColumnIndex("sale_price")));
                    newOrder.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                    newOrder.setSubTotal(cursor.getString(cursor.getColumnIndex("subtotal")));

                    resultOrders.add(newOrder);
                } while (cursor.moveToNext());
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
                    deleteOrder(v.getTag().toString());
                    getOrders(sTableId);
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                    LinearLayoutManager.VERTICAL, false);
            rvOrders.setLayoutManager(linearLayoutManager);
            rvOrders.setItemAnimator(new DefaultItemAnimator());
            rvOrders.setAdapter(orderAdapter);
            int total = 0;
            for (int priceIndex = 0; priceIndex < resultOrders.size(); priceIndex++) {
                total += Integer.parseInt(resultOrders.get(priceIndex).getSalePrice());
                tvTotal.setText(total + "");
            }
        } else {
            llOrderHeader.setVisibility(View.GONE);
            llOrderFooter.setVisibility(View.GONE);
        }
    }

    private static void saveOrderTable(String sProductId) {
        long lastInsertedOrderId = 0;
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        try {
            if (resultOrders.size() > 0) {
                for (int productIndex = 0; productIndex < resultOrders.size(); productIndex++) {
                    if (!resultOrders.get(productIndex).getProductId().equalsIgnoreCase(sProductId)) {
                        String insertSQL = "INSERT OR REPLACE INTO orders \n" +
                                "(table_id)\n" +
                                "VALUES \n" +
                                "(" + sTableId + ");";
                        myDataBase.execSQL(insertSQL);

                        String query = "SELECT order_id from orders " +
                                "order by order_id DESC limit 1";
                        Cursor cursor = myDataBase.rawQuery(query, null);
                        try {
                            if (cursor != null && cursor.moveToFirst()) {
                                lastInsertedOrderId = cursor.getLong(0);
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
                                            "(order_id, product_id, product_code, product_name, quantity, sale_price, subtotal)\n" +
                                            "VALUES \n" +
                                            "(" + lastInsertedOrderId + ", " +
                                            "'" + pCursor.getString(pCursor.getColumnIndex("product_id")) + "', " +
                                            "'" + pCursor.getString(pCursor.getColumnIndex("product_code")) + "', " +
                                            "'" + pCursor.getString(pCursor.getColumnIndex("product_name")) + "', " +
                                            "" + 1 + ", " +
                                            "'" + pCursor.getString(pCursor.getColumnIndex("sale_price")) + "', " +
                                            "" + (Integer.parseInt(pCursor.getString(pCursor.getColumnIndex("sale_price"))) * 1) + ");";
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
                        int old_quantity = 0;
                        String query = "SELECT order_items.order_id, order_items.quantity FROM orders " +
                                "INNER JOIN order_items ON orders.order_id = order_items.order_id " +
                                "WHERE orders.table_id=" + sTableId + " AND order_items.product_id=" + sProductId + "";
                        Cursor cursor = myDataBase.rawQuery(query, null);
                        try {
                            if (cursor != null && cursor.moveToFirst()) {
                                lastInsertedOrderId = cursor.getLong(0);
                                old_quantity = cursor.getInt(1) + 1;
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
                                            "(order_id, product_id, product_code, product_name, quantity, sale_price, subtotal)\n" +
                                            "VALUES \n" +
                                            "(" + lastInsertedOrderId + ", " +
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
                    }
                }
            } else {
                String insertSQL = "INSERT OR REPLACE INTO orders \n" +
                        "(table_id)\n" +
                        "VALUES \n" +
                        "(" + sTableId + ");";
                myDataBase.execSQL(insertSQL);

                String query = "SELECT order_id from orders " +
                        "order by order_id DESC limit 1";
                Cursor cursor = myDataBase.rawQuery(query, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        lastInsertedOrderId = cursor.getLong(0);
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
                                    "(order_id, product_id, product_code, product_name, quantity, sale_price, subtotal)\n" +
                                    "VALUES \n" +
                                    "(" + lastInsertedOrderId + ", " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_id")) + "', " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_code")) + "', " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("product_name")) + "', " +
                                    "" + 1 + ", " +
                                    "'" + pCursor.getString(pCursor.getColumnIndex("sale_price")) + "', " +
                                    "" + (Integer.parseInt(pCursor.getString(pCursor.getColumnIndex("sale_price"))) * 1) + ");";
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


    private static void deleteOrder(String sOrderId) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        try {
            String deleteOrderSql = "DELETE FROM order_items WHERE order_id=" + sOrderId + "";
            myDataBase.execSQL(deleteOrderSql);
            String deleteOrderItemsSql = "DELETE FROM order WHERE order_id=" + sOrderId + "";
            myDataBase.execSQL(deleteOrderItemsSql);
        } catch (Exception ex) {
            Log.e("Error", "Problem in Adding Product." + ex.getMessage());
            ex.printStackTrace();
        }
        myDataBase.close();
        Log.e("Success", "Tables Successfully Added.");
    }

    private static void deleteAllOrders() {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        try {
            if (resultOrders.size() > 0) {
                for (int orderIndex = 0; orderIndex < resultOrders.size(); orderIndex++) {
                    String deleteOrderSql = "DELETE FROM order_items WHERE order_id=" + resultOrders.get(orderIndex).getOrderId() + "";
                    myDataBase.execSQL(deleteOrderSql);
                    String deleteOrderItemsSql = "DELETE FROM order WHERE order_id=" + resultOrders.get(orderIndex).getOrderId() + "";
                    myDataBase.execSQL(deleteOrderItemsSql);
                }
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

}