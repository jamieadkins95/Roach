package com.jamieadkins.gwent.card.list

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.airbnb.epoxy.CallbackProp

import com.airbnb.epoxy.ModelView
import com.jamieadkins.gwent.R
import kotterknife.bindView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class UpdateAvailableView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CardView(context, attrs, defStyleAttr) {

    private val btnDownload by bindView<Button>(R.id.download)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_update, this, true)
    }

    @CallbackProp
    fun setClickListener(listener: View.OnClickListener?) {
        btnDownload.setOnClickListener(listener)
    }
}