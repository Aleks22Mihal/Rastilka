package com.rastilka.presentation.screens.profile_screen

import androidx.lifecycle.ViewModel
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserBySessionKeyUseCase: GetUserBySessionKeyUseCase
) : ViewModel()