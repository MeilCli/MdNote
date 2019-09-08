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

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_note_top.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.editors.IEditorPlugin
import net.meilcli.mdnote.extensions.mdNoteApplication
import net.meilcli.mdnote.models.NoteItem
import net.meilcli.mdnote.models.Project
import net.meilcli.mdnote.presenters.NoteTopPresenter
import net.meilcli.mdnote.views.INoteTopView
import net.meilcli.mdnote.views.adapters.ListAdapter
import net.meilcli.mdnote.views.holders.NoteNewViewHolder
import net.meilcli.mdnote.views.holders.NoteOpenViewHolder
import net.meilcli.mdnote.views.holders.NoteViewHolder
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class NoteTopFragment : BaseFragment(), INoteTopView {

    companion object {

        private const val noteNewViewType = 0
        private const val noteOpenViewType = 1
    }

    // ToDo: auto clear when onDestroyView
    private lateinit var adapter: ListAdapter<NoteViewHolder, NoteItem>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ListAdapter(listOf(NoteNewViewHolderDelegate(), NoteOpenViewHolderDelegate()))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            recyclerView.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets
        }

        addTypedPresenter(NoteTopPresenter())

        super.onViewCreated(view, savedInstanceState)
    }

    override fun addAllNoteItem(items: Collection<NoteItem>) {
        adapter.addAll(items)
    }

    override fun addNoteItem(index: Int, item: NoteItem) {
        adapter.add(index, item)
    }

    override fun clearNote() {
        adapter.clear()
    }

    private fun isAvailableStorage(): Boolean {
        return requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) != null
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun launchNewEditor(editor: IEditorPlugin) {
        if (isAvailableStorage().not()) {
            Toast.makeText(requireContext(), R.string.explorer_not_available_storage, Toast.LENGTH_SHORT).show()
            return
        }

        editor.launchNewEditor(requireActivity())
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun launchEditor(path: String, project: Project) {
        if (isAvailableStorage().not()) {
            Toast.makeText(requireContext(), R.string.explorer_not_available_storage, Toast.LENGTH_SHORT).show()
            return
        }
        if (project.isValid(requireContext()).not()) {
            Toast.makeText(requireContext(), R.string.editor_error_plugins, Toast.LENGTH_SHORT).show()
            return
        }

        requireContext().mdNoteApplication()
            .getEditorPlugins()
            .first { it.name == project.editor }
            .launchEditor(requireActivity(), path, project)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied() {
        Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionNeverAskAgain() {
        Toast.makeText(requireContext(), R.string.permission_never_ask_again, Toast.LENGTH_LONG).show()
    }

    private inner class NoteNewViewHolderDelegate : ListAdapter.IViewHolderDelegate<NoteViewHolder, NoteItem> {

        override val viewType = noteNewViewType

        override fun isValidViewType(position: Int): Boolean {
            return adapter[position] is NoteItem.New
        }

        override fun createViewHolder(parent: ViewGroup): NoteViewHolder {
            return NoteNewViewHolder.create(parent)
        }

        override fun onClick(element: NoteItem, clickedId: Int) {
            if (element is NoteItem.New) {
                launchNewEditorWithPermissionCheck(element.editor)
            }
        }
    }

    private inner class NoteOpenViewHolderDelegate : ListAdapter.IViewHolderDelegate<NoteViewHolder, NoteItem> {

        override val viewType = noteOpenViewType

        override fun isValidViewType(position: Int): Boolean {
            return adapter[position] is NoteItem.Open
        }

        override fun createViewHolder(parent: ViewGroup): NoteViewHolder {
            return NoteOpenViewHolder.create(parent)
        }

        override fun onClick(element: NoteItem, clickedId: Int) {
            if (element is NoteItem.Open) {
                launchEditorWithPermissionCheck(element.path, element.project)
            }
        }
    }
}