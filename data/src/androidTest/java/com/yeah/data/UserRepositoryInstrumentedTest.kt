import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yeah.data.api.UserApi
import com.yeah.data.api.model.DataUser
import com.yeah.data.api.model.LoginRequest
import com.yeah.data.api.model.UserResponse
import com.yeah.data.db.AppDatabase
import com.yeah.data.db.UserDao
import com.yeah.data.db.model.UserDbo
import com.yeah.data.repository.UserRepository
import com.yeah.utils.ISharedPreferencesManager
import com.yeah.utils.SharedPreferencesManager
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class UserRepositoryInstrumentedTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var userApi: UserApi
    private lateinit var sharedPreferencesManager: ISharedPreferencesManager
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
        userApi = mock(UserApi::class.java)
        sharedPreferencesManager = FakeSharedPreferencesManager()
        userRepository = UserRepository(userApi, userDao, sharedPreferencesManager)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun loginSaveUserAndToken() = runBlocking {
        // Arrange
        val loginRequest = LoginRequest("test@example.com", "password")
        val userResponse = UserResponse(
            token = "valid-token",
            user = DataUser(
                id = "123",
                email = "test@example.com",
                phoneNumber = "123456789",
                first_name = "John",
                middle_name = "M",
                last_name = "Doe",
                isAuthenticated = true,
                paid_hours = 10,
                a_hours = 5,
                b_hours = 3,
                c_hours = 2,
                user_role = listOf("student"),
                name = "",
                value = ""
            ),
            errorMessage = null
        )
        `when`(userApi.login(loginRequest)).thenReturn(userResponse)

        // Act
        val result = userRepository.login("test@example.com", "password")

        // Assert
        val dbUser = userDao.getUserById("123")
        assertEquals("John", dbUser?.firstName)
        assertEquals("John", result?.user?.firstName)
    }
}

