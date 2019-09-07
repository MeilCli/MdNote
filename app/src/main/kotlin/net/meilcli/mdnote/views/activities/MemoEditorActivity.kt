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

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import com.charleskorn.kaml.Yaml
import kotlinx.android.synthetic.main.activity_memo_editor.*
import net.meilcli.mdnote.Constant
import net.meilcli.mdnote.R
import net.meilcli.mdnote.extensions.setEdgeToEdgeWindow
import net.meilcli.mdnote.models.Project
import java.io.File

class MemoEditorActivity : BaseActivity() {

    companion object {

        private const val pathExtraKey = "path_extra_key"
        private const val projectExtraKey = "project_extra_key"

        fun createIntent(context: Context, path: String, project: Project): Intent {
            return Intent(context, MemoEditorActivity::class.java).apply {
                putExtra(pathExtraKey, path)
                putExtra(projectExtraKey, project)
            }
        }
    }

    override fun createView() {
        setContentView(R.layout.activity_memo_editor)
        setEdgeToEdgeWindow()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<ViewGroup>(android.R.id.content)) { _, insets ->
            statusBarSpace.updateLayoutParams<ViewGroup.LayoutParams> {
                height = insets.systemWindowInsetTop
            }
            editView.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets
        }

        val path = intent.getStringExtra(pathExtraKey)
        val project = intent.getParcelableExtra<Project>(projectExtraKey)

        titleEditView.setText(project.name)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val memoFile = File(path, Constant.memoFile)
        if (memoFile.exists()) {
            editView.setText(memoFile.readText())
        }
    }

    override fun onPause() {
        super.onPause()

        val path = intent.getStringExtra(pathExtraKey)
        val project = intent.getParcelableExtra<Project>(projectExtraKey)
        updateProject(project)

        val projectFile = File(path, Constant.projectFile)
        projectFile.writeText(Yaml.default.stringify(Project.serializer(), project))
        val memoFile = File(path, Constant.memoFile)
        memoFile.writeText(editView.text.toString())
    }

    private fun updateProject(project: Project) {
        project.name = titleEditView.text.toString()
        project.updatedAt = System.currentTimeMillis()
        intent.putExtra(projectExtraKey, project)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}