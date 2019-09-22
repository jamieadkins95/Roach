package com.jamieadkins.gwent.data.latest

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.jamieadkins.gwent.domain.latest.DeckOfTheDay
import com.jamieadkins.gwent.domain.latest.DeckOfTheDayRepository
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class DeckOfTheDayRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): DeckOfTheDayRepository {

    override fun getRandomDeckOfTheDay(): Observable<DeckOfTheDay> {
        return Observable.create { emitter ->
            val listenerRegistration = firestore
                .collection("deck-of-the-day")
                .limit(1)
                .addSnapshotListener(EventListener<QuerySnapshot> { snapshot, e ->
                    if (e != null) {
                        Timber.e(e)
                        emitter.onError(e)
                        return@EventListener
                    }

                    val deck = snapshot?.documents?.mapNotNull { doc ->
                        val result = doc.toObject(DeckLibraryResponse::class.java)
                        DeckOfTheDay(
                            result?.name ?: "",
                            result?.id?.toString() ?: "",
                            result?.url ?: "",
                            result?.author ?: "",
                            result?.votes ?: 0
                        )
                    } ?: emptyList()

                    deck.firstOrNull()?.let(emitter::onNext)
                })
            emitter.setCancellable { listenerRegistration.remove() }
        }
    }
}