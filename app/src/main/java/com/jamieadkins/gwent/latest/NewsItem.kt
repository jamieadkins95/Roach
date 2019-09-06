package com.jamieadkins.gwent.latest

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.latest.GwentNewsArticle
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_news_hero.*

data class NewsItem(
    val news: GwentNewsArticle
): Item(news.id) {

    override fun getLayout(): Int = R.layout.view_news_hero

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = news.title
        Glide.with(viewHolder.image.context)
            .load(news.thumbnail)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(viewHolder.image)
    }
}