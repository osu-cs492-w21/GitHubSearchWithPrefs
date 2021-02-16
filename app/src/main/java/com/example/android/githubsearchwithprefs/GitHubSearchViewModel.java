package com.example.android.githubsearchwithprefs;

import com.example.android.githubsearchwithprefs.data.GitHubRepo;
import com.example.android.githubsearchwithprefs.data.GitHubSearchRepository;
import com.example.android.githubsearchwithprefs.data.LoadingStatus;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class GitHubSearchViewModel extends ViewModel {
    private LiveData<List<GitHubRepo>> searchResults;
    private LiveData<LoadingStatus> loadingStatus;
    private GitHubSearchRepository repository;

    public GitHubSearchViewModel() {
        this.repository = new GitHubSearchRepository();
        this.searchResults = this.repository.getSearchResults();
        this.loadingStatus = this.repository.getLoadingStatus();
    }

    public void loadSearchResults(String query) {
        this.repository.loadSearchResults(query);
    }

    public LiveData<List<GitHubRepo>> getSearchResults() {
        return this.searchResults;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }
}
