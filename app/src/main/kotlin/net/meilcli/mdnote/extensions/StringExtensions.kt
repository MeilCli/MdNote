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

package net.meilcli.mdnote.extensions

/**
 * Pair of first: start index of line
 * Pair of second: end index of line
 */
fun String.lineIndices(): List<Pair<Int, Int>> {
    if (length == 0) {
        return listOf(Pair(0, 0))
    }
    val indices = mutableListOf<Pair<Int, Int>>()

    var startOfLine = 0
    var previousIsRLineSeparator = false
    var previousIsLineSeparator = false

    for ((i, c) in this.withIndex()) {
        if (c == '\r') {
            indices.add(Pair(startOfLine, i))
            previousIsRLineSeparator = true
            previousIsLineSeparator = true
            startOfLine = i + 1
            continue
        }
        if (c == '\n') {
            previousIsLineSeparator = true
            if (previousIsRLineSeparator) {
                previousIsRLineSeparator = false
                startOfLine = i + 1
                continue
            }
            indices.add(Pair(startOfLine, i))
            startOfLine = i + 1
            continue
        }
        if (previousIsLineSeparator) {
            previousIsRLineSeparator = false
            previousIsLineSeparator = false
        }
    }
    indices.add(Pair(startOfLine, length))

    return indices
}