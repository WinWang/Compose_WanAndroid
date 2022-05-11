package com.winwang.composewanandroid.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes


/**
 * 实现将特定文本复制到剪贴板的功能。
 * @param[label] User-visible label for the clip data.
 * @param[text] The actual text in the clip.
 */
fun Context.copyTextIntoClipboard(text: CharSequence?, label: String? = "") {
    if (text.isNullOrEmpty()) return
    val cbs = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        ?: return
    cbs.setPrimaryClip(ClipData.newPlainText(label, text))
}

fun Context.showToast(@StringRes messageId: Int) {
    showToast(getString(messageId))
}

fun Context.showToast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes messageId: Int, gravity: Int) {
    showToast(getString(messageId), gravity)
}

fun Context.showToast(message: CharSequence, gravity: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(gravity, 0, 0)
    }.show()
}


fun Context.showLongToast(@StringRes messageId: Int) {
    showLongToast(getString(messageId))
}

fun Context.showLongToast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showLongToast(@StringRes messageId: Int, gravity: Int) {
    showLongToast(getString(messageId), gravity)
}

fun Context.showLongToast(message: CharSequence, gravity: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).apply {
        setGravity(gravity, 0, 0)
    }.show()
}