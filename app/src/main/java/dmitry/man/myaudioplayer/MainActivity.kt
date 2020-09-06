package dmitry.man.myaudioplayer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1

    companion object {
        lateinit var musicFiles: ArrayList<MusicFiles>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permission()

    }

    private fun permission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            musicFiles = getAllAudio(this)
            initViewPager()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllAudio(this)
                initViewPager()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }
    }

    private fun initViewPager() {
        val viewpager: ViewPager = findViewById(R.id.viewpager)
        val tablayout: TabLayout = findViewById(R.id.tab_layout)
        val viewPagerAdapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragments(SongsFragment(), "Songs")
        viewPagerAdapter.addFragments(AlbumFragment(), "Albums")
        viewpager.adapter = viewPagerAdapter
        tablayout.setupWithViewPager(viewpager)
    }

    @Suppress("DEPRECATION")
    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments: ArrayList<Fragment> = ArrayList()
        private val titles: ArrayList<String> = ArrayList()

        internal fun addFragments(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }
    }

    private fun getAllAudio(context: Context): ArrayList<MusicFiles> {
        val tempAudioList: ArrayList<MusicFiles> = ArrayList()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA, //For Path
            MediaStore.Audio.Media.ARTIST
        )
        val cursor: Cursor? = context.contentResolver.query(
            uri, projection, null,
            null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val album: String = if (cursor.getString(0) == null) {
                    "unknown album"
                } else {
                    cursor.getString(0)
                }
                val title: String = if (cursor.getString(1) == null) {
                    "unknown title"
                } else {
                    cursor.getString(1)
                }
                val duration: String = if (cursor.getString(2) == null) {
                    "unknown duration"
                } else {
                    cursor.getString(2)
                }
                val path: String = if (cursor.getString(3) == null) {
                    "unknown path"
                } else {
                    cursor.getString(3)
                }
                val artist: String = if (cursor.getString(4) == null) {
                    "unknown artist"
                } else {
                    cursor.getString(4)
                }

                val musicFiles: MusicFiles = MusicFiles(path, title, artist, album, duration)
                Log.e("Path : $path", "Album : $album")
                tempAudioList.add(musicFiles)
            }
            cursor.close()
        }
        return tempAudioList
    }
}