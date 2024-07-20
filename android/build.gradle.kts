import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("com.android.application")
}

android {
	namespace = "com.github.hummel.sb"
	compileSdk = 34

	packaging {
		resources {
			excludes.add("META-INF/*")
		}
	}

	sourceSets {
		named("main") {
			jniLibs.srcDirs("libs")
		}
	}

	defaultConfig {
		applicationId = "com.github.hummel.sb"
		minSdk = 34
		targetSdk = 34
		versionName = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))
	}
}

val natives: Configuration by configurations.creating

dependencies {
	implementation(project(":core"))

	api("com.badlogicgames.gdx:gdx-freetype:1.12.1")
	api("com.badlogicgames.gdx:gdx-backend-android:1.12.1")

	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-armeabi-v7a")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-arm64-v8a")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-x86")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-x86_64")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}

tasks.whenTaskAdded {
	if ("package" in name) {
		dependsOn("copyAndroidNatives")
	}
}

tasks.register("copyAndroidNatives") {
	doFirst {
		natives.files.forEach { jar ->
			val outputDir = file("libs/" + jar.nameWithoutExtension.substringAfterLast("natives-"))
			outputDir.mkdirs()
			copy {
				from(zipTree(jar))
				into(outputDir)
				include("*.so")
			}
		}
	}
}