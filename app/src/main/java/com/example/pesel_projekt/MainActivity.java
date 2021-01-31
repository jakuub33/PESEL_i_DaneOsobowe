package com.example.pesel_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView_Menu;
    Button btn_Generator, btn_Verify, btn_Person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Interface();

        getSupportActionBar().hide();
    }

    public void Interface() {
        textView_Menu = (TextView) findViewById(R.id.textView_Menu);
        btn_Generator = (Button) findViewById(R.id.btn_Generator);
        btn_Verify = (Button) findViewById(R.id.btn_Verify);
        btn_Person = (Button) findViewById(R.id.btn_Person);
    }

    public void OnClick_btn_Generator(View V) {
        //Intent intent = new Intent(getApplicationContext(), ActivityGenerator.class);
        //MainActivity.this.finish();
        Intent intent = new Intent(MainActivity.this, ActivityGenerator.class);
        startActivity(intent);
    }

    public void OnClick_btn_Verify(View V) {
        Intent intent = new Intent(MainActivity.this, ActivityVerify.class);
        startActivity(intent);
    }

    public void OnClick_btn_Person(View V) {
        Intent intent = new Intent(MainActivity.this, ActivityPerson.class);
        startActivity(intent);
    }
}