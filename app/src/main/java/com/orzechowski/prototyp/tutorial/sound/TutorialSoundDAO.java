package com.orzechowski.prototyp.tutorial.sound;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TutorialSoundDAO {

    @Insert
    void insert(TutorialSound tutorialSound);

    @Delete
    void delete(TutorialSound tutorialSound);

    @Update
    void update(TutorialSound tutorialSound);

    @Query("DELETE FROM tutorialSound")
    void deleteAll();

    @Query("SELECT * FROM tutorialSound")
    LiveData<List<TutorialSound>> getAll();

    @Query("SELECT * FROM tutorialSound WHERE tutorialId = :tutorialId")
    List<TutorialSound> getByTutorialId(Long tutorialId);
}
