package com.example.pesel_projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *  Dziecziczy SQLiteOpenHelper i implementuje jego metody: onCreate i onUpgrade
 */
public class PersonDbHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "person_db"; //Nazwa bazy danych
    public static final int DATABASE_VERSION = 1; //Wersja bazy danych

    //Kod w języku SQL do tworzenia tabeli
    public static final String CREATE_TABLE = "create table " + PersonContract.PersonEntry.TABLE_NAME +
            "(" + PersonContract.PersonEntry.PERSON_ID + " number," +
            PersonContract.PersonEntry.NAME + " text," +
            PersonContract.PersonEntry.SURNAME + " text," +
            PersonContract.PersonEntry.AGE + " number," +
            PersonContract.PersonEntry.EMAIL + " text);";

    //Kod w języku SQL do usuwania tabeli
    public static final String DROP_TABLE = "drop table if exists " + PersonContract.PersonEntry.TABLE_NAME;

    //SQLiteOpenHelper nie ma domyślnego konstruktora, więc trzeba go stworzyć
    public PersonDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Tutaj tworzona jest tabela
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Tutaj aktualizowane są dane w istniejącej już metodzie
        db.execSQL(DROP_TABLE); //tabela jest usuwana
        onCreate(db); //tabela musi zostać stworzona ponownie
    }

    /**
     * Metoda, dzięki której można dodać nową dane osobowe do bazy danych.
     * @param id identyfikator
     * @param name imie
     * @param surname nazwisko
     * @param age wiek
     * @param email e-mail
     * @param db baza danych
     */
    public void addPerson(int id, String name, String surname, int age, String email, SQLiteDatabase db)
    {
        //Przypisanie danych do tabeli
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonContract.PersonEntry.PERSON_ID, id);
        contentValues.put(PersonContract.PersonEntry.NAME, name);
        contentValues.put(PersonContract.PersonEntry.SURNAME, surname);
        contentValues.put(PersonContract.PersonEntry.AGE, age);
        contentValues.put(PersonContract.PersonEntry.EMAIL, email);

        //Wstawienie danych do tabeli
        db.insert(PersonContract.PersonEntry.TABLE_NAME, null, contentValues);
        Log.d("Database Operations", "One raw inserted...");
    }

    /**
     * Metoda, dzięki której można odczytać wiartości z tabeli.
     * @param db baza danych
     */
    public Cursor readPerson(SQLiteDatabase db)
    {
        //Do tablicy dodajemy nazwy kolumn bazy danych
        String columns[] = {PersonContract.PersonEntry.PERSON_ID, PersonContract.PersonEntry.NAME,
                PersonContract.PersonEntry.SURNAME, PersonContract.PersonEntry.AGE,
                PersonContract.PersonEntry.EMAIL};

        //Odczytanie danych z tabeli
        Cursor cursor = db.query(PersonContract.PersonEntry.TABLE_NAME, columns, null,
                null, null, null, null);

        return cursor;
    }

    /**
     * Metoda pozwalająca na modyfikowanie rekordów w tabeli.
     * @param id identyfikator
     * @param name imię
     * @param surname nazwisko
     * @param age wiek
     * @param email e-mail
     * @param db baza danych
     */
    public void updatePerson(int id, String name, String surname, int age, String email, SQLiteDatabase db)
    {
        //Modyfikowanie danych
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonContract.PersonEntry.NAME, name);
        contentValues.put(PersonContract.PersonEntry.SURNAME, surname);
        contentValues.put(PersonContract.PersonEntry.AGE, age);
        contentValues.put(PersonContract.PersonEntry.EMAIL, email);

        //Tabela zostanie zaktulizowana bazując na id
        String equalId = PersonContract.PersonEntry.PERSON_ID + " = " + id;

        //Aktualizacja tabeli nowymi danymi
        db.update(PersonContract.PersonEntry.TABLE_NAME, contentValues, equalId, null);
        Log.d("Database Operations", "Table updated...");
    }

    /**
     * Metoda pozwalająca na usunięcie konkretnego wiersza w bazie danych.
     * @param id identyfikator
     * @param db baza danych
     */
    public void deletePerson(int id, SQLiteDatabase db)
    {
        //Usuwanie odbywa się poprzez wskazanie id danej osoby
        String deleteId = PersonContract.PersonEntry.PERSON_ID + " = " + id;

        //Usunięcie danego id z bazy danych
        db.delete(PersonContract.PersonEntry.TABLE_NAME, deleteId, null);
    }

    /**
     * Metoda sprawdza czy podane przez użytkownika id znajduje się w bazie danych.
     * @param db baza danych
     */
    public Cursor checkIdPerson(SQLiteDatabase db)
    {
        //Przypisanie do zminnej id z bazy danych
        String existId[] = {PersonContract.PersonEntry.PERSON_ID};

        //Odczytanie danych z tabeli
        Cursor cursor = db.query(PersonContract.PersonEntry.TABLE_NAME, existId, null,
                null, null, null, null);

        return cursor;
    }
}
