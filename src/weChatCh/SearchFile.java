/**
 * 
 */
package weChatCh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class SearchFile {
	public  List<String> getFilenameList( String htmlDirPath) {
		List<String> filenameList=new ArrayList<String>();
        File file = new File(htmlDirPath);
        String filename = null;
        if(file.exists()){
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return null;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        
                    } else {
                        int dot = file2.getName().lastIndexOf('.'); 
                        if ((dot >-1) && (dot < (file2.getName().length()))) { 
                            filename= file2.getName().substring(0, dot); 
                        } 
                    	filenameList.add(filename);
                        //System.out.println("文件:" + file2.getName());
//                        String filePath = file2.getAbsolutePath();
//                        String fileName = filePath.substring(0,filePath.lastIndexOf("\\"))+"\\aaa"+filePath.substring(filePath.lastIndexOf("."));
//                        File oriFile = new File(filePath);
//                        boolean b = oriFile.renameTo(new File(fileName));
//                        System.out.println(b);
                    }
                }
            }
        }else{
            System.out.println("该路径不存在");
        }
        
		return filenameList;
		
	}
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		//getFilename(MyClass.htmlDirPath);
//	}

}
