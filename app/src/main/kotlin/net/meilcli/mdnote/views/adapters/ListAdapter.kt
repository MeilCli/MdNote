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

package net.meilcli.mdnote.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.meilcli.mdnote.views.holders.IClickableViewHolder
import net.meilcli.mdnote.views.holders.IViewHolder

class ListAdapter<TViewHolder, TElement>(
    private val viewHolderDelegates: List<IViewHolderDelegate<TViewHolder, TElement>>
) : RecyclerView.Adapter<TViewHolder>() where TViewHolder : RecyclerView.ViewHolder, TViewHolder : IViewHolder<TElement> {

    interface IViewHolderDelegate<TViewHolder, TElement>
            where  TViewHolder : RecyclerView.ViewHolder, TViewHolder : IViewHolder<TElement> {

        val viewType: Int

        fun isValidViewType(position: Int): Boolean

        fun createViewHolder(parent: ViewGroup): TViewHolder

        fun onClick(element: TElement, clickedId: Int)
    }

    class DefaultViewHolderDelegate<TViewHolder, TElement>(
        private val viewHolderCreator: (ViewGroup) -> TViewHolder,
        private val viewClick: (TElement, Int) -> Unit
    ) : IViewHolderDelegate<TViewHolder, TElement>
            where  TViewHolder : RecyclerView.ViewHolder, TViewHolder : IViewHolder<TElement> {

        override val viewType = 0

        override fun isValidViewType(position: Int): Boolean {
            return true
        }

        override fun createViewHolder(parent: ViewGroup): TViewHolder {
            return viewHolderCreator(parent)
        }

        override fun onClick(element: TElement, clickedId: Int) {
            viewClick(element, clickedId)
        }
    }

    private val elements = mutableListOf<TElement>()

    constructor(viewHolderCreator: (ViewGroup) -> TViewHolder)
            : this(listOf(DefaultViewHolderDelegate(viewHolderCreator, { _, _ -> })))

    constructor(viewHolderCreator: (ViewGroup) -> TViewHolder, viewClick: (TElement, Int) -> Unit)
            : this(listOf(DefaultViewHolderDelegate(viewHolderCreator, viewClick)))

    constructor(viewHolderDelegate: IViewHolderDelegate<TViewHolder, TElement>) : this(listOf(viewHolderDelegate))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TViewHolder {
        return viewHolderDelegates.first { it.viewType == viewType }.createViewHolder(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return viewHolderDelegates.first { it.isValidViewType(position) }.viewType
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(holder: TViewHolder, position: Int) {
        val element = elements[position]
        holder.bind(element)
        if (holder is IClickableViewHolder<*>) {
            @Suppress("UNCHECKED_CAST")
            (holder as IClickableViewHolder<TElement>).registerClickDelegate {
                viewHolderDelegates.first { it.isValidViewType(position) }.onClick(element, it)
            }
        }
    }

    fun add(element: TElement) {
        elements.add(element)
        notifyItemInserted(elements.lastIndex)
    }

    fun addAll(elements: Collection<TElement>) {
        if (elements.isEmpty()) {
            return
        }
        this.elements.addAll(elements)
        notifyItemRangeChanged(this.elements.lastIndex - elements.size - 1, elements.size)
    }

    fun set(index: Int, element: TElement) {
        elements[index] = element
        notifyItemChanged(index)
    }

    fun contains(element: TElement): Boolean {
        return elements.contains(element)
    }

    fun indexOf(element: TElement): Int {
        return elements.indexOf(element)
    }

    fun remove(index: Int) {
        elements.removeAt(index)
        notifyItemRemoved(index)
    }

    operator fun get(index: Int): TElement {
        return elements[index]
    }
}