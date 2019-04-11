package com.atlassian.jira.jsw.sample.dataprovider;

import retrofit2.Call;
import retrofit2.http.GET;

interface MyselfClient {

    @GET("rest/api/2/myself")
    Call<User> getMyself();
}
