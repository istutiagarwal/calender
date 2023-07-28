package com.example.calender.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calender.source.CalenderRepository
import com.example.calender.source.TaskModel
import com.example.calender.source.TasksList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CalenderViewModel @Inject constructor(private val  repository: CalenderRepository) : ViewModel() {

   private val _taskListLiveData : MutableLiveData<TasksList> = MutableLiveData()
    val taskListLiveData : LiveData<TasksList> = _taskListLiveData

    fun storeData(userId : Int, title : String, description : String) {
        viewModelScope.launch(Dispatchers.IO) {
             repository.storeData(
               userId = userId,
                taskModel = TaskModel(title , description)
            )
        }
    }

    fun getListOfData(userId : Int){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getListOfData(
                userId = userId,
            )
            handleListResponse(response)
        }
    }

    fun deleteEvent(userId: Int  , taskId : Int){
        viewModelScope.launch(Dispatchers.IO){
          repository.deleteEvent(
                userId =userId,
                taskId = taskId
            )
            getListOfData(userId)
        }
    }

    private  fun handleListResponse(response: Response<TasksList>){
        if(response.isSuccessful){
            response.body()?.let {
               _taskListLiveData.postValue(it)
            }
        }
    }
}