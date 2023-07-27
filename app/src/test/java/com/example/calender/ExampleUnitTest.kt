package com.example.calender

import com.example.calender.source.ApiService
import com.example.calender.source.CalenderRepository
import com.example.calender.source.DeleteModel
import com.example.calender.source.GetEventList
import com.example.calender.source.StoreDataModel
import com.example.calender.source.Task
import com.example.calender.source.TaskModel
import com.example.calender.source.TasksList
import com.example.calender.viewModel.CalenderViewModel
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @Mock
    private lateinit var mockApiService: ApiService

    @Test
    fun `test storeData`() = runBlocking {
        // Mock data
        val userId = 123
        val taskModel = TaskModel("Test Task", "Test Description")
        val storeDataModel = StoreDataModel(userId, taskModel)

        // Set up the ApiService mock
        `when`(mockApiService.storeData(storeDataModel)).thenReturn(Unit)

        // Create the repository with the mock
        val repository = CalenderRepository(mockApiService)

        // Call the function to be tested
        repository.storeData(userId, taskModel)

        // Verify that the ApiService's storeData function was called with the correct arguments
        verify(mockApiService).storeData(storeDataModel)
    }

    @Test
    fun `test getListOfData`() = runBlocking {
        // Mock data
        val userId = 456
        val getEventList = GetEventList(userId)
        val responseTasks = listOf(
            Task(
                task_id = 1,
                task_detail = TaskModel(
                    title = "Task 1",
                    description = "Description for Task 1"
                )
            ),
            Task(
                task_id = 2,
                task_detail = TaskModel(
                    title = "Task 2",
                    description = "Description for Task 2"
                )
            )
        )
        val response = Response.success(TasksList(responseTasks))

        // Set up the ApiService mock
        // You can set up mock response data if needed for this test
         `when`(mockApiService.getListOfData(getEventList)).thenReturn(response)

        // Create the repository with the mock
        val repository = CalenderRepository(mockApiService)

        // Call the function to be tested
        repository.getListOfData(userId)

        // Verify that the ApiService's getListOfData function was called with the correct arguments
        verify(mockApiService).getListOfData(getEventList)
    }

    @Test
    fun `test deleteEvent`() = runBlocking {
        // Mock data
        val userId = 789
        val taskId = 987
        val deleteModel = DeleteModel(userId, taskId)

        // Set up the ApiService mock
        `when`(mockApiService.deleteEvent(deleteModel)).thenReturn(Unit)

        // Create the repository with the mock
        val repository = CalenderRepository(mockApiService)

        // Call the function to be tested
        repository.deleteEvent(userId, taskId)

        // Verify that the ApiService's deleteEvent function was called with the correct arguments
        verify(mockApiService).deleteEvent(deleteModel)
    }
}