package dave.gymschedule.interactor

import dave.gymschedule.Rx2SchedulersOverrideRule
import dave.gymschedule.common.database.GymLocationRepository
import dave.gymschedule.common.model.EventType
import dave.gymschedule.schedule.model.GymEventViewModel
import dave.gymschedule.common.model.Resource
import dave.gymschedule.settings.repository.EventTypeStateRepository
import dave.gymschedule.schedule.repository.GymScheduleRepository
import dave.gymschedule.schedule.interactor.GymScheduleInteractor
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

@RunWith(MockitoJUnitRunner::class)
class GymScheduleInteractorTest {

    @get:Rule
    val rule = Rx2SchedulersOverrideRule()

    @Mock
    lateinit var mockGymScheduleRepository: GymScheduleRepository

    @Mock
    lateinit var mockEventTypeStateRepository: EventTypeStateRepository

    @Mock
    lateinit var mockGymLocationRepository: GymLocationRepository

    private lateinit var interactor: GymScheduleInteractor

    @Before
    fun setUp() {
        `when`(mockGymLocationRepository.savedGymLocationIdObservable).thenReturn(Observable.just(1))
        interactor = GymScheduleInteractor(mockGymScheduleRepository, mockEventTypeStateRepository, mockGymLocationRepository)
    }

    @Test
    fun `should return unmodified gym schedule list when no event types are saved`() {
        // Arrange
        val date = Calendar.getInstance()
        val gymEventViewModels = listOf(
                createGenericGymEventViewModel(EventType.POOL_ACTIVITIES)
        )

        `when`(mockGymScheduleRepository.getGymEventsViewModelObservable(1, date)).thenReturn(Observable.just(Resource(Resource.Status.SUCCESS, gymEventViewModels)))
        `when`(mockEventTypeStateRepository.eventTypeStateObservable).thenReturn(Observable.just(mapOf()))

        // Act
        val list = interactor.getGymEventViewModelsObservable(date)
                .test()
                .assertValueCount(1)

        // Assert
        assertEquals(gymEventViewModels, list.values()[0].data)
    }

    @Test
    fun `should filter gym schedule list when enabled event types are saved`() {
        // Arrange
        val date = Calendar.getInstance()
        val gymEventViewModels = listOf(
                createGenericGymEventViewModel(EventType.POOL_ACTIVITIES),
                createGenericGymEventViewModel(EventType.FITNESS_CLASSES),
                createGenericGymEventViewModel(EventType.SPORTS_AND_RECREATION)
        )

        `when`(mockGymScheduleRepository.getGymEventsViewModelObservable(1, date)).thenReturn(
                Observable.just(Resource(Resource.Status.SUCCESS, gymEventViewModels))
        )
        `when`(mockEventTypeStateRepository.eventTypeStateObservable).thenReturn(
                Observable.just(mapOf(EventType.SPORTS_AND_RECREATION.eventTypeId to true))
        )

        // Act
        val list = interactor.getGymEventViewModelsObservable(date)
                .test()
                .assertValueCount(1)

        // Assert
        assertEquals(listOf(createGenericGymEventViewModel(EventType.SPORTS_AND_RECREATION)), list.values()[0].data)
    }

    @Test
    fun `should not filter gym schedule list when all saved event types are disabled`() {
        // Arrange
        val date = Calendar.getInstance()
        val gymEventViewModels = listOf(
                createGenericGymEventViewModel(EventType.POOL_ACTIVITIES),
                createGenericGymEventViewModel(EventType.FITNESS_CLASSES),
                createGenericGymEventViewModel(EventType.SPORTS_AND_RECREATION)
        )

        `when`(mockGymScheduleRepository.getGymEventsViewModelObservable(1, date)).thenReturn(
                Observable.just(Resource(Resource.Status.SUCCESS, gymEventViewModels))
        )
        `when`(mockEventTypeStateRepository.eventTypeStateObservable).thenReturn(
                Observable.just(mapOf(EventType.SPORTS_AND_RECREATION.eventTypeId to false))
        )

        // Act
        val list = interactor.getGymEventViewModelsObservable(date)
                .test()
                .assertValueCount(1)

        // Assert
        assertEquals(gymEventViewModels, list.values()[0].data)
    }

    private fun createGenericGymEventViewModel(eventType: EventType): GymEventViewModel {
        return GymEventViewModel(
                "${eventType}Name",
                eventType,
                "${eventType}Details",
                "${eventType}StartTime",
                "${eventType}EndTime",
                "${eventType}Location",
                "${eventType}Description",
                "${eventType}Fee",
                false,
                "${eventType}AgeRange",
                "${eventType}Registration",
                false
        )
    }
}