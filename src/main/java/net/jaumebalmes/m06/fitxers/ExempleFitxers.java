package net.jaumebalmes.m06.fitxers;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

public class ExempleFitxers {

    public static void main(String[] args) {
        if (args.length == 1) {
            Path dir = Paths.get(args[0]);
            System.out.println("Fitxers del directori " + dir);
            if (Files.isDirectory(dir)) {
                showModifiedLastWeek(dir);
            } else {
                System.err.println("Ús: java LlistarDirectori <directori>");
            }
        }

    }

    private static void showModifiedLastWeek(Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir);) {
            for (Path fitxer : stream) {
                // comprovar si ha estat modificat l'última setmana
                BasicFileAttributes attributes = Files.readAttributes(fitxer, BasicFileAttributes.class);

                Instant oneWeekAgo = Instant.now().minusSeconds(7 * 24 * 60 * 60);
                FileTime fileModification = attributes.lastModifiedTime();

                if (fileModification.toInstant().isAfter(oneWeekAgo)) {
                    System.out.println(fitxer.getFileName());
                    if (Files.isDirectory(fitxer)) {
                        showModifiedLastWeek(fitxer);
                    }
                }
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
    }

}