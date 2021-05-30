package com.orzechowski.prototyp.versionrecycler.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class VersionViewModel extends AndroidViewModel {

    private final VersionRepository mRepository;
    private final LiveData<List<Version>> mVersions;

    public VersionViewModel(@NonNull Application application){
        super(application);
        mRepository = new VersionRepository(application);
        mVersions = mRepository.getAll();
    }

    public LiveData<List<Version>> getAll(){
        return mVersions;
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void insert(Version version){
        mRepository.insert(version);
    }

    public void delete(Version version){
        mRepository.delete(version);
    }

    public void update(Version version){
        mRepository.update(version);
    }

    public LiveData<List<Version>> getByVersionId(long versionId){
        return mRepository.getByVersionId(versionId);
    }

    public LiveData<List<Version>> getByTutorialId(long tutorialId){
        return mRepository.getByTutorialId(tutorialId);
    }
}