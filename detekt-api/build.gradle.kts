import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    module
    id("org.jetbrains.dokka")
    `java-test-fixtures`
    id("binary-compatibility-validator")
}

dependencies {
    api(kotlin("compiler-embeddable"))
    api(project(":detekt-psi-utils"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    testImplementation(project(":detekt-test"))

    testFixturesApi(kotlin("stdlib-jdk8"))

    dokkaJekyllPlugin("org.jetbrains.dokka:jekyll-plugin")
}

val javaComponent = components["java"] as AdhocComponentWithVariants
listOf(configurations.testFixturesApiElements, configurations.testFixturesRuntimeElements).forEach { config ->
    config.configure {
        javaComponent.withVariantsFromConfiguration(this) {
            skip()
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    outputDirectory.set(rootDir.resolve("docs/pages/kdoc"))
}

apiValidation {
    ignoredPackages.add("io.gitlab.arturbosch.detekt.api.internal")
}
