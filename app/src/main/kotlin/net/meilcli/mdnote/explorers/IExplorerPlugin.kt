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

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import net.meilcli.mdnote.views.fragments.ContainerChildFragment

interface IExplorerPlugin {

    val name: String
    val title: Int
    val filers: List<IFiler>

    fun createMenu(activity: AppCompatActivity, path: String, menu: Menu, menuInflater: MenuInflater)

    fun onSelectMenuItem(activity: AppCompatActivity, path: String, item: MenuItem)

    fun onSelectPath(fragment: ContainerChildFragment, path: String, matchingFiler: IFiler)
}