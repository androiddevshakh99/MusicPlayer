package uz.mobiller.musicplayerappdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.mobiller.musicplayerappdemo.databinding.ActivityPlayListBinding

class PlayListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayListBinding.inflate(layoutInflater)
        setTheme(R.style.coolPink)
        setContentView(R.layout.activity_play_list)
    }
}