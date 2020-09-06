package dmitry.man.myaudioplayer

class MusicFiles(
    private var path: String,
    private var title: String,
    private var artist: String,
    private var album: String,
    private var duration: String
) {

    fun getPath(): String {
        return path
    }

    fun setPath(path: String) {
        this.path = path
    }

    fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getArtist(): String {
        return artist
    }

    fun setArtist(artist: String) {
        this.artist = artist
    }

    fun getAlbum(): String {
        return album
    }

    fun setAlbum(album: String) {
        this.album
    }

    fun getDuration(): String {
        return duration
    }

    fun setDuration(duration: String) {
        this.duration = duration
    }
}

