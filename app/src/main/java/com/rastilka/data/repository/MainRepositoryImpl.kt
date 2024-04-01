package com.rastilka.data.repository

import android.net.Uri
import com.rastilka.common.Resource
import com.rastilka.common.app_data.EditTaskBody
import com.rastilka.common.app_data.LogInBody
import com.rastilka.common.app_data.PriceBody
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.data.data_source.remote.ApiService
import com.rastilka.data.mappers.mapToTaskOrWish
import com.rastilka.data.mappers.mapToTransaction
import com.rastilka.data.mappers.mapToUser
import com.rastilka.data.mappers.mapToUserWithCondition
import com.rastilka.data.utilits.image_upload.ImageUpload
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.models.Transaction
import com.rastilka.domain.models.User
import com.rastilka.domain.models.UserWithCondition
import com.rastilka.domain.repository.MainRepository
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val upload: ImageUpload
) : MainRepository {

    override suspend fun getSession(): Response<String> {
        return apiService.getSession()
    }

    override suspend fun getUserBySession(): Resource<User> {
        return try {
            val response = apiService.getUserBySession()
            if (response.isSuccessful) {
                val result = response.body()?.mapToUser()
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun login(body: LogInBody): Resource<UserWithCondition> {
        return try {
            val response = apiService.login(body)
            if (response.isSuccessful) {
                val result = response.body()?.mapToUserWithCondition()
                Resource.Success(result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun logout(): Response<Unit> {
        return apiService.logout()
    }

    override suspend fun getFamilyList(): Resource<List<User>> {
        return try {
            val response = apiService.getFamilyList()
            if (response.isSuccessful) {
                val result = response.body()?.map { userDTO ->
                    userDTO.mapToUser()
                }
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun attachUser(userOneId: String, userTwoId: String): Resource<Unit> {
        return try {
            val response = apiService.attachUser(userOneId, userTwoId)
            if (response.isSuccessful) {
                Resource.Success(data = null)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun detachUser(userOneId: String, userTwoId: String): Resource<Unit> {
        return try {
            val response = apiService.detachUser(userOneId, userTwoId)
            if (response.isSuccessful) {
                Resource.Success(data = null)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun sendPoint(
        toUserId: String,
        points: Int,
        comment: String?
    ): Resource<User> {
        return try {
            Resource.Loading<User>()
            val response = apiService.sendPoint(
                toUserId = toUserId,
                points = points,
                body = PriceBody(comment)
            )
            if (response.isSuccessful) {
                val result = response.body()?.mapToUser()
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun getPoint(
        fromUserId: String,
        points: Int,
        comment: String?
    ): Resource<User> {
        return try {
            Resource.Loading<User>()
            val response = apiService.getPoint(
                fromUserId = fromUserId,
                points = points,
                body = PriceBody(comment)
            )
            if (response.isSuccessful) {
                val result = response.body()?.mapToUser()
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun getTransaction(): Resource<List<Transaction>> {
        return try {
            Resource.Loading<List<Transaction>>()
            val response = apiService.getTransaction()
            if (response.isSuccessful) {
                val result = response.body()?.map { transactionDTO ->
                    transactionDTO.mapToTransaction()
                }
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun getTaskOrWish(typeId: TypeIdForApi): Resource<List<TaskOrWish>> {
        return try {
            Resource.Loading<List<TaskOrWish>>()
            val response = apiService.getTaskOrWish(typeId)
            if (response.isSuccessful) {
                val result = response.body()?.map { taskOrWishDTO ->
                    taskOrWishDTO.mapToTaskOrWish()
                }
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun getTasksOrWishes(productUrl: String): Resource<List<TaskOrWish>> {
        return try {
            Resource.Loading<List<TaskOrWish>>()
            val response = apiService.getTasksOrWishes(productUrl)
            if (response.isSuccessful) {
                val result = response.body()?.map { taskOrWishDTO ->
                    taskOrWishDTO.mapToTaskOrWish()
                }
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun getTaskOrWish(productUrl: String): Resource<TaskOrWish> {
        return try {
            Resource.Loading<TaskOrWish>()
            val response = apiService.getTaskOrWish(productUrl)
            if (response.isSuccessful) {
                val result = response.body()?.mapToTaskOrWish()
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun createTaskOrWish(
        typeId: String,
        lastUrl: String,
        h1: String,
        price: String?,
        picture: Uri?,
    ): Resource<Unit> {
        return try {
            Resource.Loading<Unit>()
            val response = apiService.createTaskOrWish(
                typeId = typeId.toRequestBody(),
                lastUrl = lastUrl.toRequestBody(),
                h1 = h1.toRequestBody(),
                price = price?.toRequestBody(),
                picture = upload.createMultipartImage(picture),
            )
            if (response.isSuccessful) {
                Resource.Success(data = response.body())
            } else {
                Resource.Error(message = "Что-то пошло не так")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что-то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun deleteTaskOrWish(productUrl: String): Resource<Unit> {
        return try {
            Resource.Loading<Unit>()
            val response = apiService.deleteTaskOrWish(productUrl)
            if (response.isSuccessful) {
                Resource.Success(data = response.body())
            } else {
                Resource.Error(message = "Что-то пошло не так")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что-то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun setResponsibleUser(productUrl: String, userId: String): Resource<Unit> {
        return try {
            Resource.Loading<Unit>()
            val response = apiService.addResponsibleUser(productUrl, userId)
            if (response.isSuccessful) {
                Resource.Success(data = response.body())
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun didResponsibleUser(productUrl: String, userId: String): Resource<Unit> {
        return try {
            Resource.Loading<Unit>()
            val response = apiService.didResponsibleUser(productUrl, userId)
            if (response.isSuccessful) {
                Resource.Success(data = response.body())
            } else {
                Resource.Error(message = "Данные не получены")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun editTask(
        productUrl: String,
        property: String,
        value: String
    ): Resource<Unit> {
        return try {
            Resource.Loading<Unit>()
            val response = apiService.editTaskOrWish(
                productUrl = productUrl,
                property = property,
                body = EditTaskBody(value = value)
            )
            if (response.isSuccessful) {
                Resource.Success(data = response.body())
            } else {
                Resource.Error(message = "Что-то пошло не так")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что-то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }
}