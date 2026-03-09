# AGENTS.md - Unsplash KMP Project Guidelines

This document serves as a guide for AI agents and developers working on the Unsplash KMP project. It outlines the architecture, coding standards, and project structure to ensure consistency across the codebase.

## Project Overview
Unsplash KMP is a Kotlin Multiplatform (KMP) application that allows users to browse and bookmark photos from Unsplash. It supports Android, iOS, and Desktop (JVM) using Compose Multiplatform.

## Architecture Rules
The project follows **Clean Architecture** combined with the **MVI (Model-View-Intent)** pattern.

- **Presentation Layer (`ui`)**: Uses Compose for UI and ViewModels for state management. ViewModels must extend `MviViewModel` and handle `State` and `Event`.
- **Domain Layer (`domain`)**: Contains business logic encapsulated in `UseCase` classes. Use cases should perform a single task and be named `[Action][Entity]UseCase` (e.g., `SavePhotoUseCase`).
- **Data Layer (`data`)**: Handles data sourcing from Remote (Ktorfit) and Local (Room/DataStore).
    - Use **Mappers** to convert between Data models (Remote/Local) and Domain/UI models.
    - Repositories should have an interface in the domain/data package and an implementation in the `data.repository` package.

## Coding Conventions
- **Language**: Kotlin.
- **UI Framework**: Compose Multiplatform.
- **State Management**: Use `MviViewModel`. UI should observe `state` and dispatch `events`.
- **Dependency Injection**: Koin. All new dependencies must be registered in `di/Koin.kt`.
- **Concurrency**: Coroutines. Use `viewModelScope` in ViewModels and proper dispatchers (e.g., `Dispatchers.IO` for DB/Network).
- **Naming**:
    - ViewModels: `[Feature]ViewModel`.
    - Screens: `[Feature]Screen`.
    - UseCases: `[Action][Entity]UseCase`.
    - Mappers: `to[TargetModel]()` or `[Model]Mapper`.

## Module Responsibilities
- `commonMain`: Shared logic, UI, and data handling.
- `androidMain`: Android-specific implementations (Widgets, WorkManager, Application class).
- `iosMain`: iOS-specific configurations and framework exports.
- `desktopMain`: Desktop-specific entry point and configurations.

## Dependency Guidelines
- All dependencies must be managed in `gradle/libs.versions.toml`.
- Use **Version Catalogs** for all plugin and library declarations.
- Prefer Multiplatform libraries (e.g., Ktor, Coil, Room, Koin).

## Platform-Specific Rules
- **Android**: Use `androidMain` for Glance Widgets and WorkManager tasks.
- **iOS**: Ensure frameworks are properly exported in `build.gradle.kts`.
- **Desktop**: Handle OS-specific Skiko requirements in `desktopMain`.

## Testing Guidelines
- Shared logic tests go in `commonTest`.
- Use `KoinTest` for dependency-related tests.
- Mock network responses using `Ktor`'s `MockEngine`.

## File Structure
```
composeApp/src/commonMain/kotlin/
├── data/           # Repositories, Models, Mappers, Remote/Local sources
├── domain/         # UseCases, Domain Models
├── ui/             # Compose Screens, ViewModels, Theme, Navigation
├── di/             # Koin Modules
├── networking/     # Ktor/Ktorfit setup
└── Platform.kt     # Expect declarations
```

## Anti-patterns to Avoid
- **No Hardcoded Secrets**: Use `BuildKonfig` and `local.properties`.
- **No Direct Repo Access**: UI should interact with UseCases, not Repositories directly.
- **No Logic in UI**: Compose functions should be as stateless as possible.
- **No Platform Code in Common**: Use `expect`/`actual` or interfaces injected via Koin.

## Examples

### ViewModel (MVI)
```kotlin
class MyViewModel(private val myUseCase: MyUseCase) : MviViewModel<MyEvent, MyState>(MyState()) {
    init {
        on<MyEvent.LoadData> {
            val result = myUseCase()
            state = state.copy(data = result)
        }
    }
}
```

### UseCase
```kotlin
class SavePhotoUseCase(private val repository: UnsplashImageLocalRepository) {
    suspend operator fun invoke(photo: UnsplashPhotoRemote) = repository.insertPhoto(photo.toLocal())
}
```

### Dependency Injection
```kotlin
private fun useCaseModule() = module {
    singleOf(::SavePhotoUseCase)
}
```
