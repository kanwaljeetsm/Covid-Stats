package com.kanwaljeetsm.covidstats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DailyChange extends AppCompatActivity {

    private TextView quotes;
    private String url;
    private int count = 0;
    private TextView newActive, newRecovered, newDeceased;
    private RequestQueue requestQueue;
    private ProgressBar pOne, pTwo, pThree;
    Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_change);

        url = getString(R.string.url);
        quotes = findViewById(R.id.quotes);
        newActive = findViewById(R.id.newActive);
        newRecovered = findViewById(R.id.newRecovered);
        newDeceased = findViewById(R.id.newDeceased);
        pOne = findViewById(R.id.pOne);
        pTwo = findViewById(R.id.pTwo);
        pThree = findViewById(R.id.pThree);

        getAndSetData();

        CountDownTimer countDownTimer = new CountDownTimer(15000,5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(count == 3) {
                    count = 1;
                }
                else {
                    count++;
                }
                switch (count) {
                    case 1:
                        quotes.setText(R.string.quote1);
                        break;
                    case 2:
                        quotes.setText(R.string.quote2);
                        break;
                    case 3:
                        quotes.setText(R.string.quote3);
                        break;
                }
            }

            @Override
            public void onFinish() {
                start();
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                pOne.setVisibility(View.VISIBLE);
                pTwo.setVisibility(View.VISIBLE);
                pThree.setVisibility(View.VISIBLE);
                newActive.setVisibility(View.GONE);
                newRecovered.setVisibility(View.GONE);
                newDeceased.setVisibility(View.GONE);
                getAndSetData();
            break;
        }
        return true;
    }

    public void getAndSetData() {
        requestQueue = Volley.newRequestQueue(DailyChange.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            data.setnActiveCases(String.valueOf(response.getInt("activeCasesNew")));
                            data.setnRecovered(String.valueOf(response.getInt("recoveredNew")));
                            data.setnDeaths(String.valueOf(response.getInt("deathsNew")));
                            newRecovered.setText(data.getnRecovered());
                            newDeceased.setText(data.getnDeaths());

                            newActive.setTextSize(30f);
                            newRecovered.setTextSize(30f);
                            newDeceased.setTextSize(30f);
                            newRecovered.setTextColor(getResources().getColor(R.color.blue));
                            newDeceased.setTextColor(getResources().getColor(R.color.black));

                            if(Integer.parseInt(data.getnActiveCases()) > 0) {
                                data.setnActiveCases(("+").concat(data.getnActiveCases()).concat(String.valueOf(Character.toChars(0x2191))));
                                newActive.setTextColor(getResources().getColor(R.color.red));
                            }

                            else {
                                data.setnActiveCases(data.getnActiveCases().concat(String.valueOf(Character.toChars(0x2193))));
                                newActive.setTextColor(getResources().getColor(R.color.green));
                            }

                            newActive.setText(data.getnActiveCases());

                            pOne.setVisibility(View.GONE);
                            pTwo.setVisibility(View.GONE);
                            pThree.setVisibility(View.GONE);
                            newActive.setVisibility(View.VISIBLE);
                            newRecovered.setVisibility(View.VISIBLE);
                            newDeceased.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pOne.setVisibility(View.GONE);
                pTwo.setVisibility(View.GONE);
                pThree.setVisibility(View.GONE);
                newActive.setText(R.string.dataUnavailable);
                newRecovered.setText(R.string.dataUnavailable);
                newDeceased.setText(R.string.dataUnavailable);
                newActive.setVisibility(View.VISIBLE);
                newRecovered.setVisibility(View.VISIBLE);
                newDeceased.setVisibility(View.VISIBLE);
                newActive.setTextColor(getResources().getColor(R.color.black));
                newRecovered.setTextColor(getResources().getColor(R.color.black));
                newDeceased.setTextColor(getResources().getColor(R.color.black));
                newActive.setTextSize(20f);
                newRecovered.setTextSize(20f);
                newDeceased.setTextSize(20f);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}