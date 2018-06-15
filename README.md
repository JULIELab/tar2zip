# TAR2ZIP

This is a small Java program for converting a .tar.gz file into a ZIP archive with the exact same contents.
ZIP offers efficient random access to its internal entries where a .tar.gz contains one large TAR stream in GZIP format.
Thus it seems not to be possible to extract, update or delete single entries from a .tar.gz archive.
ZIP, on the other hand, can be handled like a file system on its own (this is actually done in this tool).

If you know a way to extract, update or delete single entries directly within .tar.gz files that does not
include uncompressing the complete TAR archive first, please let us know. But be aware that solutions on the
web or those offered by command line tools do indeed uncompress the complete TAR before returning the desired entry,
even though this fact is not explicitly stated and quite opaque from the outside. Our tests on large archives (~5GB)
showed that the extraction of single entries from .tar.gz files takes very long, probably because the complete
TAR archive has to be uncompressed first, where the same operation on a ZIP file with equal contents is almost
instantaneous.