package com.jamieadkins.gwent.data.update.repository

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.jamieadkins.gwent.data.BuildConfig
import com.jamieadkins.gwent.domain.patch.GwentPatch
import com.jamieadkins.gwent.domain.patch.PatchRepository
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class PatchRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PatchRepository {

    override fun getLatestRoachPatch(): Single<String> {
        val patchRef = firestore.collection("patch").document(BuildConfig.CARD_DATA_VERSION)
        return Single.create<String> { emitter ->
            patchRef.get()
                .addOnSuccessListener { document ->
                    val patch = document.data?.get("patch")
                    if (patch != null) {
                        emitter.onSuccess(patch.toString())
                    } else {
                        emitter.onError(NullPointerException("patch is null"))
                    }
                }
                .addOnFailureListener { exception -> emitter.onError(exception) }
        }
            .doOnError { Timber.e(it) }
    }

    override fun getLatestGwentPatch(): Observable<GwentPatch> {
        return Observable.create { emitter ->
            val patchRef = firestore.collection("patch").document("latestGwentPatch")

            val listenerRegistration = patchRef
                .addSnapshotListener(EventListener { snapshot, e ->
                    if (e != null) {
                        Timber.e(e)
                        emitter.onError(e)
                        return@EventListener
                    }

                    val patch = GwentPatch(
                        snapshot?.getString("name") ?: "",
                        snapshot?.getBoolean("upToDate") ?: false
                    )

                    emitter.onNext(patch)
                })
            emitter.setCancellable { listenerRegistration.remove() }
        }
    }
}