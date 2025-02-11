import java.util.*;
import java.util.stream.Collectors;

public class JSONSQLComparator {
    public static void compareRecords(List<Map<String, String>> jsonRecords, List<Map<String, String>> sqlRecords) {
        System.out.println("JSON Records:");
        for (Map<String, String> record : jsonRecords) {
            System.out.println(record);
        }

        System.out.println("SQL Records:");
        for (Map<String, String> record : sqlRecords) {
            System.out.println(record);
        }

        Map<String, Map<String, String>> jsonMap = new HashMap<>();
        for (Map<String, String> record : jsonRecords) {
            jsonMap.put(record.get("rea_code"), record);
        }

        Map<String, Map<String, String>> sqlMap = new HashMap<>();
        for (Map<String, String> record : sqlRecords) {
            sqlMap.put(record.get("rea_code"), record);
        }

        Set<String> allKeys = new HashSet<>(jsonMap.keySet());
        allKeys.addAll(sqlMap.keySet());

        for (String key : allKeys) {
            Map<String, String> jsonRecord = jsonMap.get(key);
            Map<String, String> sqlRecord = sqlMap.get(key);

            if (jsonRecord == null) {
                System.out.println("Record in SQL but not in JSON: " + sqlRecord);
            } else if (sqlRecord == null) {
                System.out.println("Record in JSON but not in SQL: " + jsonRecord);
            } else {
                if (!compareRecordValues(jsonRecord, sqlRecord)) {
                    System.out.println("Discrepancy found for rea_code " + key + ":");
                    System.out.println("JSON: " + jsonRecord);
                    System.out.println("SQL: " + sqlRecord);
                }
            }
        }
    }

    private static boolean compareRecordValues(Map<String, String> jsonRecord, Map<String, String> sqlRecord) {
        // Filter the JSON record to only include keys that are in the SQL record
        Map<String, String> filteredJsonRecord = jsonRecord.entrySet().stream()
                .filter(entry -> sqlRecord.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (String key : filteredJsonRecord.keySet()) {
            String jsonValue = filteredJsonRecord.get(key);
            String sqlValue = sqlRecord.get(key);

            if (jsonValue == null) {
                jsonValue = "";
            }

            if (sqlValue == null) {
                sqlValue = "";
            }

            // Remove commas from numeric values
            jsonValue = jsonValue.replace(",", "");
            sqlValue = sqlValue.replace(",", "");

            // Remove trailing zeros from floating-point numbers
            jsonValue = jsonValue.replaceAll("\\.0+$", "").replaceAll("(\\.[0-9]*[1-9])0+$", "$1");
            sqlValue = sqlValue.replaceAll("\\.0+$", "").replaceAll("(\\.[0-9]*[1-9])0+$", "$1");

            if (!jsonValue.equals(sqlValue)) {
                return false;
            }
        }
        return true;
    }
}