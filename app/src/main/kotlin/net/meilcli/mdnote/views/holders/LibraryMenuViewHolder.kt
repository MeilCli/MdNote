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

package net.meilcli.mdnote.views.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.holder_library_menu.view.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.libraries.Library

class LibraryMenuViewHolder private constructor(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), IClickableViewHolder<Library>, LayoutContainer {

    companion object {

        fun create(parent: ViewGroup): LibraryMenuViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.holder_library_menu, parent, false)
            return LibraryMenuViewHolder(itemView)
        }
    }

    override fun registerClickDelegate(delegate: (Int) -> Unit) {
        itemView.setOnClickListener {
            delegate(it.id)
        }
    }

    override fun bind(element: Library) {
        itemView.text.text = element.name
    }
}