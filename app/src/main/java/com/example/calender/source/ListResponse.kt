package com.example.calender.source


data class TasksList(
    val tasks: List<Task>
)

data class Task(
    val task_id: Int,
    val task_detail: TaskModel
)
