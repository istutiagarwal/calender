package com.example.calender.source

import javax.inject.Inject

class CalenderRepository@Inject constructor(private val apiService: ApiService) {

    suspend fun storeData(userId: Int, taskModel: TaskModel)  =
      apiService.storeData(StoreDataModel(
            user_id = userId,
            task = taskModel
        ))

    suspend fun getListOfData(userId: Int) =
       apiService.getListOfData( GetEventList(user_id = userId))

    suspend fun deleteEvent(userId: Int , taskId : Int) =
       apiService.deleteEvent(DeleteModel(user_id =  userId , task_id = taskId))

}
