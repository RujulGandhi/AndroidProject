package com.example.rujul.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String text;
    private BufferedReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // Tag used to cancel the request
//        String tag_json_obj = "json_obj_req";
//
//        String url = "http://api.androidhive.info/volley/person_object.json";
//
//       final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                url, null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        Log.d("TAG", response.toString());
//                        pDialog.hide();
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("TAG", "Error: " + error.getMessage());
//                pDialog.hide();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", "Androidhive");
//                params.put("email", "abc@androidhive.info");
//                params.put("password", "password123");
//
//                return params;
//            }
//
//        };
//
//// Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//


//    new getDaaata().execute();
    }


    private String getDataUTF(HashMap<String, String> hashMap) {
        String UTF = "";
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        return result.toString();


    }


    private class getDaaata extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

                // Defined URL  where to send data
                URL url = new URL("http://androidexample.com/media/webservice/httppost.php");

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Name","rujul");
                hashMap.put("Email","rujul");
                hashMap.put("User","rujul");
              

                String encodedString = getDataUTF(hashMap);
                wr.write(encodedString);
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }


                text = sb.toString();
            } catch (Exception ex) {

            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }

            // Show response on activity
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("updalod")){

            }else{

            }

            Toast.makeText(MainActivity.this, "" + s, Toast.LENGTH_SHORT).show();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
