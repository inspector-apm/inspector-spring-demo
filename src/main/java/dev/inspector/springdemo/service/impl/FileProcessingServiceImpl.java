package dev.inspector.springdemo.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.inspector.springdemo.service.FileProcessingService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class FileProcessingServiceImpl implements FileProcessingService {
    private static final int COMPRESSION_LEVEL = ZipOutputStream.DEFLATED;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Path TEMP_DIR;

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            TEMP_DIR = Files.createTempDirectory("tempFiles");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void cleanUpTempDir() {
        try {
            Files.walk(TEMP_DIR)
                    .map(Path::toFile)
                    .forEach(File::delete);
            Files.deleteIfExists(TEMP_DIR);
        } catch (IOException e) {
            log.error("Failed to clean up temporary files", e);
        }
    }

    @Override
    public File convertToJsonFile(Object object, String fileName) {
        File jsonFile = null;
        try {
            jsonFile = new File(TEMP_DIR.toFile(), fileName + ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, object);
        } catch (Exception e) {
            log.error("convertToJsonFile error", e);
            throw new IllegalArgumentException();
        }
        return jsonFile;
    }

    @Override
    public File convertToJsonFileAndZip(Object object, String fileName) {
        return archivingToZip(convertToJsonFile(object, fileName), fileName);
    }

    @Override
    public File unarchiving(MultipartFile archive) {
        try {
            return unarchive(archive.getInputStream());
        } catch (IOException e) {
            log.error("Error reading MultipartFile", e);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public File unarchiving(File archive) {
        try {
            return unarchive(new FileInputStream(archive));
        } catch (IOException e) {
            log.error("Error reading File", e);
            throw new IllegalArgumentException();
        }
    }

    private File unarchive(InputStream inputStream) {
        File extractedFile = null;
        try (ZipInputStream zipIn = new ZipInputStream(inputStream)) {
            ZipEntry entry = zipIn.getNextEntry();
            if (entry != null) {
                String fileName = entry.getName();
                extractedFile = TEMP_DIR.resolve(fileName).toFile();
                try (OutputStream out = Files.newOutputStream(extractedFile.toPath())) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipIn.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
            }
        } catch (Exception e) {
            log.error("unarchiving error", e);
            throw new IllegalArgumentException();
        }
        return extractedFile;
    }

    @Override
    public List<File> unarchivingAllFile(MultipartFile archive) {
        List<File> extractedFiles = new ArrayList<>();
        try (ZipInputStream zipIn = new ZipInputStream(archive.getInputStream())) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String fileName = entry.getName();
                    File extractedFile = TEMP_DIR.resolve(fileName).toFile();
                    try (OutputStream out = Files.newOutputStream(extractedFile.toPath())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipIn.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                    }
                    extractedFiles.add(extractedFile);
                }
                zipIn.closeEntry();
            }
        } catch (Exception e) {
            log.error("unarchiving error", e);
            throw new IllegalArgumentException();
        }
        return extractedFiles;
    }

    @Override
    public File archivingToZip(File raw, String fileName) {
        File zipFile = null;
        try {
            zipFile = new File(TEMP_DIR.toFile(), fileName + ".zip");
            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                zos.setLevel(COMPRESSION_LEVEL);

                ZipEntry zipEntry = new ZipEntry(raw.getName());
                zos.putNextEntry(zipEntry);

                byte[] fileBytes = Files.readAllBytes(raw.toPath());
                zos.write(fileBytes);

                zos.closeEntry();
            }
        } catch (Exception e) {
            log.error("archivingToZip error", e);
            throw new IllegalArgumentException();
        }
        return zipFile;
    }

    @Override
    public File base64ToFile(String base64, String fileName) {
        var file = new File(TEMP_DIR.toFile(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(Base64.getDecoder().decode(base64));
        } catch (Exception e) {
            log.error("base64ToFile error", e);
            throw new IllegalArgumentException();
        }
        return file;
    }

    @Override
    public File inputStreamToFile(InputStream inputStream, String fileName) {
        var file = new File(TEMP_DIR.toFile(), fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("base64ToFile error", e);
            throw new IllegalArgumentException();
        }
        return file;
    }

    @Override
    public <T> T unzipAndConvertJsonToObject(File archive, Class<T> valueType) {
        return convertJsonToObject(unarchiving(archive), valueType);
    }

    @Override
    public <T> T unzipAndConvertJsonToObject(MultipartFile archive, Class<T> valueType) {
        return convertJsonToObject(unarchiving(archive), valueType);
    }

    @Override
    public File unZip(File archive) {
        File extractedFile = null;
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(archive))) {
            ZipEntry entry = zipIn.getNextEntry();
            if (entry != null) {
                String fileName = entry.getName();
                extractedFile = TEMP_DIR.resolve(fileName).toFile();
                try (OutputStream out = Files.newOutputStream(extractedFile.toPath())) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipIn.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
            }
        } catch (Exception e) {
            log.error("unarchiving error", e);
            throw new IllegalArgumentException();
        }
        return extractedFile;
    }

    @Override
    public <T> T convertJsonToObject(File file, Class<T> valueType) {
        try {
            return objectMapper.readValue(file, valueType);
        } catch (IOException e) {
            log.error("Error converting json to dto", e);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String fileToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            log.error("fileToBase64 error", e);
            throw new IllegalArgumentException();
        }
    }
}
