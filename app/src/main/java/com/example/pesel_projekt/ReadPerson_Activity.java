package com.example.pesel_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class ReadPerson_Activity extends AppCompatActivity {

    TextView textView_Display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_person_);

        textView_Display = (TextView) findViewById(R.id.textView_Display);

        readPerson();
    }

    /**
     * Metoda, dzięki której można wyświetlić aktualną zawartość bazy danych.
     */
    private void readPerson() {

        //Odwołanie do bazy danych za pomocą DbHelper
        PersonDbHelper personDbHelper = new PersonDbHelper(ReadPerson_Activity.this);
        SQLiteDatabase db = personDbHelper.getReadableDatabase();

        //Teraz można sprowadzić wartości za pomocą Cursor
        Cursor cursor = personDbHelper.readPerson(db);

        String text = "";

        //Za pomocą pętli while odczytywane są dane
        while (cursor.moveToNext())
        {
            //Odczytane dane z bazy danych przekazywane są do zmiennych
            String id = Integer.toString(cursor.getInt(cursor.getColumnIndex(PersonContract.PersonEntry.PERSON_ID)));
            String name = cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.NAME));
            String surname = cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.SURNAME));
            String age = Integer.toString(cursor.getInt(cursor.getColumnIndex(PersonContract.PersonEntry.AGE)));
            String email = cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.EMAIL));

            //Wartości zmiennych formatowane są w spójny tekst
            text = text + "\n\n" + "Id: " + id + "\nImię: " + name + "\nNazwisko: " + surname +
                    "\nWiek: " + age + "\nEmail: " + email;
        }

        personDbHelper.close();

        if (text.length() > 0) {
            //Sformatowany tekst wpisany do zmiennej wyświetlamy na ekranie
            textView_Display.setText(text);
        }
        else
            textView_Display.setText("\n\t\t\t\t\t\t\tBaza danych jest pusta!");
    }
}