package com.example.numad21su_mohammadsjaleel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class A3Activity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3);
        if(savedInstanceState != null && savedInstanceState.getString("PressedInfo") != null){
            String retrievedString = savedInstanceState.getString("PressedInfo");
            TextView textView = findViewById(R.id.text_pressed);
            textView.setText(retrievedString);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        TextView textView = findViewById(R.id.text_pressed);
        savedInstanceState.putString("PressedInfo", textView.getText().toString());
    }

    public void onClick(View view) {
        TextView textView = findViewById(R.id.text_pressed);
        switch (view.getId()) {
            case R.id.button_a:
                textView.setText("Pressed A");
                break;
            case R.id.button_b:
                textView.setText("Pressed B");
                break;
            case R.id.button_c:
                textView.setText("Pressed C");
                break;
            case R.id.button_d:
                textView.setText("Pressed D");
                break;
            case R.id.button_e:
                textView.setText("Pressed E");
                break;
            case R.id.button_f:
                textView.setText("Pressed F");
                break;
        }

    }
}