package com.dqserv.dqpos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pos.printer.PrinterFunctions;
import com.pos.printer.PrinterFunctionsLAN;

public class WifiPrinterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView myeditText_PortName;
    //--------------------------
    public static boolean isLAN=false; //¬O§_¨Ï¥Îºô¸ô¤¶­±
    //public static String portName="COM:3"; //******
    //public static int portSettings=115200; //******
    public static String portName="WIFI:192.168.1.168"; //******
    public static int portSettings=9100; //******
    public static int value_StatusSpecified=1;
    //public static String portName="LAN:192.168.6.2"; //******
    //public static int portSettings=9100; //******
    //public static String portName="WIFI:192.168.23.123"; //******
    //public static int portSettings=9100; //******
    Button mainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_printer);
        myeditText_PortName=(TextView)findViewById(R.id.textView_PortName);
        //--------------------------
        //PrinterFunctions.PortDiscovery(Main.portName,Main.portSettings); //******
        //PrinterFunctionsLAN.PortDiscovery(portName,portSettings);

        mainBtn = (Button)findViewById(R.id.BtnPrint);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    IntentPrint("\nTest Print from Bluetooth\n Welcome to DQPOS \n Testing Bluetooth Printing\n");
                //    textDemoPrint();
                // textPrint();
                samplewifiprint();
            }
        });
    }

    public void samplewifiprint() {
        if(WifiPrinterActivity.isLAN) {
           PrinterFunctionsLAN.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,0,0,1,0,0, 0,5,0,"Welcome to DQPOS Wifi Common");
        } else {
            //PrinterFunctions.PrintSampleReceipt(WifiPrinterActivity.portName,WifiPrinterActivity.portSettings);
            PrinterFunctions.PrintText(WifiPrinterActivity.portName, WifiPrinterActivity.portSettings,0,0,1,0,0, 0,5,0,"Welcome to DQPOS Common");
        }
    }

    public void PortDiscovery(View view)
    {
        final EditText editPortName = new EditText(view.getContext());
        editPortName.setText(portName); //******

        final EditText editPortName2 = new EditText(view.getContext());
        editPortName2.setText(String.valueOf(portSettings)); //******

        final RadioButton RadioButton_JAVA = new RadioButton(view.getContext());
        final RadioButton RadioButton_CPP = new RadioButton(view.getContext());
        RadioButton_JAVA.setText("JAVA Code (LanPP.jar)");
        RadioButton_CPP.setText("C++ Code (libPosPPdrv.so)");
        final RadioGroup RadioGroup_NetworkProgram = new RadioGroup(view.getContext());
        RadioGroup_NetworkProgram.addView(RadioButton_JAVA);
        RadioGroup_NetworkProgram.addView(RadioButton_CPP);

        new AlertDialog.Builder(view.getContext())
                .setTitle("Please Input IP Address or Port Name")
                .setView(editPortName)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int button)
                    {
                        TextView portNameField = (TextView)findViewById(R.id.textView_PortName);
                        portNameField.setText(editPortName.getText().toString());

                        portName=editPortName.getText().toString();

                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("m_portName", portNameField.getText().toString());
                        editor.commit();
                        //--------------------------
                        if(portName.length()>0) {
                            AlertDialog.Builder dialog2 = new AlertDialog.Builder(WifiPrinterActivity.this);

                            dialog2.setTitle("Please Input Port Settings Parameters");
                            dialog2.setView(editPortName2);

                            dialog2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        portSettings=Integer.parseInt(editPortName2.getText().toString());
                                    } catch(Exception e) {}
                                    //--------------------------
                                    //¿ï¾Ü¨Ï¥Îªººô¸ô¤¶­±
                                    if(portName.substring(0, 3).equals("LAN") || portName.substring(0, 4).equals("WIFI")) {
                                        AlertDialog.Builder dialog3 = new AlertDialog.Builder(WifiPrinterActivity.this);
                                        dialog3.setTitle("Use of the network program");
                                        RadioButton_JAVA.setChecked(true);
                                        RadioButton_CPP.setChecked(false);
                                        dialog3.setView(RadioGroup_NetworkProgram);
                                        dialog3.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(RadioButton_JAVA.isChecked()) isLAN=true;
                                                else isLAN=false;
                                                //--------------------------
                                                int resOpen=(-1);
                                                AlertDialog.Builder dialog4 = new AlertDialog.Builder(WifiPrinterActivity.this);
                                                if(isLAN) {
                                                    resOpen= PrinterFunctionsLAN.PortDiscovery(WifiPrinterActivity.portName,WifiPrinterActivity.portSettings);
                                                } else {
                                                    resOpen= PrinterFunctions.PortDiscovery(WifiPrinterActivity.portName,WifiPrinterActivity.portSettings);
                                                }
                                                if(resOpen==0) {
                                                    dialog4.setTitle("PortDiscovery");
                                                    dialog4.setMessage("ok.");
                                                    dialog4.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {}
                                                    });
                                                    dialog4.show();
                                                } else {
                                                    dialog4.setTitle("PortDiscovery");
                                                    dialog4.setMessage("fail.");
                                                    dialog4.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {}
                                                    });
                                                    dialog4.show();
                                                }
                                            }
                                        });
                                        dialog3.show();
                                    } else {
                                        isLAN=false;
                                        //--------------------------
                                        int resOpen2=(-1);
                                        AlertDialog.Builder dialog5 = new AlertDialog.Builder(WifiPrinterActivity.this);
                                        if(isLAN) {
                                            resOpen2= PrinterFunctionsLAN.PortDiscovery(WifiPrinterActivity.portName,WifiPrinterActivity.portSettings);
                                        } else {
                                            resOpen2= PrinterFunctions.PortDiscovery(WifiPrinterActivity.portName,WifiPrinterActivity.portSettings);
                                        }
                                        if(resOpen2==0) {
                                            dialog5.setTitle("PortDiscovery");
                                            dialog5.setMessage("ok.");
                                            dialog5.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {}
                                            });
                                            dialog5.show();
                                        } else {
                                            dialog5.setTitle("PortDiscovery");
                                            dialog5.setMessage("fail.");
                                            dialog5.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {}
                                            });
                                            dialog5.show();
                                        }
                                    }
                                }
                            });
                            dialog2.show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int button)
                    {}
                })
                .show();
    }



    public void SampleReceipt_(View view)
    {
        if(WifiPrinterActivity.isLAN) {
            PrinterFunctionsLAN.PrintSampleReceipt(WifiPrinterActivity.portName,WifiPrinterActivity.portSettings);
        } else {
            PrinterFunctions.PrintSampleReceipt(WifiPrinterActivity.portName,WifiPrinterActivity.portSettings);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_pos) {
            // Handle the camera action
            if (!POS.class.getSimpleName().equalsIgnoreCase("POS")) {
                startActivity(new Intent(this, POS.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_gallery) {
            if (!POS.class.getSimpleName().equalsIgnoreCase("OrdersList")) {
                startActivity(new Intent(this, OrdersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        } else if (id == R.id.nav_printers) {
            if (!POS.class.getSimpleName().equalsIgnoreCase("PrintersActivity")) {
                startActivity(new Intent(this, PrintersActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
