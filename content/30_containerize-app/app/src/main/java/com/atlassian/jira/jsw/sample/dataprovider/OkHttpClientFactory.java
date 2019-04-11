package com.atlassian.jira.jsw.sample.dataprovider;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Component
class OkHttpClientFactory {

    private final JwtInterceptor jwtInterceptor;
    private final OAuth2Interceptor oAuth2Interceptor;
    private final OkHttpClient client;

    OkHttpClientFactory(JwtInterceptor jwtInterceptor, OAuth2Interceptor oAuth2Interceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.oAuth2Interceptor = oAuth2Interceptor;
        this.client = clientWithInterceptor(jwtInterceptor);
    }

    OkHttpClient jwtClient() {
        return client;
    }

    private OkHttpClient clientWithInterceptor(Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }
}
