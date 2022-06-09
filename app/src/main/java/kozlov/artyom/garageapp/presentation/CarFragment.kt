package kozlov.artyom.garageapp.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.FragmentCarBinding
import kozlov.artyom.garageapp.utils.MyApplication
import javax.inject.Inject


class CarFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[(CarFragmentViewModel::class.java)]
    }

    private var _binding: FragmentCarBinding? = null
    private val binding: FragmentCarBinding
        get() = _binding ?: throw RuntimeException("FragmentCarBinding == null")

    private val component by lazy {
        (requireActivity().application as MyApplication).component
    }


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_car, container, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}