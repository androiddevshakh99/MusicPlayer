package uz.mobiller.musicplayerappdemo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.mobiller.musicplayerappdemo.ui.PlayerActivity
import uz.mobiller.musicplayerappdemo.R
import uz.mobiller.musicplayerappdemo.databinding.MusicViewBinding
import uz.mobiller.musicplayerappdemo.models.Music
import uz.mobiller.musicplayerappdemo.models.formatDuration

class MusicItemAdapter(private var context: Context, private var musicList: List<Music>) :
    RecyclerView.Adapter<MusicItemAdapter.VH>() {

    inner class VH(var binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(music: Music, position: Int) {
            binding.apply {
                Glide.with(context)
                    .load(music.artUri)
                    .apply(RequestOptions().placeholder(R.drawable.music_player_icon_splash_screen))
                    .into(binding.imgView)
                binding.songNameTv.text = music.title
                binding.songAlbumTv.text = music.album
                binding.songDuration.text = formatDuration(music.duration)

                binding.root.setOnClickListener {
                    var intent = Intent(context, PlayerActivity::class.java)
                    intent.putExtra("index",position)
                    intent.putExtra("class","MusicItemAdapter")
                    ContextCompat.startActivity(context, intent,  null)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return (VH(MusicViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(musicList[position], position)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}