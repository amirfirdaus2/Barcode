package com.theta_edge.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Scan extends AppCompatActivity {
    String codeFormat,codeContent,scanDate;
    JSONObject jsonobject;
    String jabatanPkid,ptjPkid,name,jabatanP,ptjP,pkidUser,pkidSamAimLokasiAsal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //getData from LOGIN ACTIVITY
        Intent data = getIntent();
        pkidUser = data.getStringExtra("pkid");
        name=data.getStringExtra("name");
        jabatanPkid=data.getStringExtra("jabatanPkid");
        ptjPkid=data.getStringExtra("ptjPkid");
        jabatanP=data.getStringExtra("jabatanP");
        ptjP=data.getStringExtra("ptjP");
        scanBarcode();

    }

    @Override
    public void onBackPressed() {
    }

    public void scanBarcode () {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent inspect) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, inspect);
        codeContent = scanningResult.getContents();
        String noSiriPendaftaran = codeContent.replaceAll("/","&");
        String URL = url.MAIN_URL + "/api/getNosiri/"+noSiriPendaftaran+"/"+jabatanPkid+"/"+ptjPkid;
        SimpleDateFormat simpledateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        scanDate = simpledateFormat.format(calendar.getTime());
        if(noSiriPendaftaran.length() > 0){

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                JSONObject jObj = new JSONObject(String.valueOf(response));
                                boolean error = jObj.getBoolean("_status");
                                if(error){
                                    JSONArray cast = jObj.getJSONArray("_vals");
                                    if(cast.length() > 0){
                                        for (int i = 0; i < cast.length(); i++) {
                                            jsonobject = cast.getJSONObject(i);
                                            Long pkidAimFailinduk = jsonobject.optLong("pkid");
                                            String namaAset = jsonobject.optString("namaAset");
                                            String noSiri = jsonobject.optString("nosiri");
                                            String lokasi = jsonobject.optString("samAimLokasiPerihal");
                                            pkidSamAimLokasiAsal = jsonobject.optString("pkidSamAimLokasi");


                                            Intent intent = new Intent(Scan.this,InternalInspection.class);
                                            intent.putExtra("pkidUser", pkidUser);
                                            intent.putExtra("namaUser", name);
                                            intent.putExtra("pkidSamAimLokasiAsal",pkidSamAimLokasiAsal);
                                            intent.putExtra("pkidAimFailinduk", pkidAimFailinduk.toString());
                                            intent.putExtra("namaAset", namaAset);
                                            intent.putExtra("noSiri", noSiri);
                                            intent.putExtra("lokasi", lokasi);
                                            intent.putExtra("scanDate",scanDate);
                                            intent.putExtra("jabatanPkid",jabatanPkid);
                                            intent.putExtra("ptjPkid",ptjPkid);
                                            startActivity(intent);
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(),"TIADA DATA",Toast.LENGTH_LONG).show();
                                        Intent back = new Intent(Scan.this,UserDetails.class);
                                        back.putExtra("pkid",pkidUser);
                                        back.putExtra("name",name);
                                        back.putExtra("jabatanPkid",jabatanPkid);
                                        back.putExtra("ptjPkid",ptjPkid);
                                        back.putExtra("jabatan",jabatanP);
                                        back.putExtra("ptj",ptjP);
                                        startActivity(back);
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(),"TIADA DATA =",Toast.LENGTH_LONG).show();
                                    Intent back = new Intent(Scan.this,UserDetails.class);
                                    startActivity(back);
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            );
            queue.add(getRequest);

        }else{
            Toast.makeText(getApplicationContext(),"SILA CUBA LAGI",Toast.LENGTH_SHORT).show();
        }


    }
}
