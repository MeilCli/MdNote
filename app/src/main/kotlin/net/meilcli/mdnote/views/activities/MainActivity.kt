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

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import kotlinx.android.synthetic.main.activity_main.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.extensions.setEdgeToEdgeWindow
import net.meilcli.mdnote.presenters.MainPresenter
import net.meilcli.mdnote.views.IMainView
import net.meilcli.mdnote.views.fragments.MemoTopFragment
import net.meilcli.mdnote.views.fragments.NoteTopFragment
import net.meilcli.mdnote.views.fragments.SettingContainerFragment

class MainActivity : BaseActivity(), IMainView {

    override fun createView() {
        setEdgeToEdgeWindow()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<ViewGroup>(android.R.id.content)) { _, insets ->
            statusBarSpace.updateLayoutParams<ViewGroup.LayoutParams> {
                height = insets.systemWindowInsetTop
            }
            bottomNavigation.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets
        }

        addTypedPresenter(MainPresenter())

        val noteTopFragment = NoteTopFragment()
        val memoTopFragment = MemoTopFragment()
        val settingContainerFragment = SettingContainerFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.container, noteTopFragment)
            .add(R.id.container, memoTopFragment)
            .add(R.id.container, settingContainerFragment)
            .hide(memoTopFragment)
            .hide(settingContainerFragment)
            .commitNow()

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab_note -> {
                    supportFragmentManager.beginTransaction()
                        .show(noteTopFragment)
                        .hide(memoTopFragment)
                        .hide(settingContainerFragment)
                        .commit()
                }
                R.id.tab_memo -> {
                    supportFragmentManager.beginTransaction()
                        .hide(noteTopFragment)
                        .show(memoTopFragment)
                        .hide(settingContainerFragment)
                        .commit()
                }
                R.id.tab_setting -> {
                    supportFragmentManager.beginTransaction()
                        .hide(noteTopFragment)
                        .hide(memoTopFragment)
                        .show(settingContainerFragment)
                        .commit()
                }
            }
            true
        }
    }
}
