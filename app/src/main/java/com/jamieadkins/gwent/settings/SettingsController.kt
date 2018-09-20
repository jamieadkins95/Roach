package com.jamieadkins.gwent.settings

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.jamieadkins.gwent.R

class SettingsController : EpoxyController() {

    @AutoModel lateinit var news: MaterialSettingsOneLineViewModel_
    @AutoModel lateinit var esports: MaterialSettingsOneLineViewModel_
    @AutoModel lateinit var forums: MaterialSettingsOneLineViewModel_
    @AutoModel lateinit var reddit: MaterialSettingsOneLineViewModel_
    @AutoModel lateinit var discord: MaterialSettingsOneLineViewModel_
    @AutoModel lateinit var twitch: MaterialSettingsOneLineViewModel_
    @AutoModel lateinit var youtube: MaterialSettingsOneLineViewModel_

    var listener: SettingsNavigationCallback? = null

    override fun buildModels() {

        news.title(R.string.news)
            .icon(R.drawable.ic_news)
            .clickListener { _ -> listener?.onNewsClicked() }
            .addTo(this)

        esports.title(R.string.esports)
            .icon(R.drawable.ic_swords)
            .clickListener { _ -> listener?.onEsportsClicked() }
            .addTo(this)

        forums.title(R.string.forums)
            .icon(R.drawable.ic_forum)
            .clickListener { _ -> listener?.onForumsClicked() }
            .addTo(this)

        reddit.title(R.string.reddit)
            .icon(R.drawable.ic_reddit)
            .clickListener { _ -> listener?.onRedditClicked() }
            .addTo(this)

        discord.title(R.string.discord)
            .icon(R.drawable.ic_discord)
            .clickListener { _ -> listener?.onDiscordClicked() }
            .addTo(this)

        twitch.title(R.string.twitch)
            .icon(R.drawable.ic_twitch)
            .clickListener { _ -> listener?.onTwitchClicked() }
            .addTo(this)

        youtube.title(R.string.youtube)
            .icon(R.drawable.ic_youtube)
            .clickListener { _ -> listener?.onYoutubeClicked() }
            .addTo(this)
    }

    interface SettingsNavigationCallback {

        fun onNewsClicked()

        fun onEsportsClicked()

        fun onForumsClicked()

        fun onRedditClicked()

        fun onDiscordClicked()

        fun onTwitchClicked()

        fun onYoutubeClicked()
    }
}