package com.example.calender.source

class CalenderRepository() {

    suspend fun storeData(userId: Int, taskModel: TaskModel)  =
        RetrofitHelper.api.storeData(StoreDataModel(
            user_id = userId,
            task = taskModel
        ))

    suspend fun getListOfData(userId: Int) =
        RetrofitHelper.api.getListOfData( GetEventList(user_id = userId))

    suspend fun deleteEvent(userId: Int , taskId : Int) =
        RetrofitHelper.api.deleteEvent(DeleteModel(user_id =  userId , task_id = taskId))

}
