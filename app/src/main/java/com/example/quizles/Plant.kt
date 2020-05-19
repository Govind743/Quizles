package com.example.quizles

/**
 * Created by Govind Yadav on 17-05-2020.
 */
class Plant(genus:String, species:String, cultivar:String, common:String, picture_name:String,
            description:String, difficulty:Int, id:Int=0 ) {

    constructor(): this("", "", "", "", "",
        "", 0, 0)
    private var _plantName:String?=null

    var plantName:String?
    get()=_plantName
    set(value){
        _plantName=value
    }
}