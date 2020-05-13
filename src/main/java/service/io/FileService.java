package service.io;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zk
 */
public class FileService {

    static String _CUSTOM_PATH = "custom.zk";

    static String _sqlFileName;

    public static String setSqlFileName() {
        return _sqlFileName = System.currentTimeMillis() + "";
    }

    public static File createDirBySqlFileName() {
        File dir = new File("./" + _sqlFileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File createDir(String dirFilePath) {
        File dir = new File(dirFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String[] readFileByLine(String filePath) {
        return ResourceUtil.readUtf8Str(StrUtil.isBlank(filePath) ? _CUSTOM_PATH : filePath).split("\\r\\n");
    }

    public static String readFile(String filePath) {
        return ResourceUtil.readUtf8Str(filePath);
    }

    public static boolean writeSql(String fileName, List<String> writeData) throws IOException {

//        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//        try {
//            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//            Document doc = documentBuilder.parse(ResourceUtil.getResource("config.xml").getFile());
//            doc.getElementById("1").getFirstChild().getNodeValue();
//
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Element element = XmlUtil.readXML(ResourceUtil.getResource("config.xml").getFile()).getElementById("1");
        String typeValue = "1";
        try {
            String sqlConfig =  ResourceUtil.readUtf8Str("sql.conf").trim();
            String sqlDownType = sqlConfig.split(";")[0].trim();
            typeValue = sqlDownType.split(":")[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (typeValue.equals("1")) {
            writeFile(fileName, writeData);
        }
        if (typeValue.equals("2")) {
            writeMultipleFileForSql(fileName, writeData);
        }
         return true;
    }

    public static boolean writeFile(String fileName, List<String> writeData) throws IOException {
        String fileFullPath = FileService.createDirBySqlFileName().getPath() + "/" + fileName + ".sql";
        FileWriter writer = new FileWriter(fileFullPath);
        writeData.stream().forEach(s -> {
            try {
                writer.append(s);
                writer.append((char) 13);
                writer.append((char) 10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.flush();
        writer.close();
        return true;
    }

    public static boolean writeMultipleFileForSql(String fileName, List<String> writeData) throws IOException {

        String fileFullPath = FileService.createDirBySqlFileName().getPath() + "/" + fileName;
        int par = writeData.size() / 2000;
        int remainder = writeData.size() % 2000;
        if (par > 1 || (par == 1 && remainder > 0)) {
            for (int i = 0; i < par; i++) {
                FileWriter writer = new FileWriter( fileFullPath + "-" + i + ".sql");
                writeData.subList(i * 2000, (i + 1) * 2000).stream().forEach(s -> {
                    try {
                        writer.append(s);
                        writer.append((char) 13);
                        writer.append((char) 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                writer.flush();
                writer.close();
            }
            if (remainder > 0) {
                FileWriter writer = new FileWriter(fileFullPath + "-" + par + ".sql");
                writeData.subList(par * 2000, writeData.size() - 1).stream().forEach(s -> {
                    try {
                        writer.append(s);
                        writer.append((char) 13);
                        writer.append((char) 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                writer.flush();
                writer.close();
            }
        } else {
            FileWriter writer = new FileWriter(fileFullPath + "-" + ".sql");
            writeData.stream().forEach(s -> {
                try {
                    writer.append(s);
                    writer.append((char) 13);
                    writer.append((char) 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.flush();
            writer.close();
        }
        return true;
    }

    public static boolean writeExcel(String filePath, List<List> writeData, String contentTitle) {
        File writeDir = new File(filePath);
        writeDir.getParentFile().mkdirs();
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getBigWriter(filePath);
        // 跳过当前行，既第一行，非必须，在此演示用
        writer.passCurrentRow();
        //合并单元格后的标题行，使用默认标题样式
        writer.merge(writeData.get(0).size() - 1, contentTitle);
        //一次性写出内容，强制输出标题
        writer.write(writeData, true);
        //关闭writer，释放内存
        writer.close();
        return true;
    }

    public static List readExcel(String filePath) {
        ExcelReader reader = ExcelUtil.getReader(filePath);
        List<List<Object>> readAll = reader.read(3);
        return readAll.stream().map(data -> data.get(0)).collect(Collectors.toList());
    }

    public static List readPartnerExcel(String filePath) {
        ExcelReader reader = ExcelUtil.getReader(filePath);
        List<List<Object>> readAll = reader.read(3);
        return readAll;
    }
}
