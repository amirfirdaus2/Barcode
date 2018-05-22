package com.theta_edge.barcode;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import utils.LoadProperties;

public class ExternalInspection extends AppCompatActivity {
    Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_inspection);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();

        Intent extras = getIntent();
        LoadProperties loadProperties = new LoadProperties();
        TextView nosiriAset = (TextView) findViewById(R.id.displaynosiri);
        TextView namaAset = (TextView) findViewById(R.id.displayassetname);
        TextView lokasiSekarang = (TextView) findViewById(R.id.displaycurrLoc);
        TextView namaPegawai = (TextView) findViewById(R.id.displayofficername);
        TextView tarikhImbas = (TextView) findViewById(R.id.displayscanDate);
        finish = (Button) findViewById(R.id.button_finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //EditText notes = (EditText) findViewById(R.id.notes);
        if (extras != null) {
            String data = extras.getStringExtra("SCAN_RESULT");
            Log.d("TAG", "The result from scan: " + data);

            String dateofScan = extras.getStringExtra("SCAN_DATE");
            tarikhImbas.setText(dateofScan);
            
            try {
                String iptext = loadProperties.getIP(getApplicationContext());
                String url1 = "http://" + iptext + "/WebService/integrate/Android/aset/" + data;
                Log.d("TAG", "urls" + "=" + url1);
                String receiveData = new JSONData1().execute(url1).get();

                try {
                    JSONObject assetNameObj = new JSONObject(receiveData);

                    String nosiri = assetNameObj.getString("NO SIRI");
                    nosiriAset.setText(nosiri);
                    extras.putExtra("nosiri", nosiri);

                    String nama = assetNameObj.getString("NAMA ASET");
                    namaAset.setText(nama);

                    String statussekarang = assetNameObj.getString("STATUS SEKARANG");
                    extras.putExtra("statussekarang", statussekarang);

                    String lokasiAsal = assetNameObj.getString("LOKASI ASAL");
                    int encodelokasi;
                    if (!(lokasiAsal.equals("TIADA MAKLUMAT"))) {
                        int n = lokasiAsal.indexOf("-");
                        encodelokasi = Integer.parseInt(lokasiAsal.substring(0,n-1));//replaceAll("[\\D]", "")
                        System.out.println("pkidlokasiAsal =" + encodelokasi);
                        extras.putExtra("pkidlokasiAsal", encodelokasi);
                    }else {
                        encodelokasi = 0;
                        System.out.println("pkidlokasiAsal =" + encodelokasi);
                        extras.putExtra("pkidlokasiAsal", encodelokasi);
                    }

                    String lokasisekarang = assetNameObj.getString("LOKASI SEKARANG");
                    int encodelokasisekarang;
                    if (!(lokasisekarang.equals("TIADA MAKLUMAT"))) {
                        int n = lokasisekarang.indexOf("-");
                        encodelokasisekarang = Integer.parseInt(lokasisekarang.substring(0,n-1));//replaceAll("[\\D]", "")
                        System.out.println("pkidlokasiSebenar =" + encodelokasisekarang);
                        extras.putExtra("pkidlokasiSebenar", encodelokasisekarang);
                    }else {
                        encodelokasisekarang = 0;
                        System.out.println("pkidlokasiSebenar =" + encodelokasisekarang);
                        extras.putExtra("pkidlokasiSebenar", encodelokasisekarang);
                    }
                    lokasiSekarang.setText(lokasisekarang);

                    String pegawai = assetNameObj.getString("NAMA PEGAWAI");
                    int n = pegawai.indexOf("-");
                    if (!(pegawai.equals("TIADA MAKLUMAT"))) {
                        String substringpegawai = pegawai.substring(0,n-1);
                        extras.putExtra("idpegawai", substringpegawai);
                    }else{
                        String substringpegawai = "TM";
                        extras.putExtra("idpegawai", substringpegawai);
                    }
                    namaPegawai.setText(pegawai);
                    //String catatan = assetNameObj.getString("CATATAN");
                    //notes.setText(catatan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }catch (ExecutionException|InterruptedException|IOException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LoadProperties loadProperties = new LoadProperties();
        Bundle extras = getIntent().getExtras();

    }
    @Override
    public void onBackPressed() {
        Intent getMain = new Intent(this,MainActivity.class);
        startActivity(getMain);
    }

    public void kePengimbas2(View view) {
        Bundle extras = getIntent().getExtras();
        String data1 = extras.getString("jabatanName");
        int data2 = extras.getInt("pkidPTJ");
        String data3 = extras.getString("ptjName");
        String data4 = extras.getString("selectedPTJ");
        String data5 = extras.getString("selectedDept");
        //int data6 = extras.getInt("pkidJabatan");
        String data7 = extras.getString("idpemeriksa");

        Intent imbas = new Intent(this,Scan.class);
        imbas.putExtra("jabatanName", data1);
        imbas.putExtra("pkidPTJ", data2);
        imbas.putExtra("ptjName", data3);
        imbas.putExtra("selectedPTJ", data4);
        imbas.putExtra("selectedDept", data5);
        //imbas.putExtra("pkidJabatan", data6);
        imbas.putExtra("idpemeriksa", data7);
        startActivity(imbas);
    }

    public void logKeluar(View view) {
        Intent getMain = new Intent(this,MainActivity.class);
        startActivity(getMain);
    }

    public void simpan(View view) {
        LoadProperties loadProperties = new LoadProperties();
        try {
            String iptext = loadProperties.getIP(getApplicationContext());
            String stringurl = "http://" + iptext + "/WebService/integrate/Android/pemeriksaan/externalUpdate";
            new JSONData2().execute(stringurl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String currentdateandtime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate = df.format(c.getTime());
        System.out.println(strDate);
        return strDate;

    }

        private class JSONData1 extends AsyncTask<String, Void, String> {
            String jsonData;
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

                jsonData = readIt(is);
                System.out.println("receivedata" + jsonData);
                return jsonData;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        @Override
        protected void onPostExecute(String result1) {

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

    private class JSONData2 extends AsyncTask<String, Void, String> {
        String success;
        @Override
        protected String doInBackground(String... strings) {
            try {
                return makeServiceCall(strings[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL / data may be invalid.";
            }

        }

        private String makeServiceCall(String myurl) throws IOException {
            String writeJSON;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.connect();
                writeJSON = writeData();
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream ());
                out.write(writeJSON);
                out.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while (in.readLine() != null) {
                }
                //System.out.println("\n");
                success = "1SPEKS Web Service Invoked Successfully..";
            } catch(Exception e) {
                success = "Error while calling 1SPEKS Web Service";
                e.printStackTrace();
            }
            return success;
        }
        @Override
        protected void onPostExecute(String result1) {
            if (success.equals("1SPEKS Web Service Invoked Successfully..")) {
                Toast.makeText(ExternalInspection.this, "DATA BERJAYA DISIMPAN", Toast.LENGTH_SHORT).show();
            } else if (success.equals("Error while calling 1SPEKS Web Service") || success.equals("Unable to retrieve web page. URL may be invalid.")) {
                Toast.makeText(ExternalInspection.this, "DATA TIDAK BERJAYA DISIMPAN, sila cuba lagi", Toast.LENGTH_SHORT).show();
            }

        }
        protected String writeData () throws JSONException {
            Bundle extras = getIntent().getExtras();
            String data1 = extras.getString("statussekarang");
            String data2 = extras.getString("nosiri");
            int data3 = extras.getInt("pkidlokasiAsal");
            int data4 = extras.getInt("pkidlokasiSebenar");
            String data5 = currentdateandtime();
            String data6 = extras.getString("idpemeriksa");
            String data7 = extras.getString("idpegawai");

            EditText notes = (EditText) findViewById(R.id.notes);
            String data8 = notes.getText().toString();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("STATUS", data1);
            jsonParam.put("NO SIRI", data2);
            jsonParam.put("LOKASI ASAL", data3);
            jsonParam.put("LOKASI SEBENAR", data4);
            jsonParam.put("CATATAN", data8);
            jsonParam.put("ID PEGAWAI ASET",data7);
            jsonParam.put("TARIKH",data5);
            jsonParam.put("ID PEMERIKSA",data6);

            System.out.println(jsonParam);
            return jsonParam.toString();
        }
    }
}
