Meeting Manager

Meeting Class

The Meeting class is a class used to model a Meeting providing data such as:
- Location which is made of 2 doubles representing Longitude and Latitude.
- A string that stores the date/time
- A string that stores the attendees
- A string that stores the notes about the meeting which is a description of the meeting and what it is about.

MeetingDBHandler Class

The MeetingDBHandler class is a class that is used for storing the database of all meeting data. This is
to ensure that all data remains stored when the device is restarted. The class has various different methods that
are used across the application such as getDatabaseById() which returns the data at the row id that is specified.
There are also methods in this class to add meetings and attendees.

MainActivity Class

The main activity class handles the main page of the application where all the meetings are displayed to the user.
This involves loading the ListView with meeting data and providing the ability to view meeting details by clicking on
specific meetings within the ListView. The main activity class also handles the inflation of the menu which provides easier
navigation to the settings and create meeting pages.

SettingsActivity Class

This class handles the settings for the themes. The class has options for font size and font colour and can be tweaked using
drop down lists. Once selected the class will handle setting the theme and restarting the activity for them to be viewed.

MeetingActivity Class

The MeetingActivity class is a class that handles all of the features present on the meeting creation page. This includes the
time and date picker dialogs for the user to be able to set a time and date for their meeting. This class provides
meaningful notifications via Toasts to alert the user as to whether or not the meeting they have created has actually 
been created and pushed through to the database.



--Usage--

To use the application you can add a new meeting via the use of the + icon at the top of the menu. This will take you to the 
meeting creation page where you can type all of the information about your meetings. Once a meeting is added, all of the attendees that you
have added will be visible again in the "Previous Attendees" list for you to be able to invite them once again.

You can navigate to the settings menu to tweak the settings as you please for the font colour and size options. When you have
selected your choices from their respective drop down menu's you can press Apply Changes to save them (A toast will alert you to this).
After pressing apply the page will refresh and the font size/font colour will be changed across all activities.

To view a meeting in more depth simply click on a meeting entry whilst on the home screen and a dialog will appear. From here you can see 
all the details about the meeting as well as view its location on a map by clicking the map button.

