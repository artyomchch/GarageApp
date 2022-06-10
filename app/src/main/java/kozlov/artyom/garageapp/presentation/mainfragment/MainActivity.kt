package kozlov.artyom.garageapp.presentation.mainfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kozlov.artyom.garageapp.R
import kozlov.artyom.garageapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}