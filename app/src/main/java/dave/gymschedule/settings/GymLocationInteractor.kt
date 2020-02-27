package dave.gymschedule.settings

import dave.gymschedule.settings.model.GymLocation
import dave.gymschedule.settings.repository.GymLocationRepository
import io.reactivex.Completable
import io.reactivex.Observable

class GymLocationInteractor(private val gymLocationRepository: GymLocationRepository) {

    val savedGymLocationObservable: Observable<GymLocation> = gymLocationRepository.savedGymLocationIdObservable.map { id ->
        GymLocation.getGymLocationByLocationId(id)
    }

    fun setSavedGymLocation(gymLocation: GymLocation): Completable {
        return gymLocationRepository.setSavedGymLocationId(gymLocation.locationId)
    }

}