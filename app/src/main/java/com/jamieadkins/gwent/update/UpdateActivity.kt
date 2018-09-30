package com.jamieadkins.gwent.update

import android.os.Bundle
import android.widget.TextView
import com.jamieadkins.gwent.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class UpdateActivity : DaggerAppCompatActivity(), UpdateContract.View {

    lateinit var tvText: TextView

    @Inject
    lateinit var presenter: UpdateContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        tvText = findViewById(R.id.text)

        presenter.onAttach()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onDetach()
    }

    override fun openCardDatabase() {
        finish()
    }
}
