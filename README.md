# Make Source Dirs Gradle Plugin

[![Gradle Plugin](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/gradle/plugin/com/kdabir/mksrc/mksrc/maven-metadata.xml?color=blue&label=GradlePlugin)](https://plugins.gradle.org/plugin/com.kdabir.mksrc)

1. Creates source directory structure as per the applied plugins (`java`, `groovy`, `kotlin`, `scala`, `war`) and custom `sourceSet` dirs
2. Automatically generate / updates `settings.gradle` file to include child projects based on convention

## Usage

Apply the plugin to the project

    plugins {
      id "com.kdabir.mksrc" version "1.0.1"
    }


The plugin adds `makeSourceDirs` to the project. Run the following command 

    $ gradle makeSourceDirs 

Based on the plugins applied to project (and configured `sourceSets`), the source dirs will be created 

    $ tree src
    src
    ├── main
    │   ├── java
    │   └── resources
    └── test
        ├── java
        └── resources




## For multi-project builds

If the top level project doesn't need source dirs, but the subprojects do, then use `apply false` in `plugins` block 
and apply the plugin to all `subprojects`. Adding the following snippet in the top level project's `build.gradle` 
should work :
 

```
plugins {
  id "com.kdabir.mksrc" version "1.0.0" apply false
}

subprojects {
  apply plugin: 'com.kdabir.mksrc'
}
```


## Generating `settings.gradle` File Automatically

In a multi-module project, it is often pain to keep `settings.gradle` in sync with the nested modules/projects structure
(as we need to include sub-projects explicitly). Also, since all files are named `build.gradle`, it is hard to locate 
the right build file for the sub-project (in IDEs). This plugin solves both the problems by using simple convention and 
then generating/syncing `settings.gradle` file on-demand. 

Instead of naming the build files for all sub-projects as `build.gradle`, we keep a `<module-name>.gradle` 
within the `<module-name>/` dir. The plugin adds `generateSettingsFile` task to the project. This task 
updates `settings.gradle` to include all sub projects' `<module-name>/<module-name>.gradle` into the build.     


Usage:

    $ gradle generateSettingsFile 


> do commit your existing settings.gradle to version control before calling this task.
