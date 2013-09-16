package my.com.tm.workgroup.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DataExtractorUtil {

    // STATION ID STATION NAME WORKGROUP REGION NON - EXEC EXEC
    public static final String[] DEFAULT_KEYS = { "stationId", "stationName", "workgroup", "region" };
    public static final String DEFAULT_OUTPUT_DIR = "output";

    private static void closeOPCPackage(OPCPackage pkg) {
        if (pkg != null) {
            try {
                pkg.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeData(String filename, JsonArray array) throws IOException {
        String dir = DEFAULT_OUTPUT_DIR;
        writeData(dir, filename, array);
    }

    private static void writeData(String dir, String filename, JsonArray array) throws IOException {
        // write json array into output file
        Writer writer = null;
        try {
            String out = (new StringBuilder(dir)).append('/').append(filename).toString();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "utf-8"));
            writer.write(array.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static void extract(String filename) {
        extract(filename, true);
    }

    public static void extract(String filename, boolean firstRowIsHeader) {
        File file = new File(filename);
        extract(file, firstRowIsHeader);
    }

    public static String extract(File file, boolean firstRowIsHeader) {
        String filename = file.getName();
        String simple = filename.substring(0, filename.lastIndexOf('.'));
        String out = (new StringBuilder(simple)).append(".json").toString();
        
        OPCPackage pkg = null;
        try {

            pkg = OPCPackage.open(file);
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            String[] keys = DEFAULT_KEYS;
            int length = keys.length;
            boolean begin = !firstRowIsHeader;

            JsonArray array = new JsonArray();

            Sheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                if (begin) {
                    JsonObject json = new JsonObject();
                    int ind = 0;
                    
                    //System.out.println(row.getPhysicalNumberOfCells());
                    
                    int lastColumnIndex = -1;
                    
                    for (Cell cell : row) {
                        
                        //System.out.println(cell.getColumnIndex());

                        int currentColumnIndex = cell.getColumnIndex();
                        if (lastColumnIndex < 0) {
                            lastColumnIndex = currentColumnIndex - 1;
                        }
                        
                        int indexDiff = currentColumnIndex - lastColumnIndex;
                        
                        if (indexDiff > 1) {
                            ind += indexDiff - 1;
                        }
                        
                        if (ind < length) {
                            String value = cell.getStringCellValue();
                            if (keys[ind].equals("region")) {
                                System.out.println(value);
                            }
                            if (value != null && value.trim().length() > 0) {
                                json.addProperty(keys[ind], value);
                            }
                        }
                        ind++;
                        lastColumnIndex = currentColumnIndex;
                    }
                    array.add(json);
                }
                if (firstRowIsHeader) {
                    begin = true;
                }
            }

            writeData(out, array);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeOPCPackage(pkg);
        }
        return out;
    }

    public static void extractFiles(String dir) {
        File file = new File(dir);
        if (file != null) {
            boolean firstRowIsHeader = true;
            
            JsonArray array = new JsonArray();

            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    String name = extract(f, firstRowIsHeader);
                    JsonObject json = new JsonObject();
                    json.addProperty("name", name);
                    array.add(json);
                }
            } else {
                String name = extract(file, firstRowIsHeader);
                JsonObject json = new JsonObject();
                json.addProperty("name", name);
                array.add(json);
            }

            String filename = "files.json";
            try {
                writeData(filename, array);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String dir = "input";
        
        DataExtractorUtil.extractFiles(dir);

    }
}