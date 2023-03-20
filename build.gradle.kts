// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val kotlinVersion by extra("1.8.10")
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {

        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.hiltAndroidGradlePlugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath(BuildPlugins.googleGms)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
