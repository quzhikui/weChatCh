package weChatCh;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChapterCatch {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
			String url=null;
	        int startIndex=0;
	        int currentTimes = 0;	
	        String date="29991231";
	   while(date.compareTo("20200101")>0) {
		   System.out.println(" startIndex= " + startIndex );
	        url="https://mp.weixin.qq.com/mp/profile_ext?action=getmsg&"
	        		+ "__biz="+MyClass.__biz
	        		+ "&f=json&"
	        		+ "offset="+startIndex
	        		+ "&count=10&is_ok=1&scene=&uin=MjA3MDQwMDQwNw%3D%3D&"
	        		+ "key="+MyClass.key
	        		+ "&"
	        		+ "pass_ticket="+MyClass.pass_ticket
	        		+ "&wxtoken=&"
	        		+ "appmsg_token="+MyClass.appmsg_token
	        		+ "&x5=0&f=json";
	        
	        String cookie="rewardsn=; wxtokenkey=777; wxuin=2070400407; devicetype=android-28; version=2700103e; lang=zh_CN; "
	        		+ "pass_ticket="+MyClass.pass_ticket_02
	        		+ "; "
	        		+ "wap_sid2="+MyClass.wap_sid2;
	        
	        
	        OkHttpClient okHttpClient = new OkHttpClient(); // 创建OkHttpClient对象
	        Request request = new Request.Builder()
	                .url(url)
	                .get()
	                .addHeader("Host", "mp.weixin.qq.com")
	                .addHeader("Connection", "keep-alive")
	                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 QBCore/4.0.1301.400 QQBrowser/9.0.2524.400 Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2875.116 Safari/537.36 NetType/WIFI MicroMessenger/7.0.5 WindowsWechat")
	                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.5;q=0.4")
	                .addHeader("X-Requested-With", "XMLHttpRequest")
	                .addHeader("Cookie", cookie)
	                .addHeader("Accept", "*/*")
	                .build();
	        	
	            Response response = okHttpClient.newCall(request).execute();
	            if (response.isSuccessful()) {
	                String body = response.body().string();
	                //System.out.println(body);
	                JSONObject jo= JSONObject.fromObject(body);
	                String general_msg_list=null;
	                if (jo.getInt("ret") == 0) {
	                    
						currentTimes++;
	                    
	                    System.out.println("当前是第" + currentTimes + "次");
	                    
	                    general_msg_list = jo.getString("general_msg_list");
	                    general_msg_list = general_msg_list.replace("\\/", "/");
	                }
	                    // json 解析
	                	JSONObject jo2= JSONObject.fromObject(general_msg_list);
	                    JSONArray msgList = jo2.getJSONArray("list");
	                    for (int i = 0; i < msgList.size(); i++) {
	                        JSONObject j = msgList.getJSONObject(i);
	                        JSONObject msgInfo = j.getJSONObject("comm_msg_info");

	                        long datetime = msgInfo.getLong("datetime");
	                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	                        date = sdf.format(new Date(datetime * 1000));
	                        
	                        if (j.containsKey("app_msg_ext_info")) {
	                            JSONObject app_msg_ext_info = j.getJSONObject("app_msg_ext_info");
	                            JSONArray multi_app_msg_item_list = app_msg_ext_info.getJSONArray("multi_app_msg_item_list");
	                            if (multi_app_msg_item_list.size() > 0) {
	                                //多图文 do nothing
	                            } else {
	                                String content_url = app_msg_ext_info.getString("content_url");
	                                String title = app_msg_ext_info.getString("title");
	                                int copyright_stat = app_msg_ext_info.getInt("copyright_stat");
	                                String record = date + "-@@-" + title + "-@@-" + content_url;
	                                System.out.println(record);
	                                //datas.add(record);
	                                htmlToPdf(content_url,title,date); 
	                                
	                            }
	                        } else {
	                            System.out.println("非图文推送");
	                        }
	                    }
	                    
	                    // can_msg_continue 来判断是否还有下一页数据
	                    if (jo.getInt("can_msg_continue") == 1) {
	                        Thread.sleep(1000);
	                        startIndex = jo.getInt("next_offset");
	                       // execute();
	                    } else {
	                        System.out.println("爬取完成！");
	                        
	                        // 完成之后，保存结果
	                        //saveToFile();
	                    }

	                } else {
	                    System.out.println("无法获取文章，参数错误");
	                }
	            }  
	   System.out.println("html文件下载完成后开始HTML2pdf"); 
	   //convert(MyClass.htmlDirPath, MyClass.pdfDirPath);
	   
	       }

//提取字符串
     public static String getMatcher(String regex, String source) {  
         String result = "";  
         Pattern pattern = Pattern.compile(regex);  
         Matcher matcher = pattern.matcher(source);  
         while (matcher.find()) {  
        	
             result = matcher.group(1);
        	
         }  
         return result;  
     } 
     
