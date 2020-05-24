package Model

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Govind Yadav on 22-05-2020.
 */
class ParsePlantUtility {

    fun parseJSONDatafromLink(): List<Plant>{

        //Downloading the data
        var allPlantObjects : ArrayList<Plant> = ArrayList()
        var downloadObject = DownloadingObject()
        var toplevelPlnatData = DownloadingObject().downloadJSONDataFromLink("https://plantplaces.com/perl/mobile/flashcard.pl")


        //Creating a JSON Object
        var toplevelJSONObject: JSONObject= JSONObject(toplevelPlnatData)
        var plantObjectsArray : JSONArray= toplevelJSONObject.getJSONArray("values")

        var index: Int= 0

        //Executing the JSON Object until the last index
        while (index < plantObjectsArray.length()){

            var plantObject : Plant = Plant()
            var jsonObject = plantObjectsArray.getJSONObject(index)

            with(jsonObject){

                plantObject.genus = getString("genus")
                plantObject.species = getString("species")
                plantObject.cultivar = getString("cultivar")
                plantObject.common = getString("common")
                plantObject.picture_name = getString("picture_name")
                plantObject.description = getString("description")
                plantObject.difficulty = getInt("difficulty")
                plantObject.id= getInt("id")
            }

            allPlantObjects.add(plantObject)
            index++
        }

        return allPlantObjects
        }
}