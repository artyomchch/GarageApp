package kozlov.artyom.garageapp.presentation.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.SortingDialogBinding
import kotlin.math.log

class FilterDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = SortingDialogBinding.inflate(layoutInflater)


        val listener = DialogInterface.OnClickListener { _, which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
            Log.d(TAG, "onCreateDialog: $which")
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setView(dialogBinding.root)
            .setTitle(getString(R.string.choose_power_count))
            .setPositiveButton(getString(R.string.up), listener)
            .setNeutralButton(getString(R.string.cancel), listener)
            .setNegativeButton(getString(R.string.down), listener)
            .create()

        dialog.setOnShowListener {
            dialogBinding.editPower.requestFocus()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = dialogBinding.editPower.text.toString()
                if (enteredText.isBlank()) {
                    dialogBinding.editPower.error = getString(R.string.empty_value)
                    return@setOnClickListener
                }
                val volume = enteredText.toIntOrNull()
                if (volume == null || volume > 1500) {
                    dialogBinding.editPower.error = getString(R.string.invalid_value)
                    return@setOnClickListener
                }
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to DialogInterface.BUTTON_POSITIVE, POWER_VALUE to enteredText ))
            //    parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(POWER_VALUE to enteredText))
                dialog.dismiss()
            }

            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                val enteredText = dialogBinding.editPower.text.toString()
                if (enteredText.isBlank()) {
                    dialogBinding.editPower.error = getString(R.string.empty_value)
                    return@setOnClickListener
                }
                val volume = enteredText.toIntOrNull()
                if (volume == null || volume > 1500) {
                    dialogBinding.editPower.error = getString(R.string.invalid_value)
                    return@setOnClickListener
                }
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to DialogInterface.BUTTON_NEGATIVE, POWER_VALUE to enteredText))
            //    parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(POWER_VALUE to enteredText))
                dialog.dismiss()
            }
        }

        return dialog


    }


    companion object {
        @JvmStatic
        val TAG: String = FilterDialog::class.java.simpleName

        @JvmStatic
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        const val REQUEST_KEY_POWER = "REQUEST_VALUE"

        @JvmStatic
        val KEY_RESPONSE = "RESPONSE"
        const val POWER_VALUE = "POWER_VALUE"

        fun show(manager: FragmentManager) {
            val dialogFragment = FilterDialog()
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (String) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getInt(KEY_RESPONSE).toString())
                result.getString(POWER_VALUE)

            })
        }
    }
}