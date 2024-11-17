plugins {
	id("org.jetbrains.kotlin.jvm")
}

dependencies {
	api("com.badlogicgames.gdx:gdx:1.13.0")
	api("com.badlogicgames.gdx:gdx-freetype:1.13.0")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}