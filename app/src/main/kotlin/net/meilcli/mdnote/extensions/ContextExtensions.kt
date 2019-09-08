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

import android.content.Context
import com.charleskorn.kaml.Yaml
import net.meilcli.mdnote.Constant
import net.meilcli.mdnote.IMdNoteApplication
import net.meilcli.mdnote.models.Project
import java.io.File

fun Context.mdNoteApplication(): IMdNoteApplication {
    return applicationContext as IMdNoteApplication
}

fun Context.newMemoFolderPath(): String {
    val root = File(filesDir, Constant.memoFolder)
    if (root.exists().not()) {
        root.mkdir()
    }
    val existFolders = root.list()
    var newFolder = 1
    while (existFolders.any { it == newFolder.toString() }) {
        newFolder += 1
    }
    val newFile = File(root, newFolder.toString()).also {
        it.mkdir()
    }
    return newFile.absolutePath
}

fun Context.memoFolders(): Sequence<Pair<String, Project>> = sequence {
    val root = File(filesDir, Constant.memoFolder)
    if (root.exists().not()) {
        return@sequence
    }

    for (file in root.listFiles()) {
        try {
            if (file.isDirectory.not()) {
                continue
            }

            val memoFile = File(file, Constant.memoFile)
            if (memoFile.exists().not() || memoFile.isFile.not()) {
                continue
            }

            val projectFile = File(file, Constant.projectFile)
            if (projectFile.exists().not() || projectFile.isFile.not()) {
                continue
            }
            val project = Yaml.default.parse(Project.serializer(), projectFile.readText())

            yield(Pair(file.absolutePath, project))
        } catch (_: Exception) {
        }
    }
}

