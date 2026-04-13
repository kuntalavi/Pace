package com.rk.pace.presentation.screens.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.di.ApplicationIoCoroutineScope
import com.rk.pace.domain.model.GoalProgress
import com.rk.pace.domain.model.GoalType
import com.rk.pace.domain.model.WeekGoals
import com.rk.pace.domain.model.WeekGoalsProgress
import com.rk.pace.domain.model.WeekStats
import com.rk.pace.domain.use_case.stat.GetWeekGoalsUseCase
import com.rk.pace.domain.use_case.stat.GetWeekRunsUseCase
import com.rk.pace.domain.use_case.stat.GetWeekStatsUseCase
import com.rk.pace.domain.use_case.stat.MapWeekGoalsToWeekGoalsProgress
import com.rk.pace.domain.use_case.stat.MapWeekRunsToChartData
import com.rk.pace.domain.use_case.stat.UpdateWeekGoalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getWeekRunsUseCase: GetWeekRunsUseCase,
    private val mapWeekRunsToChartData: MapWeekRunsToChartData,
    private val getWeekStatsUseCase: GetWeekStatsUseCase,
    getWeekGoalsUseCase: GetWeekGoalsUseCase,
    private val mapWeekGoalsToGoalsProgress: MapWeekGoalsToWeekGoalsProgress,
    private val updateWeekGoalsUseCase: UpdateWeekGoalsUseCase,
    @param:ApplicationIoCoroutineScope private val scope: CoroutineScope,
) : ViewModel() {

    private val _weekOffset = MutableStateFlow(0)
    val weekOffset: StateFlow<Int> = _weekOffset.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    val weekRuns = _weekOffset
        .flatMapLatest { offset -> getWeekRunsUseCase(offset) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val weekGoals: StateFlow<WeekGoals> = getWeekGoalsUseCase()
        .distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            WeekGoals(null, null, null)
        )

    @RequiresApi(Build.VERSION_CODES.O)
    val weekDistanceChartData =
        combine(weekRuns, _weekOffset) { runs, weekOffset ->
            mapWeekRunsToChartData(runs, weekOffset)
        }
            .distinctUntilChanged()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                emptyList()
            )

    @RequiresApi(Build.VERSION_CODES.O)
    val weekStats =
        weekRuns.map { runs ->
            getWeekStatsUseCase(runs)
        }
            .distinctUntilChanged()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                WeekStats(0, 0f, 0L, 0f)
            )

    @RequiresApi(Build.VERSION_CODES.O)
    val weekGoalsProgress =
        combine(weekRuns, weekGoals) { runs, goals ->
            mapWeekGoalsToGoalsProgress(runs, goals)
        }
            .distinctUntilChanged()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                WeekGoalsProgress(
                    runGoalProgress = GoalProgress(
                        GoalType.RUNS,
                        0f,
                        0f,
                        false
                    ),
                    distanceGoalProgress = GoalProgress(
                        GoalType.DISTANCE_METERS,
                        0f,
                        0f,
                        false
                    ),
                    durationGoalProgress = GoalProgress(
                        GoalType.DURATION_MILLISECONDS,
                        0f,
                        0f,
                        false
                    )
                )
            )

    fun updateWeekGoals(weekGoals: WeekGoals) {
        if (weekOffset.value == 0) {
            scope.launch {
                updateWeekGoalsUseCase(weekGoals)
            }
        }
    }

    fun goToPreviousWeek() {
        _weekOffset.update { it - 1 }
    }

    fun goToNextWeek() {
        if (weekOffset.value < 0) {
            _weekOffset.update { it + 1 }
        }
    }

    val canGoForward: StateFlow<Boolean> = _weekOffset
        .map { it < 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    @RequiresApi(Build.VERSION_CODES.O)
    val weekLabel: StateFlow<String> = _weekOffset
        .map { offset ->
            when (offset) {
                0 -> "This Week"
                -1 -> "Last Week"
                else -> {
                    val monday = LocalDate.now()
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                        .plusWeeks(offset.toLong())
                    val fmt = DateTimeFormatter.ofPattern("MMM d")
                    "${fmt.format(monday)} – ${fmt.format(monday.plusDays(6))}"
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            "This Week"
        )


}