package com.poolingpeople.calendar;

import com.poolingpeople.utils.neo4jApi.Neo4jRestApiAdapter;
import com.poolingpeople.utils.neo4jApi.Neo4jRestApiAdapterImpl;
import com.poolingpeople.utils.neo4jApi.parsing.CypherQueryProperties;
import org.jboss.weld.environment.se.Weld;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PeriodCalculationST {

    PeriodCalculation cut;
    Neo4jRestApiAdapter neo4jRestApiAdapter;

    @Before
    public void setUp() throws Exception {
        cut = new PeriodCalculation();
        neo4jRestApiAdapter = new Weld().initialize().instance().select(Neo4jRestApiAdapter.class).get();
        neo4jRestApiAdapter.runCypherQuery("MATCH (n)\n" +
                "OPTIONAL MATCH (n)-[r]-()\n" +
                "DELETE n,r", new CypherQueryProperties());
    }

    private void createEvent(long start, long end, int period){

        String query = "CREATE (n:event{event}) return count(n) as total";

        neo4jRestApiAdapter.runCypherQuery(query,
                new CypherQueryProperties()
                        .forId("event").add("start", start).add("end", end).done("period", period)
        );
    }

    private void createEvent(long start, long end, int period, int day){

        String query = "CREATE (n:event{event}) return count(n) as total";

        neo4jRestApiAdapter.runCypherQuery(query,
                new CypherQueryProperties()
                        .forId("event").add("start", start).add("end", end).add("day", day).done("period", period)
        );
    }

    private Long getNextEvent(long today){

        String query = "match (e:event) with e, e.end as end, e.period as period, (24 * 3600 * 1000) as milisInDay " +
                "with e, (" + today + "-end)/(period*milisInDay) as slots, period, milisInDay " +
                "with (slots + 1) * (period*milisInDay) + e.start as next" +
                " return next";

        Collection<Map<String, Map<String, Object>>> r = neo4jRestApiAdapter.runCypherQuery(query, new CypherQueryProperties());
        return (Long) r.iterator().next().get("next").get("next");
    }

    private List<Next> getNextEvents(long today){

        String query = "match (e:event) with e, e.end as end, e.period as period, (24 * 3600 * 1000) as milisInDay " +
                "with e, (" + today + "-end)/(period*milisInDay) as slots, period, milisInDay " +
                "with e, (slots + 1) * (period*milisInDay) + e.start as next" +
                " return next, e.start, e.day order by next limit 10";

        System.out.println(query);

        List<Map<String, Object>> r = neo4jRestApiAdapter.runParametrizedCypherQuery(query, new CypherQueryProperties());

        return r.stream()
                .map(s -> new Next(new Date((Long) s.get("e.start")), new Date((Long) s.get("next")), (Long)s.get("e.day")))
                .collect(Collectors.toList());
    }

    private Long getPreviousEvent(long today){

        String query = "match (e:event) with e, e.end as end, e.period as period, (24 * 3600 * 1000) as milisInDay " +
                "with e, (" + today + "-end)/(period*milisInDay) as slots, period, milisInDay, case when period=1 then 1 else 0 end as discount " +
                "with (slots - discount) * (period*milisInDay) + e.start as next" +
                " return next";

        Collection<Map<String, Map<String, Object>>> r = neo4jRestApiAdapter.runCypherQuery(query, new CypherQueryProperties());
        return (Long) r.iterator().next().get("next").get("next");
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

        createEvent(start.getTime(), end.getTime(), period);
        Long r = getNextEvent(today.getTime());
        cal.setTime(new Date(r));

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
        createEvent(start.getTime(), end.getTime(), period);
        Long r = getNextEvent(today.getTime());
        cal.setTime(new Date(r));

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
        createEvent(start.getTime(), end.getTime(), period);
        Long r = getNextEvent(today.getTime());
        cal.setTime(new Date(r));

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
        createEvent(start.getTime(), end.getTime(), period);
        Long r = getNextEvent(today.getTime());
        cal.setTime(new Date(r));

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
        createEvent(start.getTime(), end.getTime(), period);
        Long r = getPreviousEvent(today.getTime());
        cal.setTime(new Date(r));

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
        createEvent(start.getTime(), end.getTime(), period);
        Long r = getPreviousEvent(today.getTime());
        cal.setTime(new Date(r));

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
        createEvent(start.getTime(), end.getTime(), period);
        Long r = getPreviousEvent(today.getTime());
        cal.setTime(new Date(r));

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
        createEvent(start.getTime(), end.getTime(), period);
        Long r = getPreviousEvent(today.getTime());
        cal.setTime(new Date(r));

        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(10));
        assertThat(cal.get(Calendar.MONTH), is(1));
        assertThat(cal.get(Calendar.YEAR), is(2015));
    }

    @Test
    public void testGetNextList() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 19);
        Date today = cal.getTime();

        for (int i = 0; i<28; i++){

            cal = Calendar.getInstance();
            cal.set(2015, 1, 1+i);
            Date start = cal.getTime();

            cal = Calendar.getInstance();
            cal.set(2015, 1, 2+i);
            Date end = cal.getTime();

            Integer period = 14;

            createEvent(start.getTime(), end.getTime(), period, i+1);
        }

        List<Next> r = getNextEvents(today.getTime());

        r.stream().forEach(System.out::println);
    }

    public class Next{
        Date start;
        Date next;
        long day;

        public Next(Date start, Date next, long day) {
            this.start = start;
            this.next = next;
            this.day = day;
        }

        @Override
        public String toString() {
            return next + ":::" + start + ":::" + day;
        }
    }

}