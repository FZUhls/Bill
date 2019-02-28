package com.example.henry.bill;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


public class onDateSetListener implements DatePickerDialog.OnDateSetListener {
    private TextView dateTextView;
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dateTextView.setText(date);
    }
}
