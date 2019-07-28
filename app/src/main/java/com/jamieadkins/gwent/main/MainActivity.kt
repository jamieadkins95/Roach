package com.jamieadkins.gwent.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.DaggerAndroidActivity
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.bus.ScrollToTopEvent
import com.jamieadkins.gwent.card.list.CardDatabaseFragment
import com.jamieadkins.gwent.deck.list.DeckListFragment
import com.jamieadkins.gwent.di.DaggerAppComponent
import com.jamieadkins.gwent.settings.GwentFragment
import com.jamieadkins.gwent.tracker.DeckTrackerSetupFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAndroidActivity() {

    override fun onInject() {
        DaggerAppComponent.builder()
            .core(coreComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Cold start, launch card db fragment.
            val fragment = CardDatabaseFragment()
            launchFragment(fragment, fragment.javaClass.simpleName)
        }

        navigation.setOnNavigationItemSelectedListener { item ->
            val fragment = getFragmentForMenuItem(item.itemId)
            launchFragment(fragment, fragment.javaClass.simpleName)
            true
        }

        navigation.setOnNavigationItemReselectedListener { RxBus.post(ScrollToTopEvent()) }
    }

    private fun getFragmentForMenuItem(itemId: Int): Fragment {
        return when (itemId) {
            R.id.navigation_card_db -> CardDatabaseFragment()
            R.id.navigation_decks -> DeckListFragment()
            R.id.navigation_deck_tracker -> DeckTrackerSetupFragment()
            R.id.navigation_gwent -> GwentFragment()
            else -> CardDatabaseFragment()
        }
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentContainer, fragment, tag)
        fragmentTransaction.commit()

        // Our options menu will be different for different tabs.
        invalidateOptionsMenu()
    }
}
