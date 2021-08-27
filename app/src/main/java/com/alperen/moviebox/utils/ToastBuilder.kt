package com.alperen.moviebox.utils

import android.content.Context
import android.widget.Toast

class ToastBuilder(private val ctx: Context?) {
    fun build(msg: String?) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }
}