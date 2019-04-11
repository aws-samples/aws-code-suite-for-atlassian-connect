package com.atlassian.jira.jsw.sample.dataprovider;

import com.atlassian.connect.spring.IgnoreJwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.atlassian.jira.jsw.sample.dataprovider.Utils.buildUrlWithQueryParams;
import static java.util.Objects.requireNonNull;

/**
 * This endpoint will proxy ANY requests to Jira. It will proxy requests like /proxy/A/B/C?baseUrl=https://jira.instance.com/
 * to https://jira.instance.com/A/B/C with all query parameters(except baseUrl) and content.
 * <p>
 * Special query parameter hostBaseUrl=X will be used to determine Jira host
 */
@RequestMapping("/proxy/**")
@RestController
@IgnoreJwt
public class ProxyController {

    private static final Logger log = LoggerFactory.getLogger(ProxyController.class);

    public static final String PROXY_PATH_PREFIX = "/proxy/";
    Pattern regexUrlPathPattern = Pattern.compile(PROXY_PATH_PREFIX + "(.*)");

    private final WhitelistService whitelistService;
    private final HttpCommonOperations httpCommonOperations;

    @Autowired
    public ProxyController(final WhitelistService whitelistService,
                           final HttpCommonOperations commonOperations) {
        this.whitelistService = requireNonNull(whitelistService);
        this.httpCommonOperations = requireNonNull(commonOperations);
    }

    @GetMapping
    void get(@RequestParam String hostBaseUrl,
             HttpServletRequest httpRequest,
             HttpServletResponse httpResponse) throws IOException {

        final String proxyPath = extractActualRequestPathPath(httpRequest);
        if (!whitelistService.isAllowed(proxyPath)) {
            httpResponse.sendError(403, "Forbidden");
            return;
        }

        final String requestUrlWithQueryParams = buildUrlWithQueryParams(hostBaseUrl, httpRequest, proxyPath);
        httpCommonOperations.get(httpResponse, requestUrlWithQueryParams);
    }

    @DeleteMapping
    void delete(@RequestParam String hostBaseUrl,
                HttpServletRequest httpRequest,
                HttpServletResponse httpResponse) throws IOException {

        final String proxyPath = extractActualRequestPathPath(httpRequest);
        if (!whitelistService.isAllowed(proxyPath)) {
            httpResponse.sendError(403, "Forbidden");
            return;
        }

        final String requestUrlWithQueryParams = buildUrlWithQueryParams(hostBaseUrl, httpRequest, proxyPath);
        httpCommonOperations.delete(httpResponse, requestUrlWithQueryParams);
    }

    @PostMapping
    void post(@RequestParam String hostBaseUrl,
              HttpServletRequest httpRequest,
              HttpServletResponse httpResponse) throws IOException {

        final String proxyPath = extractActualRequestPathPath(httpRequest);
        if (!whitelistService.isAllowed(proxyPath)) {
            httpResponse.sendError(403, "Forbidden");
            return;
        }

        final String requestUrlWithQueryParams = buildUrlWithQueryParams(hostBaseUrl, httpRequest, proxyPath);
        httpCommonOperations.post(httpRequest, httpResponse, requestUrlWithQueryParams);
    }

    private String extractActualRequestPathPath(final HttpServletRequest httpRequest) {
        final String contextPath = httpRequest.getServletPath();

        final Matcher pathMatcher = regexUrlPathPattern.matcher(contextPath);
        final boolean pathParseResult = pathMatcher.find();
        if (!pathParseResult) {
            throw new RuntimeException("Internal Server Error. Can't parse URL path");
        }
        return "/" + pathMatcher.group(1);
    }

}
