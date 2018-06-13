package de.julielab.tools.tar2zip;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import static org.assertj.core.api.Assertions.*;
public class Tar2ZipTest {

    @BeforeClass
    @AfterClass
    public static void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get("src/test/resources/archive.zip"));
    }

    @Test
    public void testConversion() throws IOException {
        Tar2Zip.main(new String[]{"src/test/resources/archive.tgz"});

        try (ZipFile zf = new ZipFile("src/test/resources/archive.zip")) {
            assertThat(zf.stream().map(ZipEntry::getName)).contains("testfile.txt", "testdir/", "testdir/lowertestfile.txt");
        }
    }
}
