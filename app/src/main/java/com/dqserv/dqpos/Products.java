package com.dqserv.dqpos;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.ProductAdapter;
import com.dqserv.config.Constants;
import com.dqserv.connection.DBConstants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.ProductObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Products extends AppCompatActivity {

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
    static List<ProductObject.Products> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

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
            //  View rootView = inflater.inflate(R.layout.fragment_products, container, false);
            // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));


            View rootView = null;
            final EditText editTextProdName, editTextProdCode, editTextProdPrice;

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                final ProductAdapter[] productAdapter = new ProductAdapter[1];
                results = new ArrayList<>();

                rootView = inflater.inflate(R.layout.fragment_products, container, false);

                final RelativeLayout mProgressBar = (RelativeLayout) rootView.findViewById(R.id.products_rl_progress);
                final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.products_recycler_view);
                final Button btnSyncProducts = (Button) rootView.findViewById(R.id.products_btn_sync_data);

                if (ConnectivityReceiver.isConnected()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<ProductObject> call = apiService.getProducts
                            (Constants.AUTH_TOKEN);
                    call.enqueue(new Callback<ProductObject>() {
                        @Override
                        public void onResponse(Call<ProductObject> call, Response<ProductObject> response) {
                            results.clear();
                            fetchResults(response);
                            if (results.size() > 0) {
                                productAdapter[0] = new ProductAdapter(results);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                                        LinearLayoutManager.VERTICAL, false);
                                rv.setLayoutManager(linearLayoutManager);
                                rv.setItemAnimator(new DefaultItemAnimator());
                                rv.setAdapter(productAdapter[0]);
                            }
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<ProductObject> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    results.clear();
                    getProductsFromLocal();
                    if (results.size() > 0) {
                        productAdapter[0] = new ProductAdapter(results);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.VERTICAL, false);
                        rv.setLayoutManager(linearLayoutManager);
                        rv.setItemAnimator(new DefaultItemAnimator());
                        rv.setAdapter(productAdapter[0]);
                    }
                }

                btnSyncProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ConnectivityReceiver.isConnected()) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            ApiInterface apiService =
                                    ApiClient.getClient().create(ApiInterface.class);
                            Call<ProductObject> call = apiService.getProducts
                                    (Constants.AUTH_TOKEN);
                            call.enqueue(new Callback<ProductObject>() {
                                @Override
                                public void onResponse(Call<ProductObject> call, Response<ProductObject> response) {
                                    results.clear();
                                    fetchResults(response);
                                    if (results.size() > 0) {
                                        productAdapter[0] = new ProductAdapter(results);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                                                LinearLayoutManager.VERTICAL, false);
                                        rv.setLayoutManager(linearLayoutManager);
                                        rv.setItemAnimator(new DefaultItemAnimator());
                                        rv.setAdapter(productAdapter[0]);
                                    }
                                    mProgressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<ProductObject> call, Throwable t) {
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });

                return rootView;
            }

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {

                rootView = inflater.inflate(R.layout.fragment_add_product, container, false);
                editTextProdCode = (EditText) rootView.findViewById(R.id.editTextProdCode);
                editTextProdName = (EditText) rootView.findViewById(R.id.editTextProdName);
                editTextProdPrice = (EditText) rootView.findViewById(R.id.editTextProdPrice);
                Button addprod_btn = (Button) rootView.findViewById(R.id.prod_add_btn);

                addprod_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String prod_code = editTextProdCode.getText().toString();
                            String prod_name = editTextProdName.getText().toString();
                            String prod_price = editTextProdPrice.getText().toString();

                            //Open the database
                            String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
                            SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

                            String insertSQL = "INSERT INTO products \n" +
                                    "(product_code, product_name)\n" +
                                    "VALUES \n" +
                                    "('" + prod_code + "', '" + prod_name + "', '" + prod_price + "');";

                            Log.i("Add Product SQL ", insertSQL);
                            //using the same method execsql for inserting values
                            //this time it has two parameters
                            //first is the sql string and second is the parameters that is to be binded with the query
                            myDataBase.execSQL(insertSQL);

                            myDataBase.close();
                            Toast.makeText(v.getContext(),
                                    "Product " + prod_name + " Successfully Added ", Toast.LENGTH_SHORT).show();

                        } catch (Exception ex) {
                            Toast.makeText(v.getContext(),
                                    "Problem in Adding Product " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            ex.printStackTrace();
                        }
                    }
                });


                return rootView;


            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {

            }


            return rootView;
        }
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
            // Show 3 total pages.
            return 3;
        }
    }


    //get Products
    private static void fetchResults(Response<ProductObject> response) {
        ProductObject productObj = response.body();
        saveProducts(productObj.getProducts());
    }

    private static void saveProducts(List<ProductObject.Products> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int productIndex = 0; productIndex < items.size(); productIndex++) {
            try {
                results.add(items.get(productIndex));
                String insertSQL = "INSERT OR REPLACE INTO products \n" +
                        "(product_id, product_code, product_name, sale_price)\n" +
                        "VALUES \n" +
                        "('" + items.get(productIndex).getProductId() + "', " +
                        "('" + items.get(productIndex).getProductCode() + "', " +
                        "'" + items.get(productIndex).getProductName() + "', " +
                        "'" + items.get(productIndex).getProductCost() + "');";

                myDataBase.execSQL(insertSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();

        Log.e("Success", "Products Successfully Added.");
    }

    public static void getProductsFromLocal() {
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM products");

        //Open the database
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

                    results.add(newProduct);
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
