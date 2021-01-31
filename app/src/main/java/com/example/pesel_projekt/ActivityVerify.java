package com.example.pesel_projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class ActivityVerify extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "ActivityVerify";

    EditText eT_Pesel;
    Button btn_DatePicker, btn_Check, btn_Clear;
    TextView textView_date;
    RadioGroup radioGroup;
    RadioButton radioButton, rB_woman, rB_man;
    String day, month, year;
    DatePickerDialog.OnDateSetListener dateSetListener;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

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
                day = d + "";
                month = m + "";
                year = y + "";
            }
        };
    }

    /**
     * Odwołoanie do elementów interfejsu
     */
    private void Interface() {
        eT_Pesel = (EditText) findViewById(R.id.editText_Pesel);
        btn_DatePicker = (Button) findViewById(R.id.btn_DatePicker2);
        btn_Check = (Button) findViewById(R.id.btn_Check);
        btn_Clear = (Button) findViewById(R.id.btn_Clear);
        textView_date = (TextView) findViewById(R.id.textView_date2);
        radioGroup = findViewById(R.id.radioGroup);
        rB_woman = (RadioButton) findViewById(R.id.radioButton_woman2);
        rB_man = (RadioButton) findViewById(R.id.radioButton_man2);
        rootView = (View) findViewById(android.R.id.content).getRootView();
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
                ActivityVerify.this,
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

        Toast.makeText(ActivityVerify.this, "Wybrano: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Dodaje poszczególne cyfry PESELu do tablicy.
     * @return Zwraca numer pesel wpisany przez użytkownika.
     */
    private int[] getPesel()
    {
        int[] arrayPesel = new int[11];

        for (int i = 0; i < arrayPesel.length; i++)
        {
            arrayPesel[i] = Integer.parseInt(eT_Pesel.getText().toString().substring(i, i+1));
        }

        return arrayPesel;
    }

    /**
     * Sprawdza czy ostatnia liczba kontrolna numeru PESEL wpisanego
     * przez użytkownika jest prawidłowa.
     */
    public void verifyPesel()
    {
        //Jeśli wpisany Pesel nie ma 11 cyfr, to zwracany jest błąd.
        if (eT_Pesel.length() == 11)
        {
            eT_Pesel.setBackgroundColor(0); //usuwięcie error border

            //Do tablicy "arrayPesel" zostaje przypisany pesel wpisany przez użytkownika.
            int[] arrayPesel = getPesel();

            int result = 0;
            int[] multipliers = { 1, 3, 7, 9, 1, 3, 7, 9, 1, 3 };

            //Każdą liczbę z Peselu wymnażamy przez określony mnożnik.
            for (int i = 0; i < multipliers.length; i++)
            {
                result += multipliers[i] * arrayPesel[i];
            }

            int rest = result % 10;
            if (rest == 0)
            {
                if (arrayPesel[10] == 0)
                {
                    Toast.makeText(ActivityVerify.this, "Pesel jest poprawny!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ActivityVerify.this, "Pesel nie jest poprawny!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if (arrayPesel[10] == 10 - rest)
                {
                    Toast.makeText(ActivityVerify.this, "Pesel jest poprawny!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ActivityVerify.this, "Pesel nie jest poprawny!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            Snackbar.make(rootView, "Pesel jest za krótki!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
            eT_Pesel.setBackgroundResource(R.drawable.red_border); //error border
        }
    }

    /**
     * Porównuje datę urdodzenia wpisaną przez użytkownika
     * w numerze PESEL z wybraną przez użytkownika datą.
     */
    public void verifyBirth()
    {
        //Jeśli wpisany Pesel nie ma 11 cyfr, to zwracany jest błąd.
        if (eT_Pesel.length() == 11)
        {
            eT_Pesel.setBackgroundColor(0); //usuwięcie error border

            //Jeśli nie wybrano daty urdozenia, to zwracany jest błąd.
            if (textView_date.length() != 0)
            {
                //Data z DatePicker
                //Dzień nie musi być przerobiony na typ Integer, bo sprawdzamy tylko czy jest mniejszy od 10
                int monthDP = Integer.parseInt(month); //tutaj mogą być dodane wartości zależnie od stulecia
                int yearDP = Integer.parseInt(year); //ułatwienie do sprawdzania stulecia
                //Dodatkowo tworzone są nowe zmienne tekstowe, aby nie operować na danych z DatePicker'a
                String monthString = month;
                String dayString = day;

                //Data z numeru PESEL
                String dayPesel = eT_Pesel.getText().toString().substring(4, 6);
                String monthPesel = eT_Pesel.getText().toString().substring(2, 4);
                String yearPesel = eT_Pesel.getText().toString().substring(0, 2);

                //Jeśli wybrany dzień jest mniejszy niż 10, to trzeba dopisać mu "0"
                if (Integer.parseInt(day) < 10)
                {
                    dayString = "0" + day;
                }

                //Sprawdzenie stulecia
                if (yearDP >= 1800 && yearDP <= 1899)
                {
                    monthDP += 80;
                    monthString = monthDP + "";
                }
                else if (yearDP >= 2000 && yearDP <= 2099)
                {
                    monthDP += 20;
                    monthString = monthDP + "";
                }
                else if (yearDP >= 2100 && yearDP <= 2199)
                {
                    monthDP += 40;
                    monthString = monthDP + "";
                }
                else if (yearDP >= 2200 && yearDP <= 2299)
                {
                    monthDP += 60;
                    monthString = monthDP + "";
                }
                else if (yearDP >= 1900 && yearDP <= 1999)
                {
                    if (monthDP < 10)
                    {
                        monthString = "0" + month;
                    }
                }

                //Skrócenie roku np z 1999 na 99.
                String shortYearDP = Integer.toString(yearDP).substring(2, 4);

                if (dayString.equals(dayPesel) && monthString.equals(monthPesel) && shortYearDP.equals(yearPesel))
                {
                    Toast.makeText(ActivityVerify.this, "Data urodzenia jest prawidłowa!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ActivityVerify.this, "Data urodzenia nie jest prawidłowa!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Snackbar.make(rootView, "Wybierz datę urodzenia!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
                textView_date.setBackgroundResource(R.drawable.red_border); //error border
            }
        }
        else
        {
            Snackbar.make(rootView, "Pesel jest za krótki!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
            eT_Pesel.setBackgroundResource(R.drawable.red_border); //error border
        }
    }

    /**
     * Sprawdza czy liczba w numerze PESEL podanym przez użytkownika oznaczająca płeć jest
     * prawidłowa względem zaznaczonej przez użytkownika płci.
     */
    public void verifySex()
    {
        if (eT_Pesel.length() == 11)
        {
            eT_Pesel.setBackgroundColor(0); //usuwięcie error border

            //Do tablicy "arrayPesel" zostaje przypisany PESEL wpisany przez użytkownika.
            int[] arrayPesel = getPesel();

            //Liczba odpowiadająca za płeć zostanie przypisana do zmiennej
            int genderIndex = arrayPesel[9];

            if (!(radioGroup.getCheckedRadioButtonId() == -1))
            {
                radioGroup.setBackgroundColor(0); //usuwa error border

                if (genderIndex % 2 == 0)
                {
                    if (rB_woman.isChecked())
                    {
                        Toast.makeText(ActivityVerify.this, "Płeć jest prawidłowa!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ActivityVerify.this, "Płeć nie jest prawidłowa!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if (rB_man.isChecked())
                    {
                        Toast.makeText(ActivityVerify.this, "Płeć jest prawidłowa!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ActivityVerify.this, "Płeć nie jest prawidłowa!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Snackbar.make(rootView, "Zaznacz płeć!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
                radioGroup.setBackgroundResource(R.drawable.red_border); //error border
            }
        }
        else
        {
            Snackbar.make(rootView, "Pesel jest za krótki!", Snackbar.LENGTH_SHORT).setTextColor(Color.RED).show();
            eT_Pesel.setBackgroundResource(R.drawable.red_border); //error border
        }
    }

    /**
     * Przycisk czyści pole z numerem PESEL i zeruje ustawienia.
     */
    public void onClick_btn_Clear(View view)
    {
        eT_Pesel.setText("");
        textView_date.setText("");
        radioGroup.clearCheck();
        eT_Pesel.setBackgroundColor(0);
        textView_date.setBackgroundColor(0);
        radioGroup.setBackgroundColor(0);
    }

    /**
     * Wyświetlenie PopupMenu czyli listy rozwijanej z opcjami po wciśnięciu przycisku "Sprawdź".
     */
    public void onClick_btn_Check(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        //Przypisanie czynności odpowiednio wybranej opcji po wciśnięciu przycisku "Sprawdź".
        switch (item.getItemId())
        {
            case R.id.check_pesel:
                verifyPesel();
                return true;
            case R.id.check_birth:
                verifyBirth();
                return true;
            case R.id.check_sex:
                verifySex();
                return true;
            default:
                return false;
        }
    }
}