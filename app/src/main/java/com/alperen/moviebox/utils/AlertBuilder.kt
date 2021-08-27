package com.alperen.moviebox.utils

import android.app.AlertDialog
import android.content.Context
import com.alperen.moviebox.R

class AlertBuilder(private val ctx: Context?) {
    fun build(title: String?, msg: String?) {
        AlertDialog.Builder(ctx)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(R.string.alert_dialog_okay) { _, _ -> }
            .show()
    }
}