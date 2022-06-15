package kozlov.artyom.garageapp.presentation.mainfragment


import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.FragmentCarBinding
import kozlov.artyom.garageapp.presentation.dialogfragment.FilterDialog
import kozlov.artyom.garageapp.presentation.mainfragment.swipelistener.MyButton
import kozlov.artyom.garageapp.presentation.mainfragment.swipelistener.MySwipeHelper
import kozlov.artyom.garageapp.presentation.mainfragment.swipelistener.SwipeListenerButton
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
        toolbarButtonListener()
        observeSortingFilterData()
        setupListenerFilterDialog()

        return binding.root
    }

    private fun showSortingDialog() {
        FilterDialog.show(parentFragmentManager)

    }

    private fun setupListenerFilterDialog() {

        FilterDialog.setupListener(parentFragmentManager, viewLifecycleOwner) {
            when (it.first) {
                DialogInterface.BUTTON_POSITIVE -> viewModel.filterByPower(it.second?.toInt() ?: RESET_VALUE, true)
                DialogInterface.BUTTON_NEGATIVE -> viewModel.filterByPower(it.second?.toInt() ?: RESET_VALUE, false)
                DialogInterface.BUTTON_NEUTRAL -> viewModel.filterByPower(RESET_VALUE, true)
            }
        }
    }

    private fun observeSortingFilterData() {
        viewModel.carListSort.observe(viewLifecycleOwner, carsListAdapter::submitList)
    }

    private fun toolbarButtonListener() {
        binding.mainToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> {
                    viewModel.sortByAlphabet()
                    true
                }
                R.id.filter -> {
                    showSortingDialog()
                    true
                }
                else -> false
            }
        }
    }


    private fun observeRecycler() {
        viewModel.carList.observe(viewLifecycleOwner, carsListAdapter::submitList)
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

        val swipe = object : MySwipeHelper(requireContext(), binding.listRecyclerView, 200) {
            override fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder, buffer: MutableList<MyButton>) {
                val item = carsListAdapter.currentList[viewHolder.layoutPosition]
                buffer.add(
                    MyButton(requireContext(), "Delete", 60, 0, Color.parseColor("#FF3C30"),
                        object : SwipeListenerButton {
                            override fun onClick(pos: Int) {
                                viewModel.deleteCarItem(item)
                                Log.d("TAG", "onClick: delete")
                                Toast.makeText(requireContext(), "Delete button", Toast.LENGTH_SHORT).show()
                            }

                        })
                )
                buffer.add(
                    MyButton(requireContext(), "Edit", 60, 0, Color.parseColor("#FF9502"),
                        object : SwipeListenerButton {
                            override fun onClick(pos: Int) {
                                launchFragment(CarItemFragment.newInstanceEditItem(item.id), R.id.container_view)
                                Log.d("TAG", "onClick: edit")
                                Toast.makeText(requireContext(), "Edit button", Toast.LENGTH_SHORT).show()
                            }

                        })

                )


            }

        }

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

    companion object {
        private const val RESET_VALUE = 0
    }

}