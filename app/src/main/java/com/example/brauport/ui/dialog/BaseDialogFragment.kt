package com.example.brauport.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext()) {
            override fun dispatchTouchEvent(event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val currentFocus: View? = currentFocus
                    if (currentFocus is EditText) {
                        val outRect = Rect()
                        currentFocus.getGlobalVisibleRect(outRect)
                        if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                            currentFocus.clearFocus()
                            val imm: InputMethodManager =
                                context.getSystemService(Context.INPUT_METHOD_SERVICE)
                                        as InputMethodManager
                            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                        }
                    }
                }
                return super.dispatchTouchEvent(event)
            }
        }
    }
}