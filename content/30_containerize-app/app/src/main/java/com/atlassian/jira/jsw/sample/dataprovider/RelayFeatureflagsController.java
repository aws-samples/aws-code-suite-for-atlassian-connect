package com.atlassian.jira.jsw.sample.dataprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.atlassian.jira.jsw.sample.dataprovider.Utils.buildUrl;
import static com.atlassian.jira.jsw.sample.dataprovider.Utils.buildUrlWithQueryParams;
import static java.util.Objects.requireNonNull;

@RequestMapping("/relay/featureflags")
@RestController
class RelayFeatureflagsController {

    private static final Logger log = LoggerFactory.getLogger(RelayFeatureflagsController.class);
    private static final String API_VERSION = "0.1";

    private final HttpCommonOperations httpCommonOperations;

    RelayFeatureflagsController(HttpCommonOperations httpCommonOperations) {
        this.httpCommonOperations = requireNonNull(httpCommonOperations);
    }

    @PostMapping("/bulk")
    void bulkPost(@RequestParam String hostBaseUrl,
                  HttpServletRequest httpRequest,
                  HttpServletResponse httpResponse) throws IOException {

        final String path = buildPath("bulk");
        final String url = buildUrl(hostBaseUrl, path);

        log.info("calling get url = {}", url);
        httpCommonOperations.post(httpRequest, httpResponse, url);
    }

    @DeleteMapping("/bulkByProperties")
    void deleteBulk(@RequestParam String hostBaseUrl,
                            HttpServletRequest httpRequest,
                            HttpServletResponse httpResponse) throws IOException {

        final String path = buildPath("bulkByProperties");
        final String url = buildUrlWithQueryParams(hostBaseUrl, path, httpRequest);

        log.info("calling delete url = {}", url);
        httpCommonOperations.delete(httpResponse, url);
    }

    @GetMapping("/flag/{featureFlagId}")
    void getFlagDetails(@RequestParam String hostBaseUrl,
                    @PathVariable String featureFlagId,
                    HttpServletResponse httpResponse) throws IOException {

        final String path = buildPath("flag/" + featureFlagId);
        final String url = buildUrl(hostBaseUrl, path);


        log.info("calling get url = {}", url);
        httpCommonOperations.get(httpResponse, url);
    }

    @DeleteMapping("/flag/{featureFlagId}")
    void deleteFlagDetails(@RequestParam String hostBaseUrl,
                           @PathVariable String featureFlagId,
                           HttpServletResponse httpResponse) throws IOException {

        final String path = buildPath("flag/" + featureFlagId);
        final String url = buildUrl(hostBaseUrl, path);

        log.info("calling delete url = {}", url);
        httpCommonOperations.delete(httpResponse, url);
    }

    private static String buildPath(String operartionPath) {
        return String.format("/rest/featureflags/%s/%s", API_VERSION, operartionPath);
    }
}
