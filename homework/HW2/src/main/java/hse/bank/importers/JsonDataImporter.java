package hse.bank.importers;

import java.util.ArrayList;
import java.util.List;

public class JsonDataImporter extends DataImporter {
    @Override
    protected List<Object> parseData(String fileContent) {
        System.out.println("Parsing JSON data...");
        System.out.println("File content length: " + fileContent.length() + " characters");

        List<Object> parsedData = new ArrayList<>();
        parsedData.add("Parsed JSON object 1");
        parsedData.add("Parsed JSON object 2");

        System.out.println("Successfully parsed " + parsedData.size() + " JSON objects");
        return parsedData;
    }

    @Override
    protected void postProcess() {
        super.postProcess();
        System.out.println("Additional JSON-specific post-processing completed");
    }
}