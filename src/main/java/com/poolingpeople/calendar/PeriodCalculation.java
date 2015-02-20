package com.poolingpeople.calendar;

import java.util.Date;

/**
 * Created by alacambra on 19.02.15.
 */
public class PeriodCalculation {

    public Date getNextAppointmentFor(Appointment appointment){

        //PARAM
        Date today = new Date();

        //ATTR
        Date startDate = appointment.getStart();

        //ATTR
        Date endDate = appointment.getEnd();

        //ATTR
        Integer period = appointment.getPeriod();

        Long milisInDay =  (24 * 3600 * 1000L);
        long slots = (today.getTime()-endDate.getTime())/(period * milisInDay);
        Long timeStamp = ++slots * (period * milisInDay) + startDate.getTime();

        Date next = new Date(timeStamp);

        return next;
    }

    public Date getPreviousAppointmentFor(Appointment appointment){

        Date today = new Date();
        Date startDate = appointment.getStart();
        Date endDate = appointment.getEnd();
        Integer period = appointment.getPeriod();

        Long milisInDay =  (24 * 3600 * 1000L);
        long slots = (today.getTime()-endDate.getTime())/(period * milisInDay);
        Long timeStamp = (slots - (period==1 ? 1 : 0)) * (period * milisInDay) + startDate.getTime();

        Date next = new Date(timeStamp);

        return next;
    }





}
