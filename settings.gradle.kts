pluginManagement {
	repositories {
		maven("https://oss.sonatype.org/content/repositories/snapshots/")
		maven("https://oss.sonatype.org/content/repositories/releases/")
		maven("https://jitpack.io")
		google()
		mavenLocal()
		mavenCentral()
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		maven("https://oss.sonatype.org/content/repositories/snapshots/")
		maven("https://oss.sonatype.org/content/repositories/releases/")
		maven("https://jitpack.io")
		google()
		mavenLocal()
		mavenCentral()
		gradlePluginPortal()
	}
}

include("android")
include("core")