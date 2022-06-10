package kozlov.artyom.garageapp.presentation.secondfragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

//    private lateinit var onSaveButtonClickListener: OnSaveButtonClickListener
//    private lateinit var onItemSelectedListener: OnSaveButtonClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnSaveButtonClickListener) {
//            onSaveButtonClickListener = context
//            onItemSelectedListener = context
//
//        } else {
//            throw RuntimeException("Activity must implement OnItemSelectedListener")
//        }
//        onItemSelectedListener.onItemSelected()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onDetach() {
        super.onDetach()
     //   onSaveButtonClickListener.onSaveButtonClick()
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

        viewModel.errorInputBrand.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_brand)
            } else {
                null
            }
            binding.brandTextField.error = message
        }

        viewModel.errorInputModel.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.invalid_cylinder_volume)
            } else {
                null
            }
            binding.countCylinderTextField.error = message
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

        binding.editBrandField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputBrand()
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
    }

    private fun launchEditMode() {
        with(binding) {
            viewModel.getCarItem(carItemId)
            viewModel.carItem.observe(viewLifecycleOwner) {

                editNameField.setText(it.name)
                editBrandField.setText(it.color)
                editModelField.setText(it.model)

            }
            saveButton.setOnClickListener {
                viewModel.editCarItem(editNameField.text?.toString(), editBrandField.text?.toString(), editModelField.text?.toString())
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            saveButton.setOnClickListener {
                viewModel.addCarItem(editNameField.text?.toString(), editBrandField.text?.toString(), editModelField.text?.toString())
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
        private const val MODE_UNKNOWN = ""

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