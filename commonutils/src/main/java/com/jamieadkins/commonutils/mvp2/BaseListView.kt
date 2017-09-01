package com.jamieadkins.commonutils.mvp2

import com.jamieadkins.commonutils.ui.RecyclerViewItem

interface BaseListView : BaseView {
    fun showItems(items: List<RecyclerViewItem>)

    fun showEmptyView()
}
