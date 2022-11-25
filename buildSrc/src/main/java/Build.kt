object Build {
    const val applicationId = "com.alvaro.notes_compose"
    const val minSdk =  24
    const val targetSdk = 32
    const val versionCode = 1
    const val versionName = "1.0"
    const val compileSdk = 32
    const val buildToolsVersion = "29.0.3"


    //gradle plugin
    const val gradle = "com.android.tools.build:gradle:${Version.gradle}"
    const val gradle_plugin_kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
    const val hilt_gradle_plugin = "com.google.dagger:hilt-android-gradle-plugin:${Version.hilt}"
}