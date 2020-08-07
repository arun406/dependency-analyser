package com.wellsforgo.dependency.exporters;

import com.wellsforgo.dependency.Dependency;
import com.wellsforgo.dependency.Options;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Component
public class DependencyExcelExporter {

    public void export(File file, List<Dependency> dependencies, Options option) throws IOException {

        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet(" Maven Dependencies ");
        //Create row object
        XSSFRow row;

        Map<String, Object[]> dataMap = new TreeMap<String, Object[]>();

        dataMap.put("1", new Object[]{
                "Name", "Group Id", "Artifact Id", "Version", "Scope", "Type", "System Path"});
        int counter = 2;
        for (Dependency dependency : dependencies) {
            List<Object> record = new ArrayList<>();
            record.add(dependency.getGroupId());
            record.add(dependency.getArtifactId());
            String version = dependency.getVersion();
            record.add(version);
            record.add(dependency.getVersion());
            record.add(dependency.getScope());
            record.add(dependency.getSystemPath());
            record.add(dependency.getType());
            dataMap.put(Integer.toString(counter), record.toArray());
            counter++;
        }

        //Iterate over data and write to sheet
        Set<String> keyid = dataMap.keySet();
        int rowid = 0;

        for (
                String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = dataMap.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String) obj);
            }
        }

        //Write the workbook in file system
        FileOutputStream out = new FileOutputStream(file);

        workbook.write(out);
        out.close();
        System.out.println(file.getName() + " written successfully");
    }
}
