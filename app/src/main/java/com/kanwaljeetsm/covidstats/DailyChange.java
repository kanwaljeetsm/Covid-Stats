package com.kanwaljeetsm.covidstats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class DailyChange extends AppCompatActivity {

    private TextView quotes;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_change);

        quotes = findViewById(R.id.quotes);
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
}