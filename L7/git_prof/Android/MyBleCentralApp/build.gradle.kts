// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

extra["compose_version"] = "1.4.3"

// You can access the extra properties like this:
val composeVersion: String by extra