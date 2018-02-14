package com.dqserv.dqpos;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.ProductAdapter;
import com.dqserv.adapter.ProductByCategoryAdapter;
import com.dqserv.config.Constants;
import com.dqserv.connection.DBConstants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.CategoryObject;
import com.dqserv.rest.ProductObject;

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
    static TabLayout tabLayout;
    static String sTableId = "", sTableName = "";
    static List<ProductObject.Products> resultProducts;
    static List<ProductObject.Products> resultProductsFromCategory;
    static List<CategoryObject.Categories> resultCategories;
    RelativeLayout rlOrders;
    FrameLayout rlPagerProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        resultProducts = new ArrayList<>();
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

        rlOrders = (RelativeLayout) findViewById(R.id.orders_rl_listing);
        AppBarLayout.LayoutParams rlOrdersParams = (AppBarLayout.LayoutParams)
                rlOrders.getLayoutParams();
        rlOrdersParams.width = displayMetrics.widthPixels;
        rlOrdersParams.height = ((displayMetrics.heightPixels - (tabLayoutHeight * 2)) * 40) / 100;

        rlPagerProducts = (FrameLayout) findViewById(R.id.orders_rl_pager_products);
        CoordinatorLayout.LayoutParams rlPagerProductsParams = (CoordinatorLayout.LayoutParams)
                rlPagerProducts.getLayoutParams();
        rlPagerProductsParams.width = displayMetrics.widthPixels;
        rlPagerProductsParams.height = ((displayMetrics.heightPixels - (tabLayoutHeight * 2)) * 50) / 100;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(sTableName);
        setSupportActionBar(toolbar);

        loadCategories(ConnectivityReceiver.isConnected());
        loadProducts(ConnectivityReceiver.isConnected());

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
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

        public PlaceholderFragment() {
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
            ProductByCategoryAdapter productByCategoryAdapter = null;
            RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.orders_categories_recycler_view_products);
            resultProductsFromCategory.clear();
            getProductsFromLocalByCategoryId(resultCategories.get(getArguments().getInt(ARG_SECTION_NUMBER)).getCategoryId());
            if (resultProductsFromCategory.size() > 0) {
                productByCategoryAdapter = new ProductByCategoryAdapter(resultProductsFromCategory);
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
