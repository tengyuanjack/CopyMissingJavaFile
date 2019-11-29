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
 * ���ܣ�
 *    Ϊ�˷���鿴���õ�jar���д��룬��Դ���е�java�ļ���������Ӧ��class�ļ���ͬ���ļ�����
 * 
 * @since 2018-4-19 ����2:44:35
 * @version 1.1.0
 * @author ����
 */
public class Tools {
  
  private static final String fromSuffix = ".java";
  private static final String toSuffix = ".class";
  
  /**
   * �ݹ�õ�ĳ���ļ����µ������ļ�
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
   * ���Ʋ���
   * ͨ��ƥ��·�����õ�ÿ��class�ļ���Ӧ��java�ļ���λ�ã�Ȼ��ʹ��FileUtils����������
   * 
   * @param fromDir
   * @param toDir
   * @param fromList
   * @param toList
   */
  public static void copyCorrespondingFiles(String fromDir, String toDir, 
      List<String> fromList, List<String> toList) {
    
    for (String toItem : toList) {
      // toItem ʾ���� classes\nc\bap\portal\page\FreeReportExportAction�� "classes\"�����ȫ����
      String completeClassName = StringUtils.substringAfter(toItem, "classes\\");
      
      // toBeCopyMap ��Ϊjava��·���� ֵΪclass·��
      Map<String, String> toBeCopyMap = new HashMap<>();
      for (String fromItem : fromList) {
        if (fromItem.endsWith(completeClassName)) {
          toBeCopyMap.put(fromItem, toItem);
          break;
        }
      }
      
      // ����      
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
   * ������
   * 
   * fromDir��toDir�ֱ�������ģ����ļ��и�·��
   */
  public static void main(String[] args) {
    String fromDir = "D:\\NCCLOUD\\��������\\ae_aert\\";
    String toDir = "D:\\NCCLOUD\\KING65_JINPAN\\modules\\aert\\";
    List<String> fromList = new ArrayList<>();
    List<String> toList = new ArrayList<>();
    getFiles(fromDir, fromDir, fromSuffix, fromList);
    getFiles(toDir, toDir, toSuffix, toList);
    copyCorrespondingFiles(fromDir, toDir, fromList, toList);
    
    System.out.println("Completed!");
  }  
}
