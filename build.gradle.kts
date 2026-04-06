plugins {
    kotlin("jvm") version "2.2.10"
    jacoco
}

group = "ru.netology"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.test {
    //useJUnitPlatform()
    useJUnit()
    finalizedBy(tasks.jacocoTestReport)
    systemProperty("file.encoding", "UTF-8")
    jvmArgs = listOf("-Dfile.encoding=UTF-8")

}
kotlin {
    jvmToolchain(21)
}

tasks.withType<JavaExec> {
    systemProperty("file.encoding", "UTF-8")
    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}
