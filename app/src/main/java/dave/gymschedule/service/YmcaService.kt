package dave.gymschedule.service

import dave.gymschedule.model.Gym
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface YmcaService {

    @GET("api/Classes/GetByCentreId?centreId=39")
    fun getGymSchedule(
            @Query("startDateTime", encoded = true) startDateTime: String,
            @Query("endDateTime", encoded = true) endDateTime: String): Single<Gym>

}