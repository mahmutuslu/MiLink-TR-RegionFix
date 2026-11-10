plugins {
    id("com.android.application")
}

android {
    namespace = "dev.milinktr.regionfix"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.milinktr.regionfix"
        minSdk = 29
        targetSdk = 32
        versionCode = 6
        versionName = "0.6.0-alpha6-api102"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }
}

dependencies {
    compileOnly("io.github.libxposed:api:102.0.0")
}
