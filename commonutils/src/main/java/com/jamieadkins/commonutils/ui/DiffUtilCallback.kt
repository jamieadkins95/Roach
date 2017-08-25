package com.jamieadkins.commonutils.ui

import android.support.v7.util.DiffUtil

class DiffUtilCallback(val oldList: List<RecyclerViewItem>?, val newList: List<RecyclerViewItem>?) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList?.get(newItemPosition)?.equals(oldList?.get(oldItemPosition))!!
    }

    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList?.get(newItemPosition)?.areContentsTheSame(oldList?.get(oldItemPosition)) ?: false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}