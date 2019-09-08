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

package net.meilcli.mdnote.presenters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.meilcli.mdnote.models.NoteItem
import net.meilcli.mdnote.models.ProjectType
import net.meilcli.mdnote.views.INoteTopView
import kotlin.properties.Delegates

class NoteTopPresenter : BasePresenter(), ILifecyclePresenter<INoteTopView> {

    private var insertPosition by Delegates.notNull<Int>()

    override fun onCreatedView(view: INoteTopView) {
    }

    override fun onStartView(view: INoteTopView) {
    }

    override fun onResumeView(view: INoteTopView) {
        view.clearNote()

        val newItems = view.mdNoteApplication.getEditorPlugins()
            .asSequence()
            .filter { it.projectType == ProjectType.Note }
            .map { NoteItem.New(it) }
            .toSet()
        view.addAllNoteItem(newItems)
        insertPosition = newItems.size

        // ToDo: cancel when onPause
        launch(Dispatchers.IO) {
            for (note in view.mdNoteApplication.getNotes()) {
                withContext(Dispatchers.Main) {
                    view.addNoteItem(insertPosition, NoteItem.Open(note.first, note.second))
                }
            }
        }
    }

    override fun onPauseView(view: INoteTopView) {
    }

    override fun onStopView(view: INoteTopView) {
    }

    override fun onDestroyView(view: INoteTopView) {
    }
}