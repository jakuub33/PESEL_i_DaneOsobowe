package com.example.pesel_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityPerson extends AppCompatActivity {

    Button btn_Add, btn_Show, btn_Update, btn_Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
    }

    public void Interface() {
        btn_Add = (Button) findViewById(R.id.btn_Add);
        btn_Show = (Button) findViewById(R.id.btn_Show);
        btn_Update = (Button) findViewById(R.id.btn_Update);
        btn_Delete = (Button) findViewById(R.id.btn_Delete);
    }

    public void OnClick_btn_Add(View V) {
        Intent intent = new Intent(ActivityPerson.this, AddPerson_Activity.class);
        startActivity(intent);
    }

    public void OnClick_btn_Show(View V) {
        Intent intent = new Intent(ActivityPerson.this, ReadPerson_Activity.class);
        startActivity(intent);
    }

    public void OnClick_btn_Update(View V) {
        Intent intent = new Intent(ActivityPerson.this, UpdatePerson_Activity.class);
        startActivity(intent);
    }

    public void OnClick_btn_Delete(View V) {
        Intent intent = new Intent(ActivityPerson.this, DeletePerson_Activity.class);
        startActivity(intent);
    }
}