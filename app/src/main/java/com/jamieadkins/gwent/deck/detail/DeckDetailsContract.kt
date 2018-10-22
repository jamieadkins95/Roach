package com.jamieadkins.gwent.deck.detail


interface DeckDetailsContract {
    interface DeckDetailsView : DeckBuilderContract.View {
        fun onLeaderChanged(newLeaderId: String)
    }

    interface Presenter : DeckBuilderContract.Presenter
}
