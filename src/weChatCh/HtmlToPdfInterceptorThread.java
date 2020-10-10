/**
 * 
 */
package weChatCh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import weChatCh.CountDownLatchThread.Worker;

/**
 * @author Administrator
 *
 */
public class HtmlToPdfInterceptorThread{
	 class HtmlToPdfInterceptor extends Thread {
	
		private List<InputStream> is;
		private List<String> tr=new ArrayList<String>();;
		private CountDownLatch countDownLatch;
		private int stepNum;/*步长*/
		private int startIndex;/*起始值*/
		
		
		public HtmlToPdfInterceptor(CountDownLatch countDownLatch) {
		    this.countDownLatch = countDownLatch;
		}

		public void setInputStream(List<InputStream> is)
	    {
	        this.is = is;
	    }
		public void setStepNum(int stepNum)
	    {
	        this.stepNum = stepNum;
	    }
		public void setStartIndex(int startIndex)
	    {
	        this.startIndex = startIndex;
	    }
		public void setTr(List<String> tr)
	    {
	        this.tr = tr;
	    }	
		
		@Override
		public void run() {
			System.out.println("当前线程 参数 "+"startIndex= "+startIndex); //输出内容
			readInputStream01(tr,stepNum,startIndex);
		}
	}
	    public void doRun(List<InputStream> inputStreamList) throws InterruptedException {
	        final int N = 5; // 线程数
	        int stepNum=(int) Math.ceil(inputStreamList.size()/5);//任务列表的步长
	        int startIndex=0;
	        CountDownLatch countDownLatch = new CountDownLatch(N);
	        for(int i=0;i<N;i++){
	        	System.out.println("线程  "+i+" 状态:启动--执行--"); //输出内容
	        	System.out.println("当前线程  "+i+" 运行参数：stepNum="+stepNum+" ,startIndex="+startIndex+"..."); //输出内容
	        	HtmlToPdfInterceptor htmlToPdfInterceptor=new HtmlToPdfInterceptor(countDownLatch);
	        	htmlToPdfInterceptor.setInputStream(inputStreamList);
	        	htmlToPdfInterceptor.setStepNum(stepNum);
	        	htmlToPdfInterceptor.setStartIndex(startIndex);
	        	Thread thread=new Thread(htmlToPdfInterceptor);
	        	thread.start(); 	        			            	            
	        	countDownLatch.countDown();	
	        	startIndex=startIndex+stepNum;
	        	System.out.println("线程  "+i+" 状态:完成--执行--"); //输出内容
	        }
	    } 
	    public void doRun01(List<String> comList) throws InterruptedException {
	        final int N = 3; // 线程数
	        int stepNum=(int) Math.round(comList.size()/N)+1;//任务列表的步长
	        int startIndex=0;
	        CountDownLatch countDownLatch = new CountDownLatch(N);
	        for(int i=0;i<N;i++){
	        	System.out.println("线程  "+i+" 状态:启动--执行--"); //输出内容
	        	System.out.println("当前线程  "+i+" 运行参数：stepNum="+stepNum+" ,startIndex="+startIndex+"..."); //输出内容
	        	
	        	HtmlToPdfInterceptor htmlToPdfInterceptor=new HtmlToPdfInterceptor(countDownLatch);
	        	htmlToPdfInterceptor.setTr(comList);
	        	htmlToPdfInterceptor.setStepNum(stepNum);
	        	htmlToPdfInterceptor.setStartIndex(startIndex);
	        	Thread thread=new Thread(htmlToPdfInterceptor);
	        	thread.start(); 	        			            	            
	        	countDownLatch.countDown();	
	        	startIndex=startIndex+stepNum;
	        	System.out.println("线程  "+i+" 状态:完成--执行--"); //输出内容
	        }
	    }	    
	    public void readInputStream(List<InputStream> inputStreamList,int stepNum/*步长*/,int startIndex/*起始值*/) {
        	for (int j = startIndex; j < stepNum; j++) {		
        		if (j>=inputStreamList.size()) {
        			break; 
				}
    		    try {
    		        InputStreamReader isr = new InputStreamReader(inputStreamList.get(j), "utf-8");
    		        BufferedReader br = new BufferedReader(isr);
    		        String line = null;
    		        while ((line = br.readLine()) != null) {
    		            System.out.println(line.toString()); //输出内容
    		        }
    		    } catch (IOException e) {
    		        e.printStackTrace();
    		    }
        	}
	    	
	    }
	    public void readInputStream01(List<String> comList,int stepNum/*步长*/,int startIndex/*起始值*/) {
        	for (int j = startIndex; j < startIndex+stepNum; j++) {		
        		if (j>=comList.size()) {
        			break; 
				}       		
    		    try {
            		Process proc = Runtime.getRuntime().exec(comList.get(j));
            		System.out.println("执行命令：开始 "+comList.get(j)); //输出内容
    		        InputStreamReader isr = new InputStreamReader(proc.getInputStream(), "utf-8");
    		        InputStreamReader errorinput = new InputStreamReader(proc.getErrorStream(), "utf-8");
    		        BufferedReader br = new BufferedReader(isr);
    		        BufferedReader errorbr = new BufferedReader(errorinput);
    	            Thread.sleep(1000);
    		        br.close();
    		        errorbr.close();
//    		        String line = null;
//    		        while ((line = br.readLine()) != null) {
//    		            System.out.println(line.toString()); //输出内容
//    		        }
//    		        while ((line = errorbr.readLine()) != null) {
//    		            System.out.println(line.toString()); //输出内容
//    		        }
    		        proc.waitFor();
    		        System.out.println("执行命令：结束。。。"); //输出内容
    		    } catch (IOException | InterruptedException  e) {
    		        System.out.println("阻塞超时处理。。。"); //输出内容
    		    }
        	}
	    	
	    }	    
//	    public static void main(String[] args) throws InterruptedException, IOException {
//	    	StringBuilder cmd = new StringBuilder();
//	    	List<InputStream> inputStreamList = new ArrayList<InputStream>();
//	    	cmd.append("wkhtmltopdf --enable-local-file-access --allow --enable-plugins --enable-forms  \"E:\\学习\\微信公众号文章\\复利人生\\HTML\\20200915-今天，分析一只龙头股.html\"  E:\\学习\\微信公众号文章\\复利人生\\PDF\\20200915-今天，分析一只龙头股.pdf");
//	    	Process proc = Runtime.getRuntime().exec(cmd.toString());
//	    	inputStreamList.add(proc.getInputStream());
//	    	HtmlToPdfInterceptorThread testCountDownLatch = new HtmlToPdfInterceptorThread();
//	    	testCountDownLatch.doRun(inputStreamList);
//	    	
//    }	    
}