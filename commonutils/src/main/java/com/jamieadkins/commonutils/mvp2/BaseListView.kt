package com.jamieadkins.commonutils.mvp2

import com.jamieadkins.commonutils.ui.RecyclerViewItem

interface BaseListView : BaseView {
    fun showItem(item: RecyclerViewItem)

    fun onClear()
}
