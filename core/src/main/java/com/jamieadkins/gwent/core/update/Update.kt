package com.jamieadkins.gwent.core.update

sealed class Update {

    object UpToDate : Update()

    class Available(val patchName: String) : Update()

}