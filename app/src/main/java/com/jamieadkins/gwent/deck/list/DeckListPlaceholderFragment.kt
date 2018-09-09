package com.jamieadkins.gwent.deck.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamieadkins.gwent.R
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_collection_placeholder.*

class DeckListPlaceholderFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_collection_placeholder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            title = getString(R.string.deck_builder)
        }

        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        title?.text = getString(R.string.deck_builder_title)
        message?.text = getString(R.string.deck_builder_message)
    }
}
