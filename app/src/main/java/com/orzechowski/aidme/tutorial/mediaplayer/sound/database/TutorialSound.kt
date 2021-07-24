package com.orzechowski.aidme.tutorial.mediaplayer.sound.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TutorialSound(
    @PrimaryKey val soundId: Long,
                val soundStart: Long,
                val soundLoop: Boolean,
                val interval: Long,
                val tutorialId: Long,
                val fileName: String)
