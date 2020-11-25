package com.example.conor.mobilecoursework;

// Class to model a Meeting
public class Meeting {

    // Variables of a meeting
    private int columnId;
    private String date;
    private String numberOfAttendees;
    private String notes;
    private double lat;
    private double lon;

    public Meeting(String date, String numberOfAttendees, String notes, double lat, double lon) {
        this.date = date;
        this.numberOfAttendees = numberOfAttendees;
        this.notes = notes;
        this.lat = lat;
        this.lon = lon;
    }

    public void setColumnId(int id) {
        this.columnId = id;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setAttendees(String num) {
        this.numberOfAttendees = num;
    }

    public String getAttendees() {
        return numberOfAttendees;
    }

    public void setDateTime(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return date;
    }
}
