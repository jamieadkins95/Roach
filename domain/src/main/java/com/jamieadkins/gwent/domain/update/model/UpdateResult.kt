package com.jamieadkins.gwent.domain.update.model

sealed class UpdateResult {

    object LanguageSetup : UpdateResult()

    object NotificationsSetup : UpdateResult()

    class CardDatabaseUpdate(val progress: Int) : UpdateResult()

    class KeywordsUpdate(val progress: Int) : UpdateResult()

    class CategoriesUpdate(val progress: Int) : UpdateResult()

    class Failure(val message: String) : UpdateResult()
}