package com.example.pesel_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class UpdatePerson_Activity extends AppCompatActivity {

    EditText etId, etName, etSurname, etAge, etEmail;
    Button btn_Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_person_);

        Interface();
    }

    public void Interface() {
        etId = (EditText) findViewById(R.id.eT_updateId);
        etName = (EditText) findViewById(R.id.eT_updateName);
        etSurname = (EditText) findViewById(R.id.eT_updateSurname);
        etAge = (EditText) findViewById(R.id.eT_updateAge);
        etEmail = (EditText) findViewById(R.id.eT_updateEmail);
        btn_Update = (Button) findViewById(R.id.btn_Update);
    }

    /**
     * Przycisk "Zapisz", dzięki któremu można zmodyfikować dane osobowe w bazie danych.
     */
    public void OnClick_btn_Update(View V) {
        int isError = 0; //zmienna pomocnicza do ustalenia czy została wpisana wartość w każdym polu

        isError = getError(isError);

         /* Jeśli każde pole jest uzupełnione to wartość zmiennej "isError" równa się 5,
           jeśli nie to zwracany jest błąd. */
        if (isError != 5) {
            Snackbar.make(V, "Uzupełnij wszystkie pola!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
        }
        else
        {
            //Nowe wpisane wartości zostają przypisane są do zmiennych
            int id = Integer.parseInt(etId.getText().toString());
            String name = etName.getText().toString();
            String surname = etSurname.getText().toString();
            int age = Integer.parseInt(etAge.getText().toString());
            String email = etEmail.getText().toString();

            //Tworzone jest połączenie z bazą danych za pomocą DbHelper
            PersonDbHelper personDbHelper = new PersonDbHelper(UpdatePerson_Activity.this);
            SQLiteDatabase db = personDbHelper.getWritableDatabase();

            boolean equalsId = isExistId(personDbHelper, id);

            if (equalsId) {
                //Wpisane wartości zostaną przypisane do bazy danych
                personDbHelper.updatePerson(id, name, surname, age, email, db);

                displayToast("Dane zaktualizowane!");
                etId.setBackgroundColor(0);
                clearEditTexts();
            }
            else {
                displayToast("Osoba o podanym ID nie istnieje!");
                etId.setBackgroundResource(R.drawable.red_border); //error border
            }

            personDbHelper.close();
        }
    }

    /**
     * Metoda sprawdza czy została wpisana wartość w każdym polu i zwraca wartość zmiennej "isError".
     * Jeśli dane pole jest puste to wartość zmiennej jest dekrementowana, natomiast jeśli
     * nie to jest inkrementowana.
     * @param isError wskazuje czy wyświetlić błąd
     * @return zwraca wartość błędu
     */
    private int getError(int isError) {
        if (etId.length() == 0) {
            isError = displayError(etId, isError);
        }
        else {
            isError = deleteError(etId, isError);
        }

        if (etName.length() == 0) {
            isError = displayError(etName, isError);
        }
        else {
            isError = deleteError(etName, isError);
        }

        if (etSurname.length() == 0) {
            isError = displayError(etSurname, isError);
        }
        else {
            isError = deleteError(etSurname, isError);
        }

        if (etAge.length() == 0) {
            isError = displayError(etAge, isError);
        }
        else {
            isError = deleteError(etAge, isError);
        }

        if (etEmail.length() == 0) {
            isError = displayError(etEmail, isError);
        }
        else {
            isError = deleteError(etEmail, isError);
        }
        return isError;
    }

    /**
     * Metoda usuwa błąd z danego pola poprzez usunięcie czerwonej ramki z pola i inkrementuje
     * wartość błędu.
     * @param editText pole, z którego ma być usunięte czerwone obramowanie błędu
     * @param isError wskazuje czy wyświetlić błąd
     * @return zwraca wartość błędu
     */
    private int deleteError(EditText editText, int isError) {
        editText.setBackgroundColor(0);
        isError++;
        return isError;
    }

    /**
     * Metoda wyświetla błąd w danym polu poprzez ustawienie czerwonej ramki i dekrementuje
     * wartość błędu.
     * @param editText pole, z którego ma być usunięte czerwone obramowanie błędu
     * @param isError wskazuje czy wyświetlić błąd
     * @return zwraca wartość błędu
     */
    private int displayError(EditText editText, int isError) {
        editText.setBackgroundResource(R.drawable.red_border); //error border
        isError--;
        return isError;
    }

    /**
     * Czyści zawartość pól do wpiswywania danych.
     */
    private void clearEditTexts() {
        etId.setText("");
        etName.setText("");
        etSurname.setText("");
        etAge.setText("");
        etEmail.setText("");
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

    /**
     * Wyświetla wiadomość Toast.
     * @param s treść wiadomości
     */
    private void displayToast(String s) {
        Toast.makeText(UpdatePerson_Activity.this, s, Toast.LENGTH_SHORT).show();
    }
}