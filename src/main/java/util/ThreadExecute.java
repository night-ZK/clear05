package util;

import cn.hutool.core.thread.ThreadUtil;
import service.ClearService;
import window.common.error.ErrorWindow;
import window.common.info.InfoWindow;
import window.vanish.VanishWindow;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zk
 */
public class ThreadExecute {

    static ThreadPoolExecutor threadPoolExecutor;

    static {
//        threadPoolExecutor = ThreadUtil.newExecutor(50, 100);
        threadPoolExecutor = new ThreadPoolExecutor(1000, 2000, 6L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3000));
    }

    public static void createThreadForSqlByUserIds(List userIds, int cutLength) {

        if (cutLength <= 0) {
            throw new RuntimeException("切割长度数值错误");
        }
        int paragraph = userIds.size() / cutLength;
        int remainder = userIds.size() % cutLength;
        System.out.println( "paragraph: " + paragraph + ", remainder: " + remainder);
        if (paragraph > 1 || (paragraph == 1 && remainder > 0)) {
            int paragraphCount = paragraph;
            if (remainder > 0) {
                paragraphCount += 1;
            }
            CyclicBarrier barrier = new CyclicBarrier(paragraphCount, () -> {
                new InfoWindow("执行完毕, 可以关闭本程序").setVisible(true);
                VanishWindow.getVanishWindow().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                VanishWindow.getVanishWindow().getInfoWindowForCloseTip().dispose();
            });

            for (int i = 0; i < paragraph; i++) {
                List subList = userIds.subList(i * cutLength, (i + 1) * cutLength);
//                int finalI = i;
                threadPoolExecutor.execute(() -> {
                    try {
                        ClearService.delUseData(subList);
//                        new InfoWindow( "线程 " + finalI + " 执行完毕，请等待可关闭通知").setVisible(true);
                        System.out.println(Thread.currentThread().getId() + "执行完毕");
                        barrier.await();
                    } catch (Exception e) {
                        new ErrorWindow(e.getMessage()).setVisible(true);
                        e.printStackTrace();
                    }
                });
            }
            if (userIds.size() % cutLength > 0) {
//                int finalParagraphCount = paragraphCount;
                List subList = userIds.subList(paragraph * cutLength, userIds.size() - 1);
                threadPoolExecutor.execute(() -> {
                    try {
                        ClearService.delUseData(subList);
                        barrier.await();
//                        new InfoWindow( "线程 " + finalParagraphCount + " 执行完毕，请等待可关闭通知").setVisible(true);
                    } catch (Exception e) {
                        new ErrorWindow(e.getMessage()).setVisible(true);
                        e.printStackTrace();
                    }
                });
            }
        } else {
            threadPoolExecutor.execute(() -> {
                try {
                    ClearService.delUseData(userIds);
                    System.out.println("执行完毕, 可以关闭本程序");
                    new InfoWindow( "执行完毕, 可以关闭本程序").setVisible(true);
                    VanishWindow.getVanishWindow().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    VanishWindow.getVanishWindow().getInfoWindowForCloseTip().dispose();
                } catch (Exception e) {
                    new ErrorWindow(e.getMessage()).setVisible(true);
                    e.printStackTrace();
                }
            });
        }
//        threadPoolExecutor.
    }
}
