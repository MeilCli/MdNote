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

package net.meilcli.mdnote.presenters

import android.util.Log
import net.meilcli.mdnote.views.IMainView

class MainPresenter : BasePresenter(), ILifecyclePresenter<IMainView> {

    override fun onCreatedView(view: IMainView) {
        Log.d("a", "onCreatedView: ${view.javaClass}")
    }

    override fun onStartView(view: IMainView) {
        Log.d("a", "onStartView: ${view.javaClass}")
    }

    override fun onResumeView(view: IMainView) {
        Log.d("a", "onResumeView: ${view.javaClass}")
    }

    override fun onPauseView(view: IMainView) {
        Log.d("a", "onPauseView: ${view.javaClass}")
    }

    override fun onStopView(view: IMainView) {
        Log.d("a", "onStopView: ${view.javaClass}")
    }

    override fun onDestroyView(view: IMainView) {
        Log.d("a", "onDestroyView: ${view.javaClass}")
    }

    override fun onDestroy() {
        Log.d("a", "onDestroy")
    }
}