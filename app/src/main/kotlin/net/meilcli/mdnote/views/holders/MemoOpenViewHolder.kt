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
import kotlinx.android.synthetic.main.holder_memo_open.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.models.MemoItem

class MemoOpenViewHolder private constructor(
    containerView: View
) : MemoViewHolder(containerView) {

    companion object {

        fun create(parent: ViewGroup): MemoOpenViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.holder_memo_open, parent, false)
            return MemoOpenViewHolder(itemView)
        }
    }

    override fun registerClickDelegate(delegate: (Int) -> Unit) {
        itemView.setOnClickListener {
            delegate(it.id)
        }
    }

    override fun bind(element: MemoItem) {
        val openItem = element as MemoItem.Open // force cast
        if (openItem.project.isValid(itemView.context)) {
            icon.setImageResource(R.drawable.ic_folder_open_black_24dp)
            label.setText(R.string.editor_open)
            text.text = openItem.project.name
        } else {
            icon.setImageResource(R.drawable.ic_error_outline_black_24dp)
            label.setText(R.string.editor_error)
            text.text = openItem.project.name
        }
    }
}