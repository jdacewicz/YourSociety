package pl.jdacewicz.messagingservice.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static void saveFile(MultipartFile file, String fileName, String fileDir) throws IOException {
        InputStream inputStream = file.getInputStream();
        File directory = new File(fileDir + "/" + fileName);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, directory);
    }

    public static String generateFileName(String originalName) {
        int lastDotIndex = originalName.lastIndexOf('.');
        String extension = originalName.substring(lastDotIndex);

        return RandomStringUtils.randomAlphanumeric(16) + extension;
    }
}
