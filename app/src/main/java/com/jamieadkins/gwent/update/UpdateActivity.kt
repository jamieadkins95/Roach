package com.jamieadkins.gwent.update

import android.os.Bundle
import android.widget.TextView
import com.jamieadkins.commonutils.mvp2.MvpActivity
import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.model.patch.UpdateState

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

    override fun showUpdateState(updateState: UpdateState) {
        when (updateState) {
            UpdateState.CHECKING_FOR_UPDATE -> tvText.text = getString(R.string.update_check)
            UpdateState.UPDATING_DATABASE ->  tvText.text = getString(R.string.update_database)
        }
    }
}
