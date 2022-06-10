package kozlov.artyom.garageapp.presentation.mainfragment

import androidx.recyclerview.widget.DiffUtil
import kozlov.artyom.garageapp.domain.entity.CarItem

class CarItemDiffCallback : DiffUtil.ItemCallback<CarItem>() {
    override fun areItemsTheSame(oldItem: CarItem, newItem: CarItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CarItem, newItem: CarItem): Boolean {
        return oldItem == newItem
    }
}