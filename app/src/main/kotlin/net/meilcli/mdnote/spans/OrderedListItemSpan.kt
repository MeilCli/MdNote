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
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.LeadingMarginSpan

/**
 * [level] number start from 1
 */
class OrderedListItemSpan(private val level: Int, private val number: Int) : LeadingMarginSpan {

    companion object {

        val color = Color.GRAY
        val indent = 40 // ToDo from context
        val width = 80 // ToDo: from context
        val gap = 20 // ToDo: from context
    }

    private var textPaint = TextPaint()
    private var layout: StaticLayout? = null

    override fun getLeadingMargin(first: Boolean): Int {
        return width + gap + indent * (level -1)
    }

    override fun drawLeadingMargin(
        c: Canvas,
        p: Paint,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout?
    ) {
        val oldColor = p.color

        p.color = color

        val staticLayout = this.layout ?: createLayout(p)
        this.layout = staticLayout

        val saveCount = c.save()
        try {
            c.translate(x.toFloat() + indent * (level - 1), top.toFloat() - p.fontMetricsInt.leading)
            staticLayout.draw(c)
        } finally {
            c.restoreToCount(saveCount)
        }

        p.color = oldColor
    }

    private fun createLayout(paint: Paint): StaticLayout {
        textPaint.set(paint)
        val numberText = "$number."
        return StaticLayout.Builder
            .obtain(numberText, 0, numberText.length, textPaint, width)
            .setAlignment(Layout.Alignment.ALIGN_OPPOSITE)
            .build()
    }
}