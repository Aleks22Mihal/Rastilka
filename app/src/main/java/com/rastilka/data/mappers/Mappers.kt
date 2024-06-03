package com.rastilka.data.mappers

import com.rastilka.data.models.InfoUserTransactionDTO
import com.rastilka.data.models.TaskOrWishArrayDTO
import com.rastilka.data.models.TaskOrWishDTO
import com.rastilka.data.models.TaskOrWishFloatDTO
import com.rastilka.data.models.TaskOrWishUuidDTO
import com.rastilka.data.models.TaskOrWishValueDTO
import com.rastilka.data.models.TechnicalSupportMessageDTO
import com.rastilka.data.models.TransactionDTO
import com.rastilka.data.models.UserDTO
import com.rastilka.data.models.UserWithConditionDTO
import com.rastilka.domain.models.InfoUserTransaction
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.models.TaskOrWishArray
import com.rastilka.domain.models.TaskOrWishFloat
import com.rastilka.domain.models.TaskOrWishUuid
import com.rastilka.domain.models.TaskOrWishValue
import com.rastilka.domain.models.TechnicalSupportMessage
import com.rastilka.domain.models.Transaction
import com.rastilka.domain.models.User
import com.rastilka.domain.models.UserWithCondition

fun InfoUserTransactionDTO.mapToInfoUserTransaction() =
    InfoUserTransaction(
        phone = phone,
        transactionAccount = transactionAccount,
        surname = surname,
        name = name,
        mail = mail,
        picture = picture
    )

fun TransactionDTO.mapToTransaction(): Transaction = Transaction(
    status = status,
    id = id,
    comment = comment,
    transaction = transaction,
    date = date,
    recipeName = recipeName,
    authTransaction = authTransaction?.mapToInfoUserTransaction(),
    recipeTransactionId = recipeTransactionId,
    authTransactionId = authTransactionId,
    transactionString = transactionString,
    recipeTransaction = recipeTransaction?.mapToInfoUserTransaction()
)

fun UserDTO.mapToUser() =
    User(
        cargoType = cargoType,
        mail = mail,
        needDelivery = needDelivery,
        balance = balance,
        userType = userType,
        comment = comment,
        city = city,
        thisOrderIsntCreated = thisOrderIsntCreated,
        wantEdit = wantEdit,
        address = address,
        deliveryCost = deliveryCost,
        countPointsTotal = countPointsTotal,
        lang = lang,
        userExist = userExist,
        phone = phone,
        createDate = createDate,
        country = country,
        picture = picture,
        wantUsePoints = wantUsePoints,
        surname = surname,
        id = id,
        name = name,
        canUsePoints = canUsePoints
    )

fun UserWithConditionDTO.mapToUserWithCondition(): UserWithCondition = UserWithCondition(
    loginCondition = loginCondition,
    session = session,
    user = user?.mapToUser()
)

fun TaskOrWishValueDTO.mapToTaskOrWishValue(): TaskOrWishValue = TaskOrWishValue(
    parentId = parentId,
    isActive = isActive,
    url = url,
    description = description,
    photo = photo,
    h1 = h1,
    rowId = rowId,
    typeId = typeId,
    price = price,
    salePrice = salePrice,
    id = id,
    date = date,
    assembly = assembly
)

fun TaskOrWishArrayDTO.mapToTaskOrWishArray(): TaskOrWishArray = TaskOrWishArray(
    optionalImages = optionalImages,
    parentId = parentId
)

fun TaskOrWishUuidDTO.mapToTaskOrWishUuid() = TaskOrWishUuid(
    forUsers = forUsers,
    didUsers = didUsers
)

fun TaskOrWishFloatDTO.mapToTaskOrWishFloat(): TaskOrWishFloat = TaskOrWishFloat(
    price = price,
    salePrice = salePrice
)

fun TaskOrWishDTO.mapToTaskOrWish(): TaskOrWish = TaskOrWish(
    value = value.mapToTaskOrWishValue(),
    array = array.mapToTaskOrWishArray(),
    uuid = uuid.mapToTaskOrWishUuid(),
    float = float.mapToTaskOrWishFloat()
)

fun TechnicalSupportMessageDTO.maoToTechnicalSupportMessage(): TechnicalSupportMessage =
    TechnicalSupportMessage(
        message = message,
        time = time,
        name = name,
        picture = picture
    )
