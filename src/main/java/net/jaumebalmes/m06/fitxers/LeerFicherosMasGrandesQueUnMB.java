package net.jaumebalmes.m06.fitxers;


import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Ejemplo de como leer ficheros con una condicion de tamaño
 * Tambien de los modificados la ultima semana
 */
public class LeerFicherosMasGrandesQueUnMB {
    public static void main(String[] args) {
        if (args.length == 1) {
            Path dir = Paths.get(args[0]);
            System.out.println("Fitxers del directori " + dir);
            if (Files.isDirectory(dir)) {
                //Para hacer algun ejercicio de este estilo solo tenemos que cambiar la funcion de aqui
                //Mirar tambien el otro ejemplo del main por si nos piden otra cosa mas compleja
                showMoreThan1MB(dir);
            } else {
                System.err.println("Ús: java LlistarDirectori <directori>");
            }
        }

    }
    //Muestra los ficheros de 1 MB o mas si pide otro tamaño solo cambia el numero 1000000
    private static void showMoreThan1MB(Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir);) {
            for (Path fitxer : stream) {
                BasicFileAttributes attributes = Files.readAttributes(fitxer, BasicFileAttributes.class);
                long size = attributes.size();
                System.out.println(size);
                if (size>1000000) {
                    System.out.println(fitxer.getFileName());
                    if (Files.isDirectory(fitxer)) {
                        showMoreThan1MB(fitxer);
                    }
                }
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
    }
    //Printa los ficheros que se han modificado la ultima semana
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
