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
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_explorer.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.explorers.IExplorerPlugin
import net.meilcli.mdnote.models.ExplorerItem
import net.meilcli.mdnote.presenters.ExplorerPresenter
import net.meilcli.mdnote.views.IExplorerView
import net.meilcli.mdnote.views.adapters.ListAdapter
import net.meilcli.mdnote.views.holders.ExplorerViewHolder
import java.io.File

class ExplorerFragment : ContainerChildFragment(), IExplorerView {

    companion object {

        private const val explorerPluginArgumentKey = "explorer_plugin_argument_key"
        private const val pathArgumentKey = "path_argument_key"
        private const val basePathArgumentKey = "base_path_argument_key"

        fun create(explorerPlugin: IExplorerPlugin, path: String, basePath: String): ExplorerFragment {
            return ExplorerFragment().apply {
                arguments = Bundle().apply {
                    putString(explorerPluginArgumentKey, explorerPlugin.name)
                    putString(pathArgumentKey, path)
                    putString(basePathArgumentKey, basePath)
                }
            }
        }
    }

    // ToDo: auto clear when onDestroyView
    private lateinit var adapter: ListAdapter<ExplorerViewHolder, ExplorerItem>
    private lateinit var explorerPlugin: IExplorerPlugin
    private lateinit var currentPath: String
    private lateinit var basePath: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val arguments = checkNotNull(arguments)
        val explorerPluginName = checkNotNull(arguments.getString(explorerPluginArgumentKey))
        explorerPlugin = mdNoteApplication.getExplorerPlugins().first { it.name == explorerPluginName }
        currentPath = checkNotNull(arguments.getString(pathArgumentKey))
        basePath = checkNotNull(arguments.getString(basePathArgumentKey))

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_explorer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ListAdapter({ ExplorerViewHolder.create(it) }, ::onItemClick)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        path.text = relativePath()

        addTypedPresenter(ExplorerPresenter(currentPath, explorerPlugin.filers))

        super.onViewCreated(view, savedInstanceState)
    }

    private fun relativePath(): String {
        return "/" + File(currentPath).toRelativeString(File(basePath))
    }

    override fun addAllExplorerItem(items: Collection<ExplorerItem>) {
        adapter.addAll(items)
    }

    override fun clearExplorer() {
        adapter.clear()
    }

    private fun onItemClick(item: ExplorerItem, @Suppress("UNUSED_PARAMETER") clickedViewId: Int) {
        explorerPlugin.onSelectPath(this, item.path, basePath, item.filer)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        explorerPlugin.createMenu(requireAppCompatActivity(), currentPath, menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        explorerPlugin.onSelectMenuItem(requireAppCompatActivity(), currentPath, item)
        return true
    }
}