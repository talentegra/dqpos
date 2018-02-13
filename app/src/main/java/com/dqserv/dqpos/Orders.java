package com.dqserv.dqpos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.ProductAdapter;
import com.dqserv.config.Constants;
import com.dqserv.connection.DBConstants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.CategoryObject;
import com.dqserv.rest.ItemObject;
import com.dqserv.rest.ProductObject;
import com.dqserv.rest.TableObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    static String sTableId = "";
    static List<ProductObject.Products> resultProducts;
    static List<CategoryObject.Categories> resultCategories;
    static List<ItemObject> categoryObjects;
    static List<ItemObject> productObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        resultProducts = new ArrayList<>();
        resultCategories = new ArrayList<>();

        if (getIntent() != null) {
            sTableId = getIntent().hasExtra("param_table_id") ?
                    getIntent().getStringExtra("param_table_id") : "";
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        //getJSON("http://teswaiter.dqserv.com/api/get_products");

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
            View rootView = null;

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_orders, container, false);
                return rootView;
            }

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.fragment_orders_categories, container, false);
                Spinner spinnerCategories = (Spinner) rootView.findViewById(R.id.order_categories_spinner_categories);
                Spinner spinnerProducts = (Spinner) rootView.findViewById(R.id.order_categories_spinner_products);

                loadCategories(getActivity(), ConnectivityReceiver.isConnected(), spinnerCategories);
                loadProducts(getActivity(), ConnectivityReceiver.isConnected(), spinnerProducts);

                return rootView;
            }

            return rootView;
        }
    }

    private static void loadCategories(final Context mContext, boolean isConnected, final Spinner categories) {
        if (isConnected) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<CategoryObject> call = apiService.getCategories
                    (Constants.AUTH_TOKEN);
            call.enqueue(new Callback<CategoryObject>() {
                @Override
                public void onResponse(Call<CategoryObject> call, Response<CategoryObject> response) {
                    categoryObjects = new ArrayList<>();
                    categoryObjects.add(new ItemObject("Select categories from list...", 0));
                    if (response.body().getCategories().size() > 0) {
                        fetchResults(response);
                        for (int catIndex = 0; catIndex < response.body().getCategories().size(); catIndex++) {
                            categoryObjects.add(new ItemObject(response.body().getCategories().get(catIndex).getCategoryName(),
                                    Integer.parseInt(response.body().getCategories().get(catIndex).getCategoryId())));
                        }
                    }
                    ArrayAdapter<ItemObject> myAdapter = new ArrayAdapter<ItemObject>(mContext,
                            R.layout.layout_spinner_item, categoryObjects);
                    categories.setAdapter(myAdapter);
                }

                @Override
                public void onFailure(Call<CategoryObject> call, Throwable t) {
                    categoryObjects = new ArrayList<>();
                    categoryObjects.add(new ItemObject("Select categories from list...", 0));
                    ArrayAdapter<ItemObject> myAdapter = new ArrayAdapter<ItemObject>(mContext,
                            R.layout.layout_spinner_item, categoryObjects);
                    categories.setAdapter(myAdapter);
                }
            });
        } else {
            categoryObjects = new ArrayList<>();
            categoryObjects.add(new ItemObject("Select categories from list...", 0));

            String POSTS_SELECT_QUERY = String.format("SELECT * FROM categories");

            //Open the database
            String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
            SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        categoryObjects.add(new ItemObject(
                                cursor.getString(cursor.getColumnIndex("category_name")),
                                Integer.parseInt(cursor.getString(cursor.getColumnIndex("category_id")))));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d("LocalResponse", "Error while trying to get posts from database");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            ArrayAdapter<ItemObject> myAdapter = new ArrayAdapter<ItemObject>(mContext,
                    R.layout.layout_spinner_item, categoryObjects);
            categories.setAdapter(myAdapter);
        }
    }

    //get Tables
    private static void fetchResults(Response<CategoryObject> response) {
        CategoryObject categoryObject = response.body();
        saveTables(categoryObject.getCategories());
    }

    private static void saveTables(List<CategoryObject.Categories> items) {
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


    private static void loadProducts(final Context mContext, boolean isConnected, final Spinner products) {
        if (isConnected) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ProductObject> call = apiService.getProducts
                    (Constants.AUTH_TOKEN);
            call.enqueue(new Callback<ProductObject>() {
                @Override
                public void onResponse(Call<ProductObject> call, Response<ProductObject> response) {
                    productObjects = new ArrayList<>();
                    productObjects.add(new ItemObject("Select products from list...", 0));
                    if (response.body().getProducts().size() > 0) {
                        fetchProductResults(response);
                        for (int catIndex = 0; catIndex < response.body().getProducts().size(); catIndex++) {
                            productObjects.add(new ItemObject(response.body().getProducts().get(catIndex).getProductName(),
                                    Integer.parseInt(response.body().getProducts().get(catIndex).getProductId())));
                        }
                    }
                    ArrayAdapter<ItemObject> myAdapter = new ArrayAdapter<ItemObject>(mContext,
                            R.layout.layout_spinner_item, productObjects);
                    products.setAdapter(myAdapter);
                }

                @Override
                public void onFailure(Call<ProductObject> call, Throwable t) {
                    productObjects = new ArrayList<>();
                    productObjects.add(new ItemObject("Select products from list...", 0));
                    ArrayAdapter<ItemObject> myAdapter = new ArrayAdapter<ItemObject>(mContext,
                            R.layout.layout_spinner_item, productObjects);
                    products.setAdapter(myAdapter);
                }
            });
        } else {
            productObjects = new ArrayList<>();
            productObjects.add(new ItemObject("Select products from list...", 0));

            String POSTS_SELECT_QUERY = String.format("SELECT * FROM products");

            //Open the database
            String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
            SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        productObjects.add(new ItemObject(
                                cursor.getString(cursor.getColumnIndex("product_name")),
                                Integer.parseInt(cursor.getString(cursor.getColumnIndex("product_id")))));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d("LocalResponse", "Error while trying to get posts from database");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            ArrayAdapter<ItemObject> myAdapter = new ArrayAdapter<ItemObject>(mContext,
                    R.layout.layout_spinner_item, productObjects);
            products.setAdapter(myAdapter);
        }
    }

    //get Tables
    private static void fetchProductResults(Response<ProductObject> response) {
        ProductObject productObject = response.body();
        saveProductTables(productObject.getProducts());
    }

    private static void saveProductTables(List<ProductObject.Products> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int tableIndex = 0; tableIndex < items.size(); tableIndex++) {
            try {
                resultProducts.add(items.get(tableIndex));
                String insertSQL = "INSERT OR REPLACE INTO products \n" +
                        "(product_id, product_name)\n" +
                        "VALUES \n" +
                        "('" + items.get(tableIndex).getProductId() + "', " +
                        "'" + items.get(tableIndex).getProductName() + "');";

                myDataBase.execSQL(insertSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();

        Log.e("Success", "Tables Successfully Added.");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

}
