package com.kanwaljeetsm.covidstats;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String url;
    private List<String> localStateList = new ArrayList<>();
    private List<String> localStateNums1 = new ArrayList<>();
    private List<String> localStateNums2 = new ArrayList<>();
    private RequestQueue requestQueue;
    private Data data;
    private TextView txtTotal, txtActive, txtRecovered, txtDeaths, txtUpdateTime, info, txtNoInternet, faq, call, web;
    private String mDateTimeUpdate, link;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar nationProgress, stateProgress;
    private HorizontalScrollView horizontalScroll;
    private ScrollView mainView;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();
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
        faq = findViewById(R.id.faq);
        call = findViewById(R.id.call);
        web = findViewById(R.id.web);
        builder = new AlertDialog.Builder(this);

        getVersion();

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.faqLink)));
                startActivity(intent);            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(getString(R.string.helpline)));
                startActivity(intent);
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.webLink)));
                startActivity(intent);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
            }
        });

        getAndSetData();
    }

    private void getVersion() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("AppVersion");
        parseQuery.getInBackground(getString(R.string.versionCheckObjId), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(!((getString(R.string.version)).equals(String.valueOf(object.get("Version"))))) {
                    link = String.valueOf(object.get("Link"));
                    builder.setMessage(getString(R.string.txtDialogSubtitle))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.txtDialogPositive), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(getString(R.string.txtDialogNegative), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.txtDialogToast), Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.setTitle(getString(R.string.txtDialogTitle));
                    dialog.show();
                }
            }
        });
    }

    public void appOpeningTime() {
            ParseObject appOpeningTime = new ParseObject("OpeningTimes");
            SimpleDateFormat sdf = new SimpleDateFormat();
            String dateTime = sdf.format(new Date());
            appOpeningTime.put("time", dateTime);
            appOpeningTime.saveInBackground();
    }

    public void getAndSetData() {
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
                    mDateTimeUpdate = (getString(R.string.txtLast_updated).concat(mDateTimeUpdate.substring(0,10)
                            .concat(" at ").concat(mDateTimeUpdate.substring(11, 16))));
                    data.setDateTimeUpdate(mDateTimeUpdate);

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

                    appOpeningTime();

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