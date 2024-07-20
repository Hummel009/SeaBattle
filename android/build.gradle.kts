import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

android {
	namespace = "com.github.hummel.sb"
	compileSdk = 34

	packaging {
		resources {
			excludes.add("META-INF/*")
		}
	}

	defaultConfig {
		applicationId = "com.github.hummel.sb"
		minSdk = 34
		targetSdk = 34
		versionName = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))
	}
}

dependencies {
	api("com.badlogicgames.gdx:gdx-freetype:1.12.1")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-armeabi-v7a")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-arm64-v8a")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-x86")
	natives("com.badlogicgames.gdx:gdx-freetype-platform:1.12.1:natives-x86_64")
}

tasks.named("preBuild").configure {
	dependsOn("copyAndroidNatives")
}

tasks.register("copyAndroidNatives") {
	doFirst {
		file("libs/armeabi-v7a/").mkdirs()
		file("libs/arm64-v8a/").mkdirs()
		file("libs/x86_64/").mkdirs()
		file("libs/x86/").mkdirs()

		project.configurations["natives"].files.forEach { jar ->
			var outputDir: File? = null
			if (jar.name.endsWith("natives-arm64-v8a.jar")) outputDir = file("libs/arm64-v8a")
			if (jar.name.endsWith("natives-armeabi-v7a.jar")) outputDir = file("libs/armeabi-v7a")
			if (jar.name.endsWith("natives-x86_64.jar")) outputDir = file("libs/x86_64")
			if (jar.name.endsWith("natives-x86.jar")) outputDir = file("libs/x86")
			if (outputDir != null) {
				copy {
					from(zipTree(jar))
					into(outputDir)
					include("*.so")
				}
			}
		}
	}
}

tasks.register<Exec>("run") {
	description = "Runs the application with ADB"
	group = "install"

	val localProperties = project.file("../local.properties")
	val path = if (localProperties.exists()) {
		val properties = Properties()
		properties.load(localProperties.inputStream())
		val sdkDir = properties.getProperty("sdk.dir")
		sdkDir ?: System.getenv("ANDROID_HOME")
	} else {
		System.getenv("ANDROID_HOME")
	}

	val adb = "$path/platform-tools/adb"
	commandLine(adb, "shell", "am", "start", "-n", "pl.baftek.spitfire/pl.baftek.spitfire.AndroidLauncher")
}