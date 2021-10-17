package uz.mobiller.musicplayerappdemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.mobiller.musicplayerappdemo.R
import uz.mobiller.musicplayerappdemo.databinding.ActivityFavouriteBinding

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}