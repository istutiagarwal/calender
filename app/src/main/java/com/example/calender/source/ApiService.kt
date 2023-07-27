package com.example.calender.source

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/storeCalendarTask")
    suspend fun storeData(
        @Body raw : StoreDataModel
    )

    @POST("/api/getCalendarTaskList")
    suspend fun getListOfData(
        @Body raw: GetEventList,
    ) : Response<TasksList>

    @POST("/api/deleteCalendarTask")
    suspend fun deleteEvent(
        @Body raw : DeleteModel
    )
}