package com.jamieadkins.gwent.settings

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.jamieadkins.gwent.R
import kotlinx.android.synthetic.main.view_settings_item.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SettingsListItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_settings_item, this, true)
        val outValue = TypedValue()
        context.theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
    }

    @TextProp
    fun setTitle(text: CharSequence) {
        title?.text = text
    }

    @ModelProp
    fun setIcon(@DrawableRes iconRes: Int) {
        icon.setImageDrawable(ContextCompat.getDrawable(icon.context, iconRes))
    }

    @CallbackProp
    fun setClickListener(listener: View.OnClickListener?) {
        setOnClickListener(listener)
    }
}