package com.jamieadkins.gwent.domain.update.repository

import com.jamieadkins.gwent.domain.update.model.Notice
import io.reactivex.Observable

interface NoticesRepository {

    fun getNotices(): Observable<List<Notice>>
}