package com.jamieadkins.gwent.card.detail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CardImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    fun setCardImage(imageUrl: String?) {
        Glide.with(context)
            .load(imageUrl)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(this)
    }
}