package com.rastilka.presentation.screens.create_wish_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.TypeId
import com.rastilka.domain.use_case.CreateTaskOrWishUseCase
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.create_task_screen.data.CreateWishState
import com.rastilka.presentation.screens.create_wish_screen.data.CreateWishEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWishViewModel @Inject constructor(
    private val createTaskOrWishUseCase: CreateTaskOrWishUseCase,
    private val getUserBySessionKeyUseCase: GetUserBySessionKeyUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CreateWishState())
    val state get() = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: CreateWishEvent) {
        when (event) {
            is CreateWishEvent.ChangeTitle -> {
                _state.value = state.value.copy(
                    titleText = event.text
                )
            }

            is CreateWishEvent.CreateWish -> {
                createTask(event.navController, event.uri)
            }

            is CreateWishEvent.ChangeCountPrice -> {
                _state.value = state.value.copy(
                    countPrice = event.countPrice
                )
            }
        }
    }

    private fun init() {
        _state.value = state.value.copy(
            loadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            when (val resourceUser = getUserBySessionKeyUseCase.invoke()) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        errorMessage = resourceUser.message ?: ""
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.Loading
                    )
                }
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        user = resourceUser.data,
                        loadingState = LoadingState.SuccessfulLoad
                    )
                }
            }
        }
    }

    private fun createTask(navController: NavController, imageUri: Uri?) {
        _state.value = state.value.copy(
            loadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            when (val resource = createTaskOrWishUseCase.invoke(
                    typeId = TypeId.wish.name,
                    lastUrl = "${state.value.user?.id}_wishes",
                    price = state.value.countPrice,
                    h1 = state.value.titleText,
                    picture = imageUri
                )
            ) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        errorMessage = resource.message ?: ""
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.Loading
                    )
                }
                is Resource.Success -> {
                    navController.navigate(AppNavGraph.WishesGraph.rout) {
                        popUpTo(NavigationScreens.WishScreen.rout) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}