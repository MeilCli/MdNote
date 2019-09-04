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

package net.meilcli.mdnote.licenses

class AppLicensePlugin : ILicensePlugin {

    override fun applyLicenses(addLicense: (License) -> Unit) {
        addLicense(
            License(
                "Kotlin Standard Library",
                "https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib",
                LicenseType.ApacheLicense2,
                "https://github.com/JetBrains/kotlin/blob/master/license/LICENSE.txt"
            )
        )
        addLicense(
            License(
                "Android Jetpack",
                "https://github.com/aosp-mirror/platform_frameworks_support",
                LicenseType.ApacheLicense2,
                "https://github.com/aosp-mirror/platform_frameworks_support/blob/androidx-master-dev/LICENSE.txt"
            )
        )
        addLicense(
            License(
                "flexmark-java",
                "https://github.com/vsch/flexmark-java",
                LicenseType.Bsd2Clause,
                "https://github.com/vsch/flexmark-java/blob/master/LICENSE.txt"
            )
        )
    }
}