package com.poolingpeople.calendar;

import java.util.Date;

/**
 * Created by alacambra on 19.02.15.
 */
public class Appointment {

    Date start;
    Date end;
    int period;
    int stepSize;

    public Appointment(Date start, Date end, int period) {
        this.start = start;
        this.period = period;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public int getPeriod() {
        return period;
    }

    public Date getEnd() {
        return end;
    }

    public int getStepSize() {
        return stepSize;
    }
}
