package com.theta_edge.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class UserDetails extends AppCompatActivity {
    TextView staffName,jbtn,PTJ;
    JSONObject jsonobject;
    ArrayList<String> worldlist;
    ArrayList<String> SAMPTJ;
    String jabatanPkid;
    String ptjPkid,name,jabatanP,ptjP;
    Intent aa;
    String pkidUser;
    ArrayList<SamJabatan> samJabatan;
    ArrayList<samPtj> samptjj;
    Button imbas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);

        //getData from LOGIN ACTIVITY
        Intent data = getIntent();
        pkidUser=data.getStringExtra("pkid");
        name=data.getStringExtra("name");
        name=data.getStringExtra("name");
        jabatanP=data.getStringExtra("jabatan");
        ptjP=data.getStringExtra("ptj");


        //Declaration
        staffName = (TextView)findViewById(R.id.textView_staffName);
        jbtn = (TextView)findViewById(R.id.textView_jabatan);
        PTJ = (TextView)findViewById(R.id.textView_ptj);
        imbas = (Button)findViewById(R.id.button_imbas);


        staffName.setText(name);
        jbtn.setText(jabatanP);
        PTJ.setText(ptjP);
        aa = new Intent(this,Scan.class);
        imbas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aa.putExtra("pkid",pkidUser);
                aa.putExtra("name",name);
                aa.putExtra("jabatanPkid",jabatanPkid);
                aa.putExtra("ptjPkid",ptjPkid);
                aa.putExtra("jabatanP",jabatanP);
                aa.putExtra("ptjP",ptjP);
                startActivity(aa);
            }
        });


        //get jabatan n ptj
        String URL = url.MAIN_URL + "/api/samJabatan";
        samJabatan = new ArrayList<SamJabatan>();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean status = false;
                        try{
                            worldlist = new ArrayList<String>();
                            JSONObject jObj = new JSONObject(String.valueOf(response));
                            boolean error = jObj.getBoolean("_status");
                            if(error){
                                JSONArray cast = jObj.getJSONArray("_vals");

                                for (int i = 0; i < cast.length(); i++) {
                                    jsonobject = cast.getJSONObject(i);

                                    SamJabatan samJabatana = new SamJabatan();
                                    samJabatana.setPkid(jsonobject.optString("pkid"));
                                    // Populate spinner with country names
                                    samJabatan.add(samJabatana);
                                    worldlist.add(jsonobject.optString("perihal"));

                                }

                                final Spinner mySpinner = (Spinner) findViewById(R.id.jabatan);
                                ArrayAdapter adapter = new ArrayAdapter(UserDetails.this,android.R.layout.simple_spinner_dropdown_item,worldlist);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mySpinner.setAdapter(adapter);
                                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        String urlptj = url.MAIN_URL + "/api/samPtj/"+samJabatan.get(position).getPkid();
                                        jabatanPkid = samJabatan.get(position).getPkid();
                                        getPtj(urlptj);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"TIADA DATA UTK DROPDOWN JABATAN",Toast.LENGTH_LONG).show();
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

    }

    public void getPtj(String url){
        samptjj = new ArrayList<samPtj>();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean status = false;
                        try{
                            SAMPTJ = new ArrayList<String>();
                            JSONObject jObj = new JSONObject(String.valueOf(response));
                            boolean error = jObj.getBoolean("_status");
                            if(error){
                                JSONArray cast = jObj.getJSONArray("_vals");

                                for (int i = 0; i < cast.length(); i++) {
                                    jsonobject = cast.getJSONObject(i);

                                    samPtj samm = new samPtj();
                                    samm.setPkid(jsonobject.optString("pkid"));
                                    // Populate spinner with country names
                                    samptjj.add(samm);
                                    // Populate spinner with country names
                                    SAMPTJ.add(jsonobject.optString("perihal"));

                                }
                                final Spinner mySpinner = (Spinner) findViewById(R.id.ptj);
                                ArrayAdapter adapter = new ArrayAdapter(UserDetails.this,android.R.layout.simple_spinner_dropdown_item,SAMPTJ);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mySpinner.setAdapter(adapter);

                                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                       ptjPkid = samptjj.get(position).getPkid();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                    }

                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"TIADA DATA UTK DROPDOWN PTJ",Toast.LENGTH_LONG).show();
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
    }


    @Override
    public void onBackPressed() {

    }
}