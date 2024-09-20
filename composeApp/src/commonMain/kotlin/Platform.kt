sealed class Platform {
    data object Android : Platform()

    data object Ios : Platform()
    
    data object Desktop : Platform()
}

expect fun getPlatform(): Platform