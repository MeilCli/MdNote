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

package net.meilcli.mdnote.markdown.extractors

import com.vladsch.flexmark.ext.escaped.character.EscapedCharacter
import com.vladsch.flexmark.util.ast.Node
import net.meilcli.mdnote.markdown.IMarkdownSpanExtractor
import net.meilcli.mdnote.markdown.MarkdownContext
import net.meilcli.mdnote.markdown.MarkdownSpan
import net.meilcli.mdnote.markdown.spans.SkipSpan

class EscapedCharacterExtractor : IMarkdownSpanExtractor {

    override fun extract(node: Node, context: MarkdownContext, addSpanToResult: (MarkdownSpan) -> Unit) {
        when (node) {
            is EscapedCharacter -> addSpanToResult(node.toSpan())
        }
    }

    private fun EscapedCharacter.toSpan(): MarkdownSpan {
        return MarkdownSpan(
            MarkdownSpan.Element(
                SkipSpan(),
                startOffset,
                startOffset + openingMarker.length
            ),
            startOffset,
            endOffset
        )
    }
}