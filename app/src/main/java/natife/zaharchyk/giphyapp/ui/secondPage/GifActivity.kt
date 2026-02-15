package natife.zaharchyk.giphyapp.ui.secondPage

import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import natife.zaharchyk.giphyapp.databinding.ActivityGifBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target


class GifActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGifBinding
    private var gifUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gifUrl = intent.getStringExtra("gif_url")

        gifUrl?.let {
            Glide.with(this)
                .asGif()
                .load(it)
                .into(binding.imageView)
        }

        binding.btnSave.setOnClickListener {
            gifUrl?.let { url ->
                downloadAndSaveGif(url)
            }
        }
    }

    private fun downloadAndSaveGif(url: String) {
        Thread {
            try {
                val file = Glide.with(this)
                    .asFile()
                    .load(url)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get()

                saveToGallery(file)

                runOnUiThread {
                    Toast.makeText(this, "GIF збереено до галереї", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Помилка при збереженні GIF", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun saveToGallery(file: File) {

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "gif_${System.currentTimeMillis()}.gif")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/gif")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/GiphyApp")
        }

        val resolver = contentResolver
        val uri = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return

        val outputStream: OutputStream? = resolver.openOutputStream(uri)

        val inputStream = FileInputStream(file)

        inputStream.copyTo(outputStream!!)

        inputStream.close()
        outputStream.close()
    }
}