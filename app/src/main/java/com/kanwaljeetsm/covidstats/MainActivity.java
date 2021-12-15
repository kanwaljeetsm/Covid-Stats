package com.kanwaljeetsm.covidstats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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
    private List<String> localStateNums5 = new ArrayList<>();
    private List<String> localStateNums6 = new ArrayList<>();
    private List<String> localStateNums7 = new ArrayList<>();

    private RequestQueue requestQueue;
    private Data data;
    private TextView txtTotal, txtActive, txtRecovered, txtDeaths, txtUpdateTime, info, txtRestartApp, txtNoInternet, faq, call, web;
    private String mDateTimeUpdate, link;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar nationProgress, stateProgress;
    private HorizontalScrollView horizontalScroll;
    private ScrollView mainView;
    private AlertDialog.Builder builder;
    private CardView nxtActCard;
    private boolean mState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
//            ParseInstallation.getCurrentInstallation().saveInBackground();
            url = getString(R.string.url);
            data = new Data();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtTotal = findViewById(R.id.txtTotal);
        txtActive = findViewById(R.id.txtActive);
        txtRecovered = findViewById(R.id.txtRecovered);
        txtDeaths = findViewById(R.id.txtDeaths);
        txtUpdateTime = findViewById(R.id.txtUpdateTime);
        txtRestartApp = findViewById(R.id.txtRestartApp);
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

//        getVersion();

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.faqLink)));
                startActivity(intent);
            }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general_v1.12.5")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = ("Successful");
                        if (!task.isSuccessful()) {
                            msg = ("Failed");
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        if(mState == true) {
            MenuItem item = menu.findItem(R.id.refresh);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if((item.getItemId()) == R.id.refresh) {
                nationProgress.setVisibility(View.VISIBLE);
                stateProgress.setVisibility(View.VISIBLE);
                horizontalScroll.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
                getAndSetData();
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
                    localStateNums4.clear();
                    localStateNums5.clear();
                    localStateNums6.clear();
                    localStateNums7.clear();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        localStateList.add(jsonObject.getString("region"));

                        //fix for the '***' appearing in regions
                        int fixCounter = 0;
                        for(String t: localStateList) {
                            localStateList.set(fixCounter,t.replaceAll("\\*",""));
                            fixCounter++;
                        }
                        localStateNums1.add(String.valueOf(jsonObject.getInt("totalInfected")));
                        localStateNums2.add(String.valueOf(jsonObject.getInt("recovered")));
                        localStateNums3.add(String.valueOf(jsonObject.getInt("deceased")));
                        localStateNums4.add(String.valueOf(jsonObject.getInt("activeCases")));
                        localStateNums5.add(String.valueOf(jsonObject.getInt("newInfected")));
                        localStateNums6.add(String.valueOf(jsonObject.getInt("newRecovered")));
                        localStateNums7.add(String.valueOf(jsonObject.getInt("newDeceased")));
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
                    adapter = new RecyclerAdapter(localStateList, localStateNums1, localStateNums2, localStateNums3, localStateNums4,
                            localStateNums5, localStateNums6, localStateNums7);
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
                    txtRestartApp.setVisibility(View.VISIBLE);

                    mState = true;
                    invalidateOptionsMenu();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                mainView.setVisibility(View.GONE);
                txtNoInternet.setVisibility(View.VISIBLE);
                txtRestartApp.setVisibility(View.VISIBLE);

                mState = true;
                invalidateOptionsMenu();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}