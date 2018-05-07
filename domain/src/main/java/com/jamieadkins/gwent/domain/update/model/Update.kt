package com.jamieadkins.gwent.domain.update.model

sealed class Update {

    object UpToDate : Update()

    class Available(val patchName: String) : Update()

}