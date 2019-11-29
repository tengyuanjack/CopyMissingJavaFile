package com.zt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 功能：
 *    为了方便查看引用的jar包中代码，将源码中的java文件拷贝到相应的class文件的同级文件夹下
 * 
 * @since 2018-4-19 下午2:44:35
 * @version 1.1.0
 * @author 赵腾
 */
public class Tools {
  
  private static final String fromSuffix = ".java";
  private static final String toSuffix = ".class";
  
  /**
   * 递归得到某个文件夹下的所有文件
   * 
   * @param commonDirPath
   * @param directoryPath
   * @param suffix
   * @param files
   */
  public static void getFiles(String commonDirPath, String directoryPath, 
      String suffix, List<String> files) {
    
    File file = new File(directoryPath);
    File[] firstLayerFiles = file.listFiles();
    
    for(File itemFile : firstLayerFiles) {      
      String absPath = itemFile.getAbsolutePath();
      if (itemFile.isFile()) {
        if (absPath.endsWith(suffix)) {
          String sub = StringUtils.substringAfter(absPath, commonDirPath);
          files.add(StringUtils.substringBeforeLast(sub, suffix));
        }
      } else if (itemFile.isDirectory()) {
        getFiles(commonDirPath, absPath, suffix, files);
      }
     
    }
  }
  
  /**
   * 复制操作
   * 通过匹配路径，得到每个class文件对应的java文件的位置，然后使用FileUtils做拷贝操作
   * 
   * @param fromDir
   * @param toDir
   * @param fromList
   * @param toList
   */
  public static void copyCorrespondingFiles(String fromDir, String toDir, 
      List<String> fromList, List<String> toList) {
    
    for (String toItem : toList) {
      // toItem 示例： classes\nc\bap\portal\page\FreeReportExportAction， "classes\"后边是全类名
      String completeClassName = StringUtils.substringAfter(toItem, "classes\\");
      
      // toBeCopyMap 键为java类路径， 值为class路径
      Map<String, String> toBeCopyMap = new HashMap<>();
      for (String fromItem : fromList) {
        if (fromItem.endsWith(completeClassName)) {
          toBeCopyMap.put(fromItem, toItem);
          break;
        }
      }
      
      // 复制      
      for (Map.Entry<String, String> entry : toBeCopyMap.entrySet()) {
        String fromPath = fromDir + entry.getKey() + fromSuffix;
        String toPathDir = toDir + StringUtils.substringBeforeLast(entry.getValue(), File.separator);
        try {
          FileUtils.copyFileToDirectory(new File(fromPath), new File(toPathDir));
          System.out.println("Copied " + entry.getKey());
        }
        catch (IOException ex) {
          ex.printStackTrace();
        }
      }
      
    }
    
  }
  
  /**
   * 主函数
   * 
   * fromDir和toDir分别是两个模块的文件夹根路径
   */
  public static void main(String[] args) {
    String fromDir = "D:\\NCCLOUD\\其他工程\\ae_aert\\";
    String toDir = "D:\\NCCLOUD\\KING65_JINPAN\\modules\\aert\\";
    List<String> fromList = new ArrayList<>();
    List<String> toList = new ArrayList<>();
    getFiles(fromDir, fromDir, fromSuffix, fromList);
    getFiles(toDir, toDir, toSuffix, toList);
    copyCorrespondingFiles(fromDir, toDir, fromList, toList);
    
    System.out.println("Completed!");
  }  
}
