package com.kanwaljeetsm.covidstats;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String url = "https://api.apify.com/v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true";
    private TextView textView8;
    private List<String> localStateList = new ArrayList<>();
    private List<String> localStateNums = new ArrayList<>();
    private RequestQueue requestQueue;
    private Data data;
    private TextView txtTotal, txtActive, txtRecovered, txtDeaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new Data();

        txtTotal = findViewById(R.id.txtTotal);
        txtActive = findViewById(R.id.txtActive);
        txtRecovered = findViewById(R.id.txtRecovered);
        txtDeaths = findViewById(R.id.txtDeaths);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    data.setTotalCases(String.valueOf(response.getInt("totalCases")));
                    data.setActiveCases(String.valueOf(response.getInt("activeCases")));
                    data.setRecovered(String.valueOf(response.getInt("recovered")));
                    data.setDeaths(String.valueOf(response.getInt("deaths")));

                    txtTotal.setText(data.getTotalCases());
                    txtActive.setText(data.getActiveCases());
                    txtRecovered.setText(data.getRecovered());
                    txtDeaths.setText(data.getDeaths());

                    JSONArray jsonArray = response.getJSONArray("regionData");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        localStateList.add(jsonObject.getString("region"));
                        localStateNums.add(String.valueOf(jsonObject.getInt("totalInfected")));
                    }

                    data.setRegion(localStateList);
                    data.setTotalInfected(localStateNums);


                    Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Not Done!", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Not at all Done!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);


    }
}