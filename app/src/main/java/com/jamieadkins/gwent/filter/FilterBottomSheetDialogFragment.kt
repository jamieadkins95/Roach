package com.jamieadkins.gwent.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.DaggerSupportDialogFragment
import kotlinx.android.synthetic.main.fragment_filter.*
import javax.inject.Inject

class FilterBottomSheetDialogFragment : DaggerSupportDialogFragment(), FilterContract.View {

    @Inject lateinit var presenter: FilterContract.Presenter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), this.theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach()
        btnApply.setOnClickListener { presenter.applyFilters() }
        btnReset.setOnClickListener { presenter.resetFilters() }

        filter_faction_ng.setOnCheckedChangeListener { _, checked -> presenter.onNilfgaardFilterChanged(checked) }
        filter_faction_mon.setOnCheckedChangeListener { _, checked -> presenter.onMonsterFilterChanged(checked) }
        filter_faction_nr.setOnCheckedChangeListener { _, checked -> presenter.onNorthernRealmsFilterChanged(checked) }
        filter_faction_sk.setOnCheckedChangeListener { _, checked -> presenter.onSkelligeFilterChanged(checked) }
        filter_faction_sc.setOnCheckedChangeListener { _, checked -> presenter.onScoiataelFilterChanged(checked) }
        filter_faction_ne.setOnCheckedChangeListener { _, checked -> presenter.onNeutralFilterChanged(checked) }
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun setNilfgaardFilter(checked: Boolean) { filter_faction_ng.isChecked = checked }

    override fun setMonsterFilter(checked: Boolean) { filter_faction_mon.isChecked = checked }

    override fun setNorthernRealmsFilter(checked: Boolean) { filter_faction_nr.isChecked = checked }

    override fun setScoiataelFilter(checked: Boolean) { filter_faction_sc.isChecked = checked }

    override fun setSkelligeFilter(checked: Boolean) { filter_faction_sk.isChecked = checked }

    override fun setNeutralFilter(checked: Boolean) { filter_faction_ne.isChecked = checked }

    override fun close() = dismiss()
}
