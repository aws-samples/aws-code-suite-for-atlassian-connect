package com.atlassian.jira.jsw.sample.dataprovider;

class User {

    private String name;
    private String emailAddress;
    // ... more fields here

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
