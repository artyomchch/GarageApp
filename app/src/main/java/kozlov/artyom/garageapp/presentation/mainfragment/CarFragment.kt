package kozlov.artyom.garageapp.presentation.mainfragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.FragmentCarBinding
import kozlov.artyom.garageapp.presentation.secondfragment.CarItemFragment
import kozlov.artyom.garageapp.utils.MyApplication
import javax.inject.Inject


class CarFragment : Fragment() {
    private lateinit var carsListAdapter: CarsListAdapter

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeRecycler()
        setupSwipeListener()

        return binding.root
    }


    private fun observeRecycler() {
        viewModel.carList.observe(viewLifecycleOwner) {
            carsListAdapter.submitList(it)

        }
    }


    private fun setupRecyclerView() {

        with(binding.listRecyclerView) {
            carsListAdapter = CarsListAdapter()
            adapter = carsListAdapter
            recycledViewPool.apply {
                setMaxRecycledViews(CarsListAdapter.VIEW_TYPE_ENABLED, CarsListAdapter.MAX_POOL_SIZE)
            }
        }


        setupClickItemListener()
        setupClickFabListener()

    }

    private fun setupClickFabListener() {
        binding.fabAddButton.setOnClickListener {
            launchFragment(CarItemFragment.newInstanceAddItem(), R.id.container_view)
        }
    }

    private fun setupClickItemListener() {
        carsListAdapter.onCarItemClickListener = {
            launchFragment(CarItemFragment.newInstanceEditItem(it.id), R.id.container_view)
        }
    }


    private fun setupSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = carsListAdapter.currentList[viewHolder.layoutPosition]
                launchFragment(CarItemFragment.newInstanceEditItem(item.id), R.id.container_view)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.listRecyclerView)
    }


    private fun launchFragment(fragment: Fragment, fragment_container: Int) {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}