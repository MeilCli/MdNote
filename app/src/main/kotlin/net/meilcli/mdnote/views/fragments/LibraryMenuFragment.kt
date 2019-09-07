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
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_library_menu.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.libraries.Library
import net.meilcli.mdnote.libraries.LibraryList
import net.meilcli.mdnote.views.adapters.ListAdapter
import net.meilcli.mdnote.views.holders.LibraryMenuViewHolder

class LibraryMenuFragment : ContainerChildFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ListAdapter({ LibraryMenuViewHolder.create(it) }, ::onItemClicked)
        adapter.addAll(LibraryList(mdNoteApplication.getLibraryPlugins()))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            recyclerView.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun onItemClicked(item: Library, @Suppress("UNUSED_PARAMETER") clickedViewId: Int) {
        addFragmentOnContainer(LibraryFragment.create(item))
    }
}