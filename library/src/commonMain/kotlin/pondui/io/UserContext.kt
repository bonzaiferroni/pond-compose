package pondui.io

import androidx.lifecycle.viewModelScope
import kabinet.console.globalConsole
import pondui.ui.core.StateModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import pondui.LocalValueSource
import kabinet.model.User
import kabinet.model.LoginRequest
import kabinet.utils.obfuscate
import pondui.ui.core.ModelState

private val console = globalConsole.getHandle(UserContext::class)

class UserContext(
    private val apiClient: NeoApiClient,
    private val settingsValueRepository: LocalValueSource = LocalValueSource(),
    private val userStore: UserRepository = UserRepository(apiClient)
): StateModel<UserContextState>() {

    override val state = ModelState(UserContextState())

    var _cache = settingsValueRepository.readObjectOrNull() ?: UserContextCache()
    var cache
        get() = _cache
        set(value) = settingsValueRepository.writeObject(value).also { _cache = value }

    init {
        val (usernameOrEmail, stayLoggedIn, refreshToken) = cache
        if (usernameOrEmail != null && refreshToken != null) {
            login(LoginRequest(
                usernameOrEmail = usernameOrEmail,
                stayLoggedIn = stayLoggedIn,
                refreshToken = refreshToken
            ))
        }
        setState{ it.copy(
            isAnon = cache.usernameOrEmail == null,
            usernameOrEmail = cache.usernameOrEmail ?: "",
            stayLoggedIn = cache.stayLoggedIn
        )}
    }

    fun toggle() {
        setState { it.copy(dialogVisible = !it.dialogVisible) }
    }

    fun login() {
        println("logging in: ${stateNow.loginReady}")
        if (!stateNow.loginReady) return
        login(LoginRequest(
            usernameOrEmail = stateNow.usernameOrEmail,
            stayLoggedIn = stateNow.stayLoggedIn,
            password = stateNow.password.obfuscate(),
        ))
    }

    private fun login(request: LoginRequest) {
        setState { it.copy(isLoggingIn = true) }
        viewModelScope.launch {
            val auth = userStore.login(LoginRequest(
                usernameOrEmail = request.usernameOrEmail,
                stayLoggedIn = request.stayLoggedIn,
                refreshToken = request.refreshToken,
                password = request.password
            ))
            if (auth != null) {
                console.log("logged in")
                cache = cache.copy(refreshToken = auth.refreshToken)
                settingsValueRepository.writeObject(cache)
                val user = userStore.readUser()
                setState { it.copy(user = user, dialogVisible = false, isLoggingIn = false)}
            } else {
                console.logError("Unable to log in")
                setState { it.copy(dialogVisible = true, isLoggingIn = false)}
            }
        }
    }

    fun logout() {
        userStore.logout()
        setState { it.copy(user = null) }
    }

    fun setUsernameOrEmail(value: String) = setState { it.copy(usernameOrEmail = value) }
        .also { if (stateNow.saveLogin) cache = cache.copy(usernameOrEmail = value) }
    fun setPassword(value: String) = setState { it.copy(password = value) }
    fun setStayLoggedIn(value: Boolean) = setState { it.copy(stayLoggedIn = value) }
        .also { cache = cache.copy(stayLoggedIn = value) }
    fun setSaveLogin(value: Boolean) = setState { it.copy(saveLogin = value) }.also {
        if (value) {
            cache = cache.copy(usernameOrEmail = stateNow.usernameOrEmail)
        } else {
            cache = cache.copy(usernameOrEmail = null)
        }
    }
}

data class UserContextState(
    val user: User? = null,
    val isAnon: Boolean = true,
    val dialogVisible: Boolean = false,
    val usernameOrEmail: String = "",
    val password: String = "",
    val saveLogin: Boolean = true,
    val stayLoggedIn: Boolean = false,
    val isLoggingIn: Boolean = false,
) {
    val isLoggedIn get() = user != null
    val loginReady get() = usernameOrEmail.isNotEmpty() && password.isNotEmpty()
}

@Serializable
data class UserContextCache(
    val usernameOrEmail: String? = null,
    val stayLoggedIn: Boolean = false,
    val refreshToken: String? = null,
)

const val USER_CACHE_KEY = "user_cache_key"