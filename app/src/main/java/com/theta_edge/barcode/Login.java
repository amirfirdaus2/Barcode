package com.theta_edge.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class  Login extends AppCompatActivity {
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onBackPressed() {
        Intent getMain = new Intent(this,MainActivity.class);
        startActivity(getMain);
    }

    public void login(View view){
        String URLMAIN= com.theta_edge.barcode.url.MAIN_URL;
        EditText id = (EditText)findViewById(R.id.noPengguna);
        EditText password = (EditText)findViewById(R.id.kataLaluan);


        if(id.getText().toString().equals("") || password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"NO PENGGUNA ATAU PASSWORD KOSONG",Toast.LENGTH_LONG).show();
        }else{
            String URL = URLMAIN+"/security/doLogin/"+id.getText().toString()+"?pwd="+password.getText().toString();
            final Intent intent = new Intent(this,UserDetails.class);
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            boolean status = false;
                            try{
                                JSONObject jObj = new JSONObject(String.valueOf(response));
                                boolean error = jObj.getBoolean("_status");
                                if(error){
                                    String pkid = jObj.getString("_pkid");
                                    String name = jObj.getString("_name");
                                    String jabatan = jObj.getString("_jabatan");
                                    String ptj =  jObj.getString("_ptj");

                                    intent.putExtra("pkid", pkid);
                                    intent.putExtra("jabatan", jabatan);
                                    intent.putExtra("name", name);
                                    intent.putExtra("ptj", ptj);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"KATA LALUAN ATAU ID PENGGUNA SALAH",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getApplicationContext(),"TIADA INTERNET",Toast.LENGTH_LONG).show();
                        }
                    }
            );

// add it to the RequestQueue
            queue.add(getRequest);
        }
    }
}




