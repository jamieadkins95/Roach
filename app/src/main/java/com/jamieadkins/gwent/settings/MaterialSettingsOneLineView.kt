package com.jamieadkins.gwent.settings

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.jamieadkins.gwent.R
import kotlinx.android.synthetic.main.view_material_settings_one_line.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MaterialSettingsOneLineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_material_settings_one_line, this, true)
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

        if (listener != null) {
            val outValue = TypedValue()
            context.theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)
            setBackgroundResource(outValue.resourceId)
        } else {
            setBackgroundResource(0)
        }
    }
}