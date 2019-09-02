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

import com.vladsch.flexmark.ext.escaped.character.EscapedCharacterExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import net.meilcli.mdnote.markdown.extractors.CoreExtractor
import net.meilcli.mdnote.markdown.extractors.EscapedCharacterExtractor

class AppMarkdownPlugin : IMarkdownPlugin {

    override fun applyOptions(option: MutableDataSet) {
        option.set(Parser.EXTENSIONS, listOf(EscapedCharacterExtension.create()))
    }

    override fun attachExtractor(
        rootExtractor: IMarkdownSpanExtractor,
        addExtractor: (IMarkdownSpanExtractor) -> Unit
    ) {
        addExtractor(CoreExtractor(rootExtractor))
        addExtractor(EscapedCharacterExtractor())
    }
}