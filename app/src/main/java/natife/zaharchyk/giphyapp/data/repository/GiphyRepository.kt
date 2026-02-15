package natife.zaharchyk.giphyapp.data.repository

import natife.zaharchyk.giphyapp.data.api.RetrofitClient
import natife.zaharchyk.giphyapp.data.model.GifObject

class GiphyRepository {

    suspend fun searchGifs(query: String, limit: Int, offset: Int): Result<List<GifObject>> {
        return try {
            val response = RetrofitClient.api.searchGifs(
                apiKey = "VN6jteMPAvPzgXQALzUSbNSADKByCsqR",
                query = query,
                limit = limit,
                offset = offset
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}