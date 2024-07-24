plugins {
	id("org.jetbrains.kotlin.jvm")
}

dependencies {
	api("com.badlogicgames.gdx:gdx-freetype:latest.release")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}