package com.atlassian.jira.jsw.sample.dataprovider;

import com.atlassian.connect.spring.AtlassianHostRestClients;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
class JwtInterceptor implements Interceptor {

    private final AtlassianHostRestClients atlassianHostRestClients;

    JwtInterceptor(AtlassianHostRestClients atlassianHostRestClients) {
        this.atlassianHostRestClients = atlassianHostRestClients;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request jwtRequest = requestWithJwtHeader(originalRequest);
        return chain.proceed(jwtRequest);
    }

    private Request requestWithJwtHeader(Request request) {
        return request.newBuilder()
                .header("Authorization", jwtHeaderValue(request))
                .method(request.method(), request.body())
                .build();
    }

    private String jwtHeaderValue(Request originalRequest) {
        String jwt = createJwt(originalRequest);
        return String.format("JWT %s", jwt);
    }

    private String createJwt(Request request) {
        HttpMethod method = HttpMethod.resolve(request.method());
        URI uri = request.url().uri();
        return atlassianHostRestClients.createJwt(method, uri);
    }
}
