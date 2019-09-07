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
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

abstract class ContainerFragment : BaseFragment() {

    companion object {

        private const val containerId = 123 // no-meaning value
    }

    private inner class BackPressed : OnBackPressedCallback(false) {

        override fun handleOnBackPressed() {
            var backStackCount = childFragmentManager.backStackEntryCount
            if (0 < backStackCount) {
                childFragmentManager.popBackStack()
                backStackCount -= 1
            }
            if (backStackCount == 0) {
                isEnabled = false
            }
        }
    }

    private val backPressed = BackPressed()

    abstract fun createFirstFragment(): Fragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, backPressed)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FrameLayout(inflater.context).apply {
            id = containerId
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .add(containerId, createFirstFragment())
            .commitNow()
    }

    fun addFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .add(containerId, fragment)
            .addToBackStack(null)
            .commit()
        backPressed.isEnabled = true
    }

    fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
        backPressed.isEnabled = true
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            backPressed.isEnabled = false
        } else {
            backPressed.isEnabled = 0 < childFragmentManager.backStackEntryCount
        }
    }
}