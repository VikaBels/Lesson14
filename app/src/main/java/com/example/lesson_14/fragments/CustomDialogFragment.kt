package com.example.lesson_14.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.lesson_14.R
import com.example.lesson_14.listeners.FinishAppListener
import com.example.lesson_14.listeners.SaveElementsListener

class CustomDialogFragment : DialogFragment() {
    private var fragmentFinishListener: FinishAppListener? = null
    private var fragmentSaveElementsListener: SaveElementsListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentFinishListener = context as? FinishAppListener
            ?: error("$context${resources.getString(R.string.exceptionInterface)}")
        fragmentSaveElementsListener = context as? SaveElementsListener
            ?: error("$context${resources.getString(R.string.exceptionInterface)}")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle(resources.getString(R.string.txt_exit))
            .setMessage(resources.getString(R.string.txt_save_elements))
            .setPositiveButton(resources.getString(R.string.btn_OK)) { _, _ ->
                fragmentSaveElementsListener?.saveAllElements()
                fragmentFinishListener?.finishApp()
            }
            .setNegativeButton(resources.getString(R.string.btn_CANCEL)) { _, _ ->
                fragmentFinishListener?.finishApp()
            }
            .create()
    }

    override fun onDetach() {
        super.onDetach()
        fragmentFinishListener = null
        fragmentSaveElementsListener = null
    }
}