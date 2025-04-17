package pondui.io

import kabinet.api.UserApi
import kabinet.model.Auth
import kabinet.model.LoginRequest
import kabinet.model.User

class UserStore : ApiStore() {

    suspend fun login(request: LoginRequest): Auth? = client.login(request)

    suspend fun readUser(): User = client.getSameData(UserApi.Users.GetUser)

    // returns null if successful, otherwise returns an error message
    // suspend fun createUser(info: SignUpRequest): SignUpResult = client.post(Api.user, info)
    // suspend fun updateUser(info: EditUserRequest): Boolean = client.put(Api.user, info)
    // suspend fun getPrivateInfo(): PrivateInfo = client.get(Api.privateInfo)
}