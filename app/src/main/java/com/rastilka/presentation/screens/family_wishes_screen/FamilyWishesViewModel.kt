package com.rastilka.presentation.screens.family_wishes_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.domain.use_case.DeleteTaskOrWishUseCase
import com.rastilka.domain.use_case.EditTaskOrWishUseCase
import com.rastilka.domain.use_case.GetFamilyMembersUseCase
import com.rastilka.domain.use_case.GetPointsUseCase
import com.rastilka.domain.use_case.GetTasksOrWishesUseCase
import com.rastilka.domain.use_case.SetResponsibleUser
import com.rastilka.presentation.screens.family_wishes_screen.data.FamilyWishScreenEvent
import com.rastilka.presentation.screens.family_wishes_screen.data.FamilyWishScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyWishesViewModel @Inject constructor(
    private val getFamilyMembers: GetFamilyMembersUseCase,
    private val getWishesUseCase: GetTasksOrWishesUseCase,
    private val addResponsibleUser: SetResponsibleUser,
    private val deleteWish: DeleteTaskOrWishUseCase,
    private val editTaskUseCase: EditTaskOrWishUseCase,
    private val getPointsUseCase: GetPointsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FamilyWishScreenState())
    val state get() = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: FamilyWishScreenEvent) {
        when (event) {
            FamilyWishScreenEvent.Refresh -> {
                init()
            }

            is FamilyWishScreenEvent.AddResponsibleUser -> {
                setResponsibleUser(event.productUrl, event.userId, event.activeUserId)
            }

            is FamilyWishScreenEvent.DeleteWish -> {
                deleteWish(event.productUrl)
            }

            is FamilyWishScreenEvent.GetPoint -> {
                getPoints(event.fromUserId, event.points, event.comment, event.productUrl, event.assembly)
            }
        }
    }

    private fun init() {
        viewModelScope.launch {
            _state.value = state.value.copy(initLoadingState = LoadingState.Loading)
            when (val resourceFamilyMembers = getFamilyMembers.invoke()) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        initLoadingState = LoadingState.FailedLoad,
                        errorMessage = resourceFamilyMembers.message ?: ""
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        initLoadingState = LoadingState.Loading,
                        errorMessage = resourceFamilyMembers.message ?: ""
                    )
                }

                is Resource.Success -> {
                    when (val resourceWishList = getWishesUseCase.invoke(TypeIdForApi.wishes)) {
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.FailedLoad,
                                errorMessage = resourceWishList.message ?: ""
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.Loading,
                                errorMessage = resourceFamilyMembers.message ?: ""
                            )
                        }

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                familyMembers = resourceFamilyMembers.data ?: emptyList(),
                                wishList = resourceWishList.data ?: emptyList(),
                                initLoadingState = LoadingState.SuccessfulLoad
                            )
                        }
                    }
                }
            }

        }
    }

    private fun setResponsibleUser(productUrl: String, userId: String, activeUserId: List<String>) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading
            )
            if (userId !in activeUserId) {
                activeUserId.forEach { activeUserId ->
                    when (val resource = addResponsibleUser.invoke(productUrl, activeUserId)) {
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.FailedLoad,
                                errorMessage = resource.message ?: ""
                            )
                            return@forEach
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.Loading
                            )
                        }

                        is Resource.Success -> {}
                    }
                }
            }
            when (val resource = addResponsibleUser.invoke(productUrl, userId)) {
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
                    when (val resourceListTasks =
                        getWishesUseCase.invoke(type = TypeIdForApi.wishes)) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.SuccessfulLoad,
                                wishList = resourceListTasks.data ?: emptyList()
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.Loading,
                            )
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.FailedLoad,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getPoints(fromUserId: String, points: Int, comment: String, productUrl: String, assembly: String) {
        _state.value = state.value.copy(
            loadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            when (val resourceGetPoints =
                getPointsUseCase.invoke(
                    fromUserId = fromUserId,
                    points = points,
                    comment = comment
                )) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        errorMessage = resourceGetPoints.message ?: ""
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.Loading
                    )
                }

                is Resource.Success -> {
                    when (val resourceEditSalePointsWish = editTaskUseCase.invoke(
                        productUrl = productUrl,
                        property = "salePrice",
                        value = points.toString()
                    )) {
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.FailedLoad,
                                errorMessage = resourceEditSalePointsWish.message ?: ""
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.Loading
                            )
                        }

                        is Resource.Success -> {
                            val sum = assembly.toInt() + points
                            when (val resourceEditAssemblyWish = editTaskUseCase.invoke(
                                productUrl = productUrl,
                                property = "assembly",
                                value = sum.toString()
                            )) {
                                is Resource.Error -> {
                                    _state.value = state.value.copy(
                                        loadingState = LoadingState.FailedLoad,
                                        errorMessage = resourceEditAssemblyWish.message ?: ""
                                    )
                                }

                                is Resource.Loading -> {
                                    _state.value = state.value.copy(
                                        loadingState = LoadingState.Loading
                                    )
                                }

                                is Resource.Success -> {
                                    when (val resourceListWishes =
                                        getWishesUseCase.invoke(type = TypeIdForApi.wishes)) {
                                        is Resource.Success -> {
                                            when(val resourceListFamily = getFamilyMembers.invoke()){
                                                is Resource.Error ->{
                                                    _state.value = state.value.copy(
                                                        loadingState = LoadingState.FailedLoad,
                                                        errorMessage = resourceListFamily.message ?: ""
                                                    )
                                                }
                                                is Resource.Loading -> {
                                                    _state.value = state.value.copy(
                                                        loadingState = LoadingState.Loading,
                                                    )
                                                }
                                                is Resource.Success -> {
                                                    _state.value = state.value.copy(
                                                        familyMembers = resourceListFamily.data ?: emptyList(),
                                                        wishList = resourceListWishes.data ?: emptyList(),
                                                        loadingState = LoadingState.SuccessfulLoad
                                                    )
                                                }
                                            }
                                        }

                                        is Resource.Loading -> {
                                            _state.value = state.value.copy(
                                                loadingState = LoadingState.Loading,
                                            )
                                        }

                                        is Resource.Error -> {
                                            _state.value = state.value.copy(
                                                loadingState = LoadingState.FailedLoad,
                                                errorMessage = resourceListWishes.message ?: ""
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun deleteWish(productUrl: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading
            )
            when (val resource = deleteWish.invoke(productUrl)) {
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
                    when (val resourceListTasks =
                        getWishesUseCase.invoke(type = TypeIdForApi.wishes)) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.SuccessfulLoad,
                                wishList = resourceListTasks.data ?: emptyList()
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.Loading,
                            )
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.FailedLoad,
                            )
                        }
                    }
                }
            }
        }
    }
}