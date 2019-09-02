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

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText

class MarkdownEditView : EditText {

    private val textWatcher: TextWatcher
    private val spans = mutableListOf<MarkdownSpan>()
    private var hidedSpan: MarkdownSpan? = null

    private val spanExtractor = MarkdownSpanExtractor(listOf(AppMarkdownPlugin())) // ToDo: from application

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        textWatcher = MarkdownWatcher()
        addTextChangedListener(textWatcher)
    }

    override fun onSelectionChanged(selectionStart: Int, selectionEnd: Int) {
        super.onSelectionChanged(selectionStart, selectionEnd)
        if (text.isEmpty()) {
            // guard null-pointer when constructing instance
            return
        }

        showHidedSpanIfNeeded(selectionStart, selectionEnd)
        hideSpanIfNeeded(selectionStart, selectionEnd)
    }

    // ToDo: Range check
    private fun showHidedSpanIfNeeded(selectionStart: Int, selectionEnd: Int) {
        val hidedSpan = hidedSpan ?: return
        if (selectionStart !in hidedSpan.startIndex..hidedSpan.endIndex) {
            hidedSpan.attachEditable(text)
            this.hidedSpan = null

            invalidate()
            requestLayout()
        }
    }

    // ToDo: Range check
    private fun hideSpanIfNeeded(selectionStart: Int, selectionEnd: Int) {
        if (hidedSpan != null) {
            // hided span must be single
            return
        }
        val requireHideSpan = spans.firstOrNull { selectionStart in it.startIndex..it.endIndex } ?: return
        requireHideSpan.detachEditable(text)
        hidedSpan = requireHideSpan

        invalidate()
        requestLayout()
    }

    private fun attachSpan(editable: Editable) {
        // clear previous data
        for (span in spans) {
            span.detachEditable(editable)
        }
        spans.clear()
        hidedSpan = null

        // attach
        val extractedSpans = spanExtractor.extract(editable.toString())
        for (extractedSpan in extractedSpans) {
            extractedSpan.attachEditable(editable)
            spans.add(extractedSpan)
        }

        invalidate()
        requestLayout()
    }

    private inner class MarkdownWatcher : TextWatcher {

        override fun afterTextChanged(text: Editable?) {
            text ?: return
            attachSpan(text)
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}