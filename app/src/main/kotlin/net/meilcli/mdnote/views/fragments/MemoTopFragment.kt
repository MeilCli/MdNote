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
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_memo_top.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.extensions.mdNoteApplication
import net.meilcli.mdnote.models.MemoItem
import net.meilcli.mdnote.presenters.MemoTopPresenter
import net.meilcli.mdnote.views.IMemoTopView
import net.meilcli.mdnote.views.adapters.ListAdapter
import net.meilcli.mdnote.views.holders.MemoNewViewHolder
import net.meilcli.mdnote.views.holders.MemoOpenViewHolder
import net.meilcli.mdnote.views.holders.MemoViewHolder

class MemoTopFragment : BaseFragment(), IMemoTopView {

    companion object {

        private const val memoNewViewType = 0
        private const val memoOpenViewType = 1
    }

    // ToDo: auto clear when onDestroyView
    private lateinit var adapter: ListAdapter<MemoViewHolder, MemoItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_memo_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ListAdapter(listOf(MemoNewViewHolderDelegate(), MemoOpenViewHolderDelegate()))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            recyclerView.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets
        }

        addTypedPresenter(MemoTopPresenter())

        super.onViewCreated(view, savedInstanceState)
    }

    private fun onItemClicked(item: MemoItem, @Suppress("UNUSED_PARAMETER") clickedViewId: Int) {
        if (item is MemoItem.New) {
            item.editor.launchNewEditor(requireActivity())
        }
    }

    override fun addAllMemoItem(items: Collection<MemoItem>) {
        adapter.addAll(items)
    }

    override fun addMemoItem(index: Int, item: MemoItem) {
        adapter.add(index, item)
    }

    override fun clearMemo() {
        adapter.clear()
    }

    private inner class MemoNewViewHolderDelegate : ListAdapter.IViewHolderDelegate<MemoViewHolder, MemoItem> {

        override val viewType = memoNewViewType

        override fun isValidViewType(position: Int): Boolean {
            return adapter[position] is MemoItem.New
        }

        override fun createViewHolder(parent: ViewGroup): MemoViewHolder {
            return MemoNewViewHolder.create(parent)
        }

        override fun onClick(element: MemoItem, clickedId: Int) {
            if (element is MemoItem.New) {
                element.editor.launchNewEditor(requireActivity())
            }
        }
    }

    private inner class MemoOpenViewHolderDelegate : ListAdapter.IViewHolderDelegate<MemoViewHolder, MemoItem> {

        override val viewType = memoOpenViewType

        override fun isValidViewType(position: Int): Boolean {
            return adapter[position] is MemoItem.Open
        }

        override fun createViewHolder(parent: ViewGroup): MemoViewHolder {
            return MemoOpenViewHolder.create(parent)
        }

        override fun onClick(element: MemoItem, clickedId: Int) {
            if (element is MemoItem.Open) {
                if (element.project.isValid(requireContext())) {
                    requireContext().mdNoteApplication()
                        .getEditorPlugins()
                        .first { it.name == element.project.editor }
                        .launchEditor(requireActivity(), element.path, element.project)
                } else {
                    Toast.makeText(requireContext(), R.string.editor_error_plugins, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}