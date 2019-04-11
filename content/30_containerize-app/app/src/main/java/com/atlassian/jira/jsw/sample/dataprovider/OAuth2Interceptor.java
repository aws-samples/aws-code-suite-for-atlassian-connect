package com.atlassian.jira.jsw.sample.dataprovider;

import com.atlassian.connect.spring.AtlassianHost;
import com.atlassian.connect.spring.AtlassianHostRepository;
import com.atlassian.connect.spring.AtlassianHostRestClients;
import com.atlassian.connect.spring.AtlassianHostUser;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2Interceptor implements Interceptor {

    private final AtlassianHostRepository hostRepository;
    private final AtlassianHostRestClients atlassianHostRestClients;

    private String userKey;

    OAuth2Interceptor(AtlassianHostRepository hostRepository, AtlassianHostRestClients atlassianHostRestClients) {
        this.hostRepository = hostRepository;
        this.atlassianHostRestClients = atlassianHostRestClients;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request oAuth2request = requestWithOauth2Header(originalRequest);
        return chain.proceed(oAuth2request);
    }

    private Request requestWithOauth2Header(Request request) {
        return request.newBuilder()
                .header("Authorization", oAuth2HeaderValue(request))
                .method(request.method(), request.body())
                .build();
    }

    private String oAuth2HeaderValue(Request originalRequest) {
        AtlassianHostUser hostUser = new AtlassianHostUser(getHost(originalRequest), Optional.of(userKey));
        String token = atlassianHostRestClients.authenticatedAs(hostUser).getAccessToken().getValue();
        return String.format("Bearer %s", token);
    }

    private AtlassianHost getHost(Request request) {
        HttpUrl url = request.url();
        String baseUrl = String.format("%s://%s", url.scheme(), url.host());
        return hostRepository.findFirstByBaseUrl(baseUrl).get();
    }

    void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
