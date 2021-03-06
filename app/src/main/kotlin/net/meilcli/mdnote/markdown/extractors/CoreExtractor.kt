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

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import com.vladsch.flexmark.ast.*
import com.vladsch.flexmark.util.ast.Node
import net.meilcli.mdnote.markdown.IMarkdownSpanExtractor
import net.meilcli.mdnote.markdown.MarkdownContext
import net.meilcli.mdnote.markdown.MarkdownSpan
import net.meilcli.mdnote.markdown.spans.*

class CoreExtractor(
    private val parent: IMarkdownSpanExtractor
) : IMarkdownSpanExtractor {

    override fun extract(node: Node, context: MarkdownContext, addSpanToResult: (MarkdownSpan) -> Unit) {
        when (node) {
            is Heading -> addSpanToResult(node.toSpan())
            is HardLineBreak -> addSpanToResult(node.toSpan()) // ToDo: replace to line icon
            is Paragraph -> {
                for (child in node.children) {
                    parent.extract(child, context, addSpanToResult)
                }
            }
            is Emphasis -> addSpanToResult(node.toSpan())
            is StrongEmphasis -> addSpanToResult(node.toSpan())
            is Link -> addSpanToResult(node.toSpan())
            is BlockQuote -> addSpanToResult(node.toSpan(parent, context))
            is BulletList -> addSpanToResult(node.toSpan(parent, context))
            is OrderedList -> addSpanToResult(node.toSpan(parent, context))
            is TextBase -> {
                if (node.hasChildren()) {
                    for (child in node.children) {
                        parent.extract(child, context, addSpanToResult)
                    }
                }
            }
            is Text -> { /* skip */
            }
            else -> Log.d("a", "unknown: ${node.javaClass}")
        }
    }

    private fun Heading.toSpan(): MarkdownSpan {
        val proportion = when (level) {
            1 -> 3F
            2 -> 2.5F
            3 -> 2.0F
            4 -> 1.8F
            5 -> 1.5F
            6 -> 1.3F
            else -> 1.3F
        }
        val underline = when (level) {
            1, 2 -> true
            else -> false
        }
        when {
            isAtxHeading -> {
                // ↓This type
                // # Heading
                val textStartIndex = chars.indexOf(text)
                return MarkdownSpan(
                    listOf(
                        MarkdownSpan.Element(
                            SkipSpan(),
                            startOffset,
                            startOffset + textStartIndex
                        ),
                        MarkdownSpan.Element(
                            RelativeSizeSpan(proportion),
                            startOffset + textStartIndex,
                            endOffset
                        ),
                        MarkdownSpan.Element(
                            HeadingUnderlineSpan(underline),
                            endOffset - 1,
                            endOffset
                        )
                    ),
                    startOffset,
                    endOffset
                )
            }
            isSetextHeading ->
                // ↓This type
                // Heading
                // ---
                return MarkdownSpan(
                    listOf(
                        MarkdownSpan.Element(
                            RelativeSizeSpan(proportion),
                            startOffset,
                            startOffset + text.length
                        ),
                        MarkdownSpan.Element(
                            HeadingUnderlineSpan(underline),
                            startOffset + text.length - 1,
                            startOffset + text.length
                        ),
                        MarkdownSpan.Element(
                            SkipSpan(),
                            startOffset + text.length,
                            endOffset
                        )
                    ),
                    startOffset,
                    endOffset
                )
            else -> throw IllegalStateException("unhandled Heading type")
        }
    }

    private fun HardLineBreak.toSpan(): MarkdownSpan {
        return MarkdownSpan(
            MarkdownSpan.Element(
                HardLineBreakSpan(),
                startOffset,
                endOffset
            )
        )
    }

    private fun Emphasis.toSpan(): MarkdownSpan {
        val textStartIndex = chars.indexOf(text)
        return MarkdownSpan(
            listOf(
                MarkdownSpan.Element(SkipSpan(), startOffset, startOffset + textStartIndex),
                MarkdownSpan.Element(
                    StyleSpan(Typeface.ITALIC),
                    startOffset + textStartIndex,
                    startOffset + textStartIndex + text.length
                ),
                MarkdownSpan.Element(
                    SkipSpan(),
                    startOffset + textStartIndex + text.length,
                    endOffset
                )
            ),
            startOffset,
            endOffset
        )
    }

    private fun StrongEmphasis.toSpan(): MarkdownSpan {
        val textStartIndex = chars.indexOf(text)
        return MarkdownSpan(
            listOf(
                MarkdownSpan.Element(SkipSpan(), startOffset, startOffset + textStartIndex),
                MarkdownSpan.Element(
                    StyleSpan(Typeface.BOLD),
                    startOffset + textStartIndex,
                    startOffset + textStartIndex + text.length
                ),
                MarkdownSpan.Element(
                    SkipSpan(),
                    startOffset + textStartIndex + text.length,
                    endOffset
                )
            ),
            startOffset,
            endOffset
        )
    }

    private fun Link.toSpan(): MarkdownSpan {
        val textStartIndex = textOpeningMarker.length
        return MarkdownSpan(
            listOf(
                MarkdownSpan.Element(SkipSpan(), startOffset, startOffset + textStartIndex),
                MarkdownSpan.Element(
                    ForegroundColorSpan(Color.BLUE),
                    startOffset + textStartIndex,
                    startOffset + textStartIndex + text.length
                ),
                MarkdownSpan.Element(
                    SkipSpan(),
                    startOffset + textStartIndex + text.length,
                    endOffset
                )
            ),
            startOffset,
            endOffset
        )
    }

    private fun BlockQuote.toSpan(
        parent: IMarkdownSpanExtractor,
        context: MarkdownContext,
        nested: Int = 0
    ): MarkdownSpan {
        if (hasChildren().not()) {
            return MarkdownSpan(
                MarkdownSpan.Element(
                    SkipSpan(),
                    startOffset,
                    endOffset
                )
            )
        }

        val elements = mutableListOf<MarkdownSpan.Element>()
        elements.add(MarkdownSpan.Element(BlockQuoteSpan(), context.getStartIndexOfLine(startLineNumber), endOffset))

        val spans = mutableListOf<MarkdownSpan>()
        for (node in children) {
            when (node) {
                is Paragraph -> {
                    // first marker
                    elements.add(MarkdownSpan.Element(SkipSpan(), startOffset, node.startOffset))

                    // second marker or later
                    var searchStartIndex = 0
                    var startIndex = node.startOffset
                    for (line in node.chars.lines()) {
                        val content = line.trimStart().trimStart(openingMarker.firstChar()).trimStart()
                        val endIndex = node.chars.indexOf(content, searchStartIndex)
                        if (startIndex - (node.startOffset + endIndex) != 0) {
                            elements.add(MarkdownSpan.Element(SkipSpan(), startIndex, node.startOffset + endIndex))
                        }
                        searchStartIndex = endIndex + content.length
                        startIndex = node.startOffset + endIndex + content.length
                    }

                    parent.extract(node, context) { spans.add(it) }
                }
                is BulletList -> spans.add(node.toSpan(parent, context))
                is OrderedList -> spans.add(node.toSpan(parent, context))
                is BlockQuote -> {
                    // skip marker, because cannot escape when no-skip
                    elements.add(MarkdownSpan.Element(SkipSpan(), node.startOffset - (nested + 1), node.startOffset))
                    elements.addAll(node.toSpan(parent, context, nested + 1).elements)
                }
                else -> Log.d("a", "unknown in block quote: ${node.javaClass}")
            }
        }
        elements.addAll(spans.flatMap { it.elements })

        return MarkdownSpan(elements, startOffset, endOffset)
    }

    private fun BulletList.toSpan(
        parent: IMarkdownSpanExtractor,
        context: MarkdownContext,
        nested: Int = 0
    ): MarkdownSpan {
        val spans = mutableListOf<MarkdownSpan>()

        for (node in children) {
            when (node) {
                is BulletListItem -> spans.add(node.toSpan(parent, context, nested))
            }
        }

        return MarkdownSpan(spans.flatMap { it.elements }, startOffset, endOffset)
    }

    private fun BulletListItem.toSpan(
        parent: IMarkdownSpanExtractor,
        context: MarkdownContext,
        nested: Int
    ): MarkdownSpan {
        val spans = mutableListOf<MarkdownSpan>()
        val elements = mutableListOf<MarkdownSpan.Element>()

        for (node in children) {
            // children will be single
            when (node) {
                is Paragraph, is BlockQuote -> {
                    // marker
                    elements.add(
                        MarkdownSpan.Element(
                            SkipSpan(),
                            context.getStartIndexOfLine(startLineNumber),
                            node.startOffset
                        )
                    )
                    spans.add(
                        MarkdownSpan(
                            MarkdownSpan.Element(
                                BulletListItemSpan(nested + 1),
                                context.getStartIndexOfLine(startLineNumber),
                                node.endOffset
                            )
                        )
                    )

                    parent.extract(node, context) { spans.add(it) }
                }
                is BulletList -> spans.add(node.toSpan(parent, context, nested + 1))
                is OrderedList -> spans.add((node.toSpan(parent, context, nested + 1)))
            }
        }

        elements.addAll(spans.flatMap { it.elements })

        return MarkdownSpan(elements, startOffset, endOffset)
    }

    private fun OrderedList.toSpan(
        parent: IMarkdownSpanExtractor,
        context: MarkdownContext,
        nested: Int = 0
    ): MarkdownSpan {
        val spans = mutableListOf<MarkdownSpan>()

        for ((i, node) in children.withIndex()) {
            when (node) {
                is OrderedListItem -> spans.add(node.toSpan(parent, context, nested, i + 1))
            }
        }

        return MarkdownSpan(spans.flatMap { it.elements }, startOffset, endOffset)
    }

    private fun OrderedListItem.toSpan(
        parent: IMarkdownSpanExtractor,
        context: MarkdownContext,
        nested: Int,
        number: Int
    ): MarkdownSpan {
        val spans = mutableListOf<MarkdownSpan>()
        val elements = mutableListOf<MarkdownSpan.Element>()

        for (node in children) {
            // children will be single
            when (node) {
                is Paragraph, is BlockQuote -> {
                    // marker
                    elements.add(
                        MarkdownSpan.Element(
                            SkipSpan(),
                            context.getStartIndexOfLine(startLineNumber),
                            node.startOffset
                        )
                    )
                    spans.add(
                        MarkdownSpan(
                            MarkdownSpan.Element(
                                OrderedListItemSpan(nested + 1, number),
                                context.getStartIndexOfLine(startLineNumber),
                                node.endOffset
                            )
                        )
                    )

                    parent.extract(node, context) { spans.add(it) }
                }
                is OrderedList -> spans.add(node.toSpan(parent, context, nested + 1))
                is BulletList -> spans.add(node.toSpan(parent, context, nested + 1))
            }
        }

        elements.addAll(spans.flatMap { it.elements })

        return MarkdownSpan(elements, startOffset, endOffset)
    }
}