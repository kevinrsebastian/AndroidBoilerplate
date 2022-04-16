package ph.teko.app.core.config

/** Environment configurations visible to other modules, but populated and injected in the App module */
data class EnvConfig(
    val tekoRestApiUrl: String
)
