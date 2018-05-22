package com.theta_edge.barcode;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent setting = new Intent(this, SettingActivity.class);
                startActivity(setting);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        Intent getMain = new Intent(this,MainActivity.class);
        startActivity(getMain);
    }

    public void continued (View view) {
        Intent logmasuk = new Intent(this,Login.class);
        startActivity(logmasuk);

    }

    public void internetSetting(View view) {
        Intent logmasuk = new Intent(this, SettingActivity.class);
        startActivity(logmasuk);
    }

    protected class ServiceHandler extends AsyncTask<String, Void, String> {
        String jsondata1;
        @Override
        protected String doInBackground(String... strings) {
            try {
                return makeServiceCall(strings[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        private String makeServiceCall(String myurl) throws IOException {
            InputStream is = null;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("TAG", "The response is: " + response);
                if (response == 0) {
                    Log.d("TAG", "Server may down");
                    conn.disconnect();
                }
                is = conn.getInputStream();

                jsondata1 = readIt(is);
                System.out.println(jsondata1);
                return jsondata1;
            }finally{
                if (is != null) {
                    is.close();
                }
            }
        }
        
        protected String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            Log.d("TAG", stringBuilder.toString());
            bufferedReader.close();
            return stringBuilder.toString();
        }

    }

}


