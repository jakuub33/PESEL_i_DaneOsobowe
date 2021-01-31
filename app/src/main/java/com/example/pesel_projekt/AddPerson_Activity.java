package com.example.pesel_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorStateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileOutputStream;

public class AddPerson_Activity extends AppCompatActivity {

    EditText etId, etName, etSurname, etAge, etEmail;
    Button btn_Save, btn_SaveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person_);

        Interface();
    }

    public void Interface() {
        etId = (EditText) findViewById(R.id.editText_Id);
        etName = (EditText) findViewById(R.id.editText_Name);
        etSurname = (EditText) findViewById(R.id.editText_Surname);
        etAge = (EditText) findViewById(R.id.editText_Age);
        etEmail = (EditText) findViewById(R.id.editText_Email);
        btn_Save = (Button) findViewById(R.id.btn_Save);
        btn_SaveFile = (Button) findViewById(R.id.btn_SaveFile);
    }

    /**
     * Przycisk "Zapisz do pliku", dzięki któremu można dodać dane osobowe do pliku.
     */
    public void OnClick_btn_SaveFile(View V) {
        int isError = 0; //zmienna pomocnicza do ustalenia czy została wpisana wartość w każdym polu

        isError = getError(isError);

        /* Jeśli każde pole jest uzupełnione to wartość zmiennej "isError" równa się 5,
           jeśli nie to zwracany jest błąd. */
        if (isError != 5) {
            Snackbar.make(V, "Uzupełnij wszystkie pola!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
        }
        else {
            String name = etName.getText().toString();
            String surname = etSurname.getText().toString();
            String age = etAge.getText().toString();
            String email = etEmail.getText().toString();

            // data/data/com.example.pesel_projekt/files/
            String filename = "Dane_osobowe";
            String text = "Imie:;" + name + "\nNazwisko:;" + surname + "\nWiek:;" + age +
                    "\nEmail:;" + email;
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename+".csv", Context.MODE_PRIVATE);
                outputStream.write(text.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            clearEditTexts();

            displayToast("Plik został zapisany!");
        }
    }

    /**
     * Przycisk "Zapisz", dzięki któremu można dodać nowe dane osobowe do bazy danych.
     */
    public void OnClick_btn_Save(View V) {
        int isError = 0; //zmienna pomocnicza do ustalenia czy została wpisana wartość w każdym polu

        isError = getError(isError);

        /* Jeśli każde pole jest uzupełnione to wartość zmiennej "isError" równa się 5,
           jeśli nie to zwracany jest błąd. */
        if (isError != 5) {
            Snackbar.make(V, "Uzupełnij wszystkie pola!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
        }
        else
        {
            //Wpisane wartości zostają przypisane są do zmiennych
            String id = etId.getText().toString();
            String name = etName.getText().toString();
            String surname = etSurname.getText().toString();
            String age = etAge.getText().toString();
            String email = etEmail.getText().toString();

            //Tworzone jest połączenie z bazą danych za pomocą DbHelper
            PersonDbHelper personDbHelper = new PersonDbHelper(AddPerson_Activity.this);
            SQLiteDatabase db = personDbHelper.getWritableDatabase();

            boolean equalsId = isExistId(personDbHelper, id);

            if (!equalsId) {
                //Wpisane wartości zostaną przypisane do bazy danych
                personDbHelper.addPerson(Integer.parseInt(id), name, surname, Integer.parseInt(age), email, db);

                clearEditTexts();

                displayToast("Dodano do bazy danych!");
            }
            else {
                displayToast("Podane ID już istnieje!");
                displayError(etId, isError);
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
     * Metoda sprawdza czy podane przez użytkownika id znajduje się w bazie danych.
     * @param personDbHelper obiekt klasy PersonDbHelper
     * @param id wpisane przez użytkownika id
     * @return zwraca informacje czy podane id znajduje się w bazie danych
     */
    private boolean isExistId(PersonDbHelper personDbHelper, String id)
    {
        SQLiteDatabase db = personDbHelper.getReadableDatabase();
        Cursor cursor = personDbHelper.checkIdPerson(db);
        boolean equalsId = false;   //informuje czy podane przez użytkownika id jest w bazie danych

        //Za pomocą pętli while odczytywane są po kolei identyfikatory w bazie danych
        while (cursor.moveToNext()) {
            //Przypisanie bieżącego id do zmiennej
            String existId = Integer.toString(cursor.getInt(cursor.getColumnIndex(PersonContract.PersonEntry.PERSON_ID)));

            //Sprawdzenie czy podane przez użytkownika id znajduje się w bazie danych
            if (existId.equals(id)) {
                equalsId = true;
            }
        }
        return equalsId;
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
     * Wyświetla wiadomość Toast.
     * @param s treść wiadomości
     */
    private void displayToast(String s) {
        Toast.makeText(AddPerson_Activity.this, s, Toast.LENGTH_SHORT).show();
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
}