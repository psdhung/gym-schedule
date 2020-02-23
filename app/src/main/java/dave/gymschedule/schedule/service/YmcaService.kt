package dave.gymschedule.schedule.service

import dave.gymschedule.schedule.model.GymSchedule
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YmcaService {
    @GET("api/Classes/GetByCentreId")
    fun getGymSchedule(
            @Query("centreId") centreId: Int,
            @Query("startDateTime", encoded = true) startDateTime: String,
            @Query("endDateTime", encoded = true) endDateTime: String
    ): Call<GymSchedule>
}