package com.rastilka.common.utilits_support_preview

import com.rastilka.common.app_data.StatusUser
import com.rastilka.common.app_data.TypeId
import com.rastilka.domain.models.InfoUserTransaction
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.models.TaskOrWishArray
import com.rastilka.domain.models.TaskOrWishFloat
import com.rastilka.domain.models.TaskOrWishUuid
import com.rastilka.domain.models.TaskOrWishValue
import com.rastilka.domain.models.Transaction
import com.rastilka.domain.models.User

object SupportPreview {
    val user: User = User(
        needDelivery = false,
        createDate = "2023-12-08T00:00:00Z",
        //    messageUnread = 1,
        surname = "Дамиан",
        //  commentPayment = "",
        mail = "idamiansun@icloud.com",
        userType = StatusUser.customer,
        deliveryCost = 0,
        comment = "",
        userExist = true,
        canUsePoints = 12,
        //    lastMessageDate = "2023-12-09T18:00:59Z",
        id = "C080D0CE-5114-46DC-ABF0-5BAAD5CB4A451",
        //    liftingCargo = 0,
        phone = "",
        wantUsePoints = false,
        // orderBonus = "0.0",
        //   counPointsTotal = 0,
        //  lastMessage = "привет",
        thisOrderIsntCreated = true,
        balance = 0,
        name = "Данилов Дамиан Евгеневич",
        //   commentDelivery = "",
        picture = "/img/avatars/C080D0CE-5114-46DC-ABF0-5BAAD5CB4A45old.jpeg",
        //   liftingCargoString = "0",
        //   deliveryCostString = "0"
    )
    val user2: User = User(
        needDelivery = false,
        createDate = "2023-12-08T00:00:00Z",
        //    messageUnread = 1,
        surname = "Дамиан",
        //  commentPayment = "",
        mail = "idamiansun@icloud.com",
        userType = StatusUser.customer,
        deliveryCost = 0,
        comment = "",
        userExist = true,
        canUsePoints = 12,
        //    lastMessageDate = "2023-12-09T18:00:59Z",
        id = "C080D0CE-5114-46DC-ABF0-5BAAD5CB4A45",
        //    liftingCargo = 0,
        phone = "",
        wantUsePoints = false,
        // orderBonus = "0.0",
        //   counPointsTotal = 0,
        //  lastMessage = "привет",
        thisOrderIsntCreated = true,
        balance = 0,
        name = "Дамиан",
        //   commentDelivery = "",
        picture = "/img/avatars/C080D0CE-5114-46DC-ABF0-5BAAD5CB4A45old.jpeg",
        //   liftingCargoString = "0",
        //   deliveryCostString = "0"
    )
    val listFamily = listOf<User>(user, user2)
    val transaction = Transaction(
        status = "done",
        id = "2549AAC4-A0E5-4DC8-BFFF-EB632A2CE46E",
        comment = "get",
        transaction = -25,
        date = "2024-01-03T17:39:53Z",
        recipeName = "Леша тест",
        recipeTransactionId = "1C446D55-E663-4D88-A37E-8D752EFBCA80",
        authTransaction = InfoUserTransaction(
            name = "Леша Тест"
        ),
        recipeTransaction = InfoUserTransaction(
            name = "Очень длинное слово"
        ),
        authTransactionId = "8938CEC2-7718-4AF5-A48E-BBE1EAE4F919",
        transactionString = "-25",
    )
    val task = TaskOrWish(
        value = TaskOrWishValue(
            rowId = "655B35F6-5D7F-416E-9DF7-D55F089E9950",
            date = "2023-12-26 20:20",
            url = "product82",
            h1 = "Обучение брата брата брата брата брата брата брата брата",
            isActive = "500137",
            parentId = "C10FEE3D-AA0B-42C3-AAEA-C1186A39AE14",
            price = "10",
            salePrice = "1",
            assembly = "1",
            id = "D58C18F0-6AD1-4B47-8B88-9023D5E2055F",
            photo = "/img/product/1438938CEC277184AF5A48EBBE1EAE4F919.err",
            typeId = TypeId.task,
            description = "D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\"D58C18F0-6AD1-4B47-8B88-9023D5E2055F\""
        ),
        array = TaskOrWishArray(
            optionalImages = listOf(
                "/img/product/1438938CEC277184AF5A48EBBE1EAE4F919.err"
            ),
            parentId = listOf(
                "C10FEE3D-AA0B-42C3-AAEA-C1186A39AE14"
            )
        ),
        uuid = TaskOrWishUuid(
            forUsers = listOf(
                "C080D0CE-5114-46DC-ABF0-5BAAD5CB4A45",
            ),
            didUsers = listOf(
                "C080D0CE-5114-46DC-ABF0-5BAAD5CB4A45",
            )
        ),
        float = TaskOrWishFloat(
            price = 10f,
            salePrice = 1f
        )
    )
}