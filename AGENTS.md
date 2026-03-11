# AGENTS.md - Unsplash KMP Project Guidelines

This document serves as a guide for AI agents and developers working on the Unsplash KMP project. It
outlines the architecture, coding standards, and project structure to ensure consistency across the
codebase.

## Project Context

Unsplash KMP is a Kotlin Multiplatform (KMP) application for browsing and bookmarking Unsplash
photos. It targets Android, iOS, and Desktop (JVM) using Compose Multiplatform.

## Architecture Map

The project follows **Clean Architecture** with a clear separation of concerns, combined with *
*MVI (Model-View-Intent)** in the presentation layer.

### Layer Responsibilities

- **Presentation Layer (`ui`)**:
    - **Compose Multiplatform**: For building the UI across platforms.
    - **MVI Pattern**: ViewModels extend `MviViewModel` to manage `State` and handle `Event`
      objects.
    - **Navigation**: Managed via **Navigation 3** using a custom `Navigator` and
      `DeeplinkResolver`.
        - **Deep Linking**: Uses `DeeplinkMatcher` implementations registered in `DeeplinkResolver`
          to map URIs to `NavKey` destinations.
- **Domain Layer (`domain`)**:
    - **UseCases**: Encapsulate single business actions (e.g., `SavePhotoUseCase`).
    - **Domain Models**: Plain Kotlin objects representing business entities.
- **Data Layer (`data`)**:
    - **Repositories**: Interfaces defined for data access. Implementations (`*Impl`) reside in the
      data layer.
    - **Remote**: Ktorfit-based API interfaces (`ApiInterface`).
    - **Local**: Room database and DataStore for caching and preferences (e.g., Theme, Search
      Chips).
    - **Mappers**: Essential for converting between Remote/Local entities and Domain/UI models (
      extending `UIModelMapper`).

## Coding Standards

- **Kotlin-First**: Leverage modern Kotlin features like Coroutines, Flow, and `context` parameters.
- **Compose**: Use `internal` visibility for screen-level Composables. Prefer `Modifier` as the
  first optional parameter.
- **Navigation**: Destinations are defined as `@Serializable` `NavKey` objects (e.g.,
  `PhotoScreen`).
- **Naming Conventions**:
    - ViewModels: `[Feature]ViewModel`
    - Screens: `[Feature]Screen` (Composable)
    - UseCases: `[Action][Entity]UseCase`
    - Mappers: `[Source]To[Target]Mapper` or extending `UIModelMapper`.
- **Dependency Injection**: Use **Koin**. Modules are split by responsibility (e.g.,
  `networkModule`, `repositoryModule`) and initialized in `Koin.kt`.
- **Visibility**: Use `internal` for classes and functions that don't need to be exposed outside
  their module or package.

## Dependency Rules

- **Version Catalog**: All dependencies must be defined in `gradle/libs.versions.toml`.
- **Multiplatform**: Prefer Multiplatform libraries (Ktor, Room, Coil, Koin, Datastore).
- **Secrets**: Use `BuildKonfig` for API keys. Secrets are read from `local.properties` via a helper
  function in `build.gradle.kts`.

## Platform Constraints

- **Expect/Actual**: Used for platform-specific logic like `DatabaseFactory`,
  `PlatformDownloadImage`, and `PlatformAppearance`.
- **Android**:
    - **Glance App Widgets**: Used for home screen integration. Widgets communicate with the main
      app via deep links (`unsplashkmp://...`).
    - **Manifest**: Deep link hosts (e.g., `photo`, `bookmarks`) must be explicitly declared in
      `AndroidManifest.xml`.
    - **WorkManager**: Used for background tasks like widget data updates.
- **Desktop**: Requires specific `skiko` runtime configurations for navigation support.

## Testing Expectations

- **Unit Tests**: Located in `commonTest`.
- **Repositories**: Test implementation details and mapping logic.
- **Mocking**: Use `MockEngine` for Ktor and mock implementations for platform-specific interfaces.

## File Structure

```
composeApp/src/commonMain/kotlin/
├── data/           # mapper/, local/, remote/, repository/
├── domain/         # model/, usecase/
├── ui/             # component/, screen/, viewmodel/, navigation/, theme/
├── di/             # Koin modules
├── networking/     # Ktorfit/Ktor setup
└── Platform.kt     # Common expect declarations
```

## Anti-patterns to Avoid

- **Bypassing UseCases**: Do not call Repositories directly from ViewModels.
- **Hardcoded Strings/Secrets**: Use `Res` for strings and `local.properties`/`BuildKonfig` for
  keys.
- **Hardcoded Deep Links**: Ensure new deep link paths are added to both a `DeeplinkMatcher` and the
  `AndroidManifest.xml`.
- **State in UI**: Keep Composables stateless; hoist state to ViewModels.
- **Fat ViewModels**: Delegate logic to UseCases.

## Code Examples

### Deeplink Matcher

```kotlin
internal object BookmarkMatcher : DeeplinkMatcher {
    private val bookmarkRegex = """unsplashkmp://bookmarks""".toRegex()

    override fun match(deeplink: String): NavKey? {
        return if (bookmarkRegex.containsMatchIn(deeplink)) PhotoScreen.BookmarkScreen else null
    }
}
```

### MVI ViewModel

```kotlin
internal abstract class MviViewModel<Event, State>(defaultState: State) : ViewModel() {
    var state by mutableStateOf(defaultState)
        protected set

    fun dispatch(event: Event) { /* ... */
    }

    protected inline fun <reified SpecificEvent : Event> on(
        crossinline handle: suspend (event: SpecificEvent) -> Unit
    ) { /* ... */
    }
}
```

### Data Mapping

```kotlin
internal class PhotoMapper(
    private val photosUrlsMapper: PhotosUrlsMapper,
    private val photoCreatorMapper: PhotoCreatorMapper
) : UIModelMapper<UnsplashPhotoRemote, Photo>() {
    override fun mapToUI(entity: UnsplashPhotoRemote): Photo {
        return with(entity) {
            Photo(id, ..., photosUrlsMapper.mapToUI(urls), ...)
        }
    }
}
```

### Koin Module Registration

```kotlin
private fun repositoryModule() = module {
    single<ImageRepository> { ImageRepositoryImpl(get()) }
    singleOf(::UnsplashImageLocalRepositoryImpl).bind<UnsplashImageLocalRepository>()
}
```
