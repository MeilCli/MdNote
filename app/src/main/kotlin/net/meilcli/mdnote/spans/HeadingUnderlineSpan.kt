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

package net.meilcli.mdnote.spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan

class HeadingUnderlineSpan(private val show: Boolean) : ReplacementSpan() {

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        text ?: return 0
        if(fm!=null){
            fm.descent += 10 // ToDo: margin from context
        }
        return paint.measureText(text, start, end).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        text ?: return
        canvas.drawText(text, start, end, x, y.toFloat(), paint)

        if (show.not()) {
            return
        }

        val paintY = y.toFloat() + paint.descent() + 5 // ToDo: margin from context
        canvas.drawLine(0F, paintY, canvas.width.toFloat(), paintY, paint)
    }
}