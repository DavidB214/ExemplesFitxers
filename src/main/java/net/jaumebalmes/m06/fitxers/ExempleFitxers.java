package net.jaumebalmes.m06.fitxers;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * Este ejemplo de main es un poco mas complejo que el otro porque nos piden mas cosas
 */
public class ExempleFitxers {
    public static void main(String[] args) {
        //ArrayList la usamos para guardar los ficheros/directorios que cumplen lo que queremos printar al final
        ArrayList<String> output= new ArrayList<String>();
        String content = "";
        //Los args son los arguments ( lo que en configuration podemos añadir) primero haz un run y luego saldra Edit configuration
        if (args.length == 2) {
            Path dir = Paths.get(args[0]);
            String prefix = args[1];
            System.out.println("Fitxers del directori " + dir);
            if (Files.isDirectory(dir)) {
                //como antes esto es el metodo al que llamamos
                countFiles(dir,output);
                //Este bucle es para printar todos los resultados obtenidos
                for (String nomFitxer:output) {
                    content = content + nomFitxer +"\n";
                }
                try {
                    Files.write(Paths.get("resultat.txt"),content.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //cuenta los archivos que tiene cada directorio
    private static void countFiles(Path dir, ArrayList<String> output) {
        int i =0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir);) {
            for (Path fitxer : stream) {
                i++;
                if (Files.isDirectory(fitxer)) {
                    countFiles(fitxer,output);
                }
            }
            output.add(dir.getFileName()+" "+i);
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
    }

    //muestra los que empiezan por...
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

}