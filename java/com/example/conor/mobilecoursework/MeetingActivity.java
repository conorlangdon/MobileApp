package com.example.conor.mobilecoursework;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// A class to model the activity for meetings.
public class MeetingActivity extends AppCompatActivity {

    // Variable declarations across all methods of this class.
    private TextView meetingDate, meetingTime, meetingAttendants;
    private DatePickerDialog.OnDateSetListener meetingDateSetListener;
    private TimePickerDialog.OnTimeSetListener meetingTimeSetListener;
    private int meetingYear, meetingMonth, meetingDay, meetingHour, meetingMinute;
    private SharedPreferences preferences;
    private MeetingDBHandler dbHandler;
    private ListAdapter listAttendees;
    private String attendeeString, attendeesForMeeting = "";
    private final ArrayList<String> nonDuplicateListOfAttendees = new ArrayList<>();
    private final ArrayList<String> nonDuplicateListOfTempAttendees = new ArrayList<>();
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        // Load in the design elements for use with below code.
        meetingDate = findViewById(R.id.meetingDate);
        meetingTime = findViewById(R.id.meetingTime);
        meetingAttendants = findViewById(R.id.attendeeEntry);
        final ListView previousAttendees = findViewById(R.id.previousAttendees);

        // Load the preference manager
        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        // Load the database
        dbHandler = new MeetingDBHandler(this, null, null, 14);

        // Load the data and store it in a Cursor.
        Cursor attendees = dbHandler.getListOfMeetings();

        // Create a Set that removes all duplicate names, purpose of this is for the attendee 'quick-invite' feature to prevent repeating the same name.
        final Set<String> listOfAttendees = new HashSet<>();

        // Loop through the data and create a string for each attendee adding them to the arraylist of attendees
        while(attendees.moveToNext()){
            attendeesForMeeting = attendees.getString(2);
            if(attendeesForMeeting.equals("")) {
                continue;
            } else if (attendeesForMeeting.equals(null)) {
                continue;
            } else {
                String[] temp = attendeesForMeeting.split(", ");
                for(int i = 0; i < temp.length; i++){
                    nonDuplicateListOfAttendees.add(temp[i]);
                }
            }
        }
        // Use a set to remove duplicate values
        listOfAttendees.addAll(nonDuplicateListOfAttendees);
        // Clear the original list
        nonDuplicateListOfAttendees.clear();
        // Update the values inside the arraylist with the new values from the set ( removed all duplicates)
        nonDuplicateListOfAttendees.addAll(listOfAttendees);

