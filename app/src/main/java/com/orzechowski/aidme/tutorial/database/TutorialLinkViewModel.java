package com.orzechowski.aidme.tutorial.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TutorialLinkViewModel extends AndroidViewModel
{
    private final TutorialLinkRepository mRepository;

    public TutorialLinkViewModel(@NonNull Application application)
    {
        super(application);
        mRepository = new TutorialLinkRepository(application);
    }

    public void deleteAll()
    {
        mRepository.deleteAll();
    }

    public void insert(TutorialLink tutorialLink)
    {
        mRepository.insert(tutorialLink);
    }

    public void delete(TutorialLink tutorialLink)
    {
        mRepository.delete(tutorialLink);
    }

    public void update(TutorialLink tutorialLink)
    {
        mRepository.update(tutorialLink);
    }

    public LiveData<TutorialLink> getByOriginIdAndPosition(long tutorialId, int instructionNumber)
    {
        return mRepository.getByOriginIdAndPosition(tutorialId, instructionNumber);
    }
}
