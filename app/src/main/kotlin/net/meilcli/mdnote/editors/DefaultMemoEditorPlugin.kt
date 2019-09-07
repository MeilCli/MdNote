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

package net.meilcli.mdnote.editors

import android.content.Context
import net.meilcli.mdnote.R
import net.meilcli.mdnote.extensions.mdNoteApplication
import net.meilcli.mdnote.extensions.newMemoFolderPath
import net.meilcli.mdnote.models.Project
import net.meilcli.mdnote.models.ProjectType
import net.meilcli.mdnote.views.activities.MemoEditorActivity

class DefaultMemoEditorPlugin : IEditorPlugin {

    override val projectType = ProjectType.Memo
    override val name = "default_memo"
    override val icon = R.drawable.ic_comment_black_24dp
    override val description = R.string.editor_default_memo

    override fun launchNewEditor(context: Context) {
        val path = context.newMemoFolderPath()
        val project = Project(
            type = ProjectType.Memo,
            editor = name,
            usePlugins = context.mdNoteApplication().getPlugins().map { it.name },
            name = "New Memo",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        launchEditor(context, path, project)
    }

    override fun launchEditor(context: Context, path: String, project: Project) {
        context.startActivity(MemoEditorActivity.createIntent(context, path, project))
    }
}