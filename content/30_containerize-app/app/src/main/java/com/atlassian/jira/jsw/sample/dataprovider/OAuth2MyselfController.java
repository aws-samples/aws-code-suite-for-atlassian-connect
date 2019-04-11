package com.atlassian.jira.jsw.sample.dataprovider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Retrofit;

import java.io.IOException;

@RestController
public class OAuth2MyselfController {

    private final RetrofitFactory retrofitFactory;

    OAuth2MyselfController(RetrofitFactory retrofitFactory) {
        this.retrofitFactory = retrofitFactory;
    }

    @GetMapping("/oauth2-myself")
    User getMyself(@RequestParam String hostBaseUrl, @RequestParam String userKey) throws IOException {
        Retrofit retrofit = retrofitFactory.oAuth2Retrofit(hostBaseUrl, userKey);
        MyselfClient myselfClient = retrofit.create(MyselfClient.class);
        return myselfClient.getMyself().execute().body();
    }
}
