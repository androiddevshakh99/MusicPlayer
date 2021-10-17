package uz.mobiller.musicplayerappdemo.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import uz.mobiller.musicplayerappdemo.R
import uz.mobiller.musicplayerappdemo.adapters.MusicItemAdapter
import uz.mobiller.musicplayerappdemo.databinding.ActivityMainBinding
import uz.mobiller.musicplayerappdemo.models.Music
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: MusicItemAdapter
    private var REQUEST_CODE = 1

    companion object {
        lateinit var musicList: ArrayList<Music>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLayout()
        binding.apply {
            shuffleBtn.setOnClickListener {
                var intent = Intent(this@MainActivity, PlayerActivity::class.java)
                intent.putExtra("index",0)
                intent.putExtra("class","MainActivity")
                startActivity(intent)
            }


            favouriteBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, FavouriteActivity::class.java))
            }

            playListBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, PlayListActivity::class.java))
            }

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navFeedback -> Toast.makeText(
                        this@MainActivity,
                        "Feedbacck",
                        Toast.LENGTH_SHORT
                    ).show()
                    R.id.navSettings -> Toast.makeText(
                        this@MainActivity,
                        "Settings",
                        Toast.LENGTH_SHORT
                    ).show()
                    R.id.navAbout -> Toast.makeText(this@MainActivity, "About", Toast.LENGTH_SHORT)
                        .show()
                    R.id.navExit -> exitProcess(1)
                }
                true
            }
        }
    }

    //Permission request
    private fun requestRuntimepermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)

    }

    private fun initializeLayout() {
        requestRuntimepermission()
        setTheme(R.style.coolPinkNav)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //for nav Drawer
        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        musicList = getAllAudio()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.rv.setHasFixedSize(true)
        binding.rv.setItemViewCacheSize(13)
        binding.rv.layoutManager = LinearLayoutManager(this)
        musicAdapter = MusicItemAdapter(this, musicList)
        binding.rv.adapter = musicAdapter
        binding.totalSongs.text = "Total songs: ${musicList.size}"
    }

    private fun getAllAudio(): ArrayList<Music> {
        val tempList = ArrayList<Music>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection,
            null, MediaStore.Audio.Media.DATE_ADDED + " DESC", null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    var id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    var album =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    var artist =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    var path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    var duration =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                            .toLong()
                    var albumId =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                            .toString()
                    var uri = Uri.parse("content://media/external/audio/albumart")
                    var artUri = Uri.withAppendedPath(uri, albumId).toString()

                    var music = Music(
                        id = id,
                        title = title,
                        album = album,
                        artist = artist,
                        duration = duration,
                        path = path,
                        artUri = artUri
                    )
                    var file = File(music.path)
                    if (file.exists()) {
                        tempList.add(music)
                    }
                } while (cursor.moveToNext())

                cursor.close()
            }
        }
        return tempList
    }
}