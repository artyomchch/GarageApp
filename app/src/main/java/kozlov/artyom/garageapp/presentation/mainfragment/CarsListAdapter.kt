package kozlov.artyom.garageapp.presentation.mainfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.CarItemBinding
import kozlov.artyom.garageapp.domain.entity.CarItem

class CarsListAdapter : ListAdapter<CarItem, CarItemViewHolder>(CarItemDiffCallback()) {

    var onCarItemClickListener: ((CarItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
//        val layout = when (viewType) {
//            VIEW_TYPE_DISABLED -> R.layout.item_cars_disable
//            VIEW_TYPE_ENABLED -> R.layout.item_cars
//            else -> throw RuntimeException("Unknown view type: $viewType")
//        }
        val binding = CarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: CarItemViewHolder, position: Int) {
        val carsItem = getItem(position) ?: return
        with(viewHolder.binding) {
            Glide.with(root)
                .load(carsItem.pathToPic)
                .placeholder(R.drawable.ic_car)
                .centerCrop()
                .into(imageUrl)

            nameOfCar.text = carsItem.name
            modelOfCar.text = carsItem.model
            cylinderVolumeValue.text = carsItem.cylinderVolume.toString()
            powerValue.text = carsItem.power.toString()
            fuel.text = carsItem.fuel
            driveUnit.text = carsItem.driveUnit
            color.text = carsItem.color
            price.text = carsItem.price.toString()

            viewHolder.itemView.setOnClickListener {
                onCarItemClickListener?.invoke(carsItem)
            }


        }

//    override fun getItemViewType(position: Int): Int {
//        val item = getItem(position)
//        return if (item.enable) {
//            VIEW_TYPE_ENABLED
//        } else {
//            VIEW_TYPE_DISABLED
//        }
//    }
    }
    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101

        const val MAX_POOL_SIZE = 10
    }
}