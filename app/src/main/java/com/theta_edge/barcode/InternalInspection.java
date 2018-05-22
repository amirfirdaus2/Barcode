package com.theta_edge.barcode;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class InternalInspection extends AppCompatActivity {
    TextView tarikhImbas,nosiri,namaAset,lokasi,username;
    EditText CATATAN;
    String pkid,PKIDUSER,JABATANP,PTJP,PKIDSAMAIMLOKASIASAL,NAMAUSER;
    Long PKIDSAMAIMLOKASIBARU,KODSTATUSASET;
    Spinner namaPegawai,lokasiBaru,keadaanAset;
    ArrayList<String> LIST;
    ArrayList<String> LISTL;
    ArrayList<String> LISTK;
    JSONObject jsonobject;
    JSONObject jsonobjectL;
    JSONObject jsonobjectK;
    ArrayList<PyFailinduk> pyFailinduks;
    ArrayList<SamAimLokasi> samAimLokasis;
    ArrayList<SamKodD> samKodDs;
    Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_inspection);


        //getData from LOGIN ACTIVITY
        Intent data = getIntent();
        PKIDUSER = data.getStringExtra("pkidUser");
        NAMAUSER =  data.getStringExtra("namaUser");

        pkid=data.getStringExtra("pkidAimFailinduk");
        PKIDSAMAIMLOKASIASAL = data.getStringExtra("pkidSamAimLokasiAsal");
        String NAMAASET=data.getStringExtra("namaAset");
        String NOSIRI=data.getStringExtra("noSiri");
        String LOKASI = data.getStringExtra("lokasi");
        String TARIKHSCAN = data.getStringExtra("scanDate");
        JABATANP = data.getStringExtra("jabatanPkid");
        PTJP = data.getStringExtra("ptjPkid");

        //declaration
        tarikhImbas = (TextView)findViewById(R.id.displayscanDate);
        nosiri = (TextView)findViewById(R.id.displaynosiri);
        namaAset = (TextView)findViewById(R.id.displayassetname);
        lokasi = (TextView)findViewById(R.id.displaycurrLoc);
        CATATAN = (EditText)findViewById(R.id.notes);
        username = (TextView)findViewById(R.id.textView4);

        tarikhImbas.setText(TARIKHSCAN);
        nosiri.setText(NOSIRI);
        namaAset.setText(NAMAASET);
        lokasi.setText(LOKASI);
        username.setText(NAMAUSER);


        finish = (Button) findViewById(R.id.button5);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent next = new Intent(getApplicationContext(), Login.class);
                startActivity(next);
            }
        });

        //spinner lokasi baru
        String URLL = url.MAIN_URL + "/api/getLokasi/"+JABATANP;
        samAimLokasis = new ArrayList<SamAimLokasi>();
        RequestQueue queuee = Volley.newRequestQueue(this);
        JsonObjectRequest getRequests = new JsonObjectRequest(Request.Method.GET, URLL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            LISTL = new ArrayList<String>();
                            JSONObject jObj = new JSONObject(String.valueOf(response));
                            boolean error = jObj.getBoolean("_status");
                            if(error){
                                JSONArray cast = jObj.getJSONArray("_vals");
                                for (int i = 0; i < cast.length(); i++) {
                                    jsonobjectL = cast.getJSONObject(i);
                                    SamAimLokasi samAimLokasi = new SamAimLokasi();
                                    samAimLokasi.setPkid(jsonobjectL.optLong("pkid"));
                                    samAimLokasis.add(samAimLokasi);
                                    LISTL.add(jsonobjectL.optString("perihal"));

                                }
                                final Spinner mySpinner = (Spinner) findViewById(R.id.spinner_lokasiBaru);
                                ArrayAdapter adapter = new ArrayAdapter(InternalInspection.this,android.R.layout.simple_spinner_dropdown_item,LISTL);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mySpinner.setAdapter(adapter);
                                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        PKIDSAMAIMLOKASIBARU = samAimLokasis.get(position).getPkid();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"TIADA DATA",Toast.LENGTH_LONG).show();
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
        queuee.add(getRequests);


        //spinner KEADAAN ASET
        String URLLL = url.MAIN_URL + "/api/getKeadaanAset";
        samKodDs = new ArrayList<SamKodD>();
        RequestQueue queueee = Volley.newRequestQueue(this);
        JsonObjectRequest getRequestss = new JsonObjectRequest(Request.Method.GET, URLLL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            LISTK = new ArrayList<String>();
                            JSONObject jObj = new JSONObject(String.valueOf(response));
                            boolean error = jObj.getBoolean("_status");
                            if(error){
                                JSONArray cast = jObj.getJSONArray("_vals");
                                for (int i = 0; i < cast.length(); i++) {
                                    jsonobjectK = cast.getJSONObject(i);
                                    SamKodD samKodD = new SamKodD();
                                    samKodD.setPkid(jsonobjectK.optLong("pkid"));
                                    samKodDs.add(samKodD);
                                    LISTK.add(jsonobjectK.optString("perihal"));

                                }
                                final Spinner mySpinner = (Spinner) findViewById(R.id.spinner_keadaanAset);
                                ArrayAdapter adapter = new ArrayAdapter(InternalInspection.this,android.R.layout.simple_spinner_dropdown_item,LISTK);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mySpinner.setAdapter(adapter);
                                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        KODSTATUSASET = samKodDs.get(position).getPkid();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"TIADA DATA",Toast.LENGTH_LONG).show();
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
        queueee.add(getRequestss);

    }

    public void simpan(View view){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        Integer idUser = Integer.parseInt(PKIDUSER);
        Long samJabatanPkid = Long.parseLong(JABATANP);
        Long samPtjPkid = Long.parseLong(PTJP);
        Long tahun = Long.parseLong(String.valueOf(year));
        Long aimFailindukPkid = Long.parseLong(pkid);
        Long samAimLokasiAsalPkid = Long.parseLong(PKIDSAMAIMLOKASIASAL);
        Long samAimLokasiBaruPkid = PKIDSAMAIMLOKASIBARU;
        String catatan = CATATAN.getText().toString();
        Long kodStatusPeriksaAsetPkid = KODSTATUSASET;



        String URLPOST  = url.MAIN_URL+"/api/simpan/"+idUser+"/"+samJabatanPkid+"/"+samPtjPkid+"/"+tahun+"/"+aimFailindukPkid+"/"+samAimLokasiAsalPkid+"/"+samAimLokasiBaruPkid+"/"+catatan+"/"+kodStatusPeriksaAsetPkid;
        final ProgressDialog dialog = ProgressDialog.show(InternalInspection.this, "","Sedang Disimpan...", true);
        RequestQueue queueee = Volley.newRequestQueue(this);
        JsonObjectRequest getRequestss = new JsonObjectRequest(Request.Method.GET, URLPOST, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            LISTK = new ArrayList<String>();
                            JSONObject jObj = new JSONObject(String.valueOf(response));
                            boolean error = jObj.getBoolean("result");
                            if(error){
                                dialog.dismiss();
                                String noRekod = jObj.getString("noRekod");

                                AlertDialog alertDialog = new AlertDialog.Builder(InternalInspection.this).create();
                                // Setting Dialog Title
                                alertDialog.setTitle("Berjaya");

                                // Setting Dialog Message
                                alertDialog.setMessage("No Rekod Telah Dijana : "+noRekod);

                                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(InternalInspection.this,Login.class);
                                        startActivity(intent);
                                    }
                                });
                                // Setting OK Button
                                alertDialog.show();
                            }else{
                                dialog.dismiss();
                                AlertDialog alertDialog = new AlertDialog.Builder(InternalInspection.this).create();
                                // Setting Dialog Title
                                alertDialog.setTitle("Ralat");

                                // Setting Dialog Message
                                alertDialog.setMessage("RALAT");

                                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(InternalInspection.this,Login.class);
                                        startActivity(intent);
                                    }
                                });
                                // Setting OK Button
                                alertDialog.show();
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
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"TIADA INTERNET",Toast.LENGTH_LONG).show();
                    }
                }
        );
        queueee.add(getRequestss);


    }

    @Override
    public void onBackPressed() {

    }

}
