package dave.gymschedule.interactor

import dave.gymschedule.model.EventType
import dave.gymschedule.model.GymEventViewModel
import dave.gymschedule.repository.EventTypeStateRepository
import dave.gymschedule.repository.GymScheduleRepository
import io.reactivex.Observable
import io.reactivex.Single
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
    fun `should return unmodified gym schedule list when no event types are saved`() {
        // Arrange
        val date = Calendar.getInstance()
        val gymEventViewModels = listOf(
                createGenericGymEventViewModel(EventType.POOL_ACTIVITIES)
        )

        `when`(mockGymScheduleRepository.getGymEventsViewModelObservable(date)).thenReturn(Single.just(gymEventViewModels))
        `when`(mockEventTypeStateRepository.eventTypeStateObservable).thenReturn(Observable.just(mapOf()))

        // Act
        val list = interactor.getGymEventViewModelsObservable(date)
                .test()
                .assertValueCount(1)

        // Assert
        assertEquals(gymEventViewModels, list.values()[0])
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

        `when`(mockGymScheduleRepository.getGymEventsViewModelObservable(date)).thenReturn(
                Single.just(gymEventViewModels)
        )
        `when`(mockEventTypeStateRepository.eventTypeStateObservable).thenReturn(
                Observable.just(mapOf(EventType.SPORTS_AND_RECREATION.eventTypeId to true))
        )

        // Act
        val list = interactor.getGymEventViewModelsObservable(date)
                .test()
                .assertValueCount(1)

        // Assert
        assertEquals(listOf(createGenericGymEventViewModel(EventType.SPORTS_AND_RECREATION)), list.values()[0])
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

        `when`(mockGymScheduleRepository.getGymEventsViewModelObservable(date)).thenReturn(
                Single.just(gymEventViewModels)
        )
        `when`(mockEventTypeStateRepository.eventTypeStateObservable).thenReturn(
                Observable.just(mapOf(EventType.SPORTS_AND_RECREATION.eventTypeId to false))
        )

        // Act
        val list = interactor.getGymEventViewModelsObservable(date)
                .test()
                .assertValueCount(1)

        // Assert
        assertEquals(gymEventViewModels, list.values()[0])
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