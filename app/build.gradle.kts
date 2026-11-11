plugins {
    id("com.android.application")
}

android {
    namespace = "dev.milinktr.regionfix"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.hyperostr.unified"
        minSdk = 29
        targetSdk = 32
        versionCode = 9
        versionName = "1.0.0-api102"
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
