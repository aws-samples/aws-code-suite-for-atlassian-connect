package com.atlassian.jira.jsw.sample.dataprovider;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains shared utilities.
 */
class Utils {

    static String buildUrlWithQueryParams(final @RequestParam String hostBaseUrl, final String path, final HttpServletRequest httpRequest) {
        final String queryString = buildQueryParamsString(httpRequest);

        return buildUrl(hostBaseUrl, path + queryString);
    }

    static String buildQueryParamsString(final HttpServletRequest httpRequest) {
        // need to remove the hostBaseUrl parameter
        final Map<String, String[]> parameterMap = new HashMap<>(httpRequest.getParameterMap());
        parameterMap.remove("hostBaseUrl");

        return buildQueryString(parameterMap);
    }

    static String buildQueryString(Map<String, String[]> parameterMap) {
        if (parameterMap.isEmpty()) {
            return "";
        }

        final List<String> params = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

            for (String value : entry.getValue()) {
                params.add(entry.getKey() + "=" + value);
            }
        }

        return "?" + params.stream().reduce((p1, p2) -> p1 + "&" + p2).orElse("");
    }

    static void performRequest(OkHttpClient client, Request request, HttpServletResponse httpResponse) throws IOException {
        final Call call = client.newCall(request);
        try (final okhttp3.Response callResponse = call.execute()) {
            httpResponse.setStatus(callResponse.code());
            httpResponse.setContentType(callResponse.body().contentType().toString());
            StreamUtils.copy(callResponse.body().byteStream(), httpResponse.getOutputStream());
        }
    }

    static String buildUrl(String baseUrl, String path) {
        return connectPathParts(baseUrl, path);
    }

    private static String connectPathParts(final String left, final String right) {
        final String trimmedBaseUrl = left.endsWith("/") ? left.substring(0, left.length() - 1) : left;
        final String trimmedPath = right.startsWith("/") ? right.substring(1) : right;
        return trimmedBaseUrl + "/" + trimmedPath;
    }

    static String buildUrlWithQueryParams(final @RequestParam String hostBaseUrl, final HttpServletRequest httpRequest, final String path) {
        final String queryString = buildQueryParamsString(httpRequest);

        return buildUrl(hostBaseUrl, path + queryString);
    }
}
