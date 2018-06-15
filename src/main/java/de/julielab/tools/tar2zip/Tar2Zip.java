package de.julielab.tools.tar2zip;


import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Reads a .tar.gz file, iterates through its entries and writes them into a ZipFileSystem, i.e. a ZipFile.
 */
public class Tar2Zip {
    public static void main(String args[]) {
        if (args.length < 1 || args.length > 2) {
            System.err.println("Usage: " + Tar2Zip.class.getSimpleName() + " <tar.gz file> [destination .zip file]");
            System.exit(1);
        }
        String tarPath = args[0];
        String zipPath = args.length == 1 ? stripExtension(tarPath) + ".zip" : args[1];

        convert(tarPath, zipPath);
    }

    private static void convert(String tarPath, String zipPath) {
        long time = System.currentTimeMillis();
        Path pathToZip = Paths.get(zipPath);
        try (TarArchiveInputStream tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(tarPath)));
             ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {

            TarArchiveEntry currentEntry = tarInput.getNextTarEntry();
            BufferedInputStream bis;
            while (currentEntry != null) {
                zos.putNextEntry(new ZipEntry(currentEntry.getName()));
                bis = new BufferedInputStream(tarInput);
                int read;
                byte[] buf = new byte[8192];
                while ((read = bis.read(buf)) >= 0) {
                    zos.write(buf, 0, read);
                }
                currentEntry = tarInput.getNextTarEntry();
                zos.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Conversion took " + (time / 1000) + " seconds.");
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
