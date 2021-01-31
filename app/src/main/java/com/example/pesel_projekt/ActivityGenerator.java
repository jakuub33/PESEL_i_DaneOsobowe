package com.example.pesel_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Random;

public class ActivityGenerator extends AppCompatActivity {

    private static final String TAG = "ActivityGenerator";

    TextView textView_date, textView_result;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Button btn_DatePicker, btn_Clear, btn_Generate;
    RadioGroup radioGroup;
    RadioButton radioButton, radioButton_woman, radioButton_man;
    String dayDP, monthDP, yearDP;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        Interface();

        //czynność po zatwierdzeniu daty czyli wyświetlenie jej
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m++;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + d + "/" + m + "/" + y);

                String date = "DD/MM/YYYY : " + d + "/" + m + "/" + y;
                textView_date.setBackgroundColor(0); //usuwa error border
                textView_date.setText(date);

                //Przypisane wartości do zmiennych globalnych
                dayDP = d + "";
                monthDP = m + "";
                yearDP = y + "";
            }
        };
    }

    /**
     * Odwołoanie do elementów interfejsu
     */
    public void Interface() {
        textView_date = (TextView) findViewById(R.id.textView_date);
        textView_result = (TextView) findViewById(R.id.textView_result);
        btn_DatePicker = (Button) findViewById(R.id.btn_DatePicker);
        btn_Clear = (Button) findViewById(R.id.btn_Clear);
        btn_Generate = (Button) findViewById(R.id.btn_Generate);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_woman = (RadioButton) findViewById(R.id.radioButton_woman);
        radioButton_man = (RadioButton) findViewById(R.id.radioButton_man);
    }

    /**
     * Oprogramowanie przycisku ustawiającego datę
     */
    public void onClick_btn_DatePicker(View v) {
        //Ustawienie bieżacej daty jako poczatkowa
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                ActivityGenerator.this,
                android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                dateSetListener,
                y, m, d);

        //Ustawienie daty minimalnej
        Calendar calendarMin = Calendar.getInstance();
        calendarMin.set(Calendar.YEAR, 1800);
        calendarMin.set(Calendar.MONTH, 0);
        calendarMin.set(Calendar.DAY_OF_MONTH, 1);
        dialog.getDatePicker().setMinDate(calendarMin.getTimeInMillis());

        //Ustawienie daty maksymalnej
        Calendar calendarMax = Calendar.getInstance();
        calendarMax.set(Calendar.YEAR, 2299);
        calendarMax.set(Calendar.MONTH, 11);
        calendarMax.set(Calendar.DAY_OF_MONTH, 31);
        dialog.getDatePicker().setMaxDate(calendarMax.getTimeInMillis());

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     * Wyświetla powiadomienie Toast dotyczące Radio Button'a
     */
    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        Toast.makeText(ActivityGenerator.this, "Wybrano: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Oprogramowanie przycisku Generuj Pesel
     */
    public void onClick_btn_Generate(View view)
    {
        //Jeśli nie wybrano daty urodzenia wyświetlany jest błąd
        if (textView_date.length() == 0) {
            //Toast.makeText(ActivityGenerator.this, "Wybierz datę urodzenia!", Toast.LENGTH_SHORT).show();
            //textView_date.setError("Błąd!");

            Snackbar.make(view, "Wybierz datę urodzenia!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
            textView_date.setBackgroundResource(R.drawable.red_border); //error border
        }
        else
        {
            /* c to char, czyli poszczególna liczba numeru PESEL:
                c1_2 - rok
                c3_4 - miesiąc
                c5_6 - dzień
                c7 - c9 - losowany numer serii
                c10 - płeć
                c11 - liczba kontrolna */
            String c1_2, c3_4, c5_6, c7, c8, c9, c10, c11;

            int day = Integer.parseInt(dayDP);
            int month = Integer.parseInt(monthDP);
            int year = Integer.parseInt(yearDP);

            if (day < 10)
            {
                c5_6 = "0" + day;
            }
            else
            {
                c5_6 = day + "";
            }

            //Sprawdzenie stulecia
            c3_4 = "";
            if (year >= 1800 && year <= 1899)
            {
                month += 80;
                c3_4 = month + "";
            }
            else if (year >= 2000 && year <= 2099)
            {
                month += 20;
                c3_4 = month + "";
            }
            else if (year >= 2100 && year <= 2199)
            {
                month += 40;
                c3_4 = month + "";
            }
            else if (year >= 2200 && year <= 2299)
            {
                month += 60;
                c3_4 = month + "";
            }
            else if (year >= 1900 && year <= 1999)
            {
                if (month < 10)
                {
                    c3_4 = "0" + month;
                }
                else
                {
                    c3_4 = month + "";
                }
            }

            //Skrócenie roku np z 1999 na 99.
            c1_2 = yearDP.substring(2, 4);

            //Losowanie wartości dla cyfr 7-9 (numer serii)
            Random random = new Random();
            c7 = random.nextInt(10) + "";
            c8 = random.nextInt(10) + "";
            c9 = random.nextInt(10) + "";

            //Losowanie cyfry oznaczającej płeć.
            c10 = "";
            if (radioButton_woman.isChecked())
            {
                radioGroup.setBackgroundColor(0); //usuwa error border
                c10 = (random.nextInt(5) * 2) + "";
                c11 = Calculations(c1_2, c3_4, c5_6, c7, c8, c9, c10);
            }
            else if (radioButton_man.isChecked())
            {
                radioGroup.setBackgroundColor(0); //usuwa error border
                c10 = ((random.nextInt(5)+1) * 2 - 1) + "";
                c11 = Calculations(c1_2, c3_4, c5_6, c7, c8, c9, c10);
            }
            else //Jeśli nie zaznaczono płci wyświetlany jest błąd
            {
                Snackbar.make(view, "Zaznacz płeć!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
                radioGroup.setBackgroundResource(R.drawable.red_border); //error border
            }
        }
    }

    /**
     * Obliczenia dla sumy kontrolnej.
     * @param c1_2 rok
     * @param c3_4 miesiąc
     * @param c5_6 dzień
     * @param c7 losowany numer serii
     * @param c8 losowany numer serii
     * @param c9 losowany numer serii
     * @param c10 płeć
     */
    private String Calculations(String c1_2, String c3_4, String c5_6, String c7, String c8, String c9, String c10)
    {
        String c11;
        //Mnożenie pierwszych 10 cyfr przez wyznaczone mnożniki.
        int result = 1 * Integer.parseInt(c1_2.substring(0, 1)) + 3 * Integer.parseInt(c1_2.substring(1, 2))
                + 7 * Integer.parseInt(c3_4.substring(0, 1)) + 9 * Integer.parseInt(c3_4.substring(1, 2))
                + 1 * Integer.parseInt(c5_6.substring(0, 1)) + 3 * Integer.parseInt(c5_6.substring(1, 2))
                + 7 * Integer.parseInt(c7) + 9 * Integer.parseInt(c8) + 1 * Integer.parseInt(c9) + 3 * Integer.parseInt(c10);

        int x = result % 10;
        c11 = ((10 - x) % 10) + "";

        String pesel = c1_2 + c3_4 + c5_6 + c7 + c8 + c9 + c10 + c11;
        textView_result.setText(pesel);
        return c11;
    }

    /**
     * Przycisk czyści pole z numerem PESEL i zeruje ustawienia.
     */
    public void onClick_btn_Clear(View v)
    {
        textView_date.setText("");
        textView_result.setText("");
        radioGroup.clearCheck();
        radioGroup.setBackgroundColor(0);
        textView_date.setBackgroundColor(0);
    }
}