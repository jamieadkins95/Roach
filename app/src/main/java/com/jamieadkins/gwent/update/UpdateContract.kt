package com.jamieadkins.gwent.update

import com.jamieadkins.gwent.model.patch.UpdateState

interface UpdateContract {
    interface View {
        fun showUpdateState(updateState: UpdateState)

        fun openCardDatabase()
    }

    interface Presenter
}
