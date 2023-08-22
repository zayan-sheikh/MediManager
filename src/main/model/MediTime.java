package model;

import org.json.JSONObject;
import persistance.Writable;


import java.util.Date;

import static java.lang.Math.floor;


// Class tracks the timing of a medication - whether it is ready to eat or not, depending on how frequently it is meant
// to be consumed.

public class MediTime implements Writable {
    private double frequency;
    private boolean readyToTake;
    private Date nextTime;

    public MediTime(double frequency) {
        this.frequency = frequency;
        this.readyToTake = true;
        this.nextTime = new Date();
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public boolean isReadyToTake() {
        return readyToTake;
    }

    public void setReadyToTake(boolean readyToTake) {
        this.readyToTake = readyToTake;
    }

    public Date getNextTime() {
        return this.nextTime;
    }

    public void setNextTime(Date when) {
        this.nextTime = when;
    }

    // MODIFIES: this
    // EFFECTS: makes readyToTake false and restarts nextTime timer using frequency
    public void resetTimer() {
        this.readyToTake = false;
        nextTime = new Date();
        nextTime.setTime((long) (nextTime.getTime() + frequency * 3600000));
//        nextTime.setHours((int) floor(frequency) + nextTime.getHours());
//        nextTime.setSeconds(((int) (round(3600 * (frequency - floor(frequency))))) + nextTime.getSeconds());
    }

    // EFFECTS: generates prompt for time remaining til next dose
    public String getDifferenceTime() {
        Date currentDate = new Date();
        Date diffDate = new Date();
        long diffTime = nextTime.getTime() - currentDate.getTime();
        double seconds = diffTime / 1000;
        double minutes = seconds / 60;
        int hours = (int) floor(minutes / 60);
        int remMinutes = (int) (minutes - 60 * hours);
        int remSeconds = (int) (seconds - 60 * floor(minutes));
        int remHour = hours;
        return hourText(remHour) + minuteText(remMinutes) + remSeconds + " second" + pluralize(remSeconds)
                + " remain until your next dose.";
    }

    // EFFECTS: generates string for minutes remaining til next dose
    private String minuteText(int remMinutes) {
        if (remMinutes > 0) {
            return remMinutes + " minute" + pluralize(remMinutes) + " and ";
        } else {
            return "";
        }
    }


    // EFFECTS: generates string for hours remaining til next dose
    private String hourText(int remHour) {
        if (remHour > 0) {
            return remHour + " hour" + pluralize(remHour) + ", ";
        } else {
            return "";
        }
    }

    // EFFECTS: pluralizes words in timer if there is more (or less) than 1 quantity
    public String pluralize(double quantity) {
        if (quantity != 1) {
            return "s";
        } else {
            return "";
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("frequency", frequency);
        json.put("readyToTake", readyToTake);
        json.put("nextTime", nextTime.getTime());

        return json;
    }
}
