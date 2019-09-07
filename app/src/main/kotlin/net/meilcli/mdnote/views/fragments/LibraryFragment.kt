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

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.fragment_library.*
import net.meilcli.mdnote.R
import net.meilcli.mdnote.libraries.Library

class LibraryFragment : ContainerChildFragment() {

    companion object {

        const val licenseArgumentKey = "license_argument_key"

        fun create(library: Library): LibraryFragment {
            return LibraryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(licenseArgumentKey, library)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val library = checkNotNull(arguments).getParcelable<Library>(licenseArgumentKey)
        checkNotNull(library)

        title.text = library.name
        description.text = getString(R.string.library_description, library.name, library.licenseType.licenseName)
        repository.text = library.url
        repository.setOnClickListener {
            browseUrl(library.url)
        }
        license.text = library.licenseUrl
        license.setOnClickListener {
            browseUrl(library.licenseUrl)
        }

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            content.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            insets
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun browseUrl(url: String) {
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .addDefaultShareMenuItem()
            .enableUrlBarHiding()
            .build()
        intent.launchUrl(requireActivity(), Uri.parse(url))
    }
}