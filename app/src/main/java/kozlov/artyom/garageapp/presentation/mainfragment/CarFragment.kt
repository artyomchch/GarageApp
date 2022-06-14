package kozlov.artyom.garageapp.presentation.mainfragment


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.FragmentCarBinding
import kozlov.artyom.garageapp.presentation.dialogfragment.FilterDialog
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
//        val sortingFragment = FilterDialog()
//        sortingFragment.show(parentFragmentManager, FilterDialog.TAG)
        FilterDialog.show(parentFragmentManager)
    }

    private fun setupListenerFilterDialog() {
//        parentFragmentManager.setFragmentResultListener(FilterDialog.REQUEST_KEY, viewLifecycleOwner, FragmentResultListener { _, result ->
//            when (result.getInt(FilterDialog.KEY_RESPONSE)){
//                DialogInterface.BUTTON_POSITIVE -> Log.d("TAG", "setupListenerFilterDialog: ${result.getString(FilterDialog.KEY_RESPONSE)}")
//              //  DialogInterface.BUTTON_NEGATIVE -> TODO()
//
//                 //   Toast.makeText(requireActivity().baseContext, "result.getString(FilterDialog.KEY_RESPONSE)", Toast.LENGTH_SHORT).show()
//            }
//        } )
        FilterDialog.setupListener(parentFragmentManager, viewLifecycleOwner){
            Log.d("TAG", "setupListenerFilterDialog: ${it})")
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
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = carsListAdapter.currentList[viewHolder.layoutPosition]
                launchFragment(CarItemFragment.newInstanceEditItem(item.id), R.id.container_view)
                //  viewModel.deleteCarItem(item)
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