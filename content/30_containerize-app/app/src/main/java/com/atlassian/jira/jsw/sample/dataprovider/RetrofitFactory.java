package com.atlassian.jira.jsw.sample.dataprovider;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Component
class RetrofitFactory {

    private final JwtInterceptor jwtInterceptor;
    private final OAuth2Interceptor oAuth2Interceptor;

    RetrofitFactory(JwtInterceptor jwtInterceptor, OAuth2Interceptor oAuth2Interceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.oAuth2Interceptor = oAuth2Interceptor;
    }

    Retrofit jwtRetrofit(String hostBaseUrl) {
        return create(hostBaseUrl, jwtInterceptor);
    }

    Retrofit oAuth2Retrofit(String hostBaseUrl, String userKey) {
        oAuth2Interceptor.setUserKey(userKey);
        return create(hostBaseUrl, oAuth2Interceptor);
    }

    private Retrofit create(String hostBaseUrl, Interceptor interceptor) {
        return new Retrofit.Builder()
                .baseUrl(hostBaseUrl)
                .client(clientWithInterceptor(interceptor))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient clientWithInterceptor(Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }
}
