sealed class Platform {
    data object Android : Platform()

    data object Apple : Platform()
    
    data object Desktop : Platform()
}

expect fun getPlatform(): Platform