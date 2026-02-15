package natife.zaharchyk.giphyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import natife.zaharchyk.giphyapp.data.model.GifObject
import natife.zaharchyk.giphyapp.data.repository.GiphyRepository
import kotlinx.coroutines.launch


class GiphyViewModel : ViewModel() {
    private val repository = GiphyRepository()
    private var currentPage = 0
    private val pageSize = 25
    private var currentQuery = "gif"


    private val _gifs = MutableLiveData<List<GifObject>>()
    val gifs: LiveData<List<GifObject>> = _gifs

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun search(query: String, page: Int = 0) {
        currentQuery = query
        currentPage = page
        val offset = page * pageSize

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.searchGifs(
                query = query,
                limit = pageSize,
                offset = offset
            )

            result.onSuccess {
                _gifs.value = it
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }

            _isLoading.value = false
        }
    }

    fun nextPage() {
        search(currentQuery, currentPage + 1)
    }

    fun previousPage() {
        if (currentPage > 0) {
            search(currentQuery, currentPage - 1)
        }
    }

    fun refresh() {
        search(currentQuery, currentPage)
    }

    fun getCurrentPage(): Int = currentPage
}