package com.jamieadkins.gwent.view.card

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp

import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jamieadkins.gwent.core.GwentCardRarity
import com.jamieadkins.gwent.core.GwentFaction
import com.jamieadkins.gwent.core.GwentStringHelper
import com.jamieadkins.gwent.view.R
import kotterknife.bindView
import org.jetbrains.annotations.Nullable
import java.lang.Exception

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