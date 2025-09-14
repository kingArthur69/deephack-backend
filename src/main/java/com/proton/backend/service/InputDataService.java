package com.proton.backend.service;

import com.proton.backend.model.InputData;
import com.proton.backend.repository.InputDataRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InputDataService {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    @Value("${data.filepath}")
    private String path;
    private Path uploadDataPath;

    private final InputDataRepository inputDataRepository;
    private final MongoTemplate mongoTemplate;

    @PostConstruct
    void init() throws IOException {
        uploadDataPath = Path.of(path);
        if (!Files.exists(uploadDataPath)) {
            Files.createDirectories(uploadDataPath);
        }
    }

    public Path getUploadDataPath() {
        return uploadDataPath;
    }

    public void save(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename.endsWith("zip")) {
            saveZip(file);
        } else if (filename.endsWith("csv")) {
            saveToDb(file.getInputStream());
        }
    }

    private void saveZip(MultipartFile multipartFile) throws IOException {
        Path tempFile = Files.createTempFile("tmp", multipartFile.getOriginalFilename());
        Files.copy(multipartFile.getInputStream(), tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        try (ZipFile zipFile = ZipFile.builder().setPath(tempFile).get()) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".csv")) {
                    saveToFS(zipFile.getInputStream(entry), name);
                    saveToDb(zipFile.getInputStream(entry));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void saveToDb(InputStream inputStream) throws IOException {
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

    List<InputData> findAllByMeterIdAndDateBetween(Long id, LocalDateTime start, LocalDateTime end) {
        return inputDataRepository.findAllByMeterIdAndDateTimeBetweenOrderByDateTime(id, start, end);
    }

    private void saveToFS(InputStream inputStream, String filename) throws IOException {
        String cleanFileName = StringUtils.substringAfterLast(filename, "/");
        Files.copy(inputStream, resolveFilePath(cleanFileName), StandardCopyOption.REPLACE_EXISTING);
    }

    private Path resolveFilePath(String filename) {
        return uploadDataPath.resolve(filename);
    }

    public void bulkSaveOrUpdate(List<InputData> dataList) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, InputData.class);

        for (InputData data : dataList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("meterId").is(data.getMeterId())
                    .and("dateTime").is(data.getDateTime()));

            Update update = new Update()
                    .set("energyImport", data.getEnergyImport())
                    .set("energyExport", data.getEnergyExport())
                    .set("transFullCoef", data.getTransFullCoef());

            bulkOps.upsert(query, update);
        }

        bulkOps.execute();
    }
}