//html解析  --替换网络图片为本地图片
public static void htmlToPdf(String url,String title,String date) throws IOException {
	OkHttpClient okHttpClient = new OkHttpClient(); // 创建OkHttpClient对象
	Request request = new Request.Builder().url(url).get().build();
	Response response = okHttpClient.newCall(request).execute();
	if (response.isSuccessful()) {
	    String html = response.body().string();
	    //                System.out.println(html);
	    
	    Document doc = Jsoup.parse(html);
	    
	    //找到图片标签
	    Elements img = doc.select("img");
	    for (int i = 0; i < img.size(); i++) {
	        // 图片地址
	        String imgUrl = img.get(i).attr("data-src");
	    
	        if (imgUrl != null && !imgUrl.equals("")) {
	            Request request2 = new Request.Builder()
	                    .url(imgUrl)
	                    .get()
	                    .build();
	    
	            Response execute = okHttpClient.newCall(request2).execute();
	            if (execute.isSuccessful()) {
	            	String imgDir="E:\\img\\";
	                String imgPath = imgDir + MD5Utils.MD5Encode(imgUrl, "") + ".png";
	                File imgFile = new File(imgPath);
	                if (!imgFile.exists()) {
	                    // 下载图片
	                    InputStream in = execute.body().byteStream();
	                    FileOutputStream ot = new FileOutputStream(new File(imgPath));
	                    BufferedOutputStream bos = new BufferedOutputStream(ot);
	                    byte[] buf = new byte[8 * 1024];
	                    int b;
	                    while ((b = in.read(buf, 0, buf.length)) != -1) {
	                        bos.write(buf, 0, b);
	                        bos.flush();
	                    }
	    
	                    bos.close();
	                    ot.close();
	                    in.close();
	                }
	    
	                //重新赋值为本地路径
	                img.get(i).attr("data-src", imgPath);
	                img.get(i).attr("src", imgPath);
	    
	                //导出 html
	                html = doc.outerHtml();
	            }
	    
	            execute.close();
	        }
	    }
	    
	    //去掉script标签及内容
//        String regex = "<script.*?>(.*?)</script>";
//        Pattern p_script = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
//        Matcher m_script = p_script.matcher(html);
//        html = m_script.replaceAll("");
	    //去掉换行标签
        String regex01 = "<p><br></p>";
        Pattern p_script01 = Pattern.compile(regex01, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m_script01 = p_script01.matcher(html);
        html = m_script01.replaceAll("");
 
        String regex02 = "<br>";
        Pattern p_script02 = Pattern.compile(regex02, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m_script02 = p_script02.matcher(html);
        html = m_script02.replaceAll("");
        
	    String htmlPath = MyClass.htmlDirPath + date +"-" +title + ".html";
	    final File f = new File(htmlPath);
	    if (!f.exists()) {
	        //Writer writer = new FileWriter(f);
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf-8"));
	        bw.write(html);
	    
	        bw.close();
	        //writer.close();
	    }
	    
	    // 转换
	    //convert(htmlPath, destPath);
	    
	    // 删除html文件
//	    if (f.exists()) {
//	        f.delete();
//	    }

	    response.close();
	}
}
/**
 * html转pdf
 */
private static final String toPdfCssFangSong = "D:\\workspace\\weChatCh\\css\\style_fangsong.css";
public static boolean convert(String htmlDirPath, String pdfDirPath) {
	List<InputStream> inputStreamList = new ArrayList<InputStream>();
	List<InputStream> errorStreamList = new ArrayList<InputStream>();
    
    boolean result = true;
    List<String> comList=new ArrayList<String>();
	SearchFile sf=new SearchFile();
	List<String> filenameList=sf.getFilenameList(htmlDirPath);
	
	 String pageSize = "A4";
	
	for (String fl: filenameList) {
		StringBuilder cmd = new StringBuilder();	
	    cmd.append("wkhtmltopdf");
	    cmd.append(" ");
	    cmd.append("--enable-local-file-access --allow");
	    cmd.append(" ");
	    cmd.append("--enable-plugins");
	    cmd.append(" ");
	    cmd.append("--enable-forms");
	    cmd.append(" ");
	    
	    cmd.append("--user-style-sheet ");
	    cmd.append(toPdfCssFangSong).append(" ");//为生成的 pdf 文档设置自定义样式，非常重要！！！  
	    cmd.append("--disable-smart-shrinking ");//这个参数一定要加上，加上页面就不缩小了，会正常比例显示
	    
	    cmd.append(" \"");
	    cmd.append(htmlDirPath+fl+".html");
	    cmd.append("\" ");
	    cmd.append(" ");
	    cmd.append(pdfDirPath+fl+".pdf");
	    System.out.println(cmd.toString());
	    comList.add(cmd.toString());

	}  
    	HtmlToPdfInterceptorThread outputCountDownLatch = new HtmlToPdfInterceptorThread();
    	//HtmlToPdfInterceptorThread errorCountDownLatch = new HtmlToPdfInterceptorThread();
    	try {
			outputCountDownLatch.doRun01(comList);
	    	//errorCountDownLatch.doRun(errorStreamList);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    return result;
}
}