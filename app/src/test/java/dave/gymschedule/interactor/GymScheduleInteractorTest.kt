package dave.gymschedule.interactor

import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.repository.EventTypeStateRepository
import dave.gymschedule.repository.GymScheduleRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

@RunWith(MockitoJUnitRunner::class)
class GymScheduleInteractorTest {

    @Mock
    lateinit var mockGymScheduleRepository: GymScheduleRepository

    @Mock
    lateinit var mockEventTypeStateRepository: EventTypeStateRepository

    private lateinit var interactor: GymScheduleInteractor

    @Before
    fun setUp() {
        interactor = GymScheduleInteractor(mockGymScheduleRepository, mockEventTypeStateRepository)
    }

    @Test
    fun `should return unmodified gym schedule when no event types are saved`() {
        val date = Calendar.getInstance()
        val gymEventViewModels = listOf(
                GymEventViewModel(
                        "name",
                        EventType.POOL_ACTIVITIES,
                        "details",
                        "startTime",
                        "endTime",
                        "location",
                        "description",
                        "fee",
                        false,
                        "ageRange",
                        "registration",
                        false
                )
        )

        val behaviourSubject = BehaviorSubject.create<Map<Int, Boolean>>()
        behaviourSubject.onNext(mapOf())

        `when`(mockGymScheduleRepository.getGymEventsViewModelSingle(date)).thenReturn(Single.just(gymEventViewModels))
        `when`(mockEventTypeStateRepository.eventTypeStateObservable).thenReturn(Observable.just(mapOf()))

        val list = interactor.getGymEventViewModelsObservable(date).test()
        assertEquals(gymEventViewModels, list.values()[0])
    }

}