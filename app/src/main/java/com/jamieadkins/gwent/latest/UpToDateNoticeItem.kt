package com.jamieadkins.gwent.latest

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_up_to_date_notice.*

data class UpToDateNoticeItem(
    @DrawableRes private val icon: Int,
    @ColorRes private val iconTint: Int,
    @StringRes private val notice: Int,
    private val patch: String
) : Item(notice.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_up_to_date_notice

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = viewHolder.title.resources.getString(notice, patch)
        viewHolder.icon.setImageResource(icon)
        viewHolder.icon.setColorFilter(ContextCompat.getColor(viewHolder.icon.context, iconTint), android.graphics.PorterDuff.Mode.SRC_IN)
    }
}