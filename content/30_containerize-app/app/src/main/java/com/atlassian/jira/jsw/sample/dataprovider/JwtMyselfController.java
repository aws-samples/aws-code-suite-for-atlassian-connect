package com.atlassian.jira.jsw.sample.dataprovider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Retrofit;

import java.io.IOException;

@RestController
public class JwtMyselfController {

    private final RetrofitFactory retrofitFactory;

    JwtMyselfController(RetrofitFactory retrofitFactory) {
        this.retrofitFactory = retrofitFactory;
    }

    @GetMapping("/jwt-myself")
    User getMyself(@RequestParam String hostBaseUrl) throws IOException {
        Retrofit retrofit = retrofitFactory.jwtRetrofit(hostBaseUrl);
        MyselfClient myselfClient = retrofit.create(MyselfClient.class);
        return myselfClient.getMyself().execute().body();
    }

}

