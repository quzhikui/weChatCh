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
		private int stepNum;/*����*/
		private int startIndex;/*��ʼֵ*/
		
		
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
			System.out.println("��ǰ�߳� ���� "+"startIndex= "+startIndex); //�������
			readInputStream01(tr,stepNum,startIndex);
		}
	}
	    public void doRun(List<InputStream> inputStreamList) throws InterruptedException {
	        final int N = 5; // �߳���
	        int stepNum=(int) Math.ceil(inputStreamList.size()/5);//�����б�Ĳ���
	        int startIndex=0;
	        CountDownLatch countDownLatch = new CountDownLatch(N);
	        for(int i=0;i<N;i++){
	        	System.out.println("�߳�  "+i+" ״̬:����--ִ��--"); //�������
	        	System.out.println("��ǰ�߳�  "+i+" ���в�����stepNum="+stepNum+" ,startIndex="+startIndex+"..."); //�������
	        	HtmlToPdfInterceptor htmlToPdfInterceptor=new HtmlToPdfInterceptor(countDownLatch);
	        	htmlToPdfInterceptor.setInputStream(inputStreamList);
	        	htmlToPdfInterceptor.setStepNum(stepNum);
	        	htmlToPdfInterceptor.setStartIndex(startIndex);
	        	Thread thread=new Thread(htmlToPdfInterceptor);
	        	thread.start(); 	        			            	            
	        	countDownLatch.countDown();	
	        	startIndex=startIndex+stepNum;
	        	System.out.println("�߳�  "+i+" ״̬:���--ִ��--"); //�������
	        }
	    } 
	    public void doRun01(List<String> comList) throws InterruptedException {
	        final int N = 3; // �߳���
	        int stepNum=(int) Math.round(comList.size()/N)+1;//�����б�Ĳ���
	        int startIndex=0;
	        CountDownLatch countDownLatch = new CountDownLatch(N);
	        for(int i=0;i<N;i++){
	        	System.out.println("�߳�  "+i+" ״̬:����--ִ��--"); //�������
	        	System.out.println("��ǰ�߳�  "+i+" ���в�����stepNum="+stepNum+" ,startIndex="+startIndex+"..."); //�������
	        	
	        	HtmlToPdfInterceptor htmlToPdfInterceptor=new HtmlToPdfInterceptor(countDownLatch);
	        	htmlToPdfInterceptor.setTr(comList);
	        	htmlToPdfInterceptor.setStepNum(stepNum);
	        	htmlToPdfInterceptor.setStartIndex(startIndex);
	        	Thread thread=new Thread(htmlToPdfInterceptor);
	        	thread.start(); 	        			            	            
	        	countDownLatch.countDown();	
	        	startIndex=startIndex+stepNum;
	        	System.out.println("�߳�  "+i+" ״̬:���--ִ��--"); //�������
	        }
	    }	    
	    public void readInputStream(List<InputStream> inputStreamList,int stepNum/*����*/,int startIndex/*��ʼֵ*/) {
        	for (int j = startIndex; j < stepNum; j++) {		
        		if (j>=inputStreamList.size()) {
        			break; 
				}
    		    try {
    		        InputStreamReader isr = new InputStreamReader(inputStreamList.get(j), "utf-8");
    		        BufferedReader br = new BufferedReader(isr);
    		        String line = null;
    		        while ((line = br.readLine()) != null) {
    		            System.out.println(line.toString()); //�������
    		        }
    		    } catch (IOException e) {
    		        e.printStackTrace();
    		    }
        	}
	    	
	    }
	    public void readInputStream01(List<String> comList,int stepNum/*����*/,int startIndex/*��ʼֵ*/) {
        	for (int j = startIndex; j < startIndex+stepNum; j++) {		
        		if (j>=comList.size()) {
        			break; 
				}       		
    		    try {
            		Process proc = Runtime.getRuntime().exec(comList.get(j));
            		System.out.println("ִ�������ʼ "+comList.get(j)); //�������
    		        InputStreamReader isr = new InputStreamReader(proc.getInputStream(), "utf-8");
    		        InputStreamReader errorinput = new InputStreamReader(proc.getErrorStream(), "utf-8");
    		        BufferedReader br = new BufferedReader(isr);
    		        BufferedReader errorbr = new BufferedReader(errorinput);
    	            Thread.sleep(1000);
    		        br.close();
    		        errorbr.close();
//    		        String line = null;
//    		        while ((line = br.readLine()) != null) {
//    		            System.out.println(line.toString()); //�������
//    		        }
//    		        while ((line = errorbr.readLine()) != null) {
//    		            System.out.println(line.toString()); //�������
//    		        }
    		        proc.waitFor();
    		        System.out.println("ִ���������������"); //�������
    		    } catch (IOException | InterruptedException  e) {
    		        System.out.println("������ʱ��������"); //�������
    		    }
        	}
	    	
	    }	    
//	    public static void main(String[] args) throws InterruptedException, IOException {
//	    	StringBuilder cmd = new StringBuilder();
//	    	List<InputStream> inputStreamList = new ArrayList<InputStream>();
//	    	cmd.append("wkhtmltopdf --enable-local-file-access --allow --enable-plugins --enable-forms  \"E:\\ѧϰ\\΢�Ź��ں�����\\��������\\HTML\\20200915-���죬����һֻ��ͷ��.html\"  E:\\ѧϰ\\΢�Ź��ں�����\\��������\\PDF\\20200915-���죬����һֻ��ͷ��.pdf");
//	    	Process proc = Runtime.getRuntime().exec(cmd.toString());
//	    	inputStreamList.add(proc.getInputStream());
//	    	HtmlToPdfInterceptorThread testCountDownLatch = new HtmlToPdfInterceptorThread();
//	    	testCountDownLatch.doRun(inputStreamList);
//	    	
//    }	    
}