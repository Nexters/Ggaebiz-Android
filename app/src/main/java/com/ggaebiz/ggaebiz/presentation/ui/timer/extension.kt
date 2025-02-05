package com.ggaebiz.ggaebiz.presentation.ui.timer

import android.content.Context


fun Context.getRawResId(resourceName: String): Int {
    val resName = resourceName.replace("raw/", "")
    return resources.getIdentifier(resName, "raw", packageName)
}
