package com.proton.backend.service;

import com.proton.backend.model.InputData;
import com.proton.backend.repository.InputDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Service
public class InputDataService {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final Path uploadDataPath;
    private final InputDataRepository inputDataRepository;

    public InputDataService(@Value("${data.filepath}") String path, InputDataRepository inputDataRepository) throws IOException {
        uploadDataPath = Path.of(path);
        if (!Files.exists(uploadDataPath)) {
            Files.createDirectories(uploadDataPath);
        }
        this.inputDataRepository = inputDataRepository;
    }

    public void save(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename.endsWith("zip")) {
            saveZip(file);
        } else if (filename.endsWith("csv")) {
            save(file.getInputStream());
        }
    }

    private void saveZip(MultipartFile multipartFile) throws IOException {
        Path tempFile = Files.createTempFile("tmp", multipartFile.getOriginalFilename());
        Files.copy(multipartFile.getInputStream(), tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        try (ZipFile zipFile = ZipFile.builder().setPath(tempFile).get()) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".csv")) {
                    save(zipFile.getInputStream(entry));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void save(InputStream inputStream) throws IOException {
        List<InputData> inputData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                    inputData.add(createInputData(line));
                } catch (Exception e) {
                    log.error("Error parsing line: " + line, e);
                }
            }
        }
        inputDataRepository.saveAll(inputData);
    }

    private InputData createInputData(String line) {
        String[] values = line.split(";");
        long meterId = Long.parseLong(values[0].trim());
        LocalDateTime dateTime = LocalDateTime.parse(values[1], FORMATTER);
        long enImport = Long.parseLong(values[2].trim());
        long enExport = Long.parseLong(values[3].trim());
        double coef = Double.parseDouble(values[4].trim());
        return new InputData(meterId, dateTime, enImport, enExport, coef);
    }

    private List<InputData> getInputDataByMeterId(Long id) {
        return null;
    }

//    private void saveCsv(MultipartFile file) throws IOException {
//        Files.copy(file.getInputStream(), resolveFilePath(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//    }

    private Path resolveFilePath(String filename) {
        return uploadDataPath.resolve(filename);
    }
}
