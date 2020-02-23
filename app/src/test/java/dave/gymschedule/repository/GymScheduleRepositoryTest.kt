package dave.gymschedule.repository

import dave.gymschedule.schedule.repository.GymScheduleRepository
import dave.gymschedule.schedule.service.YmcaService
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GymScheduleRepositoryTest {

    @Mock
    lateinit var ymcaService: YmcaService

    private lateinit var repository: GymScheduleRepository

    @Before
    fun setUp() {
        repository = GymScheduleRepository(ymcaService)
    }

    /*@Test
    fun `should convert gym schedule to GymEventViewModels`() {
        // Arrange
        val date = Calendar.getInstance()
        date.set(Calendar.YEAR, 2019)
        date.set(Calendar.MONTH, 1)
        date.set(Calendar.DAY_OF_MONTH, 1)

        `when`(ymcaService.getGymSchedule(1, "2019-2-1+12:00:00+AM", "2019-2-1+11:59:59+PM")).thenReturn(
                Single.just(GymSchedule(1, listOf(GymEvents(listOf(GymEvent(
                        "name",
                        EventType.SPORTS_AND_RECREATION.eventTypeId,
                        "details",
                        "startTime",
                        "endTime",
                        "location",
                        "description",
                        "ageRange",
                        "fee",
                        "instructor",
                        "registrationType",
                        "registrationInstructions",
                        false
                ))))))
        )

        // Act
        val gymEventViewModels = repository.getGymEventsViewModelObservable(date)
                .test()
                .assertValueCount(1)

        // Assert
        assertEquals(listOf(GymEventViewModel(
                "name",
                EventType.SPORTS_AND_RECREATION,
                "details",
                "startTime",
                "endTime",
                "location",
                "description",
                "fee",
                false,
                "ageRange",
                "registrationType",
                false)
        ), gymEventViewModels.values()[0])
    }

    @Test
    fun `should return empty list when gym schedule is empty`() {
        // Arrange
        val date = Calendar.getInstance()
        date.set(Calendar.YEAR, 2019)
        date.set(Calendar.MONTH, 1)
        date.set(Calendar.DAY_OF_MONTH, 1)

        `when`(ymcaService.getGymSchedule(1, "2019-2-1+12:00:00+AM", "2019-2-1+11:59:59+PM")).thenReturn(
                Single.just(GymSchedule(1, listOf()))
        )

        // Act
        val gymEventViewModels = repository.getGymEventsViewModelObservable(date)
                .test()
                .assertValueCount(1)

        // Assert
        assertEquals(listOf<GymEventViewModel>(), gymEventViewModels.values()[0])
    }*/

}