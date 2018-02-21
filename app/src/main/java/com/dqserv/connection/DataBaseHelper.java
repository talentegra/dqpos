package com.dqserv.connection;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Vivek Raghunathan on 05-02-2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.

    private SQLiteDatabase myDataBase;
    public static final String PROD_TABLE_NAME = "products";
    public static final String ORD_TABLE_NAME = "orders";
    public static final String ORD_ITEMS_TABLE_NAME = "order_items";
    public static final String ORD_PRINT_TABLE_NAME = "order_print";
    public static final String ORD_PRINT_ITEMS_TABLE_NAME = "order_print_items";
    public static final String SALE_TABLE_NAME = "sales";
    public static final String SALE_ITEMS_TABLE_NAME = "sale_items";
    public static final String CAT_TABLE_NAME = "categories";
    public static final String COLUMN_ID = "category_id";
    public static final String COLUMN_CAT_CODE = "category_code";
    public static final String COLUMN_CAT_NAME = "category_name";
    public static final String COLUMN_CAT_IMG = "category_image";
    public static final String COLUMN_ACTIVE = "active";
    public static final String TAB_TABLE_NAME = "tables";
    public static final String COLUMN_TAB_ID = "table_id";
    public static final String COLUMN_TAB_CODE = "table_code";
    public static final String COLUMN_TAB_NAME = "table_name";
    public static final String COLUMN_TAB_CAPACITY = "capacity";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_PROD_ID = "product_id";
    public static final String COLUMN_PROD_CODE = "product_code";
    public static final String COLUMN_PROD_NAME = "product_name";
    public static final String COLUMN_PROD_IMG = "product_image";
    public static final String COLUMN_PROD_PRICE = "sale_price";
    public static final String COLUMN_ORD_ID = "order_id";
    public static final String COLUMN_ORD_DATE = "order_date";
    public static final String COLUMN_ORD_ITEM_ID = "order_item_id";
    public static final String COLUMN_ORD_TOTAL = "grand_total";
    public static final String COLUMN_ORD_TOT_ITEMS = "total_items";
    public static final String COLUMN_SUB_TOTAL = "subtotal";
    public static final String COLUMN_ITEM_QTY = "quantity";
    public static final String COLUMN_SALE_ID = "sale_id";
    public static final String COLUMN_SALE_DATE = "sale_date";
    public static final String COLUMN_SALE_ITEM_ID = "sale_item_id";




    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DBConstants.DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        Log.i("DB", "Started Copying database");
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DBConstants.DB_NAME);

        // Path to the just created empty db
        String outFileName = DBConstants.DB_PATH + DBConstants.DB_NAME;

        Log.i("DB", "Path " + outFileName);
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.i("DB", "Copied succesfully");
    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DBConstants.DB_PATH + DBConstants.DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FIRST_NAME + " VARCHAR, " + COLUMN_LAST_NAME + " VARCHAR);");
        db.execSQL("create table " + CAT_TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CAT_CODE + " VARCHAR, " + COLUMN_CAT_NAME + " VARCHAR," + COLUMN_CAT_IMG + " VARCHAR, " + COLUMN_ACTIVE + " INTEGER);");
        db.execSQL("create table " + TAB_TABLE_NAME + " ( " + COLUMN_TAB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TAB_CODE + " VARCHAR, " + COLUMN_TAB_NAME + " VARCHAR," + COLUMN_TAB_CAPACITY + " VARCHAR, " + COLUMN_STATUS + " INTEGER);");
        db.execSQL("create table " + PROD_TABLE_NAME + " ( " + COLUMN_PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PROD_CODE + " VARCHAR, " + COLUMN_PROD_NAME + " VARCHAR," + COLUMN_PROD_IMG + " VARCHAR, " + COLUMN_PROD_PRICE + " REAL," + COLUMN_ID + " INTEGER," + COLUMN_ACTIVE + " INTEGER);");
        db.execSQL("create table " + ORD_TABLE_NAME + " ( " + COLUMN_ORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ORD_DATE + " TEXT, " + COLUMN_TAB_ID + " INTEGER UNIQUE," + COLUMN_ORD_TOTAL + " REAL, " + COLUMN_ORD_TOT_ITEMS + " INTEGER," + COLUMN_STATUS + " INTEGER);");
        db.execSQL("create table " + ORD_ITEMS_TABLE_NAME + " ( " + COLUMN_ORD_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ORD_ID + " INTEGER UNIQUE," + COLUMN_PROD_ID + " INTEGER," + COLUMN_PROD_CODE + " VARCHAR, " + COLUMN_PROD_NAME + " VARCHAR," + COLUMN_ITEM_QTY + " INTEGER, " + COLUMN_PROD_PRICE + " REAL," + COLUMN_SUB_TOTAL + " REAL);");
        db.execSQL("create table " + ORD_PRINT_TABLE_NAME + " ( " + COLUMN_ORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ORD_DATE + " TEXT, " + COLUMN_TAB_ID + " INTEGER UNIQUE," + COLUMN_ORD_TOTAL + " REAL, " + COLUMN_ORD_TOT_ITEMS + " INTEGER," + COLUMN_STATUS + " INTEGER);");
        db.execSQL("create table " + ORD_PRINT_ITEMS_TABLE_NAME + " ( " + COLUMN_ORD_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ORD_ID + " INTEGER UNIQUE," + COLUMN_PROD_ID + " INTEGER," + COLUMN_PROD_CODE + " VARCHAR, " + COLUMN_PROD_NAME + " VARCHAR," + COLUMN_ITEM_QTY + " INTEGER, " + COLUMN_PROD_PRICE + " REAL," + COLUMN_SUB_TOTAL + " REAL);");
        db.execSQL("create table " + SALE_TABLE_NAME + " ( " + COLUMN_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SALE_DATE + " TEXT, " + COLUMN_TAB_ID + " INTEGER UNIQUE," + COLUMN_ORD_TOTAL + " REAL, " + COLUMN_ORD_TOT_ITEMS + " INTEGER," + COLUMN_STATUS + " INTEGER);");
        db.execSQL("create table " + SALE_ITEMS_TABLE_NAME + " ( " + COLUMN_SALE_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SALE_ID + " INTEGER UNIQUE," + COLUMN_PROD_ID + " INTEGER," + COLUMN_PROD_CODE + " VARCHAR, " + COLUMN_PROD_NAME + " VARCHAR," + COLUMN_ITEM_QTY + " INTEGER, " + COLUMN_PROD_PRICE + " REAL," + COLUMN_SUB_TOTAL + " REAL);");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}