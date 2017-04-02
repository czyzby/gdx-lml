# Examples

This section includes some example LibGDX projects using the presented libraries. All these projects are separate and autonomous from the main Gradle build.

If any of the on-line examples doesn't seem to work, try reloading the page and clearing the browser cache.

Note that while this folder contains a `build.gradle` file, this is only a utility for running tasks en masse - this is **not** the root `build.gradle` of all example projects, they are all autonomous.

Running `gradle eclipseAll` will generate Eclipse meta-data for all example projects. `gradle updateVersion` copies `gradle.properties` to all example projects, allowing to quickly change the version of used libraries. `gradle runAll` will invoke `desktop:run` task on every project, starting desktop client versions one by one for quick testing. Some examples require a server to run properly (web socket tests) and will fail to run if the server application is not started - this is expected and these projects should be tested "manually". (Tip: the running order is always pretty much the same. You can open a new terminal tab and manually run appropriate server application before the desktop client launches.) `gradle cleanAll` allows you to clean build directories of all example projects. Useful for forcing rebuild after deploying an updated snapshot version to Maven Local.

Examples might be using latest snapshot version and contain new features. To check which library version is currently used, inspect `gradle.properties` file.

Note that many examples do not include mobile platform projects. This **not** because they are unsupported - they're usually excluded for simplicity.

Projects with Android modules need a `local.properties` file with `sdk.dir` property to compile.

Some test project _might_ fail on some platforms (Android, GWT) due to outdated dependencies. Try copying the sources to your project and running them locally if that's the case. If you notice that any test project fails to run, don't hesitate to create an issue.
