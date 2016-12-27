package com.gpdata.wanyou.ds.util;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理工具类
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    private static final int SIZE = 1024 * 4;

    /**
     * 关闭流
     *
     * @param ios
     */
    public static void close(Closeable... ios) {
        for (Closeable io : ios) {
            if (io != null) {
                try {
                    io.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将文件流写入服务器本地
     *
     * @param inputStream：输入流
     * @param desPath：保存路径
     * @param fileName：文件名
     */
    public static void write(InputStream inputStream, Path desPath, String fileName) throws IOException {
        File src = desPath.toFile();
        if (!src.exists()) {
            src.mkdirs();
        }
        Path des = Paths.get(desPath.toString(), fileName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(des.toFile()));
        byte[] bytes = new byte[SIZE];
        int len = 0;
        while ((len = bufferedInputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
        }
        bufferedOutputStream.flush();
        close(bufferedOutputStream, bufferedInputStream);
    }

    /**
     * 从服务器上将文件写入响应流
     *
     * @param desPath
     * @param response
     */
    public static void download(Path desPath, HttpServletResponse response) throws IOException {
        File file = desPath.toFile();
        if (!file.exists()) {
            return;
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
        byte[] bytes = new byte[SIZE];
        int len = 0;
        while ((len = bufferedInputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
        }
        bufferedOutputStream.flush();
        close(bufferedInputStream);
    }

    /**
     * 导出为excel
     *
     * @param objData   导出内容数组
     * @param sheetName 导出工作表的名称
     * @param columns   导出Excel的表头数组
     * @return
     */
    public static int exportToExcel(HttpServletResponse response, List<Map<String, Object>> objData, String sheetName, List<String> columns) {
        int flag = 0;
        //声明工作簿jxl.write.WritableWorkbook
        WritableWorkbook wwb;
        try {
            //根据传进来的file对象创建可写入的Excel工作薄
            OutputStream os = response.getOutputStream();
            wwb = Workbook.createWorkbook(os);
            /*
			 * 创建一个工作表、sheetName为工作表的名称、"0"为第一个工作表
			 * 打开Excel的时候会看到左下角默认有3个sheet、"sheet1、sheet2、sheet3"这样
			 * 代码中的"0"就是sheet1、其它的一一对应。
			 * createSheet(sheetName, 0)一个是工作表的名称，另一个是工作表在工作薄中的位置
			 */
            WritableSheet ws = wwb.createSheet(sheetName, 0);
            SheetSettings ss = ws.getSettings();
            ss.setVerticalFreeze(1);//冻结表头
            WritableFont font1 = new WritableFont(WritableFont.createFont("微软雅黑"), 10, WritableFont.BOLD);
            WritableFont font2 = new WritableFont(WritableFont.createFont("微软雅黑"), 9, WritableFont.NO_BOLD);
            WritableCellFormat wcf = new WritableCellFormat(font1);
            WritableCellFormat wcf2 = new WritableCellFormat(font2);
            WritableCellFormat wcf3 = new WritableCellFormat(font2);//设置样式，字体
            //创建单元格样式
            //WritableCellFormat wcf = new WritableCellFormat();
            //背景颜色
            wcf.setBackground(jxl.format.Colour.YELLOW);
            wcf.setAlignment(Alignment.CENTRE);  //平行居中
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
            wcf3.setAlignment(Alignment.CENTRE);  //平行居中
            wcf3.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
            wcf3.setBackground(Colour.LIGHT_ORANGE);
            wcf2.setAlignment(Alignment.CENTRE);  //平行居中
            wcf2.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
			/*
			 * 这个是单元格内容居中显示
			 * 还有很多很多样式
			 */
            wcf.setAlignment(Alignment.CENTRE);
            //判断一下表头数组是否有数据
            if (columns != null && columns.size() > 0) {
                //循环写入表头
                for (int i = 0; i < columns.size(); i++) {
					/*
					 * 添加单元格(Cell)内容addCell()
					 * 添加Label对象Label()
					 * 数据的类型有很多种、在这里你需要什么类型就导入什么类型
					 * 如：jxl.write.DateTime 、jxl.write.Number、jxl.write.Label
					 * Label(i, 0, columns[i], wcf)
					 * 其中i为列、0为行、columns[i]为数据、wcf为样式
					 * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中
					 */
                    ws.addCell(new Label(i, 0, String.valueOf(columns.get(i)), wcf));
                }
                //判断表中是否有数据
                if (objData != null && objData.size() > 0) {
                    //循环写入表中数据
                    for (int i = 0; i < objData.size(); i++) {
                        //转换成map集合{activyName:测试功能,count:2}
                        Map<String, Object> map = (Map<String, Object>) objData.get(i);
                        //循环输出map中的子集：既列值
                        int j = 0;
                        for (Object o : map.keySet()) {
                            //ps：因为要“”通用”“导出功能，所以这里循环的时候不是get("Name"),而是通过map.get(o)
                            ws.addCell(new Label(j, i + 1, String.valueOf(map.get(o))));
                            j++;
                        }
                    }
                } else {
                    flag = -1;
                }
                //写入Exel工作表
                wwb.write();
                //关闭Excel工作薄对象
                wwb.close();
                //关闭流
                os.flush();
                os.close();
                os = null;
            }
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (Exception ex) {
            flag = 0;
            ex.printStackTrace();
        }
        return flag;
    }

    /**
     * 下载excel
     *
     * @param response
     * @param filename  文件名 ,如:20110808.xls
     * @param listData  数据源
     * @param sheetName 表头名称
     * @param columns   列名称集合,如：{物品名称，数量，单价}
     * @author
     */
    public static void exportexcle(HttpServletResponse response, String filename, List<Map<String, Object>> listData, String sheetName, List<String> columns) {
        //调用上面的方法、生成Excel文件
        response.setContentType("application/vnd.ms-excel");
        //response.setHeader("Content-Disposition", "attachment;filename="+filename);
        try {
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes("utf8"), "utf8") + ".xls");
            exportToExcel(response, listData, sheetName, columns);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将多个Excel打包成zip文件
     *
     * @param srcfile
     * @param zipfile
     */
    public static void zipFiles(List<File> srcfile, File zipfile) {
        byte[] buf = new byte[2048];
        try {
            // Create the ZIP file
            // Compress the files
            if (srcfile.size() > 0) {
                // 创建ZipOutputStream类对象
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
                for (int i = 0; i < srcfile.size(); i++) {
                    File file = srcfile.get(i);
                    FileInputStream in = new FileInputStream(file);
                    // Add ZIP entry to output stream.
                    out.putNextEntry(new ZipEntry(file.getName()));// 写入此目录的Entry 创建新的进入点
                    // Transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.setLevel(9);
                        out.write(buf, 0, len);
                    }
                    // Complete the entry
                    out.closeEntry();
                    in.close();
                }
                out.close();
            } else {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
                out.putNextEntry(new ZipEntry(" "));
                out.closeEntry();
                out.close();
            }
            // Complete the ZIP file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    @SuppressWarnings({"unused", "resource"})
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    /**
     * 工具方法：字符串数组转long型数组
     *
     * @param stringArray
     * @return
     */
    public static long[] stringToLong(String stringArray[]) {
        if (stringArray == null || stringArray.length < 1) {
            return null;
        }
        long longArray[] = new long[stringArray.length];
        for (int i = 0; i < longArray.length; i++) {
            try {
                longArray[i] = Long.valueOf(stringArray[i]);
            } catch (NumberFormatException e) {
                longArray[i] = 0;
                continue;
            }
        }
        return longArray;
    }
    /**
     * 创建文件
     *
     * @param destFileName
     * @return
     */
    public static boolean CreateFile(String destFileName) {
        File file = new File(destFileName);
        // if (file.exists()) {
        // LOGGER.debug("CreateFileUtil: {}", "创建单个文件" + destFileName +
        // "失败，目标文件已存在！");
        // return false;
        // }
        if (destFileName.endsWith(File.separator)) {// 路径分隔符
            LOGGER.debug("CreateFileUtil: {}", "创建单个文件" + destFileName + "失败，目标不能是目录！");
            return false;
        }
        if (!file.getParentFile().exists()) {
            LOGGER.debug("CreateFileUtil: {}", "目标文件所在路径不存在，准备创建。。。");
            if (!file.getParentFile().mkdirs()) {
                LOGGER.debug("CreateFileUtil: {}", "创建目录文件所在的目录失败！");
                return false;
            }
        }
        // 创建目标文件
        try {
            if (file.createNewFile()) {
                LOGGER.debug("CreateFileUtil: {}", "创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                LOGGER.debug("CreateFileUtil: {}", "创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.debug("CreateFileUtil: {}", "创建单个文件" + destFileName + "失败！");
            return false;
        }
    }

    /**
     * 创建目录
     *
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (!dir.getParentFile().exists())
            dir.getParentFile().mkdirs();
        if (!destDirName.endsWith(File.separator))
            destDirName = destDirName + File.separator;
        // 创建单个目录
        // if (dir.mkdirs()) {// 该目录(或多级目录)能被创建则true否则false
        LOGGER.debug("CreateFileUtil: {}", "创建目录" + destDirName + "成功！");
        return true;
        // } else {
        // LOGGER.debug("CreateFileUtil: {}", "创建目录" + destDirName + "失败！");
        // return false;
        // }
    }

    /**
     * 创建临时文件
     *
     * @param prefix
     * @param suffix
     * @param dirName
     * @return
     */
    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;

        try {
            if (dirName == null) {
                // 在默认文件夹下创建临时文件
                tempFile = File.createTempFile(prefix, suffix);
                return tempFile.getCanonicalPath();
            } else {
                File dir = new File(dirName);
                // 如果临时文件所在目录不存在，首先创建
                if (!dir.exists()) {
                    if (!FileUtil.createDir(dirName)) {
                        LOGGER.debug("CreateFileUtil: {}", "创建临时文件失败，不能创建临时文件所在目录！");
                        return null;
                    }
                }
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.debug("CreateFileUtil: {}", "创建临时文件失败" + e.getMessage());
            return null;
        }
    }

    /**
     * spring的上传文件
     *
     * @param request
     * @param response
     * @return
     */
    public static List<MultipartFile> fileUpdate(ServletContext servletContext,
                                                 HttpServletRequest request, HttpServletResponse response, String loginname) {

        File disfile = null;
        /**
         * // 创建一个通用的多部分解析器 session无效 停用<br/>
         * CommonsMultipartResolver multipartResolver = new
         * CommonsMultipartResolver( request.getSession().getServletContext());
         * // 判断 request 是否有文件上传,即多部分请求 if
         * (multipartResolver.isMultipart(request)) {
         **/

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {

            LOGGER.debug("----------------");

            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            // 文件集合
            List<MultipartFile> listFile = new ArrayList<MultipartFile>();
            while (iter.hasNext()) {
                // 记录上传过程起始时的时间，用来计算上传时间
                int pre = (int) System.currentTimeMillis();
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());

                if (file != null) {
                    listFile.add(file);
                    // 取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();

                    // 如果名称不为"",说明该文件存在，否则说明该文件不存在
                    if (myFileName.trim() != "") {
                        // 得到文件上传路径
                        CommonsMultipartFile cf = (CommonsMultipartFile) file;
                        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
                        disfile = fi.getStoreLocation();

                        // InputStream is;
                        // Map<Integer, String> content = new HashMap<Integer,
                        // String>();
                        // try {
                        // is = new FileInputStream(disfile);
                        // } catch (FileNotFoundException e) {
                        // LOGGER.error("FileNotFoundException", e);
                        // }
                        LOGGER.debug("上传时间：" + pre + "  文件名称:  " + myFileName + "   disfile:  "
                                + disfile);

                    }
                }
            }
            return listFile;

        }
        return null;

    }

    /**
     * 统一文件上传
     *
     * @param request
     * @param rootPath
     *            根目录
     * @param realpath
     *            文件全路径
     */
    public static void saveFile(HttpServletRequest request, MultipartFile mf ,String rootPath, String realpath) {
        try {
            File saveDir = null;
//            // 文件夹
//            File dir = new File(destDirName);
//            if (!dir.getParentFile().exists())
//                dir.getParentFile().mkdirs();
//            if (!destDirName.endsWith(File.separator))
//                destDirName = destDirName + File.separator;
            // 文件
            saveDir = new File(rootPath + "" + realpath);
            saveDir.getParentFile().mkdirs();
            // 保存
            saveDir.setWritable(true, false);
            saveDir.setReadable(true, false);

            mf.transferTo(saveDir);
        } catch (IllegalStateException e) {
            LOGGER.error("IllegalStateException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }

    public static void main(String[] args) {
        // 创建目录
        String dirName = "F:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\dts\\upload";
        FileUtil.createDir(dirName);
        // 创建文件
        String fileName = dirName + "/test2/testFile.txt";
        FileUtil.CreateFile(fileName);
        // 创建临时文件
        String prefix = "temp";
        String suffix = ".txt";
        // for (int i = 0; i < 2; i++) {
        // LOGGER.debug("CreateFileUtil: {}",
        // "创建了临时文件:" + FileUtil.createTempFile(prefix, suffix, dirName));
        // }
    }

}
