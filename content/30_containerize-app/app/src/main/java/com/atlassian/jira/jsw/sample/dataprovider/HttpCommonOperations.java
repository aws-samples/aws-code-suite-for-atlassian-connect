package com.atlassian.jira.jsw.sample.dataprovider;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.atlassian.jira.jsw.sample.dataprovider.Utils.performRequest;
import static java.util.Objects.requireNonNull;

@Component
public class HttpCommonOperations {

    private static final Logger log = LoggerFactory.getLogger(HttpCommonOperations.class);

    private final OkHttpClientFactory okHttpClientFactory;

    public HttpCommonOperations(OkHttpClientFactory okHttpClientFactory) {
        this.okHttpClientFactory = requireNonNull(okHttpClientFactory);
    }

    public void post(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String url) throws IOException {
        log.info("calling post on url = {}", url);

        final byte[] requestBytes = StreamUtils.copyToByteArray(httpRequest.getInputStream());

        final RequestBody requestBody = RequestBody.create(MediaType.parse(httpRequest.getContentType()), requestBytes);
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        performRequest(okHttpClientFactory.jwtClient(), request, httpResponse);
    }


    public void delete(HttpServletResponse httpResponse, String url) throws IOException {
        log.info("calling delete on url = {}", url);

        final Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        performRequest(okHttpClientFactory.jwtClient(), request, httpResponse);
    }

    public void get(HttpServletResponse httpResponse, String url) throws IOException {
        log.info("calling get url = {}", url);

        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        performRequest(okHttpClientFactory.jwtClient(), request, httpResponse);
    }
}
