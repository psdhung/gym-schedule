package dave.gymschedule.transformer


import android.util.Log
import dave.gymschedule.Model.GymEvent
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class GymEventTransformer {
    companion object {
        private val TAG = GymEventTransformer::class.java.simpleName
    }

    fun getGymEventsFromJson(json: JSONObject): List<GymEvent> {
        val GymEvents: List<GymEvent> = try {
            val allClasses = getAllClassesArray(json)
            getGymEventsFromArray(allClasses)
        } catch (exception: JSONException) {
            Log.d(TAG, "error while parsing JSON", exception)
            ArrayList()
        }

        return GymEvents
    }

    @Throws(JSONException::class)
    private fun getAllClassesArray(rootJson: JSONObject): JSONArray {
        val classesArray = rootJson.getJSONArray("classes")
        val innerClasses = classesArray.getJSONObject(0)
        val allClassesArray = innerClasses.getJSONArray("classes")
        Log.d(TAG, "classes list: " + allClassesArray.toString(2))

        return allClassesArray
    }

    @Throws(JSONException::class)
    private fun getGymEventsFromArray(classesArray: JSONArray): List<GymEvent> {
        val gymEvents = ArrayList<GymEvent>()
        for (i in 0 until classesArray.length()) {
            val jsonObject = classesArray.getJSONObject(i)
//            val typeId = jsonObject.getInt("classTypeId")
            gymEvents.add(getGymEventFromJsonObject(jsonObject))
        }

        return gymEvents
    }

    @Throws(JSONException::class)
    private fun getGymEventFromJsonObject(`object`: JSONObject): GymEvent {
        val name = `object`.getString("className")
        val details = `object`.getString("titleDetail")
        val startTime = `object`.getString("startTime")
        val endTime = `object`.getString("endTime")
        return GymEvent(name, details, startTime, endTime)
    }
}