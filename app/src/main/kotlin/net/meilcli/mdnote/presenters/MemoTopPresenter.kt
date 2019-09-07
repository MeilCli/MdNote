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
import net.meilcli.mdnote.models.MemoItem
import net.meilcli.mdnote.models.ProjectType
import net.meilcli.mdnote.views.IMemoTopView
import kotlin.properties.Delegates

class MemoTopPresenter : BasePresenter(), ILifecyclePresenter<IMemoTopView> {

    private var insertPosition by Delegates.notNull<Int>()

    override fun onCreatedView(view: IMemoTopView) {
    }

    override fun onStartView(view: IMemoTopView) {
    }

    override fun onResumeView(view: IMemoTopView) {
        view.clearMemo()

        val newItems = view.mdNoteApplication.getEditorPlugins()
            .asSequence()
            .filter { it.projectType == ProjectType.Memo }
            .map { MemoItem.New(it) }
            .toSet()
        view.addAllMemoItem(newItems)
        insertPosition = newItems.size

        // ToDo: cancel when onPause
        launch(Dispatchers.IO) {
            for (memo in view.mdNoteApplication.getMemos()) {
                withContext(Dispatchers.Main) {
                    view.addMemoItem(insertPosition, MemoItem.Open(memo.first, memo.second))
                }
            }
        }
    }

    override fun onPauseView(view: IMemoTopView) {
    }

    override fun onStopView(view: IMemoTopView) {
    }

    override fun onDestroyView(view: IMemoTopView) {
    }
}