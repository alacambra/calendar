package com.poolingpeople.calendar;

import com.poolingpeople.utils.neo4jApi.Neo4jRestApiAdapter;
import com.poolingpeople.utils.neo4jApi.Neo4jRestApiAdapterImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PeriodCalculationUT {

    PeriodCalculation cut;

    Neo4jRestApiAdapter neo4jRestApiAdapter;

    @Before
    public void setUp() throws Exception {
        cut = new PeriodCalculation();
        neo4jRestApiAdapter = new Neo4jRestApiAdapterImpl();
    }

    @Test
    public void testGetNextAppointmentFor_week() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 1);
        Date start = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 3);
        Date end = cal.getTime();

        Integer period = 7;
        Appointment appointment = new Appointment(start, end, period);

        Date next = cut.getNextAppointmentFor(appointment);
        cal.setTime(next);

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(22));
        assertThat(cal.get(Calendar.MONTH), is(1));
        assertThat(cal.get(Calendar.YEAR), is(2015));


    }

    @Test
    public void testGetNextAppointmentFor_day() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 1);
        Date start = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 1);
        Date end = cal.getTime();

        Integer period = 1;
        Appointment appointment = new Appointment(start, end, period);

        Date next = cut.getNextAppointmentFor(appointment);
        cal.setTime(next);

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(20));
        assertThat(cal.get(Calendar.MONTH), is(1));
        assertThat(cal.get(Calendar.YEAR), is(2015));


    }

    @Test
    public void testGetNextAppointmentFor_2weeks() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 1);
        Date start = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 3);
        Date end = cal.getTime();

        Integer period = 14;
        Appointment appointment = new Appointment(start, end, period);

        Date next = cut.getNextAppointmentFor(appointment);
        cal.setTime(next);

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(cal.get(Calendar.MONTH), is(2));
        assertThat(cal.get(Calendar.YEAR), is(2015));


    }

    @Test
    public void testGetNextAppointmentFor_1weeks_on_the_middle() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 3);
        Date start = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 6);
        Date end = cal.getTime();

        Integer period = 7;
        Appointment appointment = new Appointment(start, end, period);

        Date next = cut.getNextAppointmentFor(appointment);
        cal.setTime(next);

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(17));
        assertThat(cal.get(Calendar.MONTH), is(1));
        assertThat(cal.get(Calendar.YEAR), is(2015));


    }

    @Test
    public void testGetPreviousAppointmentFor_week() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 1);
        Date start = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 3);
        Date end = cal.getTime();

        Integer period = 7;
        Appointment appointment = new Appointment(start, end, period);

        Date next = cut.getPreviousAppointmentFor(appointment);
        cal.setTime(next);

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(15));
        assertThat(cal.get(Calendar.MONTH), is(1));
        assertThat(cal.get(Calendar.YEAR), is(2015));


    }

    @Test
    public void testGetPreviousAppointmentFor_day() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 1);
        Date start = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 1);
        Date end = cal.getTime();

        Integer period = 1;
        Appointment appointment = new Appointment(start, end, period);

        Date next = cut.getPreviousAppointmentFor(appointment);
        cal.setTime(next);

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(18));
        assertThat(cal.get(Calendar.MONTH), is(1));
        assertThat(cal.get(Calendar.YEAR), is(2015));


    }

    @Test
    public void testGetPreviousAppointmentFor_2weeks() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 0, 25);
        Date start = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 0, 30);
        Date end = cal.getTime();

        Integer period = 14;
        Appointment appointment = new Appointment(start, end, period);

        Date next = cut.getPreviousAppointmentFor(appointment);
        cal.setTime(next);

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(8));
        assertThat(cal.get(Calendar.MONTH), is(1));
        assertThat(cal.get(Calendar.YEAR), is(2015));


    }

    @Test
    public void testGetPreviousAppointmentFor_1weeks_on_the_middle() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 3);
        Date start = cal.getTime();

        cal = Calendar.getInstance();
        cal.set(2015, 1, 6);
        Date end = cal.getTime();

        Integer period = 7;
        Appointment appointment = new Appointment(start, end, period);

        Date next = cut.getPreviousAppointmentFor(appointment);
        cal.setTime(next);

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(10));
        assertThat(cal.get(Calendar.MONTH), is(1));
        assertThat(cal.get(Calendar.YEAR), is(2015));


    }
}