package com.example.android.githubsearchwithprefs.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubService {
    @GET("search/repositories?sort=stars")
    Call<GitHubSearchResults> searchRepos(@Query("q") String query);
}
