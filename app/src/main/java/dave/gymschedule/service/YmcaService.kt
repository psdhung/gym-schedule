package dave.gymschedule.service

import dave.gymschedule.model.GymSchedule
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface YmcaService {

    @GET("api/Classes/GetByCentreId")
    fun getGymSchedule(
            @Query("centreId") centreId: Int,
            @Query("startDateTime", encoded = true) startDateTime: String,
            @Query("endDateTime", encoded = true) endDateTime: String
    ): Single<GymSchedule>

}