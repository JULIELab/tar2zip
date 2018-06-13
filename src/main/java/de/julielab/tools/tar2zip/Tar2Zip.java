package de.julielab.tools.tar2zip;


import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;

public class Tar2Zip {
    public static void main(String args[]) {
        if (args.length < 1 || args.length > 2) {
            System.err.println("Usage: " + Tar2Zip.class.getSimpleName() + " <tar.gz file> [destination .zip file]");
            System.exit(1);
        }
        String tarPath = args[0];
        String zipPath = args.length == 1 ? stripExtension(tarPath) + ".zip" : args[2];

        convert(tarPath, zipPath);
    }

    private static void convert(String tarPath, String zipPath) {
        Path pathToZip = Paths.get(zipPath);
        URI uri = URI.create("jar:file:" + pathToZip.toAbsolutePath().toString());
        System.out.println(uri);
        try (TarArchiveInputStream tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(tarPath)));
             FileSystem zipFs = FileSystems.newFileSystem(uri, Collections.singletonMap("create", "true"))) {

            TarArchiveEntry currentEntry = tarInput.getNextTarEntry();
            BufferedInputStream bis;
            while (currentEntry != null) {
                bis = new BufferedInputStream(tarInput);
                try (OutputStream os = new BufferedOutputStream(zipFs.provider().newOutputStream(zipFs.getPath(currentEntry.getName()), StandardOpenOption.CREATE, StandardOpenOption.WRITE))) {
                    int allread = 0;
                    int read;
                    byte[] buf = new byte[8192];
                    while ((read = bis.read(buf)) >= 0) {
                        os.write(buf, allread, read);
                        allread += read;
                    }
                }

                currentEntry = tarInput.getNextTarEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String stripExtension(String tarPath) {
        if (tarPath.toLowerCase().endsWith(".tgz"))
            return tarPath.substring(0, tarPath.length() - 4);
        else if (tarPath.toLowerCase().endsWith(".tar.gz"))
            return tarPath.substring(0, tarPath.length() - 7);
        else
            throw new IllegalArgumentException("The file path " + tarPath + " does not have a .tar.gz or .tgz ending.");
    }
}
