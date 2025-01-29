package PerofrmanceTest;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvParcer {
    static String line = null;
    static BufferedReader bufferedReader = null;

    public String[] getAttributesNames(String csvFilePath) throws IOException {
        File csvFile = new File(csvFilePath);
        FileReader fileReader = new FileReader(csvFile, StandardCharsets.UTF_8);
        bufferedReader = new BufferedReader(fileReader);
        line = bufferedReader.readLine();
        String[] columnsNames = line.split(",");
        bufferedReader.close();
        return columnsNames;
    }

    public static Iterable<CSVRecord> getCsvRecordsWithHeaders(String importCSVfilePath) throws IOException {
        Reader in = new FileReader(importCSVfilePath);
        return CSVFormat.RFC4180.withHeader().parse(in);
    }

    public HashMap<String, String> getSameNameAttributes(String importCSVFilePath, String exportCSVFilePath) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        HashSet<String> vendorImportAttributes = new HashSet<>(Arrays.asList(getAttributesNames(importCSVFilePath)));
        HashSet<String> vendorEmportAttributes = new HashSet<>(Arrays.asList(getAttributesNames(exportCSVFilePath)));
        for (String attributeName : vendorImportAttributes) {
            if (vendorEmportAttributes.contains(attributeName)) {
                map.put(attributeName, attributeName);
            }
        }
        System.out.println("Number of the same name columns: " + map.size());
        return map;
    }

    public HashMap<String, HashSet<String>> mapOfTableAndNotNullColumnsInGrid = new HashMap<>();

    public HashMap<String, HashSet<String>> getMapOfTableAndNotNullColumnsInGrid(String csvFilePath) throws IOException {
        String tableNameAttribute = "\uFEFFTable Name";
        String columnNameAttribute = "Column Name";
        Iterable<CSVRecord> importRecords = getCsvRecordsWithHeaders(csvFilePath);
        HashMap<String, HashSet<String>> mapOfTableAndNotNullColumnsInGrid = new HashMap<>();

        for (CSVRecord record : importRecords) {
            String currentTableName = record.get(tableNameAttribute);
            String currentColumnName = record.get(columnNameAttribute);
            String isNullColumn = record.get("Is Null Column");

            // Check if the column is not null
            if (!"TRUE".equals(isNullColumn)) {
                // Get the existing HashSet for the table, or create a new one if it doesn't exist
                HashSet<String> columns = mapOfTableAndNotNullColumnsInGrid.get(currentTableName);
                if (columns == null) {
                    columns = new HashSet<>();
                    mapOfTableAndNotNullColumnsInGrid.put(currentTableName, columns);
                }

                // Add the current column to the HashSet
                columns.add(currentColumnName);
            }
        }

        return mapOfTableAndNotNullColumnsInGrid;
    }

    public HashMap<String, String> getSameNameAttributes_withCombinedKey(String importCSVFilePath, String exportCSVFilePath, ArrayList<String> keyCombination) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        HashSet<String> vendorImportAttributes = new HashSet<>(Arrays.asList(getAttributesNames(importCSVFilePath)));
        HashSet<String> vendorEmportAttributes = new HashSet<>(Arrays.asList(getAttributesNames(exportCSVFilePath)));
        for (String attributeName : vendorImportAttributes) {
            if (vendorEmportAttributes.contains(attributeName)) {
                if(!keyCombination.contains(attributeName)) {
                    map.put(attributeName, attributeName);
                }
            }
        }
        System.out.println("Same name columns: " + map.entrySet().stream().toString());
        return map;
    }

    public HashMap<String, CSVRecord> getRecordsMap(String importCSVfilePath, String attribute) throws IOException {
        Iterable<CSVRecord> importRecords = getCsvRecordsWithHeaders(importCSVfilePath);
        HashMap<String, CSVRecord> importVendorIDrecordsMAP = new HashMap();
        for (CSVRecord record : importRecords) {
            importVendorIDrecordsMAP.put(record.get(attribute), record);
        }
        return importVendorIDrecordsMAP;
    }

    public HashMap<String, CSVRecord> getRecordsMapForCombinedKey(String importCSVfilePath, ArrayList<String> combinedKey) throws IOException {
        Iterable<CSVRecord> importRecords = getCsvRecordsWithHeaders(importCSVfilePath);
        HashMap<String, CSVRecord> importVendorIDrecordsMAP = new HashMap();
        for (CSVRecord record : importRecords) {
            String key= "";
            for(String field: combinedKey) {
                key += record.get(field) + "-";
            }
            importVendorIDrecordsMAP.put(key, record);
        }
        return importVendorIDrecordsMAP;
    }

    public HashMap<String, String> getAttributeValueMap(String importCSVfilePath, String keyAttribute, String valueAttrubite) throws IOException {
        Iterable<CSVRecord> importRecords = CsvParcer.getCsvRecordsWithHeaders(importCSVfilePath);
        HashMap<String, String> importVendorIDrecordsMAP = new HashMap();
        for (CSVRecord record : importRecords) {
            importVendorIDrecordsMAP.put(record.get(keyAttribute), record.get(valueAttrubite));
        }
        return importVendorIDrecordsMAP;
    }

    public HashMap<String, String> getAttributeValueMapFrom2Files(String keyFile, String valueFile, String keyAttribute) throws IOException {
        Iterable<CSVRecord> keyRecords = CsvParcer.getCsvRecordsWithHeaders(keyFile);
        Iterable<CSVRecord> valueRecords = CsvParcer.getCsvRecordsWithHeaders(valueFile);
        Iterator<CSVRecord> valueIterator = valueRecords.iterator();
        HashMap<String, String> importVendorIDrecordsMAP = new HashMap();
        for (CSVRecord record : keyRecords) {
            String value = valueIterator.next().get(keyAttribute);
            importVendorIDrecordsMAP.put(record.get(keyAttribute), value);
        }
        return importVendorIDrecordsMAP;
    }

    public HashSet<String> getRecordsSet(String importCSVfilePath, String attribute) throws IOException {
        Iterable<CSVRecord> importRecords = getCsvRecordsWithHeaders(importCSVfilePath);
        HashSet attrubutesSetfromCSV = new HashSet();
        for (CSVRecord record : importRecords) {
            attrubutesSetfromCSV.add(record.get(attribute));
        }
        return attrubutesSetfromCSV;
    }

    public HashSet<String> getValuesForAttribute(String importCSVfilePath, String attribute) throws IOException {
        Iterable<CSVRecord> importRecords = getCsvRecordsWithHeaders(importCSVfilePath);
        HashSet<String> atrributeValues = new HashSet<>();
        for (CSVRecord record : importRecords) {
            atrributeValues.add(record.get(attribute));
        }
        return atrributeValues;
    }

    public boolean containsVendor(String CSVfilePath, String importId, String exportId, CSVRecord record) throws IOException {
        HashMap<String, CSVRecord> importVendorIDrecordsMap = getRecordsMap(CSVfilePath, importId);
        if (!importVendorIDrecordsMap.containsKey(record.get(exportId))) {
            System.out.println("VendorID :" + record.get(exportId) + "is extra!");
            return false;
        }
        return true;
    }

    public HashSet<String> getNotMatchingRecords(String importCSVfilePath, String exportCSVfilePath, String importId, String exportId) throws IOException {
        HashSet<String> notMatchingRecords = new HashSet<>();
        Iterable<CSVRecord> exportRecords = getCsvRecordsWithHeaders(exportCSVfilePath);
        for (CSVRecord record : exportRecords) {
            if (!containsVendor(importCSVfilePath, record.get(importId), record.get(exportId), record)) {
                notMatchingRecords.add(record.get(exportId));
            }
        }
        return notMatchingRecords;
    }



    public int getSameNameAttributesCount(String importCSVFilePath, String exportCSVFilePath) throws IOException {
        return getSameNameAttributes(importCSVFilePath, exportCSVFilePath).size();
    }

    public static Iterable<CSVRecord> getCSVrecordsIterator(String csvFilePath) throws IOException {
        return getCsvRecordsWithHeaders(csvFilePath);
    }

}
