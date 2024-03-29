package com.kanwaljeetsm.covidstats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity{
    private String url;
    private List<String> localStateList = new ArrayList<>();
    private List<String> localStateNums1 = new ArrayList<>();
    private List<String> localStateNums2 = new ArrayList<>();
    private List<String> localStateNums3 = new ArrayList<>();
    private List<String> localStateNums4 = new ArrayList<>();
    private RequestQueue requestQueue;
    private Data data;
    private TextView txtTotal, txtActive, txtRecovered, txtDeaths, txtUpdateTime, info, txtNoInternet, txtAppClosure, faq, call, web;
    private String mDateTimeUpdate, link;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar nationProgress, stateProgress;
    private HorizontalScrollView horizontalScroll;
    private ScrollView mainView;
    private AlertDialog.Builder builder;
    private CardView nxtActCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ParseInstallation.getCurrentInstallation().saveInBackground();
            url = getString(R.string.url);
            data = new Data();
        }catch(Exception e) {e.printStackTrace();}

        txtTotal = findViewById(R.id.txtTotal);
        txtActive = findViewById(R.id.txtActive);
        txtRecovered = findViewById(R.id.txtRecovered);
        txtDeaths = findViewById(R.id.txtDeaths);
        txtUpdateTime = findViewById(R.id.txtUpdateTime);
        txtAppClosure = findViewById(R.id.txtAppClosure);
        nxtActCard = findViewById(R.id.nxtActCard);
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

        nxtActCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DailyChange.class);
                startActivity(intent);
            }
        });

        getAndSetData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                nationProgress.setVisibility(View.VISIBLE);
                stateProgress.setVisibility(View.VISIBLE);
                horizontalScroll.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
                getAndSetData();
            break;
        }
        return true;
    }

    private void getVersion() {
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("AppVersion");
            parseQuery.getInBackground(getString(R.string.versionCheckObjId), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (!((getString(R.string.version)).equals(String.valueOf(object.get("Version"))))) {
                        link = String.valueOf(object.get("Link"));
                        builder.setMessage(getString(R.string.txtDialogSubtitle).concat("\n\nUpdate Changelog:\n").concat(String.valueOf(object.get("message"))))
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
        }catch(Exception e) {e.printStackTrace();}
    }

    public void appOpeningTime() {
            try {
                ParseObject appOpeningTime = new ParseObject("OpeningTimes");
                SimpleDateFormat sdf = new SimpleDateFormat();
                String dateTime = sdf.format(new Date());
                appOpeningTime.put("time", dateTime);
                appOpeningTime.saveInBackground();
            }catch(Exception e) {e.printStackTrace();}
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
                            .concat(" at ").concat(mDateTimeUpdate.substring(11, 16)).concat(" GMT")));
                    data.setDateTimeUpdate(mDateTimeUpdate);

                    JSONArray jsonArray = response.getJSONArray("regionData");
                    localStateList.clear();
                    localStateNums1.clear();
                    localStateNums2.clear();
                    localStateNums3.clear();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        localStateList.add(jsonObject.getString("region"));
                        localStateNums1.add(String.valueOf(jsonObject.getInt("totalInfected")));
                        localStateNums2.add(String.valueOf(jsonObject.getInt("recovered")));
                        localStateNums3.add(String.valueOf(jsonObject.getInt("deceased")));
                        localStateNums4.add(String.valueOf(jsonObject.getInt("activeCases")));
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
                    adapter = new RecyclerAdapter(localStateList, localStateNums1, localStateNums2, localStateNums3, localStateNums4);
                    recyclerView.setAdapter(adapter);

                    nationProgress.setVisibility(View.GONE);
                    stateProgress.setVisibility(View.GONE);
                    horizontalScroll.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    info.setVisibility(View.VISIBLE);

                    appOpeningTime();

                } catch (JSONException e) {
                    e.printStackTrace();
                    txtAppClosure.setText(getString(R.string.txtAppClosure));
                    CountDownTimer countDownTimer = new CountDownTimer(5000,5000) {
                        @Override
                        public void onTick(long l) {}

                        @Override
                        public void onFinish() {
                            finish();
                        }
                    }.start();

                    mainView.setVisibility(View.GONE);
                    txtNoInternet.setVisibility(View.VISIBLE);
                    txtAppClosure.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                txtAppClosure.setText(getString(R.string.txtAppClosure));
                CountDownTimer countDownTimer = new CountDownTimer(5000,5000) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        finish();
                    }
                }.start();

                mainView.setVisibility(View.GONE);
                txtNoInternet.setVisibility(View.VISIBLE);
                txtAppClosure.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}