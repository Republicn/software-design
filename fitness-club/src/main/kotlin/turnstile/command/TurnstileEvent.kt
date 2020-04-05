package turnstile.command

import java.time.LocalDateTime

sealed class TurnstileEvent

data class Enter(val subscriptionId: Int, val userId: Int, val timestamp: LocalDateTime) : TurnstileEvent()
data class Exit(val subscriptionId: Int, val userId: Int, val timestamp: LocalDateTime) : TurnstileEvent()
