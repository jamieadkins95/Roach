package com.jamieadkins.gwent.model.patch

sealed class PatchState {
    class NoUpdate : PatchState()
    data class NewPatch(val name: String) : PatchState()
    data class NewVersion(val name: String, val version: Int) : PatchState()
}