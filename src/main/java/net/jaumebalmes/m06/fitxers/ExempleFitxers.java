package net.jaumebalmes.m06.fitxers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;

public class ExempleFitxers {

    public static void main(String[] args) {
        ArrayList<String> output= new ArrayList<String>();
        String content = "";
        if (args.length == 2) {
            Path dir = Paths.get(args[0]);
            String prefix = args[1];
            System.out.println("Fitxers del directori " + dir);
            if (Files.isDirectory(dir)) {
                showStartsWith(dir,prefix,output);
                for (String nomFitxer:output) {
                    content = content + nomFitxer +"\n";
                }
                try {
                    Files.write(Paths.get("resultat.txt"),content.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Ús: java LlistarDirectori <directori>");
            }
        }

    }

    private static void showModiMorethen1mb(Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir);) {
            for (Path fitxer : stream) {
                // comprovar si ha estat modificat l'última setmana
                BasicFileAttributes attributes = Files.readAttributes(fitxer, BasicFileAttributes.class);

                // Instant oneWeekAgo = Instant.now().minusSeconds(7 * 24 * 60 * 60);
                // FileTime fileModification = attributes.lastModifiedTime();
                long size= attributes.size();
                /*
                File fitxerF= new File(String.valueOf(fitxer.getFileName()));
                long lenght = fitxerF.length();
                if (lenght>1000000)
                */

                if (size>1000000) {
                    System.out.println(fitxer.getFileName()+" : " +size);

                }
                if (Files.isDirectory(fitxer)) {
                    showModiMorethen1mb(fitxer);
                }
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
    }

    private static void showStartsWith(Path dir,String prefix, ArrayList<String> output) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir);) {
            for (Path fitxer : stream) {
                if (String.valueOf(fitxer.getFileName()).startsWith(prefix)) {
                    output.add(String.valueOf(fitxer.getFileName()));
                    //System.out.println(fitxer.getFileName());
                }
                if (Files.isDirectory(fitxer)) {
                    showStartsWith(fitxer,prefix,output);
                }
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
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