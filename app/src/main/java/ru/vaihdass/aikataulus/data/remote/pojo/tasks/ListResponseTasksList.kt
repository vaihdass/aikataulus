package ru.vaihdass.aikataulus.data.remote.pojo.tasks

data class ListResponseTasksList(
    var error: Status?,
    var items: List<TaskList>?,
)