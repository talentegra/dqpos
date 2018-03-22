package com.dqserv.dqpos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dqserv.ConnectivityReceiver;
import com.dqserv.adapter.PrinterAdapter;
import com.dqserv.config.Constants;
import com.dqserv.connection.DBConstants;
import com.dqserv.rest.ApiClient;
import com.dqserv.rest.ApiInterface;
import com.dqserv.rest.PrinterObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrintersActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    static Context mContext;
    static List<PrinterObject.Printers> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printers);

        mContext = PrintersActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_printers, menu);
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
        if (id == android.R.id.home) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
            final EditText editTextProdName, editTextProdCode, editTextProdPrice;

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                final PrinterAdapter[] printerAdapter = new PrinterAdapter[1];
                results = new ArrayList<>();

                rootView = inflater.inflate(R.layout.fragment_printers, container, false);

                final RelativeLayout mProgressBar = (RelativeLayout) rootView.findViewById(R.id.printers_rl_progress);
                final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.printers_recycler_view);

                if (ConnectivityReceiver.isConnected()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<PrinterObject> call = apiService.getPrinters
                            (Constants.AUTH_TOKEN);
                    call.enqueue(new Callback<PrinterObject>() {
                        @Override
                        public void onResponse(Call<PrinterObject> call, Response<PrinterObject> response) {
                            results.clear();
                            fetchResults(response);
                            if (results.size() > 0) {
                                printerAdapter[0] = new PrinterAdapter(mContext, results);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                                        LinearLayoutManager.VERTICAL, false);
                                rv.setLayoutManager(linearLayoutManager);
                                rv.setItemAnimator(new DefaultItemAnimator());
                                rv.setAdapter(printerAdapter[0]);
                            }
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<PrinterObject> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    results.clear();
                    getPrintersFromLocal();
                    if (results.size() > 0) {
                        printerAdapter[0] = new PrinterAdapter(mContext, results);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.VERTICAL, false);
                        rv.setLayoutManager(linearLayoutManager);
                        rv.setItemAnimator(new DefaultItemAnimator());
                        rv.setAdapter(printerAdapter[0]);
                    }
                }
                return rootView;
            }

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.fragment_add_printer, container, false);
                return rootView;
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(R.layout.fragment_update_printer, container, false);
                return rootView;
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


    //get Printers
    private static void fetchResults(Response<PrinterObject> response) {
        PrinterObject printerObject = response.body();
        savePrinters(printerObject.getPrinters());
    }

    private static void savePrinters(List<PrinterObject.Printers> items) {
        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        for (int printerIndex = 0; printerIndex < items.size(); printerIndex++) {
            try {
                results.add(items.get(printerIndex));
                String insertSQL = "INSERT OR REPLACE INTO printers \n" +
                        "(printer_id, title, printer_type, profile, char_per_line, path, ip_address, port)\n" +
                        "VALUES \n" +
                        "(" + items.get(printerIndex).getPrinterId() + ", " +
                        "'" + items.get(printerIndex).getTitle() + "', " +
                        "'" + items.get(printerIndex).getPrinterType() + "', " +
                        "'" + items.get(printerIndex).getProfile() + "', " +
                        "'" + items.get(printerIndex).getCharPerLine() + "'," +
                        "'" + items.get(printerIndex).getPath() + "'," +
                        "'" + items.get(printerIndex).getIpAddress() + "'," +
                        "" + items.get(printerIndex).getPort() + ");";

                myDataBase.execSQL(insertSQL);
            } catch (Exception ex) {
                Log.e("Error", "Problem in Adding Product." + ex.getMessage());
                ex.printStackTrace();
            }
        }
        myDataBase.close();

        Log.e("Success", "Printers Successfully Added.");
    }

    public static void getPrintersFromLocal() {
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM printers");

        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = myDataBase.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    PrinterObject.Printers newPrinter = new PrinterObject.Printers();
                    newPrinter.setPrinterId(String.valueOf(cursor.getInt(cursor.getColumnIndex("printer_id"))));
                    newPrinter.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    newPrinter.setPrinterType(cursor.getString(cursor.getColumnIndex("printer_type")));
                    newPrinter.setProfile(cursor.getString(cursor.getColumnIndex("profile")));
                    newPrinter.setCharPerLine(cursor.getString(cursor.getColumnIndex("char_per_line")));
                    newPrinter.setPath(cursor.getString(cursor.getColumnIndex("path")));
                    newPrinter.setIpAddress(cursor.getString(cursor.getColumnIndex("ip_address")));
                    newPrinter.setPort(String.valueOf(cursor.getInt(cursor.getColumnIndex("port"))));

                    results.add(newPrinter);
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
