package org.revo.Util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

import static java.nio.file.Files.isRegularFile;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class FileUtil {

    public static Stream<Path> unzip(Path stored) throws ZipException, IOException {
        ZipFile zipFile = new ZipFile(stored.toFile());
        Path path = stored.getParent().resolve(FilenameUtils.getBaseName(stored.toString()));
        zipFile.extractAll(path.toString());
        return Files.walk(path).filter(it -> isRegularFile(it));
    }

    public static boolean is(Path tempFile, String ext) {
        return asList(ext.split(" ")).stream().anyMatch(it -> it.equalsIgnoreCase(getExtension(tempFile.toString())));
    }

    public static String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Stream<Path> walk(Path stored) throws ZipException, IOException {
        String videoExt = "avi mkv rmvb mp4 flv mov mpeg webm wmv ogg";
        ZipFile zipFile = new ZipFile(stored.toString());
        if (zipFile.isValidZipFile()) {
            return unzip(stored).filter(it -> is(it, videoExt));
        } else if (is(stored, videoExt)) {
            return Stream.of(stored);
        } else return Stream.empty();
    }

    public static int sizeOf(String url) {
        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    public static boolean haveSpaceFor(String url, File file) {
        long i = sizeOf(url) * 3;
        file.mkdir();
        long freeSpace = file.getFreeSpace();
        file.delete();
        return (i != 0 && i < freeSpace);
    }
}
