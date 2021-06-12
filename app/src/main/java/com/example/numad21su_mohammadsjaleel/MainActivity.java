package com.example.numad21su_mohammadsjaleel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String NAME = "Mohammad Shayan Jaleel";
    private static final String EMAIL = "jaleel.m@northeastern.edu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void displayInfo(View view) {
        TextView textView = findViewById(R.id.name_address_textview);
        Button btn= findViewById(R.id.button);
        if(textView == null) return;
        if(textView.getText() == null || textView.getText().equals("")){
            textView.setText(String.format(Locale.getDefault(), "Name: %s \n Email: %s",
                    NAME, EMAIL));
            btn.setText(getString(R.string.hide_info));
        }
        else{
            textView.setText("");
            btn.setText(getString(R.string.show_info));
        }
    }
    public void openA3(View view) {
        Intent intent = new Intent(this, A3Activity.class);
        startActivity(intent);
    }
    public void openLinkCollector(View view) {
        Intent intent = new Intent(this, LinkCollectorActivity.class);
        startActivity(intent);
    }
    public void openLocation(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
}