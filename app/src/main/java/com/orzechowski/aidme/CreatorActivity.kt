package com.orzechowski.aidme

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.orzechowski.aidme.creator.*
import com.orzechowski.aidme.tutorial.instructions.database.InstructionSet
import com.orzechowski.aidme.tutorial.mediaplayer.multimedia.database.Multimedia
import com.orzechowski.aidme.tutorial.mediaplayer.sound.database.TutorialSound
import com.orzechowski.aidme.tutorial.version.database.Version

class CreatorActivity : AppCompatActivity(R.layout.activity_creator),
    VersionTreeComposer.CallbackToActivity
{
    private val mInstructionComposer = InstructionComposer()
    private val mMultimediaComposer = MultimediaComposer()
    private val mVersionComposer = VersionComposer()
    private val mSoundComposer = SoundComposer()
    private lateinit var mVersionTreeComposer: VersionTreeComposer
    private lateinit var mVersionInstructionComposer: VersionInstructionComposer
    private lateinit var mVersions: List<Version>
    private lateinit var mMultimedias: List<Multimedia>
    private lateinit var mInstructions: List<InstructionSet>
    private lateinit var mSounds: List<TutorialSound>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        val progressButton = findViewById<Button>(R.id.creator_step_one_button)
        progressButton.setOnClickListener {
            mVersions = mVersionComposer.versions
            mMultimedias = mMultimediaComposer.multimedias
            mInstructions = mInstructionComposer.instructions
            mSounds = mSoundComposer.sounds
            supportFragmentManager.beginTransaction().remove(mInstructionComposer).commit()
            supportFragmentManager.beginTransaction().remove(mMultimediaComposer).commit()
            supportFragmentManager.beginTransaction().remove(mVersionComposer).commit()
            supportFragmentManager.beginTransaction().remove(mSoundComposer).commit()
            progressButton.visibility = View.GONE
            mVersionTreeComposer = VersionTreeComposer(mVersions, this)
            supportFragmentManager.commit {
                add(R.id.layout_version_tree, mVersionTreeComposer)
            }
        }
        supportFragmentManager.commit {
            add(R.id.layout_version_creator, mVersionComposer)
            add(R.id.layout_instruction_creator, mInstructionComposer)
            add(R.id.layout_multimedia_creator, mMultimediaComposer)
            add(R.id.layout_sound_creator, mSoundComposer)
        }
    }

    override fun callback(versions: MutableList<Version>)
    {
        mVersions = versions
        supportFragmentManager.beginTransaction().remove(mVersionTreeComposer).commit()
        supportFragmentManager.commit {
            add(R.id.layout_version_instruction, mVersionInstructionComposer)
        }
    }
}
