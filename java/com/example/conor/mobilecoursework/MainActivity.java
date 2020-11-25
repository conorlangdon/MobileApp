package com.example.conor.mobilecoursework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    MeetingDBHandler dbHandler;
    ListAdapter list;
    SharedPreferences preferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // Load preferences
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String color = preferences.getString("font_colour", "");
        System.out.println(color);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // declare variables and load database.
        final ListView meetingList = findViewById(R.id.meetingView);
        dbHandler = new MeetingDBHandler(this, null, null, 14);
        double lon;
        double lat;
        final ArrayList<String> listOfMeetings = new ArrayList<>();
        Cursor data = dbHandler.getListOfMeetings();
        final Geocoder gC = new Geocoder(this);
        List<Address> addresses;
        String locationName;

        while(data.moveToNext()) {
            lat = data.getDouble(4);
            lon = data.getDouble(5);

            try {
                // Perform lookup of address to highlight meeting name.
                addresses = gC.getFromLocation(lat, lon, 5);
                Address address = addresses.get(0);
                locationName = address.getLocality();
                listOfMeetings.add(locationName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set the list adapter equal to a new array adapter of all meetings.
            list = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfMeetings);
            meetingList.setAdapter(list);
        }

        // Adds a listener to allow the user to view individual meetings in more depth, provides a dialog.
        meetingList.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // Create an AlertDialog builder to load the custom layout.
                AlertDialog.Builder newAlert = new AlertDialog.Builder(MainActivity.this);
                View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
                newAlert.setView(alertView); // Set the view loaded from the inflater for the custom dialog layout.
                // Declare all objects on the layout for use.
                final TextView locationText = alertView.findViewById(R.id.mLocationText);
                final TextView notesText = alertView.findViewById(R.id.mNotesText);
                final TextView attendeesText = alertView.findViewById(R.id.mAttendeesText);
                final TextView dateTimeText = alertView.findViewById(R.id.mDateTimeText);

                // Store a list of addresses for the geocoder lookup.
                List<Address> address;
                String locationName = ""; // define blank string to store location in.

                // Load the database value for the current listview index.
                final Cursor rowData = dbHandler.getDatabaseById(position + 1);
                rowData.moveToFirst(); // Move to first record.
                try {
                    // Perform lookup of address from database.
                    address = gC.getFromLocation(rowData.getDouble(4), rowData.getDouble(5), 5);
                    Address addressTemp = address.get(0); // Store the first index of Address List into an Address variable.
                    locationName = addressTemp.getLocality(); // Set the locationName equal to the locality of the result of the geocoder lookup.
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Set all objects on the dialog to store the information to display to the user.
                locationText.setText(locationName);
                notesText.setText(rowData.getString(3));
                attendeesText.setText(rowData.getString(2));
                dateTimeText.setText(rowData.getString(1));

                // Apply the theme.
                setCustomTheme();
                // Provide a button to the view the location on a map.
                newAlert.setNeutralButton("Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent iMap = new Intent(MainActivity.this, MapsActivity.class);
                        iMap.putExtra("lat", rowData.getDouble(4));
                        iMap.putExtra("lon", rowData.getDouble(5));
                        startActivity(iMap);
                    }
                });

                // Create and show the dialog.
                AlertDialog newAlertDialog = newAlert.create();
                newAlertDialog.show();
            }
        });
        // Apply theme
        setCustomTheme();
    }

    // Method that updates the theme when the user presses back onto this activity from the settings page.
    public void onRestart() {
        super.onRestart();

        setCustomTheme();

        finish();
        startActivity(getIntent());
    }

    // A method called to create the menu toolbar for the application
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.toolbar, menu);
        return true;
    }

    // Set theme method from settings menu to set the theme on this activity.
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.createmeeting:
                Intent meetingIntent = new Intent(this, MeetingActivity.class);
                //meetingIntent.putExtra("names", randomNames);
                startActivity(meetingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
