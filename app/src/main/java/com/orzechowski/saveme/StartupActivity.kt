package com.orzechowski.saveme

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.orzechowski.saveme.database.GlobalRoomDatabase
import com.orzechowski.saveme.main.RequestAPI
import kotlin.concurrent.thread

//Aktywność uruchamiająca się jedynie raz po wejściu do aplikacji. Poprzedza przejście do MainActivity
//i służy jako bufor pozwalający aplikacji rozpocząć zaciąganie danych z chmury zanim użytkownik
//zacznie wyświetlać treść. Aktywność sama się wyłącza po czterech sekundach. Klasy podlegające tej
//aktywności mieszczą się w com.orzechowski.saveme.main oraz com.orzechowski.saveme.database.
class StartupActivity : AppCompatActivity()
{
    private lateinit var mRequestAPI: RequestAPI

    override fun onCreate(savedInstanceState: Bundle?)
    {
        GlobalRoomDatabase.getDatabase(applicationContext)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_startup)
        mRequestAPI = RequestAPI(this).also { it.requestData(cacheDir) }
        val progressOne = findViewById<View>(R.id.main_loading_progress_1)
        val progressTwo = findViewById<View>(R.id.main_loading_progress_2)
        val progressThree = findViewById<View>(R.id.main_loading_progress_3)
        thread {
            var timeout = 0
            try {
                while(!mRequestAPI.completed() && timeout < 10) {
                    Thread.sleep(300)
                    runOnUiThread {
                        progressOne.visibility = View.VISIBLE
                    }
                    Thread.sleep(300)
                    runOnUiThread {
                        progressTwo.visibility = View.VISIBLE
                    }
                    Thread.sleep(300)
                    runOnUiThread {
                        progressThree.visibility = View.VISIBLE
                    }
                    Thread.sleep(300)
                    runOnUiThread {
                        progressOne.visibility = View.INVISIBLE
                        progressTwo.visibility = View.INVISIBLE
                        progressThree.visibility = View.INVISIBLE
                    }
                    timeout++
                }
                runOnUiThread {
                    startActivity(Intent(this@StartupActivity,
                        MainActivity::class.java))
                }
            } catch (ignored: InterruptedException) {}
        }
    }
}
