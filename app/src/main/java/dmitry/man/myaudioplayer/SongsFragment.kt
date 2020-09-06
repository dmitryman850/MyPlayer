package dmitry.man.myaudioplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dmitry.man.myaudioplayer.MainActivity.Companion.musicFiles

class SongsFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var musicAdapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_songs, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        if (!(musicFiles.size < 1)) {
            musicAdapter = MusicAdapter(context, musicFiles)
            recyclerView.adapter = musicAdapter
            recyclerView.layoutManager = LinearLayoutManager(
                context, RecyclerView.VERTICAL,
                false
            )
        }
        return view
    }

}