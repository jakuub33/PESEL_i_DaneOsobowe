package com.example.pesel_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeletePerson_Activity extends AppCompatActivity {

    EditText etId;
    Button btn_Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_person_);

        Interface();
    }

    public void Interface() {
        etId = (EditText) findViewById(R.id.eT_deleteId);
        btn_Delete = (Button) findViewById(R.id.btn_Delete);
    }

    /**
     * Przycisk "Usuń" usuwa konkretny wiersz z bazy danych.
     */
    public void OnClick_btn_Delete(View V) {
        if (etId.length() == 0) {
            displayToast("Pole ID nie może być puste!");
            etId.setBackgroundResource(R.drawable.red_border); //error border
        }
        else {
            etId.setBackgroundColor(0); //usuwa error border

            //Tworzone jest połączenie z bazą danych za pomocą DbHelper
            PersonDbHelper personDbHelper = new PersonDbHelper(DeletePerson_Activity.this);
            SQLiteDatabase db = personDbHelper.getWritableDatabase();

            //Odczytanie wpisanego identyfikatora przez użytkownika i przypisanie go do zmiennej.
            int id = Integer.parseInt(etId.getText().toString());

            boolean equalsId = isExistId(personDbHelper, id);

            //Jeśli podane przez użytkownika id znajduje się w bazie danych to może zostać usunięte
            if (equalsId) {
                //Usunięcie osoby z bazy danych z odpowiednim identyfikatorem
                personDbHelper.deletePerson(id, db);

                displayToast("Usunięto z bazy danych!");
                etId.setBackgroundColor(0);
                etId.setText("");
            }
            else {
                displayToast("Osoba o podanym id nie istnieje!");
                etId.setBackgroundResource(R.drawable.red_border); //error border
            }

            personDbHelper.close();
        }
    }

    /**
     * Wyświetla wiadomość Toast.
     * @param s treść wiadomości
     */
    private void displayToast(String s) {
        Toast.makeText(DeletePerson_Activity.this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Metoda sprawdza czy podane przez użytkownika id znajduje się w bazie danych.
     * @param personDbHelper obiekt klasy PersonDbHelper
     * @param id wpisane przez użytkownika id
     * @return zwraca informacje czy podane id znajduje się w bazie danych
     */
    private boolean isExistId(PersonDbHelper personDbHelper, int id)
    {
        SQLiteDatabase db = personDbHelper.getReadableDatabase();
        Cursor cursor = personDbHelper.checkIdPerson(db);
        boolean equalsId = false;   //informuje czy podane przez użytkownika id jest w bazie danych

        //Za pomocą pętli while odczytywane są po kolei identyfikatory w bazie danych
        while (cursor.moveToNext()) {
            //Przypisanie bieżącego id do zmiennej
            String existId = Integer.toString(cursor.getInt(cursor.getColumnIndex(PersonContract.PersonEntry.PERSON_ID)));

            //Sprawdzenie czy podane przez użytkownika id znajduje się w bazie danych
            if (existId.equals(Integer.toString(id))) {
                equalsId = true;
            }
        }
        return equalsId;
    }
}