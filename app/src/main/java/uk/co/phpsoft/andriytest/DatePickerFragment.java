package uk.co.phpsoft.andriytest;
/**
 * Created by Andriy on 21/05/2017.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * DatePicker Fragment
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private int year;
    private int month;
    private int day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default values for the picker
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month,day);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        // could add only future date validator if needed
        String sYear  = String.valueOf(i);
        // month in human format - not int
        String sMonth = new DateFormatSymbols().getMonths()[i1];
        String sDay   = String.valueOf(i2);

        TextView setTime = (TextView) getActivity().findViewById(R.id.valueDate);
        setTime.setText(sDay + ' ' + sMonth + ' ' + sYear);
    }

}
