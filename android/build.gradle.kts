import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("com.android.application")
}

android {
	namespace = "com.github.hummel.sb"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.github.hummel.sb"
		minSdk = 34
		targetSdk = 34
		versionName = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))
	}

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
}

val natives: Configuration by configurations.creating

dependencies {
	implementation(project(":core"))

	api("com.badlogicgames.gdx:gdx-backend-android:latest.release")

	natives("com.badlogicgames.gdx:gdx-platform:1.13.0:natives-armeabi-v7a")
	natives("com.badlogicgames.gdx:gdx-platform:1.13.0:natives-arm64-v8a")
	natives("com.badlogicgames.gdx:gdx-platform:1.13.0:natives-x86")
	natives("com.badlogicgames.gdx:gdx-platform:1.13.0:natives-x86_64")

	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.13.0:natives-armeabi-v7a")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.13.0:natives-arm64-v8a")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.13.0:natives-x86")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.13.0:natives-x86_64")
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