package com.annaalfiani.gmcapps.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.annaalfiani.gmcapps.R

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showInfoAlert(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}