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
class EndFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_end, container, false)
        view.findViewById<Button>(R.id.play_again).setOnClickListener {

                dismiss()
                activity?.recreate()

        }
        view.findViewById<Button>(R.id.end_game).setOnClickListener {

                dismiss()
                activity?.finish()

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