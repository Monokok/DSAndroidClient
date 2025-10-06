import com.yeah.utils.ISharedPreferencesManager

class FakeSharedPreferencesManager : ISharedPreferencesManager {
    private var token: String? = null

    override fun saveAuthToken(token: String) {
        this.token = token
    }

    override fun getAuthToken(): String? {
        return token
    }

    override fun clearAuthToken() {
        token = null
    }
}