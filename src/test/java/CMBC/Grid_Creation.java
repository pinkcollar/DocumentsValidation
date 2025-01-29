package CMBC;

import CMBC_code.CsvParcer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static java.time.Duration.ofSeconds;

//document.querySelector("#uxpicktriggerfield-1160-inputEl")
//
public class Grid_Creation {

     WebDriver driver = new EdgeDriver();
    CsvParcer csvParcer = new CsvParcer();

    String tablesFilePAth = "src\\main\\resources\\CMBC\\CMBC_PRDEAM_Reports_Tables_Analysis.csv";
    String primaryKeys = "src\\main\\resources\\CMBC\\EAM_12.1_PrimaryKeys_revised.csv";

    String loginPage = "https://ca1.eam.hxgnsmartcloud.com/web/base/logindisp?tenant=BVBWV1707339191_DEV";

    public Grid_Creation() throws IOException {
    }

    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.edge.driver", "src\\main\\resources\\msedgedriver.exe");
    }

    @BeforeMethod
    public void setUp() {
        // Initialize EdgeDriver
        driver = new EdgeDriver();
    }

    WebDriverWait wait = new WebDriverWait(driver, ofSeconds(10));
    Map<String, HashSet<String>> tableWithNotNullColumns = csvParcer.getMapOfTableAndNotNullColumnsInGrid(tablesFilePAth);
    @Test
    public void gridcreation() throws InterruptedException {
        driver.get(loginPage);

        WebDriverWait wait = new WebDriverWait(driver, ofSeconds(40));
        Thread.sleep(20000);
        WebElement startMenu = wait.until(ExpectedConditions.elementToBeClickable(By.id("appheadericon-1041-btnEl")));
        startMenu.click();
        Thread.sleep(2000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#treeview-1056-record-11 > tbody > tr > td > div > span")));
        WebElement AdministrationMenu = driver.findElement(By.cssSelector("#treeview-1056-record-11 > tbody > tr > td > div > span"));
        AdministrationMenu.click();
        WebElement setupMenu = driver.findElement(By.cssSelector("#treeview-1056-record-838 > tbody > tr > td > div"));
        setupMenu.click();

        WebElement gridDesignerMenu = driver.findElement(By.cssSelector("#treeview-1056-record-864 > tbody > tr > td > div"));
        gridDesignerMenu.click();
        Thread.sleep(2000);
        for (Map.Entry<String, HashSet<String>> entry : tableWithNotNullColumns.entrySet()) {
            String tableName = entry.getKey();
            HashSet<String> notNullColumns = entry.getValue();

            System.out.println("Table: " + tableName);
            System.out.println("Not Null Columns:");

            // Iterate over each not null column for the current table
            for (String column : notNullColumns) {
                System.out.println(column);
            }
            String gridName = "1UT";

            if (!notNullColumns.isEmpty()) {
                String[] parts = notNullColumns.toArray()[0].toString().split("_");
                if (parts.length > 0) {
                    gridName += parts[0];
                } else {
                    // Handle the case where the split result has no parts
                    // For example, if notNullColumns.get(0) returns an empty string
                }
            } else {
                // Handle the case where notNullColumns is empty
            }
            System.out.println(gridName);
            if(ifGridExists(gridName)) {
                if(gridName.equals("1UTOBJ")){
                   // WebElement GridColumns = driver.findElement(By.id("uxtextareatrigger-1221-inputEl"));
                    //String listOfColumns = "evt_acd evt_acd,EVT_ACTION evt_action,EVT_BILLABLE evt_billable,evt_buildmaintprogram evt_buildmaintprogram,evt_burnpermit evt_burnpermit,EVT_CALSTATUS evt_calstatus,EVT_CAMPAIGN evt_campaign,EVT_CAMPAIGN_LINE evt_campaign_line,EVT_CAMPAIGN_ORG evt_campaign_org,EVT_CAMPAIGN_SURVEY evt_campaign_survey,EVT_CAUSE evt_cause,evt_ceilpermit evt_ceilpermit,EVT_CLASS evt_class,EVT_CLASS_ORG evt_class_org,EVT_CODE evt_code,EVT_COMPLETED evt_completed,evt_confinedspace evt_confinedspace,EVT_CONFLICT evt_conflict,EVT_CONFLICTRESOLVED evt_conflictresolved,evt_costcode evt_costcode,EVT_CREATEDBY evt_createdby,EVT_DATE evt_date,evt_depend evt_depend,evt_desc evt_desc,EVT_DOWNTIME evt_downtime,EVT_DOWNTIMEHRS evt_downtimehrs,EVT_DUE evt_due,EVT_DURATION evt_duration,EVT_ENTEREDBY evt_enteredby,EVT_FACILITY evt_facility,EVT_FACILITY_ORG evt_facility_org,EVT_FAILURE evt_failure,EVT_FAILUREUSAGE evt_failureusage,evt_fireincident evt_fireincident,EVT_FIXED evt_fixed,EVT_FOLLOWUP evt_followup,EVT_FREQ evt_freq,EVT_GENWINBEGVAL evt_genwinbegval,EVT_GENWINSTART evt_genwinstart,evt_hazmatincident evt_hazmatincident,evt_hipaaconfidentiality evt_hipaaconfidentiality,evt_infectcontrol evt_infectcontrol,EVT_ISSTYPE evt_isstype,EVT_JOBTYPE evt_jobtype,EVT_LASTCAL evt_lastcal,EVT_LASTSAVED evt_lastsaved,evt_laststatusupdate evt_laststatusupdate,evt_lifesafety evt_lifesafety,EVT_LOCATION evt_location,EVT_LOCATION_ORG evt_location_org,evt_lockout evt_lockout,EVT_LTYPE evt_ltype,EVT_MASTERCAL evt_mastercal,EVT_MAXCOST evt_maxcost,evt_medicequipincident evt_medicequipincident,EVT_METERDUE evt_meterdue,evt_meterinterval evt_meterinterval,EVT_METUOM evt_metuom,evt_minor evt_minor,EVT_MPPROJ evt_mpproj,EVT_MRC evt_mrc,evt_multiequip evt_multiequip,EVT_NEARWINBEGVAL evt_nearwinbegval,EVT_NEARWINSTART evt_nearwinstart,EVT_OBJCRITICALITY evt_objcriticality,EVT_OBJECT evt_object,EVT_OBJECT_ORG evt_object_org,evt_obrtype evt_obrtype,evt_obtype evt_obtype,EVT_OKWINEND evt_okwinend,EVT_OKWINENDVAL evt_okwinendval,EVT_ORG evt_org,EVT_ORIGIN evt_origin,EVT_OUTPUTCALCTYPE evt_outputcalctype,EVT_PARENT evt_parent,evt_patientsafety evt_patientsafety,evt_pcra evt_pcra,EVT_PERIODUOM evt_perioduom,evt_person evt_person,evt_personalprotectiveequip evt_personalprotectiveequip,evt_pfi evt_pfi,EVT_PPM evt_ppm,EVT_PPMREV evt_ppmrev,EVT_PPOPK evt_ppopk,evt_preservecalcpriority evt_preservecalcpriority,evt_print evt_print,evt_printed evt_printed,EVT_PRIORITY evt_priority,EVT_PROJBUD evt_projbud,EVT_PROJECT evt_project,evt_propertydamage evt_propertydamage,evt_recallnotice evt_recallnotice,evt_rejectreason evt_rejectreason,EVT_REOPENED evt_reopened,EVT_REPORTED evt_reported,EVT_REQM evt_reqm,EVT_REQUESTEND evt_requestend,EVT_REQUESTSTART evt_requeststart,EVT_ROUTE evt_route,evt_routeparent evt_routeparent,EVT_ROUTEREV evt_routerev,EVT_ROUTERSTATUS evt_routerstatus,EVT_ROUTESTATUS evt_routestatus,EVT_RSTATUS evt_rstatus,EVT_RTYPE evt_rtype,EVT_SAFETY evt_safety,EVT_SCHEDEND evt_schedend,EVT_SCHEDGRP evt_schedgrp,evt_securityincident evt_securityincident,EVT_SHIFT evt_shift,evt_smda evt_smda,evt_staffinjury evt_staffinjury,EVT_STANDWORK evt_standwork,EVT_START evt_start,evt_statementofcond evt_statementofcond,EVT_STATUS evt_status,EVT_TARGET evt_target,evt_tfdatecompleted evt_tfdatecompleted,evt_type evt_type,evt_udfchar01 evt_udfchar01,evt_udfchar02 evt_udfchar02,evt_udfchar03 evt_udfchar03,evt_udfchar04 evt_udfchar04,evt_udfchar05 evt_udfchar05,evt_udfchar06 evt_udfchar06,evt_udfchar07 evt_udfchar07,evt_udfchar08 evt_udfchar08,evt_udfchar09 evt_udfchar09,evt_udfchar10 evt_udfchar10,evt_udfchar11 evt_udfchar11,evt_udfchar12 evt_udfchar12,evt_udfchar13 evt_udfchar13,evt_udfchar14 evt_udfchar14,evt_udfchar15 evt_udfchar15,evt_udfchar16 evt_udfchar16,evt_udfchar17 evt_udfchar17,evt_udfchar18 evt_udfchar18,evt_udfchar19 evt_udfchar19,evt_udfchar20 evt_udfchar20";
                    //String listOfColumns = "evt_acd evt_acd,EVT_ACTION evt_action,EVT_BILLABLE evt_billable,evt_buildmaintprogram evt_buildmaintprogram,evt_burnpermit evt_burnpermit,EVT_CALSTATUS evt_calstatus,EVT_CAMPAIGN evt_campaign,EVT_CAMPAIGN_LINE evt_campaign_line,EVT_CAMPAIGN_ORG evt_campaign_org,EVT_CAMPAIGN_SURVEY evt_campaign_survey,EVT_CAUSE evt_cause,evt_ceilpermit evt_ceilpermit,EVT_CLASS evt_class,EVT_CLASS_ORG evt_class_org,EVT_CODE evt_code,EVT_COMPLETED evt_completed,evt_confinedspace evt_confinedspace,EVT_CONFLICT evt_conflict,EVT_CONFLICTRESOLVED evt_conflictresolved,evt_costcode evt_costcode,EVT_CREATEDBY evt_createdby,EVT_DATE evt_date,evt_depend evt_depend,evt_desc evt_desc,EVT_DOWNTIME evt_downtime,EVT_DOWNTIMEHRS evt_downtimehrs,EVT_DUE evt_due,EVT_DURATION evt_duration,EVT_ENTEREDBY evt_enteredby,EVT_FACILITY evt_facility,EVT_FACILITY_ORG evt_facility_org,EVT_FAILURE evt_failure,EVT_FAILUREUSAGE evt_failureusage,evt_fireincident evt_fireincident,EVT_FIXED evt_fixed,EVT_FOLLOWUP evt_followup,EVT_FREQ evt_freq,EVT_GENWINBEGVAL evt_genwinbegval,EVT_GENWINSTART evt_genwinstart,evt_hazmatincident evt_hazmatincident,evt_hipaaconfidentiality evt_hipaaconfidentiality,evt_infectcontrol evt_infectcontrol,EVT_ISSTYPE evt_isstype,EVT_JOBTYPE evt_jobtype,EVT_LASTCAL evt_lastcal,EVT_LASTSAVED evt_lastsaved,evt_laststatusupdate evt_laststatusupdate,evt_lifesafety evt_lifesafety,EVT_LOCATION evt_location,EVT_LOCATION_ORG evt_location_org,evt_lockout evt_lockout,EVT_LTYPE evt_ltype,EVT_MASTERCAL evt_mastercal,EVT_MAXCOST evt_maxcost,evt_medicequipincident evt_medicequipincident,EVT_METERDUE evt_meterdue,evt_meterinterval evt_meterinterval,EVT_METUOM evt_metuom,evt_minor evt_minor,EVT_MPPROJ evt_mpproj,EVT_MRC evt_mrc,evt_multiequip evt_multiequip,EVT_NEARWINBEGVAL evt_nearwinbegval,EVT_NEARWINSTART evt_nearwinstart,EVT_OBJCRITICALITY evt_objcriticality,EVT_OBJECT evt_object,EVT_OBJECT_ORG evt_object_org,evt_obrtype evt_obrtype,evt_obtype evt_obtype,EVT_OKWINEND evt_okwinend,EVT_OKWINENDVAL evt_okwinendval,EVT_ORG evt_org,EVT_ORIGIN evt_origin,EVT_OUTPUTCALCTYPE evt_outputcalctype,EVT_PARENT evt_parent,evt_patientsafety evt_patientsafety,evt_pcra evt_pcra,EVT_PERIODUOM evt_perioduom,evt_person evt_person,evt_personalprotectiveequip evt_personalprotectiveequip,evt_pfi evt_pfi,EVT_PPM evt_ppm,EVT_PPMREV evt_ppmrev,EVT_PPOPK evt_ppopk,evt_preservecalcpriority evt_preservecalcpriority,evt_print evt_print,evt_printed evt_printed,EVT_PRIORITY evt_priority,EVT_PROJBUD evt_projbud,EVT_PROJECT evt_project,evt_propertydamage evt_propertydamage,evt_recallnotice evt_recallnotice,evt_rejectreason evt_rejectreason,EVT_REOPENED evt_reopened,EVT_REPORTED evt_reported,EVT_REQM evt_reqm,EVT_REQUESTEND evt_requestend,EVT_REQUESTSTART evt_requeststart,EVT_ROUTE evt_route,evt_routeparent evt_routeparent,EVT_ROUTEREV evt_routerev,EVT_ROUTERSTATUS evt_routerstatus,EVT_ROUTESTATUS evt_routestatus,EVT_RSTATUS evt_rstatus,EVT_RTYPE evt_rtype,EVT_SAFETY evt_safety,EVT_SCHEDEND evt_schedend,EVT_SCHEDGRP evt_schedgrp,evt_securityincident evt_securityincident,EVT_SHIFT evt_shift,evt_smda evt_smda,evt_staffinjury evt_staffinjury,EVT_STANDWORK evt_standwork,EVT_START evt_start,evt_statementofcond evt_statementofcond,EVT_STATUS evt_status,EVT_TARGET evt_target,evt_tfdatecompleted evt_tfdatecompleted,evt_type evt_type,evt_udfchar01 evt_udfchar01,evt_udfchar02 evt_udfchar02,evt_udfchar03 evt_udfchar03,evt_udfchar04 evt_udfchar04,evt_udfchar05 evt_udfchar05,evt_udfchar06 evt_udfchar06,evt_udfchar07 evt_udfchar07,evt_udfchar08 evt_udfchar08,evt_udfchar09 evt_udfchar09,evt_udfchar10 evt_udfchar10,evt_udfchar11 evt_udfchar11,evt_udfchar12 evt_udfchar12,evt_udfchar13 evt_udfchar13,evt_udfchar14 evt_udfchar14,evt_udfchar15 evt_udfchar15,evt_udfchar16 evt_udfchar16,evt_udfchar17 evt_udfchar17,evt_udfchar18 evt_udfchar18,evt_udfchar19 evt_udfchar19,evt_udfchar20 evt_udfchar20";
                    String listOfColumns = "obj_accessible obj_accessible,obj_acd obj_acd,obj_alias obj_alias,obj_billable obj_billable,obj_bin obj_bin,obj_blackswan obj_blackswan,OBJ_BUILDMAINTPROGRAM obj_buildmaintprogram,obj_category obj_category,OBJ_CGMP obj_cgmp,obj_class obj_class,obj_class_org obj_class_org,obj_code obj_code,obj_commiss obj_commiss,obj_configapproved obj_configapproved,obj_configapprovedby obj_configapprovedby,obj_configautonum obj_configautonum,obj_configcode obj_configcode,obj_configdefaultstatus obj_configdefaultstatus,obj_configprefix obj_configprefix,obj_configrequested obj_configrequested,obj_configrequestedby obj_configrequestedby,obj_configrevision obj_configrevision,obj_configrstatus obj_configrstatus,obj_configseqlength obj_configseqlength,obj_configspecific obj_configspecific,obj_configstatus obj_configstatus,obj_configsuffix obj_configsuffix,obj_configtype obj_configtype,obj_configuseparentcode obj_configuseparentcode,obj_confinedspace obj_confinedspace,OBJ_CONTRACT obj_contract,OBJ_COSTCODE obj_costcode,obj_costofneededrepairs obj_costofneededrepairs,OBJ_CREATED obj_created,obj_createdby obj_createdby,obj_criticality obj_criticality,obj_depend obj_depend,obj_desc obj_desc,OBJ_DEVICETOLTYPE obj_devicetoltype,obj_dormend obj_dormend,obj_dormreuse obj_dormreuse,obj_dormstart obj_dormstart,obj_facility obj_facility,obj_facilityconditionindex obj_facilityconditionindex,OBJ_FCICALCULATION obj_fcicalculation,OBJ_FUEL obj_fuel,obj_gasindex obj_gasindex,obj_gislayer obj_gislayer,OBJ_GISMAP obj_gismap,OBJ_GISMAP_ORG obj_gismap_org,obj_gisobjid obj_gisobjid,OBJ_GISPROFILE obj_gisprofile,OBJ_GIS_SYNCCOUNT obj_gis_synccount,OBJ_GIS_UPDATECOUNT obj_gis_updatecount,OBJ_GROUP obj_group,obj_hipaaconfidentiality obj_hipaaconfidentiality,OBJ_INSTRUMENT obj_instrument,OBJ_LASTSAVED obj_lastsaved,OBJ_LASTSTATUSUPDATE obj_laststatusupdate,obj_latestinstalldate obj_latestinstalldate,obj_latestreceiptdate obj_latestreceiptdate,OBJ_LINKGISWO obj_linkgiswo,OBJ_LOCATION obj_location,obj_location_org obj_location_org,obj_lockout obj_lockout,OBJ_LOOP obj_loop,OBJ_LOTOREVIEWED obj_lotoreviewed,OBJ_LTYPE obj_ltype,OBJ_MANUFACT obj_manufact,obj_manufactmodel obj_manufactmodel,obj_manufactrevnum obj_manufactrevnum,OBJ_MRC obj_mrc,obj_nonsmoking obj_nonsmoking,obj_notused obj_notused,obj_obrtype obj_obrtype,obj_obtype obj_obtype,obj_openbay obj_openbay,OBJ_OPERATIONALRSTATUS obj_operationalrstatus,obj_operationalstatus obj_operationalstatus,OBJ_ORDER obj_order,obj_orderline obj_orderline,OBJ_ORDER_ORG obj_order_org,obj_org obj_org,obj_origconfigcode obj_origconfigcode,obj_origconfigorg obj_origconfigorg,obj_origconfigrev obj_origconfigrev,obj_originalinstalldate obj_originalinstalldate,obj_originalreceiptdate obj_originalreceiptdate,OBJ_ORIGOBJECT obj_origobject,obj_outputcalctype obj_outputcalctype,obj_ownershiptype obj_ownershiptype,obj_parent obj_parent,obj_parent_org obj_parent_org,OBJ_PART obj_part,obj_part_org obj_part_org,OBJ_PERSONALPROTECTIVEEQUIP obj_personalprotectiveequip,obj_position obj_position,obj_position_org obj_position_org,OBJ_PREVENTEVTCOMP obj_preventevtcomp,obj_primarysystem obj_primarysystem,obj_primarysystem_org obj_primarysystem_org,OBJ_PRIMARYUOM obj_primaryuom,obj_production obj_production,OBJ_PROFILE obj_profile,OBJ_PROFILE_ORG obj_profile_org,OBJ_PTYPE obj_ptype,obj_record obj_record,OBJ_RELIABILITYRANKINGLOCKED obj_reliabilityrankinglocked,OBJ_RENTAL obj_rental,obj_replacementvalue obj_replacementvalue,OBJ_RESOURCE obj_resource,OBJ_RESOURCEPRESENT obj_resourcepresent,OBJ_RSTATE obj_rstate,OBJ_RSTATUS obj_rstatus,OBJ_SAFETYREVIEWED obj_safetyreviewed,obj_sdmpath obj_sdmpath,obj_sdmpresent obj_sdmpresent,obj_serialno obj_serialno,obj_servicelife obj_servicelife,OBJ_SET obj_set,OBJ_STATE obj_state,obj_statementofcond obj_statementofcond,obj_status obj_status,OBJ_STORE obj_store,obj_tempmonitored obj_tempmonitored,obj_udfchar01 obj_udfchar01,obj_udfchar02 obj_udfchar02,obj_udfchar03 obj_udfchar03,obj_udfchar05 obj_udfchar05,obj_udfchar06 obj_udfchar06,obj_udfchar08 obj_udfchar08,obj_udfchar09 obj_udfchar09,obj_udfchar10 obj_udfchar10,obj_udfchar30 obj_udfchar30,obj_udfchkbox01 obj_udfchkbox01,obj_udfchkbox02 obj_udfchkbox02,obj_udfchkbox03 obj_udfchkbox03,obj_udfchkbox04 obj_udfchkbox04,obj_udfchkbox05 obj_udfchkbox05";
                    HashSet<String> firstGridColumns = new HashSet<>(Arrays.stream(listOfColumns.split(",")).toList());
                    String secondGridName = "2UTOBJ";
                    String gridDescription = tableName + "Grid 2/2";
                    // Subtract first grid columns from not null columns to get columns for the second grid
                    HashSet<String> columnsFor2Grid = new HashSet<>(notNullColumns);

                    HashSet<String> firstGridColumnsModified = new HashSet<>();
                    for (String columnName:
                            firstGridColumns) {
                        if(columnName.split(" ").length>0){
                            firstGridColumnsModified.add(columnName.split(" ")[0].toUpperCase());
                        } else {
                            firstGridColumnsModified.add(columnName.toUpperCase());
                        }
                    }
                    columnsFor2Grid.removeAll(firstGridColumnsModified);
                    // Process columns for the second grid
                    System.out.println(" firstGridColumnsModified");
                    for (String column: firstGridColumnsModified
                    ) {
                        System.out.println(column);
                    }
                    System.out.println(" columnsFor2Grid");
                    for (String column: columnsFor2Grid
                         ) {
                        System.out.println(column);
                    }
                    performActions(driver, secondGridName, gridDescription, tableName, columnsFor2Grid);
                }
            } else {
                Thread.sleep(2000);
                performActions(driver, gridName, tableName + " Grid 1/1", tableName, notNullColumns);
            }
        }
}

    public void performActions(WebDriver driver, String gridName, String gridDescription, String tableName, HashSet<String> columnNames) throws InterruptedException {
        // Click the "Add New Grid" button
        Thread.sleep(3000);
        WebElement addNewGridButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#button-1111-btnIconEl")));
        addNewGridButton.click();

        // Enter the grid name
        Thread.sleep(2000);
        WebElement gridNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#textfield-1207-inputEl")));
        gridNameField.sendKeys(gridName);
        // Enter the grid description
       // Thread.sleep(3000);
        WebElement gridDescriptionField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#textfield-1208-inputEl")));
        gridDescriptionField.sendKeys(gridDescription);

        // Enter the table name in the FROM clause
        Thread.sleep(2000);
        WebElement fromClause = driver.findElement(By.id("uxtextareatrigger-1219-inputEl"));
        fromClause.sendKeys(tableName);
        // Click on the element by its ID
        Thread.sleep(2000);
        TreeSet<String> sortedColumns = new TreeSet<>(columnNames);

        for (String column : sortedColumns) {
            checkColumn(column);
           // Thread.sleep(3000);
        }
            // Click the "OK" button

            // Click on the "Save" button
            WebElement SaveButton = driver.findElement(By.id("button-1110-btnIconEl"));
            SaveButton.click();
            Thread.sleep(6000);


    }



    public boolean ifGridExists(String gridName) throws InterruptedException {
        WebElement SearchWithinAllGridsTextField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uxpicktriggerfield-1145-inputEl")));
        SearchWithinAllGridsTextField.clear();
        SearchWithinAllGridsTextField.sendKeys(gridName);
        //Search icon in Grid designer
        WebElement searchIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uxpicktriggerfield-1145-trigger-trigger")));
        WebElement searchIconClickable = wait.until(ExpectedConditions.elementToBeClickable(searchIcon));
        Thread.sleep(1000);
        //searchIconClickable.click();
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", searchIconClickable);
        Thread.sleep(2000);
        if(checkIfGridFound(gridName)) return true;
        return false;
    }
      public boolean checkIfGridFound(String gridName) {
        String cssSelector = "#uxgridsummaryview-1143 > div > div:nth-child(1) > div";

        try {
            // Locate the element using the defined CSS selector
            WebElement gridsummaryview = driver.findElement(By.cssSelector(cssSelector));

            // Example action: Wait until the element is visible and then print its text
            //wait.until(ExpectedConditions.visibilityOf(element));
            //System.out.println(element.getText());

            if (gridsummaryview.getText().contains(gridName)) {
                return true;
            }

        } catch (NoSuchElementException e) {
            // Handle the case where the element is not found
            System.out.println("Element not found: " + cssSelector);
        } catch (StaleElementReferenceException e) {
            // Handle the stale element reference exception
            System.out.println("Stale element reference exception encountered.");
        }

        return false;
    }

    public void checkColumn(String columnName) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uxtextareatrigger-1221-inputEl")));
        input.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uxtextareatrigger-1221-trigger-trigger"))).click();

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("selfiltervaluectrl")));

        // Enter the column name in the text field
        element.sendKeys(columnName);
        Thread.sleep(2000);
        WebElement runButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Run")));
        runButton.click();

        String rowXPath = "//table[contains(@id, 'tableview') and contains(@id, 'record')]//tr[1]//td[contains(@class, 'x-grid-cell-uxcheckcolumn') and contains(@class, 'x-grid-cell-first')]//div//div";

        try {
            WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));
            row.click();
        } catch (StaleElementReferenceException e) {
            // Element is stale, retry click operation
            WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXPath)));
            row.click();
        }
        // Click the "OK" button
        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("OK")));
        //((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        //WebElement okButtonClick = wait.until(ExpectedConditions.elementToBeClickable(okButton));
        //

        try {
            okButton.click();
        }catch (ElementClickInterceptedException e){
             wait.until(ExpectedConditions.numberOfElementsToBe(By.linkText("OK"),1));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", okButton);
        }
        Thread.sleep(2000);
    }

}