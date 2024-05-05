package ru.vaihdass.aikataulus.data.remote.pojo.tasks

data class ListResponseTask(
    var error: Status?,
    var items: List<Task>?,
)
