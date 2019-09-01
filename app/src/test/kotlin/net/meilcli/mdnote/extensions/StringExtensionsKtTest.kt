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

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsKtTest {

    @Test
    fun testLineIndices_empty() {
        val indices = "".lineIndices()

        assertEquals(1, indices.size)
        assertEquals(Pair(0, 0), indices[0])
    }

    @Test
    fun testLineIndices_n_lineOnly(){
        val indices = "\n\n\n".lineIndices()

        assertEquals(4,indices.size)
        assertEquals(Pair(0,0),indices[0])
        assertEquals(Pair(1,1),indices[1])
        assertEquals(Pair(2,2),indices[2])
        assertEquals(Pair(3,3),indices[3])
    }

    @Test
    fun testLineIndices_r_lineOnly(){
        val indices = "\r\r\r".lineIndices()

        assertEquals(4,indices.size)
        assertEquals(Pair(0,0),indices[0])
        assertEquals(Pair(1,1),indices[1])
        assertEquals(Pair(2,2),indices[2])
        assertEquals(Pair(3,3),indices[3])
    }

    @Test
    fun testLineIndices_rn_lineOnly(){
        val indices = "\r\n\r\n\r\n".lineIndices()

        assertEquals(4,indices.size)
        assertEquals(Pair(0,0),indices[0])
        assertEquals(Pair(2,2),indices[1])
        assertEquals(Pair(4,4),indices[2])
        assertEquals(Pair(6,6),indices[3])
    }

    @Test
    fun testLineIndices_n_withText(){
        val indices = "a\nb\nc\nd".lineIndices()

        assertEquals(4,indices.size)
        assertEquals(Pair(0,1),indices[0])
        assertEquals(Pair(2,3),indices[1])
        assertEquals(Pair(4,5),indices[2])
        assertEquals(Pair(6,7),indices[3])
    }

    @Test
    fun testLineIndices_r_withText(){
        val indices = "a\rb\rc\rd".lineIndices()

        assertEquals(4,indices.size)
        assertEquals(Pair(0,1),indices[0])
        assertEquals(Pair(2,3),indices[1])
        assertEquals(Pair(4,5),indices[2])
        assertEquals(Pair(6,7),indices[3])
    }

    @Test
    fun testLineIndices_rn_withText(){
        val indices = "a\r\nb\r\nc\r\nd".lineIndices()

        assertEquals(4,indices.size)
        assertEquals(Pair(0,1),indices[0])
        assertEquals(Pair(3,4),indices[1])
        assertEquals(Pair(6,7),indices[2])
        assertEquals(Pair(9,10),indices[3])
    }
}