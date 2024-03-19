package com.example.todoapp

data class rvDataModel(val task: String, var isComplete: Boolean, val refKey: String){
    constructor() : this(
        task = "",
        refKey = "",
        isComplete = false
    )
}
