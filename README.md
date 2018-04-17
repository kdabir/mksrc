# Make Source Dirs Gradle Plugin

[![Build Status](https://travis-ci.org/kdabir/mksrc.svg?branch=master)](https://travis-ci.org/kdabir/mksrc)


Create source directory structure as per the applied plugins (`java`, `groovy`, `scala`, `war`)

## Usage

Apply the plugin to the project

    plugins {
      id "com.kdabir.mksrc" version "1.0.0"
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
