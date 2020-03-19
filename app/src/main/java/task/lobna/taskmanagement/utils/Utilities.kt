package com.shaiik.utilities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ContextThemeWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import task.lobna.taskmanagement.R


object Utilities {
    fun hideKeyboard(view: View) {
        try {
            val imm =
                view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.getWindowToken(), 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showLoading(context: Context): AlertDialog {
        val builder =
            AlertDialog.Builder(ContextThemeWrapper(context, R.style.MyAlertDialogTheme))
        builder.setView(R.layout.loading_view)

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        return alertDialog
    }

    fun dismissLoading(alertDialog: AlertDialog) {
        alertDialog.dismiss()
    }
}