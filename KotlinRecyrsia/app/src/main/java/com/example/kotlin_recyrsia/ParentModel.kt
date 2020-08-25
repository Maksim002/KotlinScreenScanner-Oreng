package com.example.kotlin_recyrsia

class ParentModel (
    var title: String,
    var parent: Int,
    var list: ArrayList<ParentModel> = arrayListOf()
)