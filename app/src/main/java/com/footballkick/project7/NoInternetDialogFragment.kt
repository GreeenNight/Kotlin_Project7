package com.footballkick.project7

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class NoInternetDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_no_internet, container, false)
        view.findViewById<Button>(R.id.try_again_button).setOnClickListener {
            if (activity is MainActivity && (activity as MainActivity).isConnected()) {
                dismiss()
                activity?.recreate()
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        activity?.finish()
    }
}