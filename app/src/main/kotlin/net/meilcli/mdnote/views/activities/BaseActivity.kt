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

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import net.meilcli.mdnote.IMdNoteApplication
import net.meilcli.mdnote.presenters.ILifecyclePresenter
import net.meilcli.mdnote.presenters.IPresenter
import net.meilcli.mdnote.presenters.ISaveStatePresenter
import net.meilcli.mdnote.presenters.ITypedPresenter
import net.meilcli.mdnote.views.IPresenterContainer
import net.meilcli.mdnote.views.IView
import net.meilcli.mdnote.views.PresenterContainer

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), IView, IPresenterContainer by PresenterContainer() {

    /**
     * This view can be cast to generics presenter's type parameter
     */
    protected val typedPresenterContainer = PresenterContainer()

    override val mdNoteApplication: IMdNoteApplication
        get() = application as IMdNoteApplication

    protected inline fun <reified TView> addTypedPresenter(presenter: ITypedPresenter<TView>) where TView : IView {
        addPresenter(presenter)
        if (this is TView) {
            typedPresenterContainer.addPresenter(presenter)
        } else {
            Log.d("a", "not cast view type")
        }
    }

    private fun Sequence<IPresenter>.filterLifecyclePresenter(): Sequence<ILifecyclePresenter<IView>> {
        return sequence {
            for (presenter in this@filterLifecyclePresenter) {
                if (presenter is ILifecyclePresenter<*>) {
                    @Suppress("UNCHECKED_CAST")
                    yield(presenter as ILifecyclePresenter<IView>)
                }
            }
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createView()

        typedPresenterContainer.getPresenters()
            .asSequence()
            .filterLifecyclePresenter()
            .forEach {
                it.onCreatedView(this)
            }

        savedInstanceState ?: return

        getPresenters().asSequence()
            .filterIsInstance<ISaveStatePresenter>()
            .forEach {
                it.onRestoreState(savedInstanceState)
            }
    }

    abstract fun createView()

    override fun onStart() {
        super.onStart()

        typedPresenterContainer.getPresenters()
            .asSequence()
            .filterLifecyclePresenter()
            .forEach {
                it.onStartView(this)
            }
    }

    override fun onResume() {
        super.onResume()

        typedPresenterContainer.getPresenters()
            .asSequence()
            .filterLifecyclePresenter()
            .forEach {
                it.onResumeView(this)
            }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState ?: return

        getPresenters().asSequence()
            .filterIsInstance<ISaveStatePresenter>()
            .forEach {
                it.onSaveState(outState)
            }
    }

    override fun onPause() {
        super.onPause()

        typedPresenterContainer.getPresenters()
            .asSequence()
            .filterLifecyclePresenter()
            .forEach {
                it.onPauseView(this)
            }
    }

    override fun onStop() {
        super.onStop()

        typedPresenterContainer.getPresenters()
            .asSequence()
            .filterLifecyclePresenter()
            .forEach {
                it.onStopView(this)
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        typedPresenterContainer.getPresenters()
            .asSequence()
            .filterLifecyclePresenter()
            .forEach {
                it.onDestroyView(this)
            }

        getPresenters().forEach {
            it.onDestroy()
        }
    }
}