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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Files.isRegularFile;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class FileUtil {

    public static Path unzip(File tempFile) {
        try {
            ZipFile zipFile = new ZipFile(tempFile);
            Path path = Paths.get(tempFile.getParent(), FilenameUtils.getBaseName(tempFile.toString()));
            zipFile.extractAll(path.toString());
            return path;
        } catch (ZipException e) {
            return null;
        }
    }

    public static boolean is(Path tempFile, String ext) {
        return asList(ext.split(" ")).stream().anyMatch(it -> it.equalsIgnoreCase(getExtension(tempFile.toString())));
    }

    public static String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //    @Override
    public static List<Path> walk(Path stored) {
        List<Path> files = new ArrayList<>();
        String videoExt = "avi mkv rmvb mp4 flv mov mpeg webm wmv ogg";
        if (is(stored, videoExt)) {
            files.add(stored);
        } else {
            String compressedExt = "zip rar";
            if (is(stored, compressedExt)) {
                Path unzip = unzip(stored.toFile());
                if (unzip != null) {
                    try {
                        Files.walk(unzip)
                                .filter(it -> isRegularFile(it))
                                .filter(it -> is(it, videoExt))
                                .forEach(files::add);
                    } catch (IOException e) {
                    }
                }
            }
        }
        return files;
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
