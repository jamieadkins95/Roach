package com.jamieadkins.gwent.data.latest

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jamieadkins.gwent.domain.latest.GwentNewsArticle
import com.jamieadkins.gwent.domain.latest.NewsRepository
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): NewsRepository {

    override fun getLatestPatchNotes(locale: String): Observable<GwentNewsArticle> {
        return Observable.create { emitter ->
            val listenerRegistration = firestore
                .collection("news")
                .document(locale)
                .collection("articles")
                .whereEqualTo("isPatchNotes", true)
                .orderBy("timestamp")
                .limit(1)
                .addSnapshotListener(EventListener<QuerySnapshot> { snapshot, e ->
                    if (e != null) {
                        emitter.onError(e)
                        return@EventListener
                    }

                    val patchNotes = snapshot?.documents?.mapNotNull { doc ->
                        val result = doc.toObject(FirebaseNewsArticle::class.java)
                        GwentNewsArticle(
                            result?.id?.toLongOrNull() ?: 0L,
                            result?.title ?: "",
                            result?.image ?: "",
                            result?.url ?: ""
                        )
                    } ?: emptyList()

                    patchNotes.firstOrNull()?.let(emitter::onNext)
                })
            emitter.setCancellable { listenerRegistration.remove() }
        }
    }

    override fun getLatestNews(locale: String): Observable<List<GwentNewsArticle>> {
        return Observable.create { emitter ->
            val ref = firestore
                .collection("news")
                .document(locale)
                .collection("articles")

            val listenerRegistration = ref.orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(EventListener<QuerySnapshot> { snapshot, e ->
                    if (e != null) {
                        emitter.onError(e)
                        return@EventListener
                    }

                    val news = snapshot?.documents?.mapNotNull { doc ->
                        val result = doc.toObject(FirebaseNewsArticle::class.java)
                        GwentNewsArticle(
                            result?.id?.toLongOrNull() ?: 0L,
                            result?.title ?: "",
                            result?.image ?: "",
                            result?.url ?: ""
                        )
                    } ?: emptyList()

                    emitter.onNext(news)
                })
            emitter.setCancellable { listenerRegistration.remove() }
        }
    }


}