package com.jamieadkins.gwent.bus

import android.support.design.widget.Snackbar
import android.view.View

class SnackbarBundle(val message: String, val actionMessage: String?, val action: View.OnClickListener?, val length: Int?) {
    constructor(message: String) : this(message, null, null, Snackbar.LENGTH_SHORT)
    constructor(message: String, actionMessage: String?, action: View.OnClickListener?) :
            this(message, actionMessage, action, Snackbar.LENGTH_SHORT)
    constructor(message: String, length: Int) : this(message, null, null, length)
}