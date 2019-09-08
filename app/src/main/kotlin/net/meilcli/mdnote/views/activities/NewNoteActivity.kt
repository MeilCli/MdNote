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

package net.meilcli.mdnote.views.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import net.meilcli.mdnote.explorers.SelectFolderExplorerPlugin

class NewNoteActivity : BaseActivity() {

    companion object {

        fun createIntent(context: Context) = Intent(context, NewNoteActivity::class.java)
    }

    override fun createView() {
        val selectFolderExplorerPlugin = mdNoteApplication.getExplorerPlugins()
            .first { it is SelectFolderExplorerPlugin }
        startActivityForResult(ExplorerActivity.createIntent(this, selectFolderExplorerPlugin), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                Toast.makeText(
                    applicationContext,
                    data?.getStringExtra(ExplorerActivity.resultPath),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            Activity.RESULT_CANCELED -> finish()
        }
    }
}