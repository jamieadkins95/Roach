package com.jamieadkins.gwent.update

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.jamieadkins.commonutils.mvp2.MvpActivity
import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.main.MainActivity
import com.jamieadkins.gwent.model.patch.UpdateState

class UpdateActivity : MvpActivity<UpdateContract.View>(), UpdateContract.View {

    private val STATE_NEWS_SHOWN = "com.jamieadkins.gwent.news.shown"
    lateinit var tvText: TextView
    private var newsItemShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            newsItemShown = savedInstanceState.getBoolean(STATE_NEWS_SHOWN, false)
        }

        setContentView(R.layout.activity_update)
        tvText = findViewById(R.id.text)

        checkIntentForNews()
    }

    override fun setupPresenter() {
        presenter = UpdatePresenter(Injection.provideSchedulerProvider(), Injection.provideUpdateRepository())
    }

    override fun openCardDatabase() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun showUpdateState(updateState: UpdateState) {
        when (updateState) {
            UpdateState.CHECKING_FOR_UPDATE -> tvText.text = getString(R.string.update_check)
            UpdateState.UPDATING_DATABASE ->  tvText.text = getString(R.string.update_database)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_NEWS_SHOWN, newsItemShown)
        super.onSaveInstanceState(outState)
    }

    private fun checkIntentForNews() {
        val url = intent?.extras?.getString("url")
        if (url != null && !newsItemShown) {
            showChromeCustomTab(url)
            newsItemShown = true
        }
    }

    private fun showChromeCustomTab(url: String) {
        CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.gwentGreen))
                .setShowTitle(true)
                .build()
                .launchUrl(this, Uri.parse(url))
    }
}
