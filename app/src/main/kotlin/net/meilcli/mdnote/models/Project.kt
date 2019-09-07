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

package net.meilcli.mdnote.models

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.meilcli.mdnote.extensions.mdNoteApplication

@Serializable
@Parcelize
data class Project(
    @SerialName("type")
    val type: ProjectType,

    @SerialName("editor")
    val editor: String,

    @SerialName("use_plugins")
    val usePlugins: List<String>,

    @SerialName("name")
    var name: String,

    @SerialName("created_at")
    val createdAt: Long,

    @SerialName("updated_at")
    var updatedAt: Long
) : Parcelable {

    fun isValid(context: Context): Boolean {
        val mdNoteApplication = context.mdNoteApplication()
        if (mdNoteApplication.getEditorPlugins().all { it.name != editor }) {
            return false
        }
        for (usePlugin in usePlugins) {
            if (mdNoteApplication.getPlugins().all { it.name != usePlugin }) {
                return false
            }
        }
        return true
    }
}