package com.jamieadkins.gwent.latest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.base.convertDpToPixel
import com.jamieadkins.gwent.domain.latest.DeckOfTheDay
import com.jamieadkins.gwent.domain.latest.GwentNewsArticle
import com.jamieadkins.gwent.settings.BasePreferenceActivity.Companion.EXTRA_PREFERENCE_LAYOUT
import com.jamieadkins.gwent.settings.BasePreferenceActivity.Companion.EXTRA_PREFERENCE_TITLE
import com.jamieadkins.gwent.settings.SettingsActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_gwent.*
import timber.log.Timber
import javax.inject.Inject

class GwentFragment : DaggerFragment(), GwentLatestContract.View {

    @Inject lateinit var presenter: GwentLatestContract.Presenter

    private val adapter = GroupAdapter<ViewHolder>()
    private val patchNotesSection = Section().apply {
        setPlaceholder(NewsHeroPlaceholderItem())
        setFooter(LatestDividerItem())
    }
    private val upToDateSection = Section()
    private val latestNewsSection = Section().apply {
        setHeader(LatestHeaderItem(R.string.latest_news))
    }
    private val deckOfTheDaySection = Section().apply {
        setHeader(LatestHeaderItem(R.string.deck_of_the_day))
        setHideWhenEmpty(true)
    }
    private val moreSection = Section().apply {
        setHeader(LatestHeaderItem(R.string.more))
        update(listOf(
            MoreItem(R.string.news, R.drawable.ic_news),
            MoreItem(R.string.esports, R.drawable.ic_swords),
            MoreItem(R.string.gwent_deck_library, R.drawable.ic_library),
            MoreItem(R.string.forums, R.drawable.ic_forum),
            MoreItem(R.string.reddit, R.drawable.ic_reddit),
            MoreItem(R.string.discord, R.drawable.ic_discord),
            MoreItem(R.string.twitch, R.drawable.ic_twitch),
            MoreItem(R.string.youtube, R.drawable.ic_youtube),
            MoreItem(R.string.about, R.drawable.ic_info)
        ))
    }

    init {
        adapter.add(patchNotesSection)
        adapter.add(upToDateSection)
        adapter.add(latestNewsSection)
        adapter.add(deckOfTheDaySection)
        adapter.add(moreSection)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.latest, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                onSettingsClicked()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gwent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            title = getString(R.string.app_name)
        }

        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        val layoutManager = LinearLayoutManager(recycler_view.context)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(VerticalSpaceItemDecoration((requireContext().convertDpToPixel(8f).toInt())))
        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is NewsHeroItem -> showChromeCustomTab(item.news.url)
                is NewsItem -> showChromeCustomTab(item.news.url)
                is MoreItem -> {
                    when (item.title) {
                        R.string.news -> onNewsClicked()
                        R.string.esports -> onEsportsClicked()
                        R.string.forums -> onForumsClicked()
                        R.string.reddit -> onRedditClicked()
                        R.string.discord -> onDiscordClicked()
                        R.string.twitch -> onTwitchClicked()
                        R.string.youtube -> onYoutubeClicked()
                        R.string.settings -> onSettingsClicked()
                        R.string.about -> onAboutClicked()
                        R.string.gwent_deck_library -> onDeckLibraryClicked()
                    }
                }
                is DeckOfTheDayItem -> showChromeCustomTab(item.deck.url)
            }
        }

        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showLatestPatchNotes(patchNotes: GwentNewsArticle) {
        patchNotesSection.update(listOf(NewsHeroItem(patchNotes)))
    }

    override fun showUpToDate(patchName: String, upToDate: Boolean) {
        val upToDateNoticeItem = if (upToDate) {
            UpToDateNoticeItem(R.drawable.ic_done, R.color.gwentGreen, R.string.up_to_date, patchName)
        } else {
            UpToDateNoticeItem(R.drawable.ic_warning, R.color.gwentAccent, R.string.not_up_to_date, patchName)
        }
        upToDateSection.update(listOf(upToDateNoticeItem))
    }

    override fun showLatestNews(news: List<GwentNewsArticle>) {
        latestNewsSection.update(news.map(::NewsItem))
    }

    override fun showLiveOnTwitch(live: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDeckOfTheDay(deck: DeckOfTheDay) {
        deckOfTheDaySection.update(listOf(DeckOfTheDayItem(deck)))
    }

    fun onNewsClicked() {
        showChromeCustomTab("https://www.playgwent.com/news")
    }

    fun onEsportsClicked() {
        showChromeCustomTab("https://masters.playgwent.com/")
    }

    fun onForumsClicked() {
        showChromeCustomTab("https://forums.cdprojektred.com/forum/en/gwent")
    }

    fun onRedditClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/r/gwent/"))
        startActivity(intent)
    }

    fun onDiscordClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/playgwent"))
        startActivity(intent)
    }

    fun onTwitchClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitch.tv/directory/game/Gwent%3A%20The%20Witcher%20Card%20Game"))
        startActivity(intent)
    }

    fun onYoutubeClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCKS8WtM7U144j4fAU4xqiCw"))
        startActivity(intent)
    }

    fun onSettingsClicked() {
        context?.let {
            val intent = Intent(it, SettingsActivity::class.java)
            intent.putExtra(EXTRA_PREFERENCE_TITLE, R.string.settings)
            intent.putExtra(EXTRA_PREFERENCE_LAYOUT, R.xml.settings)
            startActivity(intent)
        }
    }

    fun onAboutClicked() {
        context?.let {
            val intent = Intent(it, SettingsActivity::class.java)
            intent.putExtra(EXTRA_PREFERENCE_TITLE, R.string.about)
            intent.putExtra(EXTRA_PREFERENCE_LAYOUT, R.xml.about)
            startActivity(intent)
        }
    }

    private fun onDeckLibraryClicked() {
        showChromeCustomTab("https://www.playgwent.com/decks")
    }

    private fun showChromeCustomTab(url: String) {
        context?.let {
            CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(it, R.color.gwentGreen))
                .setShowTitle(true)
                .build()
                .launchUrl(context, Uri.parse(url))
        }
    }
}
