# AGENTS.md - Pace Running Tracker

Guide for AI agents contributing to the Pace codebase. This document covers architecture patterns, critical workflows, and project-specific conventions.

## Architecture Overview

Pace follows **Clean Architecture** with **MVVM** for the presentation layer:

```
 com.rk.pace/
 ├── auth/              # Authentication layer (SignIn/SignUp, Supabase Auth)
 ├── domain/            # Business logic (UseCases, interfaces, domain models)
 │   ├── repo/          # Repository interfaces (RunRepo, UserRepo, FeedRepo, SocialRepo, DataRepo)
 │   ├── model/         # Pure data models (Run, User, RunPathPoint)
 │   ├── use_case/      # Use case implementations
 │   └── tracking/      # Tracking interfaces (LocationTracker, TimeTracker, TrackerManager)
 ├── data/              # Data layer
 │   ├── room/          # Local database (entities, DAOs, PaceDatabase v13)
│   ├── repo/          # Repository implementations
│   ├── remote/        # Remote data sources (Firebase Firestore, Supabase)
│   ├── mapper/        # Entity ↔ Domain ↔ DTO mappings
│   └── tracking/      # GPS & time tracking implementations
├── presentation/      # UI layer
│   ├── screens/       # Compose screens (grouped by feature)
│   ├── navigation/    # Type-safe Compose Navigation with Route sealed classes
│   ├── components/    # Reusable Compose components
│   └── charts/        # Chart visualization components
├── background/        # Background work
│   ├── SyncRunWorker  # WorkManager task for syncing runs to Firestore
│   └── RunTrackService # Foreground service for GPS tracking
├── di/                # Hilt dependency injection modules
├── common/            # Utilities, extensions, constants
└── theme/             # Material Design 3 theme
```

### Key Architectural Decisions

1. **Offline-First with Sync**: Runs saved locally to Room DB first, then synced via WorkManager when network available
2. **Type-Safe Navigation**: Uses Kotlin sealed classes for routes (not string-based)
3. **GPS in Foreground Service**: `RunTrackService` maintains high-accuracy location tracking with `FusedLocationProviderClient`
4. **Firebase + Supabase Hybrid**: Firebase Auth/Firestore for runs; Supabase for user data/storage
5. **Encoding Polylines**: GPS paths stored as encoded polyline strings in `RunEntity.encodedPath`

## Critical Data Flows

### Run Creation & Tracking
1. **Start Run**: `ActiveRunViewModel` → `RunTrackService` (foreground service) → `LocationTracker` (Flow-based GPS updates)
2. **Location Updates**: `FusedLocationProviderClient` → `LocationTrackerImp.locationFlow` → `TrackerManager.location` StateFlow
3. **Save Run**: 
   - Save to Room: `RunEntity` + `RunPathPointEntity` (one-to-many)
   - Trigger sync: WorkManager enqueues `SyncRunWorker` with network constraints
4. **Sync to Firestore**: `SyncRunWorker` reads unsynced runs via `RunDao.getUnsyncedRunsWithPath()` → uploads → marks as synced
5. **Restore on Login**: `RunRepoImp.restoreUserRuns()` pulls from Firestore → saves to local DB

### Run Path Storage Pattern
- **Display**: Encoded polyline string in `RunEntity.encodedPath` (compact)
- **Details**: Individual points in `RunPathPointEntity` (latitude, longitude, speed, timestamp)
- **Relationship**: One-to-many via Room `@Relation` in `RunWithPathEntity`

## State Management Patterns

### ViewModel + StateFlow/MutableStateFlow
- Use `viewModelScope` for lifecycle-aware coroutines
- Collect from Flows with `collectAsStateWithLifecycle()` in Compose
- Example: `ActiveRunViewModel.runState`, `FeedViewModel.state`

### Compose State
- Local mutable state: `mutableStateOf()` for UI-only state
- Saved state: `SavedStateHandle` for navigation arguments (see `RunStatsViewModel`)
- Remember state across recomposition: use `remember { saveable }`