        // Add the non duplicate list to the arrayadapter for use with the list view.
        listAttendees = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nonDuplicateListOfAttendees);

        // Set the adapter.
        previousAttendees.setAdapter(listAttendees);

        // Call setTheme method used for changing the font colour/size to the users preferences.
        setCustomTheme();

        // Create an on click listener for the DatePickerDialog that is used for selecting a date for the meeting.
        meetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Calendar instance.
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Use the above variables to create a DatePickerDialog instance.
                DatePickerDialog dateDialog = new DatePickerDialog(MeetingActivity.this, android.R.style.Theme_Black, meetingDateSetListener, year, month, day);

                // Show the dialog.
                dateDialog.show();
            }
        });

        // When the date is set determine what is done with the data.
        meetingDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1; // Since month starts at 0 we add 1.
                String date = dayOfMonth + "/" + month + "/" + year;
                meetingDate.setText(date);
                meetingYear = year;
                meetingMonth = month;
                meetingDay = dayOfMonth;
            }
        };

        // Do the same as DatePickerDialog but for the TimePickerDialog
        meetingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(MeetingActivity.this, meetingTimeSetListener, hour, minute, true);

                timeDialog.show();
            }
        });

        // Handle the data set by the user from the TimePickerDialog.
        meetingTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // A method that is used to make sure that the time is correctly formatted
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                meetingHour = hourOfDay;
                meetingMinute = minute;

                meetingTime.setText(hourOfDay + ":" + minute);
            }
        };

        // When a user clicks an attendee in the list view send it to the text box so they can add them to the meeting list.
        previousAttendees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String attendee = nonDuplicateListOfAttendees.get(position);
                meetingAttendants.setText(attendee);
            }
        });
    }

    // Empty constructor.
    public MeetingActivity() {
    }

    // A button to cancel the meeting creation
    public void CancelMeeting(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Method for setting the theme of the application depending on user settings.
    public void setCustomTheme() {
        if(preferences.getString("font_colour", "").equals("Green") && preferences.getString("font_size", "").equals("12sp")) {
            setTheme(R.style.greenText12sp);
        } else if(preferences.getString("font_colour", "").equals("Green") && preferences.getString("font_size", "").equals("18sp")) {
            setTheme(R.style.greenText18sp);
        } else if(preferences.getString("font_colour", "").equals("Green") && preferences.getString("font_size", "").equals("20sp")) {
            setTheme(R.style.greenText20sp);
        } else if(preferences.getString("font_colour", "").equals("Blue") && preferences.getString("font_size", "").equals("12sp")) {
            setTheme(R.style.blueText12sp);
        } else if(preferences.getString("font_colour", "").equals("Blue") && preferences.getString("font_size", "").equals("18sp")) {
            setTheme(R.style.blueText18sp);
        } else if(preferences.getString("font_colour", "").equals("Blue") && preferences.getString("font_size", "").equals("20sp")) {
            setTheme(R.style.blueText20sp);
        } else if(preferences.getString("font_colour", "").equals("Red") && preferences.getString("font_size", "").equals("12sp")) {
            setTheme(R.style.redText12sp);
        } else if(preferences.getString("font_colour", "").equals("Red") && preferences.getString("font_size", "").equals("18sp")) {
            setTheme(R.style.redText18sp);
        } else if(preferences.getString("font_colour", "").equals("Red") && preferences.getString("font_size", "").equals("20sp")) {
            setTheme(R.style.redText20sp);
        } else if(preferences.getString("font_colour", "").equals("Black") && preferences.getString("font_size", "").equals("12sp")) {
            setTheme(R.style.blackText12sp);
        } else if(preferences.getString("font_colour", "").equals("Black") && preferences.getString("font_size", "").equals("18sp")) {
            setTheme(R.style.blackText18sp);
        } else if(preferences.getString("font_colour", "").equals("Black") && preferences.getString("font_size", "").equals("20sp")) {
            setTheme(R.style.blackText20sp);
        } else if(preferences.getString("font_colour", "").equals("White") && preferences.getString("font_size", "").equals("12sp")) {
            setTheme(R.style.whiteText12sp);
        } else if(preferences.getString("font_colour", "").equals("White") && preferences.getString("font_size", "").equals("18sp")) {
            setTheme(R.style.whiteText18sp);
        } else if(preferences.getString("font_colour", "").equals("White") && preferences.getString("font_size", "").equals("20sp")) {
            setTheme(R.style.whiteText20sp);
        }
    }

    // method to add an attendee
    public void addAttendee(View view) {
        String attendees = meetingAttendants.getText().toString();
        nonDuplicateListOfTempAttendees.add(attendees);
        meetingAttendants.setText("");
    }

    // Method to construct and add a meeting to the SQLite database.
    public void CreateMeeting(View view) throws IOException {
        MeetingDBHandler dbHandler = new MeetingDBHandler(this, null, null, 11);
        EditText locationText = findViewById(R.id.locationText);
        EditText notes = findViewById(R.id.notesText);
        Geocoder gC = new Geocoder(this);
        List<Address> addressList;

        String meetingDate = Integer.toString(meetingDay) + "/" + Integer.toString(meetingMonth) + "/" + Integer.toString(meetingYear) + " " + Integer.toString(meetingHour) + ":" + Integer.toString(meetingMinute);

        addressList = gC.getFromLocationName(locationText.getText().toString(), 10);
        Address temp = addressList.get(0);

        double lat = temp.getLatitude();
        double lon = temp.getLongitude();

        String meetingNotes = notes.getText().toString();

        // Constructs a list of attendees and separates each of them by a comma.
        for(int i = 0; i < nonDuplicateListOfTempAttendees.size(); i++) {
            if (nonDuplicateListOfAttendees.size() == 1) {
                attendeeString = nonDuplicateListOfTempAttendees.get(i);
            } else {
                attendeeString = android.text.TextUtils.join(", ", nonDuplicateListOfTempAttendees);
            }
        }

        Meeting newMeeting = new Meeting(meetingDate, attendeeString, meetingNotes, lat, lon);

        // Perform check to see if the new meeting has been added to the database
        boolean insertToDatabase = dbHandler.addMeeting(newMeeting);

        // Creates a Toast to alert the user if the data was added to the database or not.
        if(insertToDatabase == true) {
            Toast.makeText(MeetingActivity.this, "Data successfully added to the database", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MeetingActivity.this, "Data was not added to the database", Toast.LENGTH_LONG).show();
        }

        // Returns the user back to the main page to see the meetings.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Clears the list of temp attendees for the next time a meeting is created.
        nonDuplicateListOfTempAttendees.clear();
    }
}
