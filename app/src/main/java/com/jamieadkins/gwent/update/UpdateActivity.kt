package com.jamieadkins.gwent.update

import android.os.Bundle
import android.widget.TextView
import com.jamieadkins.commonutils.mvp2.MvpActivity
import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R

class UpdateActivity : MvpActivity<UpdateContract.View>(), UpdateContract.View {

    lateinit var tvText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        tvText = findViewById(R.id.text)
    }

    override fun setupPresenter() {
        presenter = UpdatePresenter(Injection.provideSchedulerProvider(), Injection.provideUpdateRepository())
    }

    override fun openCardDatabase() {
        finish()
    }
}
