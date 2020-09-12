package com.kanwaljeetsm.covidstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
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
    private String url;
    private TextView textView8;
    public List<String> localStateList = new ArrayList<>();
    private List<String> localStateNums1 = new ArrayList<>();
    private List<String> localStateNums2 = new ArrayList<>();
    private RequestQueue requestQueue;
    private Data data;
    private TextView txtTotal, txtActive, txtRecovered, txtDeaths, txtUpdateTime, info, txtNoInternet;
    private String mDateTimeUpdate;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar nationProgress, stateProgress;
    private HorizontalScrollView horizontalScroll;
    private ScrollView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = getString(R.string.url);
        data = new Data();

        txtTotal = findViewById(R.id.txtTotal);
        txtActive = findViewById(R.id.txtActive);
        txtRecovered = findViewById(R.id.txtRecovered);
        txtDeaths = findViewById(R.id.txtDeaths);
        txtUpdateTime = findViewById(R.id.txtUpdateTime);
        recyclerView = findViewById(R.id.recyclerView);
        nationProgress = findViewById(R.id.nationProgress);
        stateProgress = findViewById(R.id.stateProgress);
        horizontalScroll = findViewById(R.id.horizontalScroll);
        info = findViewById(R.id.info);
        mainView = findViewById(R.id.mainView);
        txtNoInternet = findViewById(R.id.txtNoInternet);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    data.setTotalCases(String.valueOf(response.getInt("totalCases")));
                    data.setActiveCases(String.valueOf(response.getInt("activeCases")));
                    data.setRecovered(String.valueOf(response.getInt("recovered")));
                    data.setDeaths(String.valueOf(response.getInt("deaths")));
                    mDateTimeUpdate =(response.getString("lastUpdatedAtApify"));
                    data.setDateTimeUpdate(getString(R.string.last_updated).concat(mDateTimeUpdate.substring(0,10)
                            .concat(" at ").concat(mDateTimeUpdate.substring(11, 16))));

                    JSONArray jsonArray = response.getJSONArray("regionData");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        localStateList.add(jsonObject.getString("region"));
                        localStateNums1.add(String.valueOf(jsonObject.getInt("totalInfected")));
                        localStateNums2.add(String.valueOf(jsonObject.getInt("recovered")));
                    }

                    data.setRegion(localStateList);
                    data.setTotalInfected(localStateNums1);
                    data.setStateRecovered(localStateNums2);

                    txtTotal.setText(data.getTotalCases());
                    txtActive.setText(data.getActiveCases());
                    txtRecovered.setText(data.getRecovered());
                    txtDeaths.setText(data.getDeaths());
                    txtUpdateTime.setText(data.getDateTimeUpdate());

                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new RecyclerAdapter(localStateList, localStateNums1, localStateNums2);
                    recyclerView.setAdapter(adapter);

                    nationProgress.setVisibility(View.GONE);
                    stateProgress.setVisibility(View.GONE);
                    horizontalScroll.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    info.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    mainView.setVisibility(View.GONE);
                    txtNoInternet.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mainView.setVisibility(View.GONE);
                txtNoInternet.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}