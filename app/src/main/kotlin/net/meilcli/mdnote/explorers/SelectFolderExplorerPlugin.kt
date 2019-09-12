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

package net.meilcli.mdnote.explorers

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import net.meilcli.mdnote.R
import net.meilcli.mdnote.views.activities.ExplorerActivity
import net.meilcli.mdnote.views.fragments.ContainerChildFragment
import net.meilcli.mdnote.views.fragments.ExplorerFragment

class SelectFolderExplorerPlugin : IExplorerPlugin {

    companion object {

        private const val selectId = 1
        private const val cancelId = 2
    }

    override val name = "select_folder"
    override val title = R.string.explorer_select_folder
    override val filers = listOf(FolderFiler, MarkdownFiler, PngFiler, JpegFiler, GifFiler)

    override fun createMenu(activity: AppCompatActivity, path: String, menu: Menu, menuInflater: MenuInflater) {
        fun iconWithTint(icon: Int): Drawable? {
            return activity.getDrawable(icon)
                ?.let { DrawableCompat.wrap(it) }
                ?.let {
                    DrawableCompat.setTint(it, Color.WHITE)
                    it
                }
        }

        menu.clear()
        menu.add(0, selectId, 1, R.string.select).apply {
            icon = iconWithTint(R.drawable.ic_check_black_24dp)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        menu.add(0, cancelId, 0, R.string.cancel).apply {
            icon = iconWithTint(R.drawable.ic_cancel_black_24dp)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
    }

    override fun onSelectMenuItem(activity: AppCompatActivity, path: String, item: MenuItem) {
        when (item.itemId) {
            selectId -> {
                activity.setResult(Activity.RESULT_OK, ExplorerActivity.createResultIntent(path))
                activity.finish()
            }
            cancelId -> {
                activity.setResult(Activity.RESULT_CANCELED)
                activity.finish()
            }
        }
    }

    override fun onSelectPath(fragment: ContainerChildFragment, path: String, basePath: String, matchingFiler: IFiler) {
        if (matchingFiler is FolderFiler) {
            fragment.replaceFragmentOnContainer(ExplorerFragment.create(this, path, basePath))
        }
    }
}