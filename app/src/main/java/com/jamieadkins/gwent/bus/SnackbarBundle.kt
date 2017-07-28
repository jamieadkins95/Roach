package com.jamieadkins.gwent.bus

import android.view.View

class SnackbarBundle(val message: String, val actionMessage: String?, val action: View.OnClickListener?) {
    constructor(message: String) : this(message, null, null)
}