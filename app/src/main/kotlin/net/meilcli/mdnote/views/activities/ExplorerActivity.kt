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

package net.meilcli.mdnote.views.activities

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import kotlinx.android.synthetic.main.activity_explorer.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.explorers.IExplorerPlugin
import net.meilcli.mdnote.extensions.setEdgeToEdgeWindow
import net.meilcli.mdnote.views.fragments.ExplorerContainerFragment

class ExplorerActivity : BaseActivity() {

    companion object {

        const val resultPath = "result_path"
        private const val explorerPluginExtraKey = "explorer_plugin_extra_key"
        private const val pathExtraKey = "path_extra_key"
        private const val basePathExtraKey = "base_path_extra_key"

        fun createIntent(
            context: Context,
            explorerPlugin: IExplorerPlugin,
            path: String? = null,
            basePath: String? = null
        ): Intent {
            return Intent(context, ExplorerActivity::class.java).apply {
                putExtra(explorerPluginExtraKey, explorerPlugin.name)
                if (path != null) {
                    putExtra(pathExtraKey, path)
                }
                if (basePath != null) {
                    putExtra(basePathExtraKey, basePath)
                }
            }
        }

        fun createResultIntent(path: String) = Intent().apply {
            putExtra(resultPath, path)
        }
    }

    override fun createView() {
        setEdgeToEdgeWindow()
        setContentView(R.layout.activity_explorer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<ViewGroup>(android.R.id.content)) { _, insets ->
            statusBarSpace.updateLayoutParams<ViewGroup.LayoutParams> {
                height = insets.systemWindowInsetTop
            }
            insets
        }

        setSupportActionBar(toolbar)

        val explorerPluginName = checkNotNull(intent.getStringExtra(explorerPluginExtraKey))
        val explorerPlugin = mdNoteApplication.getExplorerPlugins().first { it.name == explorerPluginName }
        val path: String? = intent.getStringExtra(pathExtraKey)
        val basePath: String? = intent.getStringExtra(basePathExtraKey)

        supportActionBar?.setTitle(explorerPlugin.title)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, ExplorerContainerFragment.create(explorerPlugin, path, basePath))
            .commitNow()

    }
}