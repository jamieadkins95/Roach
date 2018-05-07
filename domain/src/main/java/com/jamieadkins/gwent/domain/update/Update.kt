package com.jamieadkins.gwent.domain.update

sealed class Update {

    object UpToDate : Update()

    class Available(val patchName: String) : Update()

}