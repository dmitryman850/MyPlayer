package dmitry.man.myaudioplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dmitry.man.myaudioplayer.MainActivity.Companion.musicFiles

class PlayerActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {

    private lateinit var song_name: TextView
    private lateinit var artist_name: TextView
    private lateinit var duration_played: TextView
    private lateinit var duration_total: TextView
    private lateinit var cover_art: ImageView
    private lateinit var nextBtn: ImageView
    private lateinit var prevBtn: ImageView
    private lateinit var backBtn: ImageView
    private lateinit var shuffleBtn: ImageView
    private lateinit var repeatBtn: ImageView
    private lateinit var playPauseBtn: FloatingActionButton
    private lateinit var seekBar: SeekBar
    private var position: Int = -1

    companion object {
        private var listSongs: ArrayList<MusicFiles> = ArrayList()
        private lateinit var uri: Uri
        private var mediaPlayer: MediaPlayer? = null
    }

    private val handler: Handler = Handler()
    private lateinit var playThread: Thread
    private lateinit var prevThread: Thread
    private lateinit var nextThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initViews()
        getIntentMethod()
        song_name.text = listSongs[position].getTitle()
        artist_name.text = listSongs[position].getArtist()
        mediaPlayer?.setOnCompletionListener(this)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer?.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        this@PlayerActivity.runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    val mCurrentPosition: Int = mediaPlayer?.currentPosition?.div(1000) ?: 0
                    seekBar.progress = mCurrentPosition
                    duration_played.text = formattedTime(mCurrentPosition)
                }
                handler.postDelayed(this, 1000)
            }

        })
    }

    override fun onResume() {
        playThreadBtn()
        nextThreadBtn()
        prevThreadBtn()
        super.onResume()
    }

    private fun prevThreadBtn() {
        prevThread = object : Thread() {
            override fun run() {
                super.run()
                prevBtn.setOnClickListener {
                    prevBtnClicked()
                }
            }

        }
        prevThread.start()
    }

    private fun prevBtnClicked() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            position = if ((position - 1) < 0) {
                (listSongs.size - 1)
            } else {
                (position - 1)
            }
            uri = Uri.parse(listSongs[position].getPath())
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.text = listSongs[position].getTitle()
            artist_name.text = listSongs[position].getArtist()
            seekBar.max = (mediaPlayer?.duration)?.div(1000) ?: 0
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition: Int = mediaPlayer?.currentPosition?.div(1000) ?: 0
                        seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
            mediaPlayer?.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause)
            mediaPlayer?.start()
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            position = if ((position - 1) < 0) {
                (listSongs.size - 1)
            } else {
                (position - 1)
            }
            uri = Uri.parse(listSongs[position].getPath())
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.text = listSongs[position].getTitle()
            artist_name.text = listSongs[position].getArtist()
            seekBar.max = (mediaPlayer?.duration)?.div(1000) ?: 0
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition: Int = mediaPlayer?.currentPosition?.div(1000) ?: 0
                        seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
            mediaPlayer?.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.ic_play)
        }
    }

    private fun nextThreadBtn() {
        nextThread = object : Thread() {
            override fun run() {
                super.run()
                nextBtn.setOnClickListener {
                    nextBtnClicked()
                }
            }

        }
        nextThread.start()
    }

    private fun nextBtnClicked() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            position = ((position + 1) % listSongs.size)
            uri = Uri.parse(listSongs[position].getPath())
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.text = listSongs[position].getTitle()
            artist_name.text = listSongs[position].getArtist()
            seekBar.max = (mediaPlayer?.duration)?.div(1000) ?: 0
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition: Int = mediaPlayer?.currentPosition?.div(1000) ?: 0
                        seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
            mediaPlayer?.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause)
            mediaPlayer?.start()
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            position = ((position + 1) % listSongs.size)
            uri = Uri.parse(listSongs[position].getPath())
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.text = listSongs[position].getTitle()
            artist_name.text = listSongs[position].getArtist()
            seekBar.max = (mediaPlayer?.duration)?.div(1000) ?: 0
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition: Int = mediaPlayer?.currentPosition?.div(1000) ?: 0
                        seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
            mediaPlayer?.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.ic_play)
        }
    }

    private fun playThreadBtn() {
        playThread = object : Thread() {
            override fun run() {
                super.run()
                playPauseBtn.setOnClickListener {
                    playPauseBtnClicked()
                }
            }

        }
        playThread.start()
    }

    private fun playPauseBtnClicked() {
        if (mediaPlayer!!.isPlaying) {
            playPauseBtn.setImageResource(R.drawable.ic_play)
            mediaPlayer?.pause()
            seekBar.max = (mediaPlayer?.duration)?.div(1000) ?: 0
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition: Int = mediaPlayer?.currentPosition?.div(1000) ?: 0
                        seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
        } else {
            playPauseBtn.setImageResource(R.drawable.ic_pause)
            mediaPlayer?.start()
            seekBar.max = (mediaPlayer?.duration)?.div(1000) ?: 0
            this@PlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition: Int = mediaPlayer?.currentPosition?.div(1000) ?: 0
                        seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }

            })
        }
    }

    private fun formattedTime(mCurrentPosition: Int): String {
        var totalOut = ""
        var totalNew = ""
        val seconds = (mCurrentPosition % 60).toString()
        val minutes = (mCurrentPosition / 60).toString()
        totalOut = "$minutes:$seconds"
        totalNew = "$minutes:0$seconds"
        return if (seconds.length == 1) {
            totalNew
        } else {
            totalOut
        }
    }

    private fun getIntentMethod() {
        position = intent.getIntExtra("position", -1)
        listSongs = musicFiles
        if (listSongs != null) {
            playPauseBtn.setImageResource(R.drawable.ic_pause)
            uri = Uri.parse(listSongs[position].getPath())
        }
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer?.start()
        } else {
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer?.start()
        }
        seekBar.max = mediaPlayer?.duration?.div(1000) ?: 0
        metaData(uri)
    }

    private fun initViews() {
        song_name = findViewById(R.id.song_name)
        artist_name = findViewById(R.id.song_artist)
        duration_played = findViewById(R.id.duration_played)
        duration_total = findViewById(R.id.duration_total)
        cover_art = findViewById(R.id.cover_art)
        nextBtn = findViewById(R.id.id_next)
        prevBtn = findViewById(R.id.id_prev)
        backBtn = findViewById(R.id.back_btn)
        shuffleBtn = findViewById(R.id.id_shuffle)
        repeatBtn = findViewById(R.id.id_repeat)
        playPauseBtn = findViewById(R.id.play_pause)
        seekBar = findViewById(R.id.seek_bar)
    }

    private fun metaData(uri: Uri) {
        val retriever: MediaMetadataRetriever = MediaMetadataRetriever()
        retriever.setDataSource(uri.toString())
        val durationTotal = (listSongs[position].getDuration()).toInt() / 1000
        duration_total.text = formattedTime(durationTotal)
        val art: ByteArray? = retriever.embeddedPicture
        val bitmap: Bitmap
        if (art != null) {
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
            imageAnimation(this, cover_art, bitmap)
            Palette.from(bitmap).generate(object : Palette.PaletteAsyncListener {
                override fun onGenerated(palette: Palette?) {
                    val swatch: Palette.Swatch? = palette?.dominantSwatch
                    if (swatch != null) {
                        val gradient: ImageView = findViewById(R.id.imageViewGradient)
                        val mContainer: RelativeLayout = findViewById(R.id.mContainer)
                        gradient.setBackgroundResource(R.drawable.gradient_bg)
                        mContainer.setBackgroundResource(R.drawable.main_bg)
                        val gradientDrawable = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(swatch.rgb, 0x00000000)
                        )
                        gradient.background = gradientDrawable
                        val gradientDrawableBg = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(swatch.rgb, swatch.rgb)
                        )
                        mContainer.background = gradientDrawableBg
                        song_name.setTextColor(swatch.titleTextColor)
                        artist_name.setTextColor(swatch.bodyTextColor)
                    } else {
                        val gradient: ImageView = findViewById(R.id.imageViewGradient)
                        val mContainer: RelativeLayout = findViewById(R.id.mContainer)
                        gradient.setBackgroundResource(R.drawable.gradient_bg)
                        mContainer.setBackgroundResource(R.drawable.main_bg)
                        val gradientDrawable = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(0xf000000, 0x00000000)
                        )
                        gradient.background = gradientDrawable
                        val gradientDrawableBg = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(0xf000000, 0xf000000)
                        )
                        mContainer.background = gradientDrawableBg
                        song_name.setTextColor(Color.WHITE)
                        artist_name.setTextColor(Color.DKGRAY)
                    }
                }
            })
        } else {
            Glide.with(this)
                .asBitmap()
                .load(R.drawable.art_default)
                .into(cover_art)
            val gradient: ImageView = findViewById(R.id.imageViewGradient)
            val mContainer: RelativeLayout = findViewById(R.id.mContainer)
            gradient.setBackgroundResource(R.drawable.gradient_bg)
            mContainer.setBackgroundResource(R.drawable.main_bg)
            song_name.setTextColor(Color.WHITE)
            artist_name.setTextColor(Color.DKGRAY)
        }
    }

    private fun imageAnimation(context: Context, imageView: ImageView, bitmap: Bitmap) {
        val animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        val animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        animOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                Glide.with(context).load(bitmap).into(imageView)
                animIn.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {

                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }
                })
                imageView.startAnimation(animIn)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        imageView.startAnimation(animOut)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        nextBtnClicked()
        if (mediaPlayer != null) {
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener(this)
        }
    }
}

