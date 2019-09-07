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

package net.meilcli.mdnote

import android.app.Application
import net.meilcli.mdnote.libraries.ILibraryPlugin
import net.meilcli.mdnote.markdown.IMarkdownPlugin

class MdNoteApplication : Application(), IMdNoteApplication {

    private val plugins = mutableListOf<IPlugin>()

    override fun onCreate() {
        super.onCreate()
        loadPlugins()
    }

    private fun loadPlugins() {
        plugins.add(AppPlugin())
    }

    override fun getPlugins(): List<IPlugin> {
        return plugins
    }

    override fun getMarkdownPlugins(): List<IMarkdownPlugin> {
        return plugins.mapNotNull { it.markdown }
    }

    override fun getLibraryPlugins(): List<ILibraryPlugin> {
        return plugins.mapNotNull { it.library }
    }
}