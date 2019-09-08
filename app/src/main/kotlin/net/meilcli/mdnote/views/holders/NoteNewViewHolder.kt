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
import kotlinx.android.synthetic.main.holder_note_new.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.models.NoteItem

class NoteNewViewHolder private constructor(
    containerView: View
) : NoteViewHolder(containerView) {

    companion object {

        fun create(parent: ViewGroup): NoteNewViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.holder_note_new, parent, false)
            return NoteNewViewHolder(itemView)
        }
    }

    override fun registerClickDelegate(delegate: (Int) -> Unit) {
        itemView.setOnClickListener {
            delegate(it.id)
        }
    }

    override fun bind(element: NoteItem) {
        val newItem = element as NoteItem.New // force cast
        icon.setImageResource(newItem.editor.icon)
        text.setText(newItem.editor.description)
    }
}