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

package net.meilcli.mdnote.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.fragment_app_info.*
import net.meilcli.mdnote.R

class AppInfoFragment : ContainerChildFragment() {

    companion object {

        fun create() = AppInfoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        description.text = getString(
            R.string.app_description,
            getString(R.string.app_license),
            getString(R.string.app_author)
        )

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            content.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        updateTitle()
    }

    override fun updateTitle() {
        requireAppCompatActivity().supportActionBar
            ?.setTitle(R.string.setting_menu_app_info)
    }
}