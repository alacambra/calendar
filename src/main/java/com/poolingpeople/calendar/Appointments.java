package com.poolingpeople.calendar;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alacambra on 19.02.15.
 */
public class Appointments {
    private Set<Date> appointments;

    public Appointments() {
        appointments = new HashSet<>();

    }

    public Set<Date> getAppointments() {
        return appointments;
    }
}
