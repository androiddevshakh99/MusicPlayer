package uz.mobiller.musicplayerappdemo

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.mobiller.musicplayerappdemo.databinding.ActivityPlayerBinding
import uz.mobiller.musicplayerappdemo.models.Music

class PlayerActivity : AppCompatActivity() {
    companion object {
        lateinit var musicList: ArrayList<Music>
        var songPosition: Int = 0
        var mediaPlayer: MediaPlayer? = null
        var isPlaying: Boolean = false
    }

    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeLayout()
        binding.playPauseBtn.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        binding.previusBtn.setOnClickListener { prevNextSong(increment = false) }
        binding.nextBtn.setOnClickListener { prevNextSong(increment = true) }
    }

    private fun setLayout() {
        Glide.with(this)
            .load(musicList[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_player_icon_splash_screen))
            .into(binding.songImg)

        binding.songName.text = musicList[songPosition].title
    }

    private fun createMediaPlayer() {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(musicList[songPosition].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
        } catch (e: Exception) {
            return
        }
    }

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicItemAdapter" -> {
                musicList = ArrayList()
                musicList.addAll(MainActivity.musicList)
                setLayout()
                createMediaPlayer()
            }
            "MainActivity" -> {
                musicList = ArrayList()
                musicList.addAll(MainActivity.musicList)
                musicList.shuffle()
                setLayout()
                createMediaPlayer()
            }
        }
    }

    private fun playMusic() {
        binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
        isPlaying = true
        mediaPlayer!!.start()
    }

    private fun pauseMusic() {
        binding.playPauseBtn.setIconResource(R.drawable.play_arrow)
        isPlaying = false
        mediaPlayer!!.pause()
    }

    private fun prevNextSong(increment: Boolean) {
        if (increment) {
            setSongPosition(increment = true)
            setLayout()
            createMediaPlayer()
        } else {
            setSongPosition(increment = false)
            setLayout()
            createMediaPlayer()
        }
    }

    private fun setSongPosition(increment: Boolean) {
        if (increment) {
            if (musicList.size - 1 == songPosition)
                songPosition = 0
            else ++songPosition
        } else {
            if (0 == songPosition)
                songPosition = musicList.size - 1
            else --songPosition
        }
    }
}