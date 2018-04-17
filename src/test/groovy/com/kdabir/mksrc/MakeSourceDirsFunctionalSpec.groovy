package com.kdabir.mksrc

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class MakeSourceDirsFunctionalSpec extends Specification {

    @Rule
    TemporaryFolder projectDir = new TemporaryFolder()
    File testProject

    def setup() {
        testProject = projectDir.newFolder("test-project")
    }

    def teardown() {
        testProject.deleteDir()
    }

    def "warns for no source sets"() {
        given: "failed tests file already do not exist"
        new File(testProject, "build.gradle").text = """\
        plugins {
            id "com.kdabir.mksrc"
        }
        """

        when: "running the build expecting failure"
        def result = runTask()

        then:
        !new File(testProject, 'src').exists()
        result.output.contains("No sourceSets found. Please apply a language plugin")
    }

    def "creates dirs java"() {
        given: "failed tests file already do not exist"
        new File(testProject, "build.gradle").text = """\
        plugins {
            id 'java'
            id 'com.kdabir.mksrc'
        }
        """

        when: "running the build expecting failure"
        def result = runTask()

        then:
        dirsPresent "src/main/java", "src/test/java", "src/main/resources", "src/test/resources"
    }

    def "creates for webapp"() {
        given: "failed tests file already do not exist"
        new File(testProject, "build.gradle").text = """\
        plugins {
            id 'java'
            id 'war'
            id 'com.kdabir.mksrc'
        }
        """

        when: "running the build expecting failure"
        def result = runTask()

        then:
        dirsPresent "src/main/java", "src/test/java", "src/main/resources", "src/test/resources", "src/main/webapp"
    }


    def "creates for groovy"() {
        given: "failed tests file already do not exist"
        new File(testProject, "build.gradle").text = """\
        plugins {
            id 'groovy'
            id 'com.kdabir.mksrc'
        }
        """

        when: "running the build expecting failure"
        def result = runTask()

        then:
        dirsPresent "src/main/java", "src/test/java", "src/main/resources", "src/test/resources", "src/main/groovy", "src/test/groovy"
    }

    private BuildResult runTask() {
        GradleRunner.create()
            .withProjectDir(testProject)
            .withArguments('makeSourceDirs')
            .withPluginClasspath()
            .build()
    }

    boolean dirsPresent(String... dirnames) {
        dirnames.collect { new File(testProject, it) }.every { it.isDirectory() }
    }
}
