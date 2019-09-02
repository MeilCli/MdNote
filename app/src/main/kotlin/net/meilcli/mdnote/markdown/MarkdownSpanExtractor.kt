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

import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.MutableDataSet

class MarkdownSpanExtractor(plugins: List<IMarkdownPlugin>) : IMarkdownSpanExtractor {

    private val parser: Parser
    private val extractors: List<IMarkdownSpanExtractor>

    init {
        val options = MutableDataSet()
        for (plugin in plugins) {
            plugin.applyOptions(options)
        }
        this.parser = Parser.builder(options).build()

        val extractors = mutableListOf<IMarkdownSpanExtractor>()
        for (plugin in plugins) {
            plugin.attachExtractor(this) { extractors.add(it) }
        }
        this.extractors = extractors
    }

    fun extract(markdown: String): List<MarkdownSpan> {
        val document = parser.parse(markdown)
        val context = MarkdownContext(markdown)
        val spans = mutableListOf<MarkdownSpan>()
        for (node in document.children) {
            extract(node, context) { spans.add(it) }
        }
        return spans
    }

    override fun extract(node: Node, context: MarkdownContext, addSpanToResult: (MarkdownSpan) -> Unit) {
        for (extractor in extractors) {
            extractor.extract(node, context, addSpanToResult)
        }
    }
}