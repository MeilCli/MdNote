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
import android.os.Environment
import androidx.fragment.app.Fragment
import net.meilcli.mdnote.explorers.IExplorerPlugin

class ExplorerContainerFragment : ContainerFragment() {

    companion object {

        private const val explorerPluginArgumentKey = "explorer_plugin_argument_key"
        private const val rootPathArgumentKey = "path_argument_key"
        private const val basePathArgumentKey = "base_path_argument_key"

        fun create(explorerPlugin: IExplorerPlugin, rootPath: String?, basePath: String?): ExplorerContainerFragment {
            return ExplorerContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(explorerPluginArgumentKey, explorerPlugin.name)
                    putString(rootPathArgumentKey, rootPath)
                    putString(basePathArgumentKey, basePath)
                }
            }
        }
    }

    override fun createFirstFragment(): Fragment {
        val arguments = checkNotNull(arguments)
        val explorerPluginName = checkNotNull(arguments.getString(explorerPluginArgumentKey))
        val explorerPlugin = mdNoteApplication.getExplorerPlugins().first { it.name == explorerPluginName }
        val rootPath = arguments.getString(rootPathArgumentKey)
            ?: requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val basePath = arguments.getString(basePathArgumentKey)
        return ExplorerFragment.create(explorerPlugin, checkNotNull(rootPath), basePath ?: checkNotNull(rootPath))
    }
}