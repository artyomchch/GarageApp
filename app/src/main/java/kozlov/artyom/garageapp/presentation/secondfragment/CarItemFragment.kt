package kozlov.artyom.garageapp.presentation.secondfragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.FragmentCarItemBinding
import kozlov.artyom.garageapp.domain.entity.CarItem
import kozlov.artyom.garageapp.presentation.mainfragment.ViewModelFactory
import kozlov.artyom.garageapp.utils.MyApplication
import javax.inject.Inject


class CarItemFragment : Fragment() {

    private var _binding: FragmentCarItemBinding? = null
    private val binding: FragmentCarItemBinding
        get() = _binding ?: throw RuntimeException("FragmentCarItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var carItemId: Int = CarItem.UNDEFINED_ID

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[(CarItemViewModel::class.java)]
    }

    private val component by lazy {
        (requireActivity().application as MyApplication).component
    }

    private val locationPermissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        ::onGotPermissionsForStorage
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCarItemBinding.inflate(inflater, container, false)
        component.inject(this)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addChangeTextListeners()
        launchRightMode()
        observeViewModel()
        uploadPhoto()
    }



    private fun uploadPhoto() {
        binding.photoPicker.setOnClickListener {
            locationPermissionRequestLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            IMAGE_URI = data?.data.toString()

            binding.imagePicker.setImageURI(IMAGE_URI.toUri())
        }
    }

    private fun onGotPermissionsForStorage(grantResults: Map<String, Boolean>) {
        if (grantResults.entries.all { it.value }) {
            Toast.makeText(context, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, IMAGE_PICK_CODE)
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                askUserForOpeningAppSettings()
            } else {
                Toast.makeText(context, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun askUserForOpeningAppSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts(getString(R.string.package_value), requireActivity().packageName, null)
        )
        if (requireActivity().packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(context, getString(R.string.forever_denied), Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(requireActivity())
                .setTitle(getString(R.string.permission_denied))
                .setMessage(getString(R.string.warning_permission_denied_dialog))
                .setPositiveButton(getString(R.string.open_app_settings)) { _, _ ->
                    startActivity(appSettingsIntent)
                }
                .create()
                .show()
        }
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_name)
            } else {
                null
            }
            binding.nameTextField.error = message
        }


        viewModel.errorInputModel.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_model)
            } else {
                null
            }
            binding.modelTextField.error = message
        }

        viewModel.errorCountCylinder.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_cylinder_volume)
            } else {
                null
            }
            binding.countCylinderTextField.error = message
        }

        viewModel.errorCountPower.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_count_power)
            } else {
                null
            }
            binding.powerTextField.error = message
        }
        viewModel.errorTypeFuel.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_type_fuel)
            } else {
                null
            }
            binding.fuelTextField.error = message
        }
        viewModel.errorColor.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_car_color)
            } else {
                null
            }
            binding.colorTextField.error = message
        }

        viewModel.errorPrice.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_car_price)
            } else {
                null
            }
            binding.priceTextField.error = message
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }


    private fun addChangeTextListeners() {
        binding.editNameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.editModelField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputModel()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.editCylinderField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorCountCylinder()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.editPowerField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetPowerInput()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.editFuelField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetFuelInput()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.editColorField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputColor()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.editPriceField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetInputPrice()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun launchEditMode() {
        with(binding) {
            viewModel.getCarItem(carItemId)
            viewModel.carItem.observe(viewLifecycleOwner) {

                editNameField.setText(it.name)
                editModelField.setText(it.model)
                editCylinderField.setText(it.cylinderVolume)
                editFuelField.setText(it.fuel)
                editPowerField.setText(it.power)
                editColorField.setText(it.color)
                editPriceField.setText(it.price)

            }
            saveButton.setOnClickListener {
                viewModel.editCarItem(
                    IMAGE_URI,
                    editNameField.text?.toString(),
                    editModelField.text?.toString(),
                    editCylinderField.text?.toString(),
                    editPowerField.text?.toString(),
                    editFuelField.text?.toString(),
                    editColorField.text?.toString(),
                    editPriceField.text?.toString()
                )
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            saveButton.setOnClickListener {
                viewModel.addCarItem(
                    IMAGE_URI,
                    editNameField.text?.toString(),
                    editModelField.text?.toString(),
                    editCylinderField.text?.toString(),
                    editPowerField.text?.toString(),
                    editFuelField.text?.toString(),
                    editColorField.text?.toString(),
                    editPriceField.text?.toString()
                )
            }
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(CAR_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            carItemId = args.getInt(CAR_ITEM_ID, CarItem.UNDEFINED_ID)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val MODE_EDIT = "EDIT"
        private const val MODE_ADD = "ADD"
        private const val SCREEN_MODE = "extra_mode"
        private const val CAR_ITEM_ID = "extra_car_item_id"
        private  var IMAGE_URI = ""
        private const val MODE_UNKNOWN = ""
        private const val IMAGE_PICK_CODE = 1000
        fun newInstanceAddItem(): CarItemFragment {
            return CarItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): CarItemFragment {
            return CarItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(CAR_ITEM_ID, shopItemId)
                }
            }
        }


    }
}