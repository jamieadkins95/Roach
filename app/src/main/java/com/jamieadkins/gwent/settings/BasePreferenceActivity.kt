package com.jamieadkins.gwent.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import com.jamieadkins.gwent.R
import kotlinx.android.synthetic.main.appbar_layout.*

/**
 * Shows card image and details.
 */

open class BasePreferenceActivity : AppCompatActivity() {

    private var mTitleResource: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_fragment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            mTitleResource = intent.getIntExtra(EXTRA_PREFERENCE_TITLE, R.string.app_name)

            val layout = intent.getIntExtra(EXTRA_PREFERENCE_LAYOUT, -1)
            val fragment = PreferenceFragment.newInstance(layout)

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.contentContainer, fragment, fragment.javaClass.simpleName)
                .commit()
        } else {
            mTitleResource = savedInstanceState.getInt(EXTRA_PREFERENCE_TITLE)
        }

        setTitle(mTitleResource)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Same behaviour as back button.
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(EXTRA_PREFERENCE_TITLE, mTitleResource)
        super.onSaveInstanceState(outState)
    }

    companion object {
        val EXTRA_PREFERENCE_LAYOUT = "com.jamieadkins.gwent.preference"
        val EXTRA_PREFERENCE_TITLE = "com.jamieadkins.gwent.preference.title"
    }
}
