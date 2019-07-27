package com.jamieadkins.gwent.launch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.main.DaggerAndroidActivity
import com.jamieadkins.gwent.main.MainActivity
import javax.inject.Inject

class LaunchActivity : DaggerAndroidActivity(), LaunchContract.View {

    @Inject lateinit var presenter: LaunchContract.Presenter

    override fun onInject() {
        DaggerLaunchComponent.builder()
            .core(coreComponent)
            .activity(this)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
    }

    override fun onStart() {
        super.onStart()
        val url = intent?.extras?.getString("url")
        if (url != null) {
            showChromeCustomTab(url)
            finish()
        } else {
            presenter.onAttach()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }

    override fun onSetupComplete() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showChromeCustomTab(url: String) {
        CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.gwentGreen))
            .setShowTitle(true)
            .build()
            .launchUrl(this, Uri.parse(url))
    }
}