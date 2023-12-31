package pl.jdacewicz.sharingservice.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.InvalidFileNameException;
import pl.jdacewicz.sharingservice.exception.InvalidPathException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @AfterEach
    void cleanUp() throws IOException {
        File directory = new File("tmp");
        org.apache.commons.io.FileUtils.deleteDirectory(directory);
    }

    @Test
    @DisplayName("Given file, fileDir and null fileName " +
            "When saving file " +
            "Then should throw InvalidPathException")
      void savingFileByFileAndFileDirAndNullFileNameShouldThrowException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = "tmp";
        String fileName = null;

        assertThrows(InvalidPathException.class,
                () -> FileUtils.saveFile(file, fileName, fileDir));
    }

    @Test
    @DisplayName("Given file, fileName and null fileDir " +
            "When saving file " +
            "Then should throw InvalidPathException")
    void savingFileByFileAndNullFileDirAndFileNameShouldThrowException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = null;
        String fileName = "newName.txt";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.saveFile(file, fileName, fileDir));
    }

    @Test
    @DisplayName("Given file, fileDir and empty fileName " +
            "When saving file " +
            "Then should throw InvalidPathException")
    void savingFileByFileAndFileDirAndEmptyFileNameShouldThrowException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = "tmp";
        String fileName = "";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.saveFile(file, fileName, fileDir));
    }

    @Test
    @DisplayName("Given file, fileName and empty fileDir " +
            "When saving file " +
            "Then should throw InvalidPathException")
    void savingFileByFileAndEmptyFileDirAndFileNameShouldThrowException() {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = "";
        String fileName = "newName.txt";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.saveFile(file, fileName, fileDir));
    }

    @Test
    @DisplayName("Given file, fileName and fileDir " +
            "When saving file " +
            "Then should save file")
    void savingFileByFileAndFileDirAndFileNameShouldSaveFile() throws IOException {
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes());
        String fileDir = "tmp";
        String fileName = "newName.txt";

        FileUtils.saveFile(file, fileName, fileDir);

        assertTrue(new File(fileDir + "/" + fileName).isFile());
    }

    @Test
    @DisplayName("Given fileName and null fileDir " +
            "When deleting file " +
            "Then should throw InvalidPathException")
    void deletingFileByFileNameAndNullFileDirShouldThrowException() {
        String fileDir = null;
        String fileName = "newName.txt";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteFile(fileDir, fileName));
    }

    @Test
    @DisplayName("Given fileName and empty fileDir " +
            "When deleting file " +
            "Then should throw InvalidPathException")
    void deletingFileByFileNameAndEmptyFileDirShouldThrowException() {
        String fileDir = "";
        String fileName = "newName.txt";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteFile(fileDir, fileName));
    }

    @Test
    @DisplayName("Given fileDir and null fileName " +
            "When deleting file " +
            "Then should throw InvalidPathException")
    void deletingFileByFileDirAndNullFileNameShouldThrowException() {
        String fileDir = "tmp";
        String fileName = null;

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteFile(fileDir, fileName));
    }

    @Test
    @DisplayName("Given fileDir and empty fileName " +
            "When deleting file " +
            "Then should throw InvalidPathException")
    void deletingFileByFileDirAndEmptyFileNameShouldThrowException() {
        String fileDir = "tmp";
        String fileName = "";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteFile(fileDir, fileName));
    }

    @Test
    @DisplayName("Given fileName and fileDir " +
            "When deleting file " +
            "Then should delete file")
    void deletingFileByFileDirAndFileNameShouldDeleteFile() throws IOException {
        String fileDir = "tmp";
        String fileName = "newName.txt";

        MultipartFile testFile = new MockMultipartFile("testFile", "content".getBytes());
        File directory = new File(fileDir + "/" + fileName);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(testFile.getInputStream(), directory);

        FileUtils.deleteFile(fileDir, fileName);

        assertFalse(new File(fileDir + "/" + fileName).isFile());
    }

    @Test
    @DisplayName("Given null folderDir " +
            "When deleting directory " +
            "Then should throw InvalidPathException")
    void deletingDirectoryByNullFolderDirShouldThrowException() {
        String folderDir = null;

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteDirectory(folderDir));
    }

    @Test
    @DisplayName("Given empty folderDir " +
            "When deleting directory " +
            "Then should throw InvalidPathException")
    void deletingDirectoryByEmptyFolderDirShouldThrowException() {
        String folderDir = "";

        assertThrows(InvalidPathException.class,
                () -> FileUtils.deleteDirectory(folderDir));
    }

    @Test
    @DisplayName("Given folderDir " +
            "When deleting directory " +
            "Then should delete directory")
    void deletingDirectoryByFolderDirShouldDeleteDirectory() throws IOException {
        String folderDir = "tmp";

        Path path = Paths.get("/" + folderDir);
        Files.createDirectories(path);

        FileUtils.deleteDirectory(folderDir);

        assertFalse(new File(folderDir).isDirectory());
    }

    @Test
    @DisplayName("Given null originalFileName " +
            "When generating fileName " +
            "Then should throw InvalidFileNameException")
    void generatingFileNameByNullOriginalNameShouldThrowException() {
        String originalFileName = null;

        assertThrows(InvalidFileNameException.class,
                () -> FileUtils.generateFileName(originalFileName));
    }

    @Test
    @DisplayName("Given empty originalFileName " +
            "When generating fileName " +
            "Then should throw InvalidFileNameException")
    void generatingFileNameByEmptyOriginalNameShouldThrowException() {
        String originalFileName = "";

        assertThrows(InvalidFileNameException.class,
                () -> FileUtils.generateFileName(originalFileName));
    }

    @Test
    @DisplayName("Given originalFileName with extension " +
            "When generating fileName " +
            "Then should return new fileName")
    void generatingFileNameByOriginalNameWithExtensionShouldReturnGeneratedFileName() {
        String originalFileName = "file.jpg";

        String newFileName = FileUtils.generateFileName(originalFileName);

        assertNotEquals(originalFileName, newFileName);
    }

    @Test
    @DisplayName("Given originalFileName without extension " +
            "When generating fileName " +
            "Then should return new fileName")
    void generatingFileNameByOriginalNameWithoutExtensionShouldReturnGeneratedFileName() {
        String originalFileName = "file";

        String newFileName = FileUtils.generateFileName(originalFileName);

        assertNotEquals(originalFileName, newFileName);
    }

    @Test
    @DisplayName("Given originalFileName with only extension " +
            "When generating fileName " +
            "Then should return new fileName")
    void generatingFileNameByOriginalNameWithOnlyExtensionShouldReturnGeneratedFileName() {
        String originalFileName = ".jpg";

        String newFileName = FileUtils.generateFileName(originalFileName);

        assertNotEquals(originalFileName, newFileName);
    }
}