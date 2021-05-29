import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class watchSocket {



    public static volatile long shareTime = 0;
    public static volatile Signal flag = Signal.Start;
    public static void main(String args[]) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        socketLoop s1 = new socketLoop(6000,shareTime,flag);
        socketLoop s2 = new socketLoop(5000,shareTime,flag);
        threadPoolExecutor.execute(s1);
        threadPoolExecutor.execute(s2);

    }
}
