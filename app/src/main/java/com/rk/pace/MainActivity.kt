package com.rk.pace

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rk.pace.common.extension.hasPostNotificationPermission
import com.rk.pace.presentation.Route
import com.rk.pace.presentation.screens.run.RunScreen
import com.rk.pace.presentation.screens.top.TopScreen
import com.rk.pace.presentation.theme.PaceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!this.hasPostNotificationPermission()) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        enableEdgeToEdge()
        setContent {
            PaceTheme {
                PaceApp()
            }
        }
    }
}

@Composable
fun PaceApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Root.Top
    ) {
        composable<Route.Root.Top> {
            TopScreen(
                navController = navController
            )
        }
        composable<Route.Root.Run>(
            deepLinks = Route.Root.Run.deepLinks
        ) {
            RunScreen(
                goBack = { navController.popBackStack() }
            )
        }
    }
}

// 🔥 ADVANCED FEATURES TO SHOWCASE

// 1. GPS & Location Services
//- FusedLocationProviderClient
//- Foreground Service with notification
//- Location permissions (runtime + background)
//- Battery optimization handling
//
//// 2. Background Processing
//- WorkManager for periodic syncing
//- Service lifecycle management
//- WakeLock handling
//
//// 3. Maps Integration
//- Google Maps SDK
//- Polyline drawing for routes
//- Custom map styling
//- Marker clustering
//
//// 4. Database (Room)
//- Complex relationships with @Relation
//- Database views for aggregations
//- Proper indexing for performance
//- TypeConverters for custom types
//
//// 5. Data Visualization
//- MPAndroidChart / Compose Charts
//- Line charts for pace/distance
//- Bar charts for weekly stats
//- Pie charts for workout distribution
//
//// 6. Sensors
//- Step counter (if available)
//- Heart rate monitor (Wear OS integration)
//- Altitude/elevation tracking
//
//// 7. Architecture
//- Clean Architecture
//- MVVM with UseCases
//- Kotlin Flows for reactive data
//- Sealed classes for UI states
//```
//
//---
//
//## **Complete Tech Stack:**
//```
//UI Layer:
//├── Jetpack Compose
//├── Material Design 3
//├── Compose Navigation
//├── Google Maps Compose
//
//Domain Layer:
//├── Use Cases (StartRun, StopRun, CalculateStats)
//├── Repository interfaces
//└── Domain models
//
//Data Layer:
//├── Room Database (8 tables)
//├── Location Repository
//├── SharedPreferences/DataStore
//└── WorkManager
//
//External:
//├── Google Play Services (Location, Maps)
//├── MPAndroidChart / Vico Charts
//├── Coil (image loading)
//├── Hilt (DI)
//└── Timber (logging)
//
//Testing:
//├── JUnit + Truth
//├── MockK
//├── Robolectric (for Location services)
//└── Compose UI Tests
//```
//
//---
//
//## **Play Store Differentiation:**
//
//**Unique Features to Stand Out:**
//1. **Shoe mileage tracking** - Most apps don't have this
//2. **Route comparison** - Compare different attempts on same route
//3. **Weather integration** - Log weather conditions per run
//4. **Photo markers** - Add photos at specific points during run
//5. **Audio coaching** - "1km completed, pace 5:30/km"
//6. **Offline-first** - Works without internet, syncs later
//
//---
//
//## **Sample Feature Timeline (4-5 weeks):**
//
//### **Week 1: Core Tracking**
//- Database schema + Room setup
//- Basic GPS tracking service
//- Start/Stop/Pause run
//- Distance calculation
//- Simple list of runs
//
//### **Week 2: Maps & Visualization**
//- Google Maps integration
//- Draw route polylines
//- Statistics calculations
//- Charts for weekly stats
//
//### **Week 3: Advanced Features**
//- Goals system
//- Achievements
//- Shoe tracker
//- Personal records
//
//### **Week 4: Polish & Extras**
//- Notifications
//- App widgets
//- Settings screen
//- Export data (GPX format)
//- Dark theme
//
//### **Week 5: Testing & Play Store**
//- Unit tests
//- UI tests
//- Play Store assets
//- Beta testing
//
//---
//
//## **Resume Bullet Points Examples:**
//```
//✅ "Built GPS-based running tracker with real-time location
//tracking, processing 1000+ location points per run"
//
//✅ "Implemented complex Room database with 8 tables and multiple
//relationships, optimized queries reduced load time by 60%"
//
//✅ "Integrated Google Maps SDK with custom polyline rendering
//and route visualization"
//
//✅ "Developed foreground service for background GPS tracking
//with battery optimization"
//
//✅ "Created data visualization dashboard using MPAndroidChart
//with weekly/monthly aggregations"
//
//✅ "Achieved 4.5+ rating with 500+ downloads on Play Store
//within first month"