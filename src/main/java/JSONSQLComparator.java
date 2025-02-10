import org.json.JSONObject;
import java.util.*;

public class JSONSQLComparator {
    public static void main(String[] args) {
        SqlAndJsonHelper jsonSqlHelper = new SqlAndJsonHelper();
        String gridName = "1UTREA";
        String aliasName = "rea_lastsaved";
        String filterValue = "2025-02-06 15:50";

        List<Map<String, String>> jsonRecords = jsonSqlHelper.getJSONRecords(gridName, aliasName, filterValue);
        List<Map<String, String>> sqlRecords = jsonSqlHelper.getSQLRecords(filterValue);

        compareRecords(jsonRecords, sqlRecords);
    }

    private static void compareRecords(List<Map<String, String>> jsonRecords, List<Map<String, String>> sqlRecords) {
        System.out.println("JSON Records:");
        for (Map<String, String> record : jsonRecords) {
            System.out.println(record);
        }

        System.out.println("SQL Records:");
        for (Map<String, String> record : sqlRecords) {
            System.out.println(record);
        }

        Set<Map<String, String>> jsonSet = new HashSet<>(jsonRecords);
        Set<Map<String, String>> sqlSet = new HashSet<>(sqlRecords);

        if (jsonSet.equals(sqlSet)) {
            System.out.println("JSON and SQL records match!");
        } else {
            System.out.println("Discrepancies found:");
            jsonSet.removeAll(sqlSet);
            System.out.println("Records in JSON but not in SQL: " + jsonSet);
            sqlSet.removeAll(new HashSet<>(jsonRecords));
            System.out.println("Records in SQL but not in JSON: " + sqlSet);
        }
    }
}