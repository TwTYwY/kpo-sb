package hse.bank.importers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class DataImporter {
    public final void importData(String filePath) {
        System.out.println("Starting import from: " + filePath);
        String fileContent = readFile(filePath);
        List<Object> parsedData = parseData(fileContent);
        validateData(parsedData);
        saveData(parsedData);
        postProcess();
    }

    protected String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }

    protected abstract List<Object> parseData(String fileContent);

    protected void validateData(List<Object> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Imported data is empty");
        }
        System.out.println("Data validation passed: " + data.size() + " items");
    }

    protected void saveData(List<Object> data) {
        System.out.println("Saving " + data.size() + " imported items to storage");
    }

    protected void postProcess() {
        System.out.println("Import process completed successfully");
    }
}