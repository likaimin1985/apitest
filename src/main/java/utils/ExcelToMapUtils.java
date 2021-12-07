package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

//参考
//https://www.jianshu.com/p/2ba3c0bd3eb6
public class ExcelToMapUtils {

	//比较通用
	public static List<Map<String, Object>> importExcel(String filepath, int index) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(new FileInputStream(filepath));
			Sheet sheet = workbook.getSheetAt(index);
			Row row = sheet.getRow(0);
			List<String> keys = new ArrayList<String>();
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				keys.add(String.valueOf(getValue(cell)));
			}

			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				Row currentRow = sheet.getRow(i + 1);
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int j = 0; j < currentRow.getLastCellNum(); j++) {
					map.put(keys.get(j), getValue(currentRow.getCell(j)));
				}
				mapList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("excel解析出错");
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mapList;
	}
	
	 private static Object getValue(Cell hssfCell) {
         if (hssfCell.getCellType() == CellType.BOOLEAN) {
             return hssfCell.getBooleanCellValue();
         } else if (hssfCell.getCellType() == CellType.NUMERIC) {
        	 NumberFormat nf = NumberFormat.getInstance();
        	 return nf.format(hssfCell.getNumericCellValue()); 
         } else {
             return hssfCell.getStringCellValue();
         }
     }


	public static void main(String[] args) {
		String dir=System.getProperty("user.dir");
    	String path=dir+File.separator+"data"+File.separator+"apitest2.xlsx";
		List<Map<String, Object>> listmapList= ExcelToMapUtils.importExcel(path,1);
		System.out.println(listmapList);
	}
}
