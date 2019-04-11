package com.atlassian.jira.jsw.sample.dataprovider;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.atlassian.jira.jsw.sample.dataprovider.Utils.buildQueryString;
import static com.atlassian.jira.jsw.sample.dataprovider.Utils.buildUrl;
import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void empty_map() {
        final Map<String, String[]> input = new HashMap<>();

        final String result = buildQueryString(input);

        assertEquals(result, "");
    }

    @Test
    public void single_map_single_value() {
        final Map<String, String[]> input = new HashMap<>();
        input.put("k1", new String[]{"v1"});

        final String result = buildQueryString(input);

        assertEquals(result, "?k1=v1");
    }

    @Test
    public void single_map_double_value() {
        final Map<String, String[]> input = new HashMap<>();
        input.put("k1", new String[]{"v1", "v2"});

        final String result = buildQueryString(input);

        assertEquals(result, "?k1=v1&k1=v2");
    }

    @Test
    public void double_map_single_value() {
        final Map<String, String[]> input = new HashMap<>();
        input.put("k1", new String[]{"v1"});
        input.put("k2", new String[]{"v2"});

        final String result = buildQueryString(input);

        assertEquals(result, "?k1=v1&k2=v2");
    }

    @Test
    public void double_map_double_value() {
        final Map<String, String[]> input = new HashMap<>();
        input.put("k1", new String[]{"v1", "v2"});
        input.put("k2", new String[]{"v3", "v4"});

        final String result = buildQueryString(input);

        assertEquals(result, "?k1=v1&k1=v2&k2=v3&k2=v4");
    }

    @Test
    public void urlConstructTest1() {
        final String base = "https://base.example.com";
        final String path = "/path/example";
        final String result = buildUrl(base, path);
        assertEquals("https://base.example.com/path/example", result);
    }

    @Test
    public void urlConstructTest2() {
        final String base = "https://base.example.com/";
        final String path = "/path/example";
        final String result = buildUrl(base, path);
        assertEquals("https://base.example.com/path/example", result);
    }

    @Test
    public void urlConstructTest3() {
        final String base = "https://base.example.com";
        final String path = "path/example";
        final String result = buildUrl(base, path);
        assertEquals("https://base.example.com/path/example", result);
    }

    @Test
    public void urlConstructTest4() {
        final String base = "https://base.example.com/";
        final String path = "path/example";
        final String result = buildUrl(base, path);
        assertEquals("https://base.example.com/path/example", result);
    }
}