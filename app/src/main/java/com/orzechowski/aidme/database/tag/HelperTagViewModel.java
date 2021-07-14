package com.orzechowski.aidme.database.tag;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelperTagViewModel extends AndroidViewModel
{
    private final HelperTagRepository mRepository;

    public HelperTagViewModel(@NonNull @NotNull Application application)
    {
        super(application);
        mRepository = new HelperTagRepository(application);
    }

    public void deleteAll()
    {
        mRepository.deleteAll();
    }

    public void insert(HelperTag helperTag)
    {
        mRepository.insert(helperTag);
    }

    public void delete(HelperTag helperTag)
    {
        mRepository.delete(helperTag);
    }

    public void update(HelperTag helperTag)
    {
        mRepository.update(helperTag);
    }

    public LiveData<List<HelperTag>> getByHelperId(long helperId)
    {
        return mRepository.getByHelperId(helperId);
    }

    public LiveData<List<HelperTag>> getByTagId(long tagId)
    {
        return mRepository.getByTagId(tagId);
    }
}