### Tracking State
- GPS strength calculated from location accuracy: `TrackerManager.gpsStrength` (STRONG/MODERATE/WEAK)
- Location updates via Flow sharing: `LocationTrackerImp` uses `shareIn()` with 5s timeout
- Time tracking: separate `TimeTracker` for elapsed time during run

## Database Schema & Entities

 ### Core Entities (PaceDatabase v13)
 ```kotlin
 UserEntity(userId, username, name, email, photoURI, followers, following)
 RunEntity(runId, userId, timestamp, durationMilliseconds, distanceMeters, avgSpeedMps, encodedPath, synced, title)
 RunPathPointEntity(pointId, runId, timestamp, lat, long, speedMps, isPausePoint)
 DeleteRunEntity(runId, userId)  // Track deleted runs for sync
 WeekGoalsEntity(weekId, userId, targetDistanceMeters, completed) // Weekly goals table
 ```

### Relationships
- `UserEntity` ← 1:N → `RunEntity` (CASCADE delete)
- `RunEntity` ← 1:N → `RunPathPointEntity` (CASCADE delete)
- Foreign keys enforce referential integrity

### TypeConverters
`Converters` class handles serialization (e.g., `List<String>` for polylines, `Long` for timestamps)

## Dependency Injection (Hilt) Patterns

### Key Modules
- `AppModule`: General app services (WorkManager, InternalStorageHelper)
- `FirebaseModule`: Firebase/Supabase clients
- `TrackerModule`: LocationTracker, TimeTracker, TrackerManager (all @Singleton)
- `RepoModule`: Binds repository interfaces to implementations
- `DatabaseModule`: Provides PaceDatabase and DAOs
- `CoroutineDispatcherModule`: Custom dispatchers via qualifiers

### Custom Qualifiers
- `@ApplicationDefaultCoroutineScope`: Main app scope (Dispatchers.Main.immediate)
- `@ApplicationIoCoroutineScope`: IO operations scope
- `@IoDispatcher`: Specific dispatcher for async work

### Service Injection
```kotlin
@HiltAndroidApp  // Enable Hilt in App.kt
class App : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory
    // WorkManager auto-uses for @HiltWorker
}
```

## Background Work (WorkManager Integration)

### SyncRunWorker Pattern
- Triggered when run saved locally
- Constraints: battery, network connectivity
- Use `OneTimeWorkRequestBuilder` for one-shot syncs
- Enqueue with `ExistingWorkPolicy.APPEND_OR_REPLACE` to avoid duplicates
- Reads unsynced runs: `runDao.getUnsyncedRunsWithPath()`
- Syncs to Firestore, marks `synced=1` on success

### Foreground Service Setup
- `RunTrackService` declared in `AndroidManifest.xml` with `foregroundServiceType="location"`
- `RunTrackNotification` creates persistent notification channel
- Called from `App.onCreate()` via Hilt injection

## Navigation (Type-Safe Compose)

### Route Structure
```kotlin
sealed class Route {
    sealed class Root : Route { object Auth : Root; object BotNav : Root }
    sealed class Auth : Route { object Welcome : Auth; object SignUp : Auth }
    sealed class ActiveRun : Route { object Run : ActiveRun; object SaveRun : ActiveRun }
    sealed class BotNav : Route { object Feed : BotNav; object Stats : BotNav }
    data class UserProfile(val userId: String) : Root()
}
```

### Navigation Best Practices
- Use `@Serializable` for route classes with args
- Pass data via constructor, retrieve in ViewModel with `SavedStateHandle`
- `popUpTo()` clears back stack for auth flows
- Nested NavHost for tab navigation (`BotNavScreen` contains sub-NavHost)

## Compose Conventions

