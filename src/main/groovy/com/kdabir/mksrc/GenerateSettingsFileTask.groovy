package com.kdabir.mksrc

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenerateSettingsFileTask extends DefaultTask {

    final static String warning = "/* >>> Auto-generated section - Do not edit below this <<< */"

    @TaskAction
    def generate() {
        project.logger.info('creating settings.gradle...')

        File rootDir = project.getRootProject().getRootDir()
        File settingsGradleFile = new File(rootDir, "settings.gradle")
        String content = settingsGradleFile.isFile()? settingsGradleFile.text : ""
        int warningIndex = content.indexOf(warning)
        String nonGeneratedContent = (warningIndex >= 0) ? content.substring(0, warningIndex) : content
        String generatedContent = generateSettingsText(rootDir)
        settingsGradleFile.text = nonGeneratedContent + generatedContent
    }

    static String generateSettingsText(File projectRootDir) {
        String header = joined(warning, "rootProject.name = '${projectRootDir.name}'")

        List<String> projects = gradleFiles(projectRootDir).collect {
            String subProjectName = ":" + it.parentFile.path.replaceAll(File.separator, ":")
            String buildFileName = it.name

            joined("include('${subProjectName}')", "project('${subProjectName}').setBuildFileName('${buildFileName}')")
        }

        joined(header, *projects).trim()
    }

    static List<File> gradleFiles(File projectRootDir) {
        def rootPath = projectRootDir.toPath()
        List filesPath = []
        projectRootDir.eachDirRecurse() { dir ->
            File moduleGradleFile = new File(dir, dir.name + '.gradle')
            if (moduleGradleFile.exists()) {
                filesPath << rootPath.relativize(moduleGradleFile.toPath()).toFile()
            }
        }
        return filesPath
    }

    static String joined(String... lines) {
        lines.collect { it + System.lineSeparator() }.join() // so that line line has linefeed
    }

}
