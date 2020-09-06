package dmitry.man.myaudioplayer

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MusicAdapter(mContext: Context?, mFiles: ArrayList<MusicFiles>) :
    RecyclerView.Adapter<MusicAdapter.MyViewHolder>() {

    private var mContext: Context? = mContext
    private var mFiles: ArrayList<MusicFiles> = mFiles

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var file_name: TextView = itemView.findViewById(R.id.music_file_name)
        internal var album_art: ImageView = itemView.findViewById(R.id.music_img)
        internal var artist_name: TextView = itemView.findViewById(R.id.music_group_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.file_name.text = mFiles[position].getTitle()
        holder.artist_name.text = mFiles[position].getArtist()
        val image: ByteArray? = getAlbumArt(mFiles[position].getPath())
        if (image != null) {
            mContext?.let {
                Glide.with(it).asBitmap()
                    .load(image)
                    .into(holder.album_art)
            }
        } else {
            mContext?.let {
                Glide.with(it)
                    .load(R.drawable.art_default)
                    .into(holder.album_art)
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, PlayerActivity::class.java)
            intent.putExtra("position", position)
            mContext?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    private fun getAlbumArt(uri: String): ByteArray? {
        val retriever: MediaMetadataRetriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art: ByteArray? = retriever.embeddedPicture
        retriever.release()
        return art
    }
}