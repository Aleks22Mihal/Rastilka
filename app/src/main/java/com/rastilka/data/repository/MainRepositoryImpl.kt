package com.rastilka.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.rastilka.common.Resource
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.data.data_source.remote.ApiService
import com.rastilka.data.mappers.maoToTechnicalSupportMessage
import com.rastilka.data.mappers.mapToTaskOrWish
import com.rastilka.data.mappers.mapToTransaction
import com.rastilka.data.mappers.mapToUser
import com.rastilka.data.mappers.mapToUserWithCondition
import com.rastilka.data.models.EditTaskBody
import com.rastilka.data.models.PriceBody
import com.rastilka.data.models.UserWithConditionDTO
import com.rastilka.data.utilits.image_upload.ImageUpload
import com.rastilka.data.utilits.image_upload.NameUploadImage
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.models.TechnicalSupportMessage
import com.rastilka.domain.models.Transaction
import com.rastilka.domain.models.User
import com.rastilka.domain.models.UserWithCondition
import com.rastilka.domain.repository.MainRepository
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val upload: ImageUpload
) : MainRepository {

    override suspend fun getSession(): Resource<String> {
        return try {
            val response = apiService.getSession()
            if (response.isSuccessful) {
                val result = response.body()
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

    override suspend fun login(
        email: String,
        password: String
    ): Resource<UserWithCondition> {
        return try {
            val response = apiService.login(
                email = email.toRequestBody(),
                password = password.toRequestBody()
            )
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

    override suspend fun logout(): Resource<Unit> {
        return try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(message = "Что - то пошло не так")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun registration(
        name: String,
        email: String,
        password: String
    ): Resource<UserWithCondition> {
        return try {
            val response = apiService.registration(
                name = name.toRequestBody(),
                email = email.toRequestBody(),
                password = password.toRequestBody()
            )
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

    override suspend fun forgetPassword(email: String): Resource<Unit> {
        return try {
            val response = apiService.forgetPassword(email = email.toRequestBody())
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(message = "Что - то пошло не так")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
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

    override suspend fun attachUser(userOneId: String, userTwoId: String): Resource<User> {
        return try {
            val response = apiService.attachUser(userOneId, userTwoId)
            if (response.isSuccessful) {
                Resource.Success(data = response.body()?.mapToUser())
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
        points: Long,
        comment: String?
    ): Resource<User> {
        return try {
            Resource.Loading<User>()
            val response = apiService.sendPoint(
                toUserId = toUserId,
                points = points,
                body = PriceBody(comment = comment)
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
        points: Long,
        comment: String?
    ): Resource<User> {
        return try {
            Resource.Loading<User>()
            val response = apiService.getPoint(
                fromUserId = fromUserId,
                points = points,
                body = PriceBody(comment = comment)
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
        date: String?,
        forUserId: String?,
    ): Resource<Unit> {
        return try {
            Resource.Loading<Unit>()
            val response = apiService.createTaskOrWish(
                typeId = typeId.toRequestBody(),
                lastUrl = lastUrl.toRequestBody(),
                h1 = h1.toRequestBody(),
                price = price?.toRequestBody(),
                date = date?.toRequestBody(),
                forUserId = forUserId?.toRequestBody(),
                picture = upload.createMultipartImage(
                    selectedImage = picture,
                    nameUploadImage = NameUploadImage.picture.name
                ),
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
                Resource.Error(message = "Что-то пошло не так")
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
                Resource.Error(message = "Что-то пошло не так")
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

    override suspend fun changeIndexProducts(
        typeId: TypeIdForApi,
        urlFrom: String,
        urlTo: String
    ): Resource<Unit> {
        return try {
            Resource.Loading<Unit>()
            val response = apiService.changeIndexProducts(
                typeId = typeId,
                urlFrom = urlFrom,
                urlTo = urlTo,
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

    override suspend fun editUserAndPassword(
        name: String,
        email: String,
        password: String?,
        pictureUri: String?
    ): Resource<User> {
        return try {
            val response = apiService.editUserAndPassword(
                name = name.toRequestBody(),
                email = email.toRequestBody(),
                password = password?.toRequestBody(),
                pictureData = upload.createMultipartImage(
                    selectedImage = pictureUri?.toUri(),
                    nameUploadImage = NameUploadImage.pictureData.name
                )
            )
            if (response.isSuccessful) {
                val result = response.body()?.mapToUser()
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Что-то пошло не так")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun getTickets(): Resource<List<TechnicalSupportMessage>> {
        return try {
            val response = apiService.getTickets()
            if (response.isSuccessful) {
                val result = response.body()?.map {
                    it.maoToTechnicalSupportMessage()
                }
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Что-то пошло не так")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }

    override suspend fun postTickets(message: String): Resource<List<TechnicalSupportMessage>> {
        return try {
            val response = apiService.postTickets(
                message = message.toRequestBody()
            )
            if (response.isSuccessful) {
                val result = response.body()?.map {
                    it.maoToTechnicalSupportMessage()
                }
                Resource.Success(data = result)
            } else {
                Resource.Error(message = "Что-то пошло не так")
            }
        } catch (e: HttpException) {
            Resource.Error(message = e.localizedMessage ?: "Что - то пошло не так")
        } catch (e: IOException) {
            Resource.Error(message = "Нет интернета")
        }
    }
}
