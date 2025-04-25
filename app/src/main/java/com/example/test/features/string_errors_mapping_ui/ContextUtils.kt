package com.example.test.features.string_errors_mapping_ui

import android.content.Context
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.example.test.R
import java.util.Locale

fun Context.getErrorStringByCode(code: String?): String {
    try {
        val field = R.string::class.java.getDeclaredField("ECS$code")
        var value = 0
        try {
            value = field.getInt(value)
            return this.getString(value)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    } catch (e: Exception) {
        return ""
    }

    return ""
}

fun Context.getLocalizedString(resId: Int, locale: Locale): String {
    val config = resources.configuration
    val localizedContext = createConfigurationContext(config.apply { setLocale(locale) })
    return localizedContext.getString(resId)
}

fun Context.getErrorStringFOByCode(
    code: String?,
    isOMS: Boolean = false,
    vararg formatArgs: Any?
): String {
    val intCode = code?.toIntOrNull()
    if (code == null) return ""

    val prefix = when (intCode) {
        in 0..99 -> if (isOMS) "OMS" else "ATH"
        in 100..200 -> "OMS"
        in 201..999 -> "OCS"
        in 1000..9999 -> "OXS"
        in 10000..19999 -> "ORS"
        in 20000..20999, in 50000..59999 -> "ATH"
        in 30000..39999 -> "CMM"
        in 40000..49999 -> "OIS"
        in 21000..21999 -> "FOU"
        in 22000..22999 -> "OTA"
        in 60000..69999 -> "SYNC"
        else -> return ""
    }

    return try {
        val fieldName = "$prefix$intCode"
        val field = R.string::class.java.getDeclaredField(fieldName)
        val resId = field.getInt(null)

        if (formatArgs.isNotEmpty()) {
            if (prefix in listOf("OMS", "OCS", "OXS", "ORS")) this.getString(resId, *formatArgs)
            else this.getString(resId)
        } else {
            this.getString(resId)
        }
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
        prefix
    } catch (e: Exception) {
        e.printStackTrace()
        prefix
    }
}

fun Context.getChannelByCode(code: String?): String {
    try {
        val field = R.string::class.java.getDeclaredField("ChanelType_$code")
        var value = 0
        try {
            value = field.getInt(value)
            return this.getString(value)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

fun Context.getRejectStringByCode(code: String): String {
    try {
        val field = R.string::class.java.getDeclaredField("RCC$code")
        var value = 0
        try {
            value = field.getInt(value)
            return this.getString(value)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    } catch (e: NoSuchFieldException) {
        return ""
    }

    return this.getString(0)
}

fun Context.getDimension(@DimenRes id: Int): Int {
    return (this.resources.getDimension(id) / this.resources.displayMetrics.density).toInt()
}

fun Context.getColorFromResource(id: Int): Int {
    if (id == 0) return id
    return try {
        ContextCompat.getColor(this, id)
    } catch (ex: Exception) {
        id
    }
}

fun Context.getStringResourceByName(resName: String): String {
    val packageName = this.packageName
    val resId = this.resources.getIdentifier(
        resName, "string",
        packageName
    )
    return if (resId == 0) {
        ""
    } else {
        this.getString(resId)
    }
}