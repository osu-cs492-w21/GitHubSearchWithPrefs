package com.example.android.githubsearchwithprefs.data;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitHubSearchRepository {
    private static final String TAG = GitHubSearchRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.github.com";

    private MutableLiveData<List<GitHubRepo>> searchResults;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private String currentQuery;

    private GitHubService gitHubService;

    public GitHubSearchRepository() {
        this.searchResults = new MutableLiveData<>();
        this.searchResults.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.gitHubService = retrofit.create(GitHubService.class);
    }

    public LiveData<List<GitHubRepo>> getSearchResults() {
        return this.searchResults;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    private boolean shouldExecuteSearch(String query) {
        return !TextUtils.equals(query, this.currentQuery)
                || this.getLoadingStatus().getValue() == LoadingStatus.ERROR;
    }

    public void loadSearchResults(String query) {
        if (this.shouldExecuteSearch(query)) {
            Log.d(TAG, "running new search for this query: " + query);
            this.currentQuery = query;
            Call<GitHubSearchResults> results = this.gitHubService.searchRepos(query);
            this.searchResults.setValue(null);
            this.loadingStatus.setValue(LoadingStatus.LOADING);
            results.enqueue(new Callback<GitHubSearchResults>() {
                @Override
                public void onResponse(Call<GitHubSearchResults> call, Response<GitHubSearchResults> response) {
                    if (response.code() == 200) {
                        searchResults.setValue(response.body().items);
                        loadingStatus.setValue(LoadingStatus.SUCCESS);
                    } else {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<GitHubSearchResults> call, Throwable t) {
                    t.printStackTrace();
                    loadingStatus.setValue(LoadingStatus.ERROR);
                }
            });
        } else {
            Log.d(TAG, "using cached search results for this query: " + query);
        }
    }
}
