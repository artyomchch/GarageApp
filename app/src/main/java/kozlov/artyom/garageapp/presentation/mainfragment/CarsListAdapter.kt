package kozlov.artyom.garageapp.presentation.mainfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.CarItemBinding
import kozlov.artyom.garageapp.domain.entity.CarItem

class CarsListAdapter : ListAdapter<CarItem, CarItemViewHolder>(CarItemDiffCallback()) {

    var onCarItemClickListener: ((CarItem) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
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
            cylinderVolumeValue.text = carsItem.cylinderVolume
            powerValue.text = carsItem.power
            fuel.text = carsItem.fuel
            driveUnit.text = carsItem.driveUnit
            color.text = carsItem.color
            price.text = carsItem.price

            viewHolder.itemView.setOnClickListener {
                onCarItemClickListener?.invoke(carsItem)
            }

            imageUrl.setOnClickListener {

                StfalconImageViewer.Builder(imageUrl.context, listOf(carsItem)) { view, image ->
                    Glide.with(root)
                        .load(image.pathToPic)
                        .placeholder(R.drawable.ic_baseline_directions_car_24)
                        .into(view)
                }.show()

            }


        }


    }

    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val MAX_POOL_SIZE = 10
    }
}