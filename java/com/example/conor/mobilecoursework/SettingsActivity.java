package com.example.conor.mobilecoursework;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

// Activity to model the settings page
public class SettingsActivity extends AppCompatActivity {

    // Variable declarations for global variables.
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private Spinner colourSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Add the font spinner and allow the user to change their font size, adapted from a string array in the strings.xml resource file.
        Spinner fontSpinner = findViewById(R.id.fontSizeSpinner);
        ArrayAdapter<CharSequence> fontSizeAdapter = ArrayAdapter.createFromResource(this, R.array.font_sizes, android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(fontSizeAdapter);


        // Add the colour spinner to allow the user to change their font colour, adapted from a string array in the strings.xml resource file.
        colourSpinner = findViewById(R.id.fontColourSpinner);
        ArrayAdapter<CharSequence> fontColourAdapter = ArrayAdapter.createFromResource(this, R.array.font_colours, android.R.layout.simple_spinner_dropdown_item);
        colourSpinner.setAdapter(fontColourAdapter);

        //
        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        // Set selection value on create to be equal to what the user last chose.
        colourSpinner.setSelection(preferences.getInt("colour_position", -1));
        fontSpinner.setSelection(preferences.getInt("font_position", -1));
        preferencesEditor.putString("font_colour", colourSpinner.getSelectedItem().toString());
        preferencesEditor.putString("font_size", fontSpinner.getSelectedItem().toString());
        preferencesEditor.commit();
        setCustomTheme(); // Call set theme method.
    }

    public void applyChanges(View view) {
        // Create preferences for storing the setting data

        Spinner colourSpinner = findViewById(R.id.fontColourSpinner);

        // Set font colours

        // If statement to assign what value is committed to the preferences for font colour
        if (colourSpinner.getSelectedItem().equals("Green")) {
            // Set font to green
            preferencesEditor.putString("font_colour", "Green");
            preferencesEditor.commit();
        } else if (colourSpinner.getSelectedItem().equals("Blue")) {
            preferencesEditor.putString("font_colour", "Blue");
            preferencesEditor.commit();
        } else if (colourSpinner.getSelectedItem().equals("Red")) {
            preferencesEditor.putString("font_colour", "Red");
            preferencesEditor.commit();
        } else if (colourSpinner.getSelectedItem().equals("Black")) {
            preferencesEditor.putString("font_colour", "Black");
            preferencesEditor.commit();
        } else if (colourSpinner.getSelectedItem().equals("White")) {
            // Set font to white
            preferencesEditor.putString("font_colour", "White");
            preferencesEditor.commit();
        }

        // Load the font spinner object.
        Spinner fontSpinner = findViewById(R.id.fontSizeSpinner);

        // If statement to set font size.
        if (fontSpinner.getSelectedItem().equals("12sp")) {
            // Set font to 8sp
            preferencesEditor.putString("font_size", "12sp");
            preferencesEditor.commit();
        } else if (fontSpinner.getSelectedItem().equals("18sp")) {
            preferencesEditor.putString("font_size", "18sp");
            preferencesEditor.commit();
        } else if (fontSpinner.getSelectedItem().equals("20sp")) {
            preferencesEditor.putString("font_size", "20sp");
            preferencesEditor.commit();
        }

        // Set the position for the spinner after the user has changed the values so it saves on load-up.
        int position = colourSpinner.getSelectedItemPosition();
        int pos = fontSpinner.getSelectedItemPosition();
        preferencesEditor.putInt("colour_position", position);
        preferencesEditor.putInt("font_position", pos);
        preferencesEditor.commit();

        // Alert user that settings have been saved.
        Toast.makeText(SettingsActivity.this, "Settings saved!", Toast.LENGTH_LONG).show();

        // Reload app to apply changes
        finish();
        startActivity(getIntent());
        // Set font size
    }

    // If statement to choose the theme based on the font_colour and font_size shared preference.
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
}
