# TAR2ZIP

This is a small Java program for converting a .tar.gz file into a ZIP archive with the exact same contents.
ZIP offers efficient random access to its internal entries where a .tar.gz contains one large TAR stream in GZIP format.
Thus it seems not to be possible to extract, update or delete single entries from a .tar.gz archive without extracting the tar file at least to the entry that should be changed.
ZIP, on the other hand, can be handled like a file system on its own.

If you know a way to extract, update or delete single entries directly within .tar.gz files that does not
include uncompressing (large prts of) the TAR archive first, please let us know. But be aware that solutions on the
web or those offered by command line tools do indeed uncompress the complete TAR before returning the desired entry,
even though this fact is not explicitly stated and quite opaque from the outside. Our tests on large archives (~5GB)
showed that the extraction of single entries from .tar.gz files takes very long, probably because the complete
TAR archive has to be uncompressed first, where the same operation on a ZIP file with equal contents is almost
instantaneous.

# Building the tool

This is a Maven project. Clone or download the source and execute `mvn clean package`. There will be a file with the ending `jar-with-dependencies.jar` which is a self-executable JAR file.
Alternatively, you can look for existing packages in Maven Central where we upload it to. The respective Maven coordinates can be taken from the pom.xml of this repository.

# Usage

Type `java -jar tar2zip-<version>-jar-with-dependencies` to see the possible command line arguments.
The first argument is the `.tar.gz` file that should be converted. The second argument is optional and specifies the output file path. If omitted, the output path will be identical with the original `.tar.gz` file except that the ending is changed to `.zip`.
