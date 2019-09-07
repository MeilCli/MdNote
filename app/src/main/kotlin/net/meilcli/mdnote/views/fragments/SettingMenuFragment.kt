/*
 * Copyright (c) 2019 meil
 *
 * This file is part of MdNote.
 *
 * MdNote is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MdNote is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MdNote.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.meilcli.mdnote.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_setting_menu.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.views.adapters.ListAdapter
import net.meilcli.mdnote.views.holders.SettingMenuViewHolder

class SettingMenuFragment : ContainerChildFragment() {

    data class Item(val title: String)

    private val openSourceSoftwareLicenseItem by lazy { Item(getString(R.string.setting_menu_oss_license)) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ListAdapter({ SettingMenuViewHolder.create(it) }, ::onItemClicked)
        adapter.addAll(
            listOf(
                Item(getString(R.string.setting_menu_app_info)),
                openSourceSoftwareLicenseItem,
                Item(getString(R.string.setting_menu_privacy_policy))
            )
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        super.onViewCreated(view, savedInstanceState)
    }

    private fun onItemClicked(item: Item, @Suppress("UNUSED_PARAMETER") clickedViewId: Int) {
        when (item) {
            openSourceSoftwareLicenseItem -> addFragmentOnContainer(LibraryMenuFragment())
        }
    }
}