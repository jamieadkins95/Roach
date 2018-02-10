package com.jamieadkins.gwent.card

import android.os.Parcel
import android.os.Parcelable
import com.jamieadkins.gwent.model.*

/**
 * Used to filter cards.
 */
class CardFilter() : Parcelable {
    var searchQuery: String? = null
    var rarityFilter = mutableMapOf<Rarity, Boolean>()
    var colourFilter = mutableMapOf<CardColour, Boolean>()
    var factionFilter = mutableMapOf<GwentFaction, Boolean>()
    var loyaltyFilter = mutableMapOf<Loyalty, Boolean>()

    var isCollectibleOnly = false

    private var mBaseFilter: CardFilter? = null

    init {
        initFilters()
    }

    fun clearFilters() {
        if (mBaseFilter != null) {
            for (entry in mBaseFilter!!.rarityFilter.entries) {
                rarityFilter.put(entry.key, entry.value)
            }
            for (entry in mBaseFilter!!.colourFilter.entries) {
                colourFilter.put(entry.key, entry.value)
            }
            for (entry in mBaseFilter!!.factionFilter.entries) {
                factionFilter.put(entry.key, entry.value)
            }
            for (entry in mBaseFilter!!.loyaltyFilter.entries) {
                loyaltyFilter.put(entry.key, entry.value)
            }
            isCollectibleOnly = mBaseFilter!!.isCollectibleOnly
        } else {
            initFilters()
        }
    }

    fun initFilters() {
        for (rarity in Rarity.values()) {
            rarityFilter.put(rarity, true)
        }

        for (colour in CardColour.values()) {
            colourFilter.put(colour, true)
        }

        for (faction in GwentFaction.values()) {
            factionFilter.put(faction, true)
        }

        for (loyalty in Loyalty.values()) {
            loyaltyFilter.put(loyalty, true)
        }

        isCollectibleOnly = false
    }

    fun setCurrentFilterAsBase() {
        val cardFilter = CardFilter()
        for (entry in rarityFilter.entries) {
            cardFilter.rarityFilter.put(entry.key, entry.value)
        }
        for (entry in colourFilter.entries) {
            cardFilter.colourFilter.put(entry.key, entry.value)
        }
        for (entry in factionFilter.entries) {
            cardFilter.factionFilter.put(entry.key, entry.value)
        }
        for (entry in loyaltyFilter.entries) {
            cardFilter.loyaltyFilter.put(entry.key, entry.value)
        }
        cardFilter.isCollectibleOnly = isCollectibleOnly

        mBaseFilter = cardFilter
    }

    fun doesCardMeetFilter(card: GwentCard): Boolean {
        var loyalty = (loyaltyFilter[Loyalty.LOYAL] ?: false) && (loyaltyFilter[Loyalty.DISLOYAL] ?: false)
        for (l in card.loyalties) {
            loyalty = loyalty || (loyaltyFilter[l] ?: false)
        }

        val include = !isCollectibleOnly || card.collectible
        val faction = factionFilter[card.faction] ?: false
        val rarity = rarityFilter[card.rarity] ?: false
        val colour = colourFilter[card.colour] ?: false
        return (faction && rarity && colour && loyalty && include)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(searchQuery)
        parcel.writeInt(rarityFilter.size)
        for (entry in rarityFilter.entries) {
            parcel.writeSerializable(entry.key)
            parcel.writeSerializable(entry.value)
        }
        parcel.writeInt(colourFilter.size)
        for (entry in colourFilter.entries) {
            parcel.writeSerializable(entry.key)
            parcel.writeSerializable(entry.value)
        }
        parcel.writeInt(factionFilter.size)
        for (entry in factionFilter.entries) {
            parcel.writeSerializable(entry.key)
            parcel.writeSerializable(entry.value)
        }
        parcel.writeInt(loyaltyFilter.size)
        for (entry in loyaltyFilter.entries) {
            parcel.writeSerializable(entry.key)
            parcel.writeSerializable(entry.value)
        }
        parcel.writeSerializable(isCollectibleOnly)
    }

    private constructor(parcel: Parcel) : this() {
        searchQuery = parcel.readString()
        var size = parcel.readInt()
        for (i in 0 until size) {
            val key = parcel.readSerializable() as Rarity
            val value = parcel.readSerializable() as Boolean
            rarityFilter.put(key, value)
        }
        size = parcel.readInt()
        for (i in 0 until size) {
            val key = parcel.readSerializable() as CardColour
            val value = parcel.readSerializable() as Boolean
            colourFilter.put(key, value)
        }
        size = parcel.readInt()
        for (i in 0 until size) {
            val key = parcel.readSerializable() as GwentFaction
            val value = parcel.readSerializable() as Boolean
            factionFilter.put(key, value)
        }
        size = parcel.readInt()
        for (i in 0 until size) {
            val key = parcel.readSerializable() as Loyalty
            val value = parcel.readSerializable() as Boolean
            loyaltyFilter.put(key, value)
        }
        isCollectibleOnly = parcel.readSerializable() as Boolean

    }

    override fun toString(): String {
        return "$searchQuery,$isCollectibleOnly"
    }

    companion object CREATOR : Parcelable.Creator<CardFilter> {
        override fun createFromParcel(parcel: Parcel): CardFilter {
            return CardFilter(parcel)
        }

        override fun newArray(size: Int): Array<CardFilter?> {
            return arrayOfNulls(size)
        }
    }
}
