package com.jamieadkins.gwent.main

import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.ScrollToTopEvent
import com.jamieadkins.gwent.card.list.CardDatabaseFragment
import com.jamieadkins.gwent.collection.CollectionPlaceholderFragment
import com.jamieadkins.gwent.deck.list.DeckListPlaceholderFragment
import com.jamieadkins.gwent.settings.GwentFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var newsItemShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            newsItemShown = savedInstanceState.getBoolean(STATE_NEWS_SHOWN, false)
        }

        checkIntentForNews()

        if (savedInstanceState == null) {
            // Cold start, launch card db fragment.
            val fragment = CardDatabaseFragment()
            launchFragment(fragment, fragment.javaClass
                .simpleName)
        }

        navigation.setOnNavigationItemSelectedListener { item ->
            val fragment = getFragmentForMenuItem(item.itemId)
            launchFragment(fragment, fragment.javaClass
                .simpleName)
            true
        }

        navigation.setOnNavigationItemReselectedListener { RxBus.post(ScrollToTopEvent()) }
    }

    private fun checkIntentForNews() {
        val intent = intent
        if (intent.extras != null) {
            val url = intent.extras!!
                .getString("url")
            if (url != null && !newsItemShown) {
                showChromeCustomTab(url)
                newsItemShown = true
            }
        }
    }

    private fun getFragmentForMenuItem(itemId: Int): Fragment {
        return when (itemId) {
            R.id.navigation_card_db -> CardDatabaseFragment()
            R.id.navigation_collection -> CollectionPlaceholderFragment()
            R.id.navigation_decks -> DeckListPlaceholderFragment()
            R.id.navigation_gwent -> GwentFragment()
            else -> CardDatabaseFragment()
        }
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.contentContainer, fragment, tag)
        fragmentTransaction.commit()

        // Our options menu will be different for different tabs.
        invalidateOptionsMenu()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_NEWS_SHOWN, newsItemShown)
        super.onSaveInstanceState(outState)
    }

    private fun showChromeCustomTab(url: String) {
        CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.gwentGreen))
            .setShowTitle(true)
            .build()
            .launchUrl(this, Uri.parse(url))
    }

    companion object {
        private val STATE_NEWS_SHOWN = "com.jamieadkins.gwent.news.shown"
    }
}
