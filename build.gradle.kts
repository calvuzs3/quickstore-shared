import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "net.calvuz"
version = "0.1.1"

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // Prep per un futuro client iOS (nessuna app Xcode ancora — vedi CLAUDE.md di
    // QuickStore). Compilare i target iosArm64/iosSimulatorArm64/iosX64 richiede un
    // host macOS con Xcode: su questa macchina (Linux) solo la dichiarazione dei
    // target e la compilazione dei klib (commonMain/jvm/ios*) restano verificabili,
    // non il link di un vero .framework nativo.
    //
    // Niente `binaries.framework{}` per ora: con Kotlin Gradle Plugin 2.1.0 quel blocco
    // rompe il consumo di questo modulo da `quickstore-server` (composite build su
    // Gradle 9.1.0, mentre QuickStore/quickstore-shared sono su 8.11.1 — un build
    // composite esegue il modulo incluso con la versione di Gradle del progetto che lo
    // include, non con il proprio wrapper) — errore
    // `DefaultArtifactPublicationSet` non trovata, API interna di Gradle rimossa in
    // 9.x che il code path di export framework di KGP 2.1.0 referenzia ancora. Da
    // riprendere quando si affronta davvero l'app iOS: o si bumpa il Kotlin Gradle
    // Plugin qui (rischio: le classi Kotlin generate potrebbero non essere più
    // compatibili con QuickStore, che è fissato su Kotlin 2.1.0 — stesso vincolo già
    // documentato per Ktor in CLAUDE.md), o si isola l'export framework in una
    // configurazione che non venga valutata sotto Gradle 9.
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
