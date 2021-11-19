package com.example.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.player.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import android.content.ComponentName
import android.content.Context

import android.widget.Toast

import android.os.IBinder

import android.content.ServiceConnection
import com.example.player.MediaPlayerService.LocalBinder
import android.content.Intent
import android.provider.MediaStore.Audio
import android.database.Cursor
import android.net.Uri


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FragmentAdapter
    private lateinit var player: MediaPlayerService
    private var serviceBound = false
    private var audioList = mutableListOf<AudioInfo>()

    //Binding this Client to the AudioPlayer Service
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocalBinder
            player = binder.service
            serviceBound = true
            Toast.makeText(this@MainActivity, "Service Bound", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FragmentAdapter(supportFragmentManager, lifecycle)
        binding.pager.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        loadAudio()
        playAudio(audioList[0].data)
    }

    private fun playAudio(media: String) {
        //Check is service is active
        if (!serviceBound) {
            val playerIntent = Intent(this, MediaPlayerService::class.java)
            playerIntent.putExtra("media", media)
            startService(playerIntent)
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

    private fun loadAudio() {
        val uri: Uri = Audio.Media.EXTERNAL_CONTENT_URI
        val selection = Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = Audio.Media.TITLE + " ASC"
        val cursor: Cursor? = contentResolver.query(uri, null, selection, null, sortOrder)
        if (cursor != null && cursor.count > 0) {
            audioList = ArrayList()
            while (cursor.moveToNext()) {
                val data: String = cursor.getString(cursor.getColumnIndexOrThrow(Audio.Media.DATA))
                val title: String = cursor.getString(cursor.getColumnIndexOrThrow(Audio.Media.TITLE))
                val album: String = cursor.getString(cursor.getColumnIndexOrThrow(Audio.Media.ALBUM))
                val artist: String = cursor.getString(cursor.getColumnIndexOrThrow(Audio.Media.ARTIST))
                // Save to audioList
                audioList.add(AudioInfo(data, title, album, artist))
            }
        }
        cursor?.close()
    }


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("ServiceState", serviceBound)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceBound = savedInstanceState.getBoolean("ServiceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
            //service is active
            player.stopSelf()
        }
    }
}