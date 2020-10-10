/**
 * 
 */
package weChatCh;

import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 *
 */
public class CountDownLatchThread {
	

	    class Worker implements Runnable{

	        CountDownLatch countDownLatch;

	        Worker(CountDownLatch countDownLatch){
	            this.countDownLatch = countDownLatch;
	        }

	        @Override
	        public void run() {
	            try {
	                countDownLatch.await(); // 等待其它线程
	                System.out.println(Thread.currentThread().getName() + "启动@" + System.currentTimeMillis());
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    public void doRun() throws InterruptedException {
	        final int N = 5; // 线程数
	        CountDownLatch countDownLatch = new CountDownLatch(N);
	        for(int i=0;i<N;i++){
	            new Thread(new Worker(countDownLatch)).start();
	            countDownLatch.countDown();
	        }
	    }
//
//	    public static void main(String[] args) throws InterruptedException {
//	        TestCountDownLatch testCountDownLatch = new TestCountDownLatch();
//	        testCountDownLatch.doTest();
//	    }
}	
