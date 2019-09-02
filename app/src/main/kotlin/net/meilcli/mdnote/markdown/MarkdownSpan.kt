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

package net.meilcli.mdnote.markdown

import android.text.Editable

data class MarkdownSpan(val elements: List<Element>, val startIndex: Int, val endIndex: Int) {

    constructor(element: Element) : this(element, element.startIndex, element.endIndex)

    constructor(element: Element, startIndex: Int, endIndex: Int) : this(listOf(element), startIndex, endIndex)

    data class Element(val span: Any, val startIndex: Int, val endIndex: Int)

    fun attachEditable(editable: Editable) {
        for (element in elements) {
            editable.setSpan(
                element.span,
                element.startIndex,
                element.endIndex,
                Editable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    fun detachEditable(editable: Editable) {
        for (element in elements) {
            editable.removeSpan(element.span)
        }
    }
}