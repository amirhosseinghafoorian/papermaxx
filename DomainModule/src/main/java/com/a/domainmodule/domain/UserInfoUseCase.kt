package com.a.domainmodule.domain

import com.a.remotemodule.remotes.UserRemote
import java.util.*
import javax.inject.Inject

class UserInfoUseCase @Inject constructor(
    private val userRemote: UserRemote
) {

    fun setUserInfo(name: String, username: String) = userRemote.setUserInfo(
        name,
        username.toLowerCase(Locale.ROOT)
    )

}