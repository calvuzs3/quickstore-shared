import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "net.calvuz"
version = "0.1.8"

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
    // `binaries.framework{}` con Kotlin Gradle Plugin 2.1.0 referenzia
    // `DefaultArtifactPublicationSet`, un'API interna di Gradle rimossa in 9.x —
    // rompe il consumo di questo modulo da `quickstore-server` (composite build su
    // Gradle 9.1.0, mentre QuickStore/quickstore-shared sono su 8.11.1: un build
    // composite esegue il modulo incluso con la versione di Gradle del progetto che
    // lo include, non col proprio wrapper). Isolato quindi dietro un check di
    // versione: valutato solo quando la build gira su Gradle < 9 (questo modulo da
    // solo, o incluso da QuickStore — entrambi su 8.11.1), saltato quando gira su
    // Gradle 9 (incluso da quickstore-server). Il giorno in cui esisterà un vero
    // progetto Xcode che consuma il .framework, girerà sempre con lo stesso Gradle
    // 8.11.1 di questo modulo, quindi il check basta senza dover bumpare KGP (rischio
    // di rottura ABI con QuickStore, fissato su Kotlin 2.1.0 — vedi nota Ktor in
    // QuickStore/CLAUDE.md).
    val exportFramework = GradleVersion.current() < GradleVersion.version("9.0")
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { target ->
        if (exportFramework) {
            target.binaries.framework {
                baseName = "QuickstoreShared"
                isStatic = true
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
