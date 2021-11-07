package com.orzechowski.saveme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.orzechowski.saveme.tutorial.RequestMedia
import com.orzechowski.saveme.tutorial.version.TutorialLoading
import com.orzechowski.saveme.tutorial.version.VersionListAdapter
import com.orzechowski.saveme.tutorial.version.VersionRecycler
import com.orzechowski.saveme.tutorial.version.database.Version
import kotlin.properties.Delegates

//Wersja jest to aktywność wyodrębniona z poradnika, ale stanowiąca jego integralną część. W
//aktywności tej wybieramy wersję na podstawie której zapopulowana zostanie aktywność z samym
//poradnikiem po przejściu przez wszystkie kroki. Na koniec tej aktywności użytkownik może również
//poczekać na pobranie zawartości multimedialnych poradnika, bądź przejść bezpośrednio dalej,
//co może jednak zaskutkować m. in. brakiem narracji. Klasy podlegające tej aktywności znajdują się
//w com.orzechowski.saveme.tutorial.
class VersionActivity : AppCompatActivity(R.layout.activity_version),
    VersionListAdapter.ActivityCallback, VersionRecycler.ActivityCallback,
    TutorialLoading.ActivityCallback
{
    private val bundle = Bundle()
    private val mVersionRecycler = VersionRecycler(this)
    private val mTutorialLoading = TutorialLoading(this, this)
    private var mTutorialId by Delegates.notNull<Long>()
    private lateinit var mRequestMedia: RequestMedia
    private lateinit var mPickedVersion: Version

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        mTutorialId = intent.getLongExtra("tutorialId", -1L)
        bundle.putLong("tutorialId", mTutorialId)
        supportFragmentManager.commit {
            mVersionRecycler.arguments = bundle
            add(R.id.layout_version_fragment, mVersionRecycler)
        }

    }

    override fun onResume()
    {
        mRequestMedia = RequestMedia(this, mTutorialId).also { it.requestData(cacheDir) }
        super.onResume()
    }

    override fun onClick(version: Version)
    {
        if(version.hasChildren) {
            mVersionRecycler.getChildVersions(version.versionId)
        } else {
            mPickedVersion = version
            supportFragmentManager.beginTransaction().remove(mVersionRecycler).commit()
            supportFragmentManager.commit {
                add(R.id.layout_version_fragment, mTutorialLoading)
            }
        }
    }

    override fun defaultVersion(version: Version)
    {
        onClick(version)
    }

    override fun onDestroy()
    {
        mRequestMedia.end()
        super.onDestroy()
    }

    override fun callTutorial()
    {
        val intent = Intent(this@VersionActivity, TutorialActivity::class.java)
        intent.putExtra("versionId", mPickedVersion.versionId)
        intent.putExtra("tutorialId", mPickedVersion.tutorialId)
        intent.putExtra("delayGlobalSound", mPickedVersion.delayGlobalSound)
        supportFragmentManager.beginTransaction().remove(mTutorialLoading)
        startActivity(intent)
    }

    override fun onBackPressed()
    {
        mRequestMedia.end()
        if(mTutorialLoading.isAdded) mTutorialLoading.mProgressThread.interrupt()
        supportFragmentManager.beginTransaction().remove(mTutorialLoading)
        startActivity(Intent(this@VersionActivity, BrowserActivity::class.java))
    }
}