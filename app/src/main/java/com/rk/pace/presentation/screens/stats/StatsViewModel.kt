package com.rk.pace.presentation.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.di.DefaultDispatcher
import com.rk.pace.domain.model.WeekGoals
import com.rk.pace.domain.use_case.stat.GetWeekGoalsUseCase
import com.rk.pace.domain.use_case.stat.GetWeekRunsUseCase
import com.rk.pace.domain.use_case.stat.GetWeekStatsUseCase
import com.rk.pace.domain.use_case.stat.MapWeekGoalsToWeekGoalsProgress
import com.rk.pace.domain.use_case.stat.MapWeekRunsToChartData
import com.rk.pace.domain.use_case.stat.UpdateWeekGoalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _weekOffset = MutableStateFlow(0)

    private val weekRuns = _weekOffset
        .flatMapLatest { offset ->
            getWeekRunsUseCase(offset)
        }
        .distinctUntilChanged()

    private val weekGoals = getWeekGoalsUseCase()
        .distinctUntilChanged()

    val state: StateFlow<StatsUiState> = combine(
        _weekOffset,
        weekGoals,
        weekRuns
    ) { offset, goals, runs ->

        withContext(defaultDispatcher) {
            val label = getWeekLabe(offset)
            val canGoForward = offset < 0
            val stats = getWeekStatsUseCase(runs)
            val progress = mapWeekGoalsToGoalsProgress(runs, goals)
            val chartData = mapWeekRunsToChartData(runs, offset)

            StatsUiState(
                weekOffset = offset,
                weekLabel = label,
                canGoForward = canGoForward,
                data = StatsData(
                    weekStats = stats,
                    weekGoals = goals,
                    weekGoalsProgress = progress,
                    weekDistanceChartData = chartData
                )
            )
        }

    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            StatsUiState()
        )

    private val _events = Channel<StatsEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: StatsAction) {
        when (action) {
            StatsAction.OnNextWeekClick -> goToNextWeek()
            StatsAction.OnPreviousWeekClick -> goToPreviousWeek()
            is StatsAction.OnUpdateGoals -> updateWeekGoals(action.weekGoals)
            StatsAction.OnAddGoalClick -> {
                viewModelScope.launch {
                    _events.send(
                        StatsEvent.GoToAddGoal
                    )
                }
            }

            StatsAction.OnGoalClick -> {
                viewModelScope.launch {
                    _events.send(
                        StatsEvent.GoToGoal
                    )
                }
            }
        }
    }

    private fun goToNextWeek() {
        if (_weekOffset.value < 0) {
            _weekOffset.update { it + 1 }
        }
    }

    private fun goToPreviousWeek() {
        _weekOffset.update { it - 1 }
    }

    private fun updateWeekGoals(weekGoals: WeekGoals) {
        if (_weekOffset.value == 0) {
            viewModelScope.launch {
                updateWeekGoalsUseCase(weekGoals)
                _events.send(
                    StatsEvent.OnBack
                )
            }
        }
    }

    private fun getWeekLabe(offset: Int): String {
        return when (offset) {
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

}