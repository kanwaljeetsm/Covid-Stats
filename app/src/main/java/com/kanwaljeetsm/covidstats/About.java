package com.kanwaljeetsm.covidstats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends AppCompatActivity {

    private ImageView imgGithub, imgLinkedin;
    private TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        imgGithub = findViewById(R.id.imgGithub);
        imgLinkedin = findViewById(R.id.imgLinkedin);
        txtVersion = findViewById(R.id.txtVersion);

        txtVersion.setText(getString(R.string.txtVersion).concat(getString(R.string.version)));

        imgGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.gitLink)));
                startActivity(intent);
            }
        });

        imgLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.linkedinLink)));
                startActivity(intent);
            }
        });
    }
}