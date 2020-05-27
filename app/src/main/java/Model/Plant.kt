package Model

/**
 * Created by Govind Yadav on 17-05-2020.
 */
class Plant(var genus:String, var species:String, var cultivar:String, var common:String, var picture_name:String,
            var description:String, var difficulty:Int, var id:Int=0 ) {

    constructor(): this("", "", "", "", "",
        "", 0, 0)
    private var _plantName:String?=null

    var plantName:String?
    get()=_plantName
    set(value){
        _plantName=value
    }

    //Overrides toString() method and displays the text to buttons
    override fun toString(): String {
        return "$common "
    }
}