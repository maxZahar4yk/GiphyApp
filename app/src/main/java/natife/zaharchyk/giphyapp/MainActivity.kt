package natife.zaharchyk.giphyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import natife.zaharchyk.giphyapp.databinding.ActivityMainBinding
import natife.zaharchyk.giphyapp.ui.GifAdapter
import natife.zaharchyk.giphyapp.ui.secondPage.GifActivity
import natife.zaharchyk.giphyapp.viewmodel.GiphyViewModel
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GiphyViewModel by viewModels()
    private lateinit var adapter: GifAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupRecycler()
        setupSearch()
        setupPagination()
        observeViewModel()

        viewModel.search("gif")
    }

    private fun setupRecycler() {
        adapter = GifAdapter { gifUrl ->
            val intent = Intent(this, GifActivity::class.java)
            intent.putExtra("gif_url", gifUrl)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.search(query, 0)
                    hideKeyboard()
                }
                true
            } else false
        }
    }

    private fun setupPagination() {
        binding.btnNext.setOnClickListener {
            viewModel.nextPage()
        }

        binding.btnPrev.setOnClickListener {
            viewModel.previousPage()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observeViewModel() {

        viewModel.gifs.observe(this) { gifs ->
            adapter.submitList(gifs)

            binding.emptyState.visibility =
                if (gifs.isEmpty()) View.VISIBLE else View.GONE



            binding.pageIndicator.text =
                "Page ${viewModel.getCurrentPage() + 1}"

            binding.btnPrev.isEnabled =
                viewModel.getCurrentPage() > 0

            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility =
                if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            binding.errorText.visibility =
                if (error != null) View.VISIBLE else View.GONE
            binding.errorText.text = error
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
    }
}