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

package net.meilcli.mdnote.views

import net.meilcli.mdnote.presenters.IPresenter

class PresenterContainer<T> : IPresenterContainer<T> where T : IPresenter {

    private val presenters = mutableListOf<T>()

    override val presenterCount: Int
        get() = presenters.size

    override fun addPresenter(presenter: T) {
        presenters.add(presenter)
    }

    override fun removePresenter(presenter: T) {
        presenters.remove(presenter)
    }

    override fun containsPresenter(presenter: T): Boolean {
        return presenters.contains(presenter)
    }

    override fun clearPresenters() {
        presenters.clear()
    }

    override fun getPresenters(): List<T> {
        return presenters
    }
}