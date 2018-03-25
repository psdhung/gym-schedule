package dave.gymschedule.transformer


import android.util.Log
import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEvent
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
            gymEvents.add(getGymEventFromJsonObject(jsonObject))
        }

        return gymEvents
    }

    @Throws(JSONException::class)
    private fun getGymEventFromJsonObject(jsonObject: JSONObject): GymEvent {
        val name = jsonObject.getString("className")
        val eventTypeId = jsonObject.getInt("classTypeId")
        val details = jsonObject.getString("titleDetail")
        val startTime = jsonObject.getString("startTime")
        val endTime = jsonObject.getString("endTime")
        Log.d(TAG, "name=$name, eventTypeId=$eventTypeId, category=${EventType.getEventTypeFromId(eventTypeId)}")
        return GymEvent(name, EventType.getEventTypeFromId(eventTypeId), details, startTime, endTime)
    }
}