### Screen Structure
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel = hiltViewModel(),  // Inject via Hilt
    onNavigate: (route) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    Scaffold(topBar = { /* ... */ }) { padding ->
        // Content using padding
    }
}
```

### Material Design 3
- Use `androidx.compose.material3` components (not material)
- Theme: `com.rk.pace.theme.*` package with M3 colors & typography
- Bottom navigation via `NavigationBar` + `NavigationBarItem`
- Floating action buttons for primary actions

### Common Patterns
- `Box/Column/Row` for layouts
- `Button`/`IconButton` for interactions
- `LaunchedEffect` for side effects (one-time setup)
- `bottomSheetScaffoldState` for sheets (See `ActiveRunScreen.Content`)

## Testing Structure

- **Unit Tests**: `app/src/test/` (JUnit 4)
- **Instrumented Tests**: `app/src/androidTest/` (Espresso, JUnit runner)
- ViewModel tests use coroutine test dispatcher

## Build & Deployment

 ### Gradle Configuration
 - **compileSdk 36** (Android 15)
 - **minSdk 24**, **targetSdk 36**
 - **Version**: 0.2.0-alpha
 - **Java 21** compatibility (project uses JavaVersion.VERSION_21)

### API Keys
- `local.properties`: GOOGLE_MAPS_API_KEY, MAPBOX_ACCESS_TOKEN
- BuildConfig fields auto-injected at compile time
- Manifest placeholders for Maps SDK initialization

 ### Key Dependencies (See gradle/libs.versions.toml)
 - **Compose BOM 2026.04.01**: Latest stable (updated in libs.versions.toml)
 - **Hilt 2.59.2**, **KSP 2.3.7** for code generation
 - **Room 2.8.4**: Database
 - **Coroutines 1.10.2**: Async
 - **Ktor 3.4.3**, **Maps 8.3.0**: Networking & maps
 - **Firebase BOM 34.12.0**, **Supabase**: Backend

### Build Command
```bash
# Sync Gradle
./gradlew build

# Debug APK
./gradlew assembleDebug

# Run tests
./gradlew test  # Unit
./gradlew connectedAndroidTest  # Instrumented
```

## Common Developer Tasks

### Adding a New Screen
1. Create sealed Route in `presentation/navigation/Route.kt`
2. Add ViewModel with repository injection in `presentation/screens/{feature}/`
3. Create Composable screen with `@Composable` + `hiltViewModel()`
4. Add composable route in `NavGraph.kt` or feature NavHost
5. Ensure ViewModel observes relevant domain flows

### Saving a Run
1. Collect data in `ActiveRunViewModel.runState`
2. Call `RunRepo.saveRun(RunWithPath)` 
3. WorkManager automatically syncs; check `SyncRunWorker` logs
4. Validate `synced=1` in Room after network request

### Database Schema Changes
1. Increment `@Database(version = X)` in `PaceDatabase`
2. Create migration if data must persist, or use `exportSchema = false` (current setup)
3. Update entity classes, DAOs, TypeConverters as needed
4. Test with fresh install + restore flow

### Adding an API Endpoint
1. Use Supabase (`SupabaseClient` from `FirebaseModule`) or Firebase Firestore
2. Create remote data source in `data/remote/source/`
3. Implement in repository, handle offline via Room
4. Trigger sync via WorkManager if applicable

## Error Handling Patterns

### Try-Catch Blocks
- CoroutineWorker: wrap in try-catch, return `Result.retry()` on failure
- Repositories: let exceptions propagate to ViewModel
- ViewModels: use `try-catch` in coroutine blocks, update state with errors

### Logging
- Check logcat during active run tracking
- `SyncRunWorker` logs sync status
- GPS strength warnings if accuracy > 20m

## Performance Considerations

1. **Location Updates**: 3-second interval (`LOCATION_UPDATE_INTERVAL`) balances accuracy/battery
2. **Room Queries**: Use Flow for reactive updates, avoid blocking queries
3. **WorkManager**: Set network/battery constraints to avoid excessive syncing
4. **Compose Recomposition**: Use `collectAsStateWithLifecycle()` to avoid unnecessary recomposes
5. **Polyline Encoding**: Google's algorithm reduces storage ~95% vs raw lat/long lists

## Debugging Tips

- **GPS Issues**: Check `TrackerManager.gpsStrength` and location permissions
- **Sync Failures**: Inspect `SyncRunWorker` logs, validate Firestore rules
- **State Corruption**: Examine `SavedStateHandle` during navigation
- **Build Errors**: Run `./gradlew clean build` and check annotation processors (KSP, Hilt)

