package com.example.calender.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calender.source.CalenderRepository
import com.example.calender.viewModel.CalenderViewModel


class CalenderViewModelFactory(private val repository: CalenderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalenderViewModel(repository) as T
    }

}