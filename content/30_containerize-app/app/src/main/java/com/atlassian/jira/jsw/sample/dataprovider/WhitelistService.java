package com.atlassian.jira.jsw.sample.dataprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class WhitelistService {

    private static final Logger LOG = LoggerFactory.getLogger(WhitelistService.class);

    private final List<String> whitelist = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        final InputStream is = new ClassPathResource("proxy-whitelist.txt").getInputStream();
        final InputStreamReader sr = new InputStreamReader(is);
        final Scanner scanner = new Scanner(sr);

        while (scanner.hasNext()) {
            final String whitelistRule = scanner.next();
            whitelist.add(whitelistRule);
        }

        LOG.info("Whitelist initialised with the following rules: {}", whitelist);
    }

    /**
     * Checks if Jira Rest API call to the specified path is allowed by the whitelist
     *
     * @param path
     * @return true if path whitelisted false otherwise
     */
    public boolean isAllowed(String path) {

        return whitelist.stream()
                .map(patten -> match(path, patten))
                .filter(b -> b)
                .findFirst()
                .isPresent();
    }

    /**
     * Check if the value matches wildcard path
     *
     * @param value   path value to check
     * @param pattern path pattern which can contain wildcard * symbols
     * @return
     */
    public static boolean match(String value, String pattern) {
        return value.matches(pattern.replace("*", "[^/]*?"));
    }

}
