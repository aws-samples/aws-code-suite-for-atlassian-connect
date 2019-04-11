package com.atlassian.jira.jsw.sample.dataprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class AddonApplication extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        new SpringApplication(AddonApplication.class).run(args);
    }
}
