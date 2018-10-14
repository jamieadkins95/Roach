package com.jamieadkins.gwent.launch

import android.content.Intent
import android.os.Bundle
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class LaunchActivity : DaggerAppCompatActivity(), LaunchContract.View {

    @Inject lateinit var presenter: LaunchContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach()
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }

    override fun onSetupComplete() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}