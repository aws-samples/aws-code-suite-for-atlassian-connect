package com.atlassian.jira.jsw.sample.dataprovider;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WhitelistServiceTest {

    @Test
    public void testNormalWildcardMatch() {
        boolean result = WhitelistService.match("/jira/issue/create", "/jira/*/create");
        assertTrue(result);
    }

    @Test
    public void testTerminalWildcardMatch() {
        boolean result = WhitelistService.match("/jira/issue/create", "/jira/issue/*");
        assertTrue(result);
    }

    @Test
    public void testTerminalWildcardMatchesEmplty() {
        boolean result = WhitelistService.match("/jira/issue/", "/jira/issue/*");
        assertTrue(result);
    }

    @Test
    public void testNothingMatchesWildcard() {
        boolean result = WhitelistService.match("/jira//create", "/jira/*/create");
        assertTrue(result);
    }

    @Test
    public void testExactaMatch() {
        boolean result = WhitelistService.match("/jira/issue/create", "/jira/issue/create");
        assertTrue(result);
    }

    @Test
    public void testMultipleElementsNotMatchWildcard() {
        boolean result = WhitelistService.match("/jira/issue/new/create", "/jira/*/create");
        assertFalse(result);
    }

    @Test
    public void testWrongStringNotMatch() {
        boolean result = WhitelistService.match("/confluence/issue/create", "/jira/*/create");
        assertFalse(result);
    }


    @Test
    public void testHalfStringMatchReturnsFalse() {
        boolean result = WhitelistService.match("/jira/issue/create/new", "/jira/*/create");
        assertFalse(result);
    }

    @Test
    public void testRestStringMatchReturnsFalse() {
        boolean result = WhitelistService.match("/atlassian/jira/issue/create", "/jira/*/create");
        assertFalse(result);
    }

}
