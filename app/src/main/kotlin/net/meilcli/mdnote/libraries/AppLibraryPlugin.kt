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

package net.meilcli.mdnote.libraries

class AppLibraryPlugin : ILibraryPlugin {

    override fun applyLibraries(addLibrary: (Library) -> Unit) {
        addLibrary(
            Library(
                "Kotlin Standard Library",
                "https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib",
                LicenseType.ApacheLicense2,
                "https://github.com/JetBrains/kotlin/blob/master/library/LICENSE.txt"
            )
        )
        addLibrary(
            Library(
                "kotlinx.coroutines",
                "https://github.com/Kotlin/kotlinx.coroutines",
                LicenseType.ApacheLicense2,
                "https://github.com/Kotlin/kotlinx.coroutines/blob/master/LICENSE.txt"
            )
        )
        addLibrary(
            Library(
                "kotlinx.serialization",
                "https://github.com/Kotlin/kotlinx.serialization",
                LicenseType.ApacheLicense2,
                "https://github.com/Kotlin/kotlinx.serialization/blob/master/LICENSE.txt"
            )
        )
        addLibrary(
            Library(
                "Android Jetpack",
                "https://github.com/aosp-mirror/platform_frameworks_support",
                LicenseType.ApacheLicense2,
                "https://github.com/aosp-mirror/platform_frameworks_support/blob/androidx-master-dev/LICENSE.txt"
            )
        )
        addLibrary(
            Library(
                "Material Components for Android",
                "https://github.com/material-components/material-components-android",
                LicenseType.ApacheLicense2,
                "https://github.com/material-components/material-components-android/blob/master/LICENSE"
            )
        )
        addLibrary(
            Library(
                "Material design icons",
                "https://github.com/google/material-design-icons",
                LicenseType.ApacheLicense2,
                "https://github.com/google/material-design-icons/blob/master/LICENSE"
            )
        )
        addLibrary(
            Library(
                "flexmark-java",
                "https://github.com/vsch/flexmark-java",
                LicenseType.Bsd2Clause,
                "https://github.com/vsch/flexmark-java/blob/master/LICENSE.txt"
            )
        )
        addLibrary(
            Library(
                "kaml",
                "https://github.com/charleskorn/kaml",
                LicenseType.ApacheLicense2,
                "https://github.com/charleskorn/kaml/blob/master/LICENSE"
            )
        )
    }
}