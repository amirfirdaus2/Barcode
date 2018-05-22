package com.theta_edge.barcode;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void okButton(View view) {
        Intent setting = new Intent(this,MainActivity.class);

        EditText iptext = (EditText) findViewById(R.id.ipText);
        String ipText = iptext.getText().toString();

        String file = "setting.txt";
        try {
            FileOutputStream fos = openFileOutput(file, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fos);
            outputWriter.write(ipText);
            outputWriter.close();

            System.out.print("saved file"+ipText);
        }catch (IOException e) {
            System.err.println("Failed to open setting.txt file");
            e.printStackTrace();
        }

        startActivity(setting);
    }


}
