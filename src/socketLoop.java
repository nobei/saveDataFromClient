import javax.imageio.IIOException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

public class socketLoop implements Runnable {

    private int sockNum;
    private ServerSocket socket;
    private String kind;
    private static volatile long shareTime = 0;
    private static volatile Signal flag = null;

    public  socketLoop(int num, long shareTime, Signal flag) throws IOException {
        this.sockNum = num;
        socket = new ServerSocket(num);
        kind = readKind();
        this.shareTime = shareTime;
        this.flag =flag;
    }


    @Override
    public void run() {

        if (socket != null) {

            try {
                while (true) {

                    Socket socketData = socket.accept();
                    String kind1 = readKind();
                    InputStreamReader input = new InputStreamReader(socketData.getInputStream());
                    BufferedReader reader = new BufferedReader(input);
                    long time = System.currentTimeMillis();


                    if (flag == Signal.Start) {
                        shareTime = time;
                        flag = Signal.Wait;

                    }
                    else if(flag == Signal.Wait) {
                        time = shareTime;
                        flag = Signal.Start;
                    }



                    String path = "";
                    if(sockNum == 6000)
                        path = "src/audio/newAudio/haojiangshan/"+kind1+"/"+time + kind1 + ".txt";
                    else
                        path = "src/audio/newAudio/haojiangshan/"+kind1+"/"+time + "gyro" + ".txt";

                    FileWriter file = new FileWriter(path);
                    String length = reader.readLine();
                    while (length != null) {
                        file.write(length + "\n");
                        length = reader.readLine();
                    }

                    System.out.println(path+"hasSaved");

                    file.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String readKind() throws IOException {
        Properties properties = new Properties();
        File file = new File("src/config/config.properties");
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        properties.load(in);
        String kind = properties.getProperty(String.valueOf(this.sockNum));
        return kind.substring(1,kind.length()-1);

    }
}
