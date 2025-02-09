package dev.inspector.springdemo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface FileProcessingService {
    File convertToJsonFile(Object object, String fileName);
    File convertToJsonFileAndZip(Object object, String fileName);
    File unarchiving(MultipartFile archive);
    List<File> unarchivingAllFile(MultipartFile archive);
    File unarchiving(File archive);
    File archivingToZip(File raw, String fileName);
    File base64ToFile(String base64, String fileName);
    File inputStreamToFile(InputStream inputStream, String fileName);
    <T> T unzipAndConvertJsonToObject(File archive, Class<T> valueType);
    <T> T unzipAndConvertJsonToObject(MultipartFile archive, Class<T> valueType);
    File unZip(File archive);
    <T> T convertJsonToObject(File file, Class<T> valueType);
    String fileToBase64(File file);
}
