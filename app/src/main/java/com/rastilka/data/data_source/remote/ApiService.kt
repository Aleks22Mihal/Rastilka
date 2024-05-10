package com.rastilka.data.data_source.remote

import com.rastilka.common.app_data.EditTaskBody
import com.rastilka.common.app_data.LogInBody
import com.rastilka.common.app_data.PriceBody
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.data.models.TaskOrWishDTO
import com.rastilka.data.models.TransactionDTO
import com.rastilka.data.models.UserDTO
import com.rastilka.data.models.UserWithConditionDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    //*Получить сессию*//*
    @GET("api/login")
    suspend fun getSession(
    ): Response<String>

    @GET("api/login/getUserBySession")
    suspend fun getUserBySession(
    ): Response<UserDTO>

    @POST("api/login/")
    suspend fun login(
        @Body body: LogInBody
    ): Response<UserWithConditionDTO>

    @GET("api/login/logout")
    suspend fun logout(): Response<Unit>

    @GET("api/friend/myFriends")
    suspend fun getFamilyList(): Response<List<UserDTO>>

    @GET("api/friend/attachUser/{userOneId}/{userTwoId}")
    suspend fun attachUser(
        @Path("userOneId") userOneId: String,
        @Path("userTwoId") userTwoId: String,
    ): Response<Unit>

    @GET("api/friend/detachUser/{userOneId}/{userTwoId}")
    suspend fun detachUser(
        @Path("userOneId") userOneId: String,
        @Path("userTwoId") userTwoId: String,
    ): Response<Unit>

    @POST("api/friend/send/{toUserId}/{points}")
    suspend fun sendPoint(
        @Path("toUserId") toUserId: String,
        @Path("points") points: Int,
        @Body body: PriceBody
    ): Response<UserDTO>

    @POST("api/friend/get/{fromUserId}/{points}")
    suspend fun getPoint(
        @Path("fromUserId") fromUserId: String,
        @Path("points") points: Int,
        @Body body: PriceBody
    ): Response<UserDTO>

    @GET("api/friend/transactions")
    suspend fun getTransaction(): Response<List<TransactionDTO>>

    @GET("api/friend/getFriendsProducts/{type_id}")
    suspend fun getTaskOrWish(
        @Path("type_id") typeId: TypeIdForApi
    ): Response<List<TaskOrWishDTO>>

    @GET("api/friend/userGetProducts/{productUrl}")
    suspend fun getTasksOrWishes(
        @Path("productUrl") productUrl: String,
    ): Response<List<TaskOrWishDTO>>

    @GET("api/friend/userGetOneProduct/{productUrl}")
    suspend fun getTaskOrWish(
        @Path("productUrl") productUrl: String,
    ): Response<TaskOrWishDTO>

    @Multipart
    @POST("api/friend/userAddProduct")
    suspend fun createTaskOrWish(
        @Part("type_id") typeId: RequestBody,
        @Part("lastUrl") lastUrl: RequestBody,
        @Part("h1") h1: RequestBody,
        @Part("description") description: RequestBody? = null,
        @Part("price") price: RequestBody? = null,
        @Part picture: MultipartBody.Part? = null,
        @Part("newFileName") newFileName: RequestBody? = "".toRequestBody()
    ): Response<Unit>

    @GET("api/friend/userDeleteProduct/{productUrl}")
    suspend fun deleteTaskOrWish(
        @Path("productUrl") productUrl: String,
    ): Response<Unit>

    @GET("api/friend/userProductFor/{productUrl}/{userTwoId}")
    suspend fun addResponsibleUser(
        @Path("productUrl") productUrl: String,
        @Path("userTwoId") userId: String,
    ): Response<Unit>

    @GET("api/friend/userProductDid/{productUrl}/{userTwoId}")
    suspend fun didResponsibleUser(
        @Path("productUrl") productUrl: String,
        @Path("userTwoId") userId: String,
    ): Response<Unit>

    @POST("api/friend/userEditProduct/{productUrl}/{property}")
    suspend fun editTaskOrWish(
        @Path("productUrl") productUrl: String,
        @Path("property") property: String,
        @Body body: EditTaskBody
    ): Response<Unit>

    @GET("api/friend/moveProducts/{type_id}/{urlFrom}/{urlTo}")
    suspend fun changeIndexProducts(
        @Path("type_id") typeId: TypeIdForApi,
        @Path("urlFrom") urlFrom: String,
        @Path("urlTo") urlTo: String,
    ): Response<Unit>

    /*
        *//*Получить языки*//*
    @GET("api/s/dictionaries")
    suspend fun getListLanguage(
    ): Response<List<Product>>

    *//*Получить перевод сервера*//*
    @GET("{language}/api/allTranslate")
    suspend fun getListAllTranslateServer(
        @Path("language") language: String = MainViewModel.languagePrefix
    ): Response<Map<String, String>>

    *//*Получить перевод клиента*//*
    @GET("{language}/api/info/allinfo")
    suspend fun getListAllTranslateClient(
        @Path("language") language: String = MainViewModel.languagePrefix
    ): Response<Map<String, String>>

    *//*Получить каталог скидок*//*
    @GET("{language}/api/s/stocks")
    suspend fun getStocks(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Query("range") range: Int = 0,
        @Query("sort") sort: String = "less"
    ): Response<List<Product>>

    *//*Получить катало*//*
    @GET("{language}/api/")
    suspend fun getCatalog(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Query("range") range: Int = 0,
        @Query("sort") sort: String = "less"
    ): Response<List<Product>>

    *//*Получить секцию*//*
    @GET("{language}/api/s/{url}")
    suspend fun getSection(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Path("url") url: String,
        @Query("range") range: Int = 0,
        @Query("sort") sort: String = "less"
    ): Response<List<Product>>

    *//*Получить Юзера*//*
    @GET("{language}/api/login/getUserBySession")
    suspend fun getUser(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
    ): Response<User>

    *//*Зарегистрироваться*//*
    @POST("{language}/api/login/register")
    suspend fun registration(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Body body: RegistrationBody
    ): Response<LoginConditions>

    *//*Разлогиниться*//*
    @GET("{language}/api/login/logout")
    suspend fun logOut(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
    ): Response<Unit>

    *//*Залогинится*//*
    @POST("{language}/api/login")
    suspend fun logIn(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Body body: LogInBody,
    ): Response<LoginConditions>

    *//*Изменения пользователя*//*
    @Multipart
    @POST("{language}/api/cabinet/profilePut")
    suspend fun editUserAndPassword(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("mail") mail: RequestBody,
        @Part("surname") surname: String? = "",
        @Part("password") password: RequestBody?,
        @Part("pictureData") pictureData: RequestBody?,
    ): Response<User>

    *//*Получение продкута*//*
    @GET("{language}/api/p/{url}")
    suspend fun getProduct(
        @Path("url") url: String,
        @Path("language") language: String = MainViewModel.languagePrefix,
    ): Response<Product>

    *//*Получение Корзину*//*
    @GET("{language}/api/renewShoppingCart")
    suspend fun getProductsInShoppingCart(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
    ): Response<List<ProductShoppingCart>>

    *//*Получение суммы Корзину*//*
    @GET("{language}/api/shoppingCartSums")
    suspend fun getShoppingCartSum(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
    ): Response<ShoppingCartSum>

    *//*Долбавить товар в корзину*//*
    @GET("{language}/api/add/{productId}/{count}")
    suspend fun addProductsInShoppingCart(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Path("productId") productId: String,
        @Path("count") count: Int,
    ): Response<List<ProductShoppingCart>>

    *//*ИЗменить колличество товара*//*
    @GET("{language}/api/put/{productId}/{count}")
    suspend fun changeCountProductInShoppingCart(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Path("productId") productId: String,
        @Path("count") count: Int,
    ): Response<List<ProductShoppingCart>>

    *//*Удалть товар из корзины*//*
    @GET("{language}/api/delete/{productId}/")
    suspend fun deleteProductInShoppingCart(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Path("productId") productId: String,
    ): Response<List<ProductShoppingCart>>

    @GET("{language}/api/cabinet/getOrders")
    suspend fun getOrders(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
    ): Response<List<Order>>

    @GET("{language}/api/checkout/usePoints/{isUsePoints}")
    suspend fun usePoints(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Path("isUsePoints") isUsePoints: Boolean,
    ): Response<Int>

    @POST("{language}/api/checkout")
    suspend fun registrationUserWithoutPassword(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Body body: RegistrationWithoutPasswordBody,
    ): Response<LoginConditions>

    @POST("{language}/api/checkout/setComment")
    suspend fun setComment(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Body body: CommentBody,
    ): Response<User>

    @POST("{language}/api/checkout/goToCabinet")
    suspend fun completeRegistration(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Body body: PasswordBody,
    ): Response<User>

    @GET("{language}/api/checkout/review")
    suspend fun completeOrder(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
    ): Response<Unit>

    @GET("{language}/api/cabinet/getTickets")
    suspend fun getTickets(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
    ): Response<List<TechnicalSupportMessage>>

    *//*Отправить сообщение в Тех.Под.*//*
    @POST("{language}/api/cabinet/postTickets")
    suspend fun postTickets(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Body body: Message,
    ): Response<List<TechnicalSupportMessage>>

    @POST("{language}/api/login/forget")
    suspend fun forgetPassword(
        @Header("Authorization") session: String = "BearerSession ${MainViewModel.sessionKey}",
        @Path("language") language: String = MainViewModel.languagePrefix,
        @Body body: MailBody,
    ): Response<Unit>*/
}