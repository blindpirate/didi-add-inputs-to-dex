package com.didi;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
    }

    public static void compile(Project project, FileCollection source, File destination, FileCollection classpath) {
        project.exec(execSpec -> {
            File javaHome = new File(System.getProperty("java.home"));
            File javac = "jre".equals(javaHome.getName()) ?
                new File(javaHome, "../bin/javac") :
                new File(javaHome, "bin/javac");

            String cp = classpath.getAsPath();
            List<String> cmdArgs = new ArrayList<>();
            cmdArgs.addAll(Arrays.asList(javac.getAbsolutePath(), "-d", destination.getAbsolutePath(), "-cp", cp));
            source.getAsFileTree().getFiles().stream()
                .filter(file -> file.getName().endsWith(".java"))
                .map(File::getAbsolutePath)
                .forEach(cmdArgs::add);

            System.out.println("Running javac: " + String.join(" ", cmdArgs));

            execSpec.commandLine(cmdArgs);
            execSpec.setStandardOutput(System.out);
            execSpec.setErrorOutput(System.err);
        });
    }
}
