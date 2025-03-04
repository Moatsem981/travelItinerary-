package com.example.travelappcw;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class BookingForm1Activity extends AppCompatActivity {

    private TextInputEditText checkInDate, checkOutDate;
    private EditText guests, specialRequests;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form1);

        // Initialize views
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        guests = findViewById(R.id.guests);
        specialRequests = findViewById(R.id.specialRequests);
        continueButton = findViewById(R.id.continueButton);

        // Retrieve USER_ID and hotel object from the intent
        String userId = getIntent().getStringExtra("USER_ID");
        Hotel hotel = getIntent().getParcelableExtra("hotel");

        // Debug logs to verify USER_ID and hotel object
        Log.d("BookingForm1Activity", "USER_ID: " + userId);
        Log.d("BookingForm1Activity", "Hotel: " + (hotel != null ? hotel.getName() : "null"));

        checkInDate.setOnClickListener(v -> showDatePicker(checkInDate));

        checkOutDate.setOnClickListener(v -> showDatePicker(checkOutDate));

        continueButton.setOnClickListener(v -> {
            String checkIn = checkInDate.getText().toString().trim();
            String checkOut = checkOutDate.getText().toString().trim();
            String numGuests = guests.getText().toString().trim();
            String requests = specialRequests.getText().toString().trim();

            if (checkIn.isEmpty() || checkOut.isEmpty() || numGuests.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Proceed to the next form
                Intent intent = new Intent(BookingForm1Activity.this, BookingForm2Activity.class);
                intent.putExtra("checkIn", checkIn);
                intent.putExtra("checkOut", checkOut);
                intent.putExtra("guests", numGuests);
                intent.putExtra("specialRequests", requests);
                intent.putExtra("hotel", hotel); // Pass the hotel object
                intent.putExtra("USER_ID", userId); // Pass the USER_ID
                startActivity(intent);
            }
        });
        Log.d("BookingForm1Activity", "USER_ID: " + userId);
        Log.d("BookingForm1Activity", "Hotel: " + (hotel != null ? hotel.getName() : "null"));
    }

    private void showDatePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editText.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}