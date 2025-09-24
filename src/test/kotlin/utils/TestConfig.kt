package utils
import java.util.Properties
object TestConfig {
    private val properties: Properties by lazy {
        Properties().apply {
            load(TestConfig::class.java.classLoader.getResourceAsStream("config.properties"))
        }
    }

    val apiKey: String by lazy { properties.getProperty("api.key") }
    val baseUrl: String by lazy { properties.getProperty("base.url") }
}