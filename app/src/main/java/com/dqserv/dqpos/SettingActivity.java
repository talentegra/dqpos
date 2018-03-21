package com.dqserv.dqpos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.GlobalApplication;

public class SettingActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton, radioTax, radioNoTax;
    EditText etCGST, etSGST, etName, etIP, etPort;
    Button btnchangeTax, btnChangeWifiConfig;
    LinearLayout llTaxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioTax = (RadioButton) findViewById(R.id.radio_tax);
        radioNoTax = (RadioButton) findViewById(R.id.radio_no_tax);
        llTaxLayout = (LinearLayout) findViewById(R.id.setting_tax_layout);
        etCGST = (EditText) findViewById(R.id.setting_cgst);
        etSGST = (EditText) findViewById(R.id.setting_sgst);
        etName = (EditText) findViewById(R.id.setting_wifi_config_name);
        etIP = (EditText) findViewById(R.id.setting_wifi_config_ip);
        etPort = (EditText) findViewById(R.id.setting_wifi_config_port);
        btnchangeTax = (Button) findViewById(R.id.setting_change_tax);
        btnChangeWifiConfig = (Button) findViewById(R.id.setting_change_wifi_config);

        if (GlobalApplication.taxPref.getString("tax_val", "t").
                equalsIgnoreCase("t")) {
            llTaxLayout.setVisibility(View.VISIBLE);
            radioTax.setChecked(true);
            radioNoTax.setChecked(false);
        } else {
            llTaxLayout.setVisibility(View.GONE);
            radioTax.setChecked(false);
            radioNoTax.setChecked(true);
        }

        etCGST.setText(String.valueOf(GlobalApplication.cgstPref.getFloat("cgst", 2.5f)));
        etSGST.setText(String.valueOf(GlobalApplication.sgstPref.getFloat("sgst", 2.5f)));

        etName.setText(GlobalApplication.wifiNamePref.getString("wifi_name", "dqpos"));
        etIP.setText(GlobalApplication.wifiIpPref.getString("wifi_ip", "192.168.1.1"));
        etPort.setText(String.valueOf(GlobalApplication.wifiPortPref.getInt("wifi_port", 80)));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                SharedPreferences.Editor editor = GlobalApplication.taxPref.edit();
                if (radioButton.getTag().toString().equalsIgnoreCase("t")) {
                    llTaxLayout.setVisibility(View.VISIBLE);
                    editor.putString("tax_val", "t");
                } else {
                    llTaxLayout.setVisibility(View.GONE);
                    editor.putString("tax_val", "nt");
                }
                editor.commit();
            }
        });

        btnchangeTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalApplication.taxPref.getString("tax_val", "t").
                        equalsIgnoreCase("t")) {
                    SharedPreferences.Editor editorCGST = GlobalApplication.cgstPref.edit();
                    editorCGST.putFloat("cgst", Float.parseFloat(etCGST.getText().toString()));
                    editorCGST.commit();

                    SharedPreferences.Editor editorSGST = GlobalApplication.sgstPref.edit();
                    editorSGST.putFloat("sgst", Float.parseFloat(etSGST.getText().toString()));
                    editorSGST.commit();

                    Toast.makeText(SettingActivity.this, "Successfully change Tax Value",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnChangeWifiConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editorName = GlobalApplication.wifiNamePref.edit();
                editorName.putString("wifi_name", etName.getText().toString());
                editorName.commit();

                SharedPreferences.Editor editorIP = GlobalApplication.wifiIpPref.edit();
                editorIP.putString("wifi_ip", etIP.getText().toString());
                editorIP.commit();

                SharedPreferences.Editor editorPort = GlobalApplication.wifiPortPref.edit();
                editorPort.putInt("wifi_port", Integer.parseInt(etPort.getText().toString()));
                editorPort.commit();

                Toast.makeText(SettingActivity.this, "Successfully change Wifi Configuration",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
