package com.kanwaljeetsm.covidstats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class StateDetails extends AppCompatActivity {

    private String nActive, nRecovered, nDeceased, total, active, recovered, deceased, name;
    private TextView txtTitle, newActive, newRecovered, newDeceased, txtActiveNum, txtRecoveredNum, txtDeceasedNum, txtTotalNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);

        Intent i = getIntent();
        name = i.getStringExtra("Name");
        nActive = i.getStringExtra("nActive");
        nRecovered = i.getStringExtra("nRecovered");
        nDeceased = i.getStringExtra("nDeceased");
        total = i.getStringExtra("Total");
        active = i.getStringExtra("Active");
        recovered = i.getStringExtra("Recovered");
        deceased = i.getStringExtra("Deceased");

        txtTitle = findViewById(R.id.txtTitle);
        newActive = findViewById(R.id.newActive);
        newRecovered = findViewById(R.id.newRecovered);
        newDeceased = findViewById(R.id.newDeceased);
        txtActiveNum = findViewById(R.id.txtActiveNum);
        txtRecoveredNum = findViewById(R.id.txtRecoveredNum);
        txtDeceasedNum = findViewById(R.id.txtDeceasedNum);
        txtTotalNum = findViewById(R.id.txtTotalNum);

        if((Integer.parseInt(nActive)) <= 0) {
            newActive.setTextColor(getResources().getColor(R.color.green));
            newActive.setText(nActive.concat(String.valueOf(Character.toChars(0x2193))));
        }
        else {
            newActive.setTextColor(getResources().getColor(R.color.red));
            newActive.setText("+".concat(nActive).concat(String.valueOf(Character.toChars(0x2191))));
        }

        txtTitle.setText(getResources().getString(R.string.regionDetail).concat(" ").concat(name));
        newRecovered.setText(nRecovered);
        newDeceased.setText(nDeceased);
        txtActiveNum.setText(active);
        txtRecoveredNum.setText(recovered);
        txtDeceasedNum.setText(deceased);
        txtTotalNum.setText(total);



    }
}