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
import net.meilcli.mdnote.explorers.IFiler
import net.meilcli.mdnote.models.ExplorerItem
import net.meilcli.mdnote.views.IExplorerView
import java.io.File

class ExplorerPresenter(
    private val path: String,
    private val filers: List<IFiler>
) : BasePresenter(), ILifecyclePresenter<IExplorerView> {

    override fun onCreatedView(view: IExplorerView) {
    }

    override fun onStartView(view: IExplorerView) {
    }

    override fun onResumeView(view: IExplorerView) {
        launch(Dispatchers.IO) {
            val items = mutableListOf<ExplorerItem>()

            try {
                val folder = File(path)
                for (file in folder.listFiles()) {
                    if (file.canWrite().not() || file.canRead().not()) {
                        continue
                    }

                    val matchingFiler = filers.firstOrNull { it.isMatch(file) }
                    if (matchingFiler != null) {
                        items.add(ExplorerItem(file.absolutePath, file.name, matchingFiler))
                    }
                }
            } catch (exception: Exception) {
            }

            withContext(Dispatchers.Main) {
                view.clearExplorer()
                view.addAllExplorerItem(items)
            }
        }
    }

    override fun onPauseView(view: IExplorerView) {
    }

    override fun onStopView(view: IExplorerView) {
    }

    override fun onDestroyView(view: IExplorerView) {
    }
}