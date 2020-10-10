package weChatCh;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TEST {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		String url="http://mp.weixin.qq.com/s?__biz=MzAxMjM4MTEwNg==&amp;mid=2651709026&amp;idx=1&amp;sn=b48c739b490749960b4263db237a58e1&amp;chksm=804bc0bfb73c49a9f7b19d623967e70577a1ac149010f6df647f8c899da8b525afd9bbc62e5e&amp;scene=27#wechat_redirect";
//		String title="test";
//		String date="29991231";
//		try {
//			ChapterCatch.htmlToPdf( url, title, date);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		ChapterCatch.convert(MyClass.htmlDirPath, MyClass.pdfDirPath);	
		
	}

}
