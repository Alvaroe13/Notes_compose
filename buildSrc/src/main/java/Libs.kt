object Libs {

    const val ktx_standard_lib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
    const val androidCore = "androidx.core:core-ktx:${Version.android_core}"
    const val appCompat = "androidx.appcompat:appcompat:${Version.app_compat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraint_layout}"

    // Material Design
    const val materialDesign = "com.google.android.material:material:${Version.material_design}"

    //Nav Component
    const val nav_fragment = "androidx.navigation:navigation-fragment-ktx:${Version.nav_version}"
    const val nav_ui = "androidx.navigation:navigation-ui-ktx:${Version.nav_version}"
    //LifeCycle scope for kotlin
    const val lifecycle_runtime ="androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata:${Version.lifecycle}"
    const val lifecycle_livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    // ViewModel
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"

    //Room
    const val room = "androidx.room:room-runtime:${Version.room}"
    const val room_compiler = "androidx.room:room-compiler:${Version.room}"
    // optional - Kotlin Extensions and Coroutines support for Room
    const val room_ktx_coroutines = "androidx.room:room-ktx:${Version.room}"

    //coroutines
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"

    //hilt
    const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hilt_kapt= "com.google.dagger:hilt-android-compiler:${Version.hilt}"

    //testing
    const val jUnit = "junit:junit:${Version.jUnit}"
    const val jUnit_androidx_ext = "androidx.test.ext:junit:${Version.jUnit_androidx_ext}"
    const val core_testing = "androidx.arch.core:core-testing:${Version.core_testing}"
    const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutines_test}"
    const val mockito_core = "org.mockito:mockito-core:${Version.mockito}"
    const val mockito_inline = "org.mockito:mockito-inline:${Version.mockito}"
    const val turbine = "app.cash.turbine:turbine:${Version.turbine}"

}