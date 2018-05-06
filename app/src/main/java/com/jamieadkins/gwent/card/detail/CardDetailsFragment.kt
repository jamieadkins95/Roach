package com.jamieadkins.gwent.card.detail

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp3.MvpFragment
import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.view.card.CardResourceHelper
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.core.GwentFaction
import com.jamieadkins.gwent.view.card.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.view.card.detail.CardDetailsController
import kotterknife.bindView

class CardDetailsFragment : MvpFragment<DetailContract.View>(), DetailContract.View {

    lateinit var cardId: String
    private lateinit var controller: CardDetailsController

    private val recyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val refreshLayout by bindView<SwipeRefreshLayout>(R.id.refreshContainer)
    private val imgCard by bindView<ImageView>(R.id.card_image)
    private val progress by bindView<ProgressBar>(R.id.progress)

    companion object {
        const val KEY_ID = "cardId"
    }

    override fun setupPresenter(): BasePresenter<DetailContract.View> {
        return DetailPresenter(cardId, Injection.provideCardRepository(), Injection.provideSchedulerProvider())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cardId = savedInstanceState?.getString(KEY_ID) ?: arguments?.getString(KEY_ID) ?: throw Exception("Card id not found.")
        super.onCreate(savedInstanceState)
        controller = CardDetailsController(resources)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
        }

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = controller.adapter
        val dividerItemDecoration = VerticalSpaceItemDecoration(convertDpToPixel(8f, requireContext()).toInt())
        recyclerView.addItemDecoration(dividerItemDecoration)
        refreshLayout.setColorSchemeResources(R.color.gwentAccent)
    }

    override fun showScreen(cardDetailsScreenData: CardDetailsScreenData) {
        showCard(cardDetailsScreenData.card)
        controller.setData(cardDetailsScreenData.card, cardDetailsScreenData.relatedCards)
    }

    private fun showCard(card: GwentCard) {
        (activity as? AppCompatActivity)?.title = card.name

        loadCardImage(card.cardArt?.medium)

        toolbar.setBackgroundColor(CardResourceHelper.getColorForFaction(resources, card.faction))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = CardResourceHelper.getDarkColorForFaction(resources, card.faction)
            progress.indeterminateTintList = ColorStateList.valueOf(CardResourceHelper.getColorForFaction(resources, card.faction))
        }
    }

    fun loadCardImage(imageUrl: String?) {
        imgCard.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE
        if (imageUrl != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(e: java.lang.Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                            progress.visibility = View.GONE
                            imgCard.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            progress.visibility = View.GONE
                            imgCard.visibility = View.VISIBLE
                            return false
                        }

                    })
                    .into(imgCard)
        } else {
            imgCard.setImageDrawable(null)
            progress.visibility = View.GONE
            imgCard.visibility = View.VISIBLE
        }
    }

    override fun setLoadingIndicator(loading: Boolean) {
        refreshLayout.isEnabled = loading
        refreshLayout.isRefreshing = loading
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_ID, cardId)
        super.onSaveInstanceState(outState)
    }
}