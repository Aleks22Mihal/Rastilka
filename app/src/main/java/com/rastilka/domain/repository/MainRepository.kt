package com.rastilka.domain.repository

import android.net.Uri
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LogInBody
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.models.Transaction
import com.rastilka.domain.models.User
import com.rastilka.domain.models.UserWithCondition
import retrofit2.Response

interface MainRepository {

    suspend fun getSession(): Response<String>

    suspend fun getUserBySession(): Resource<User>

    suspend fun login(body: LogInBody): Resource<UserWithCondition>

    suspend fun logout(): Response<Unit>

    suspend fun getFamilyList(): Resource<List<User>>

    suspend fun attachUser(userOneId: String, userTwoId: String): Resource<Unit>

    suspend fun detachUser(userOneId: String, userTwoId: String): Resource<Unit>

    suspend fun sendPoint(toUserId: String, points: Int, comment: String?): Resource<User>

    suspend fun getPoint(fromUserId: String, points: Int, comment: String?): Resource<User>

    suspend fun getTransaction(): Resource<List<Transaction>>

    suspend fun getTaskOrWish(typeId: TypeIdForApi): Resource<List<TaskOrWish>>

    suspend fun getTasksOrWishes(productUrl: String): Resource<List<TaskOrWish>>

    suspend fun getTaskOrWish(productUrl: String): Resource<TaskOrWish>

    suspend fun createTaskOrWish(
        typeId: String,
        lastUrl: String,
        h1: String,
        price: String?,
        picture: Uri?,
    ): Resource<Unit>

    suspend fun deleteTaskOrWish(productUrl: String): Resource<Unit>

    suspend fun setResponsibleUser(productUrl: String, userId: String): Resource<Unit>

    suspend fun didResponsibleUser(productUrl: String, userId: String): Resource<Unit>

    suspend fun editTask(productUrl: String, property: String, value: String): Resource<Unit>
}