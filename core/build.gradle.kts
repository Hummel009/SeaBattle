plugins {
	id("org.jetbrains.kotlin.jvm")
}

dependencies {
	api("com.badlogicgames.gdx:gdx:1.12.1")
	api("com.badlogicgames.gdx:gdx-freetype:1.12.1")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}