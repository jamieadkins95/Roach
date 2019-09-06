package com.jamieadkins.gwent.latest

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_material_settings_one_line.*

data class MoreItem(@StringRes val title: Int, @DrawableRes private val icon: Int): Item(title.toLong()) {

    override fun getLayout(): Int = R.layout.view_material_settings_one_line

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.icon.setImageResource(icon)
        viewHolder.title.setText(title)
    }
}