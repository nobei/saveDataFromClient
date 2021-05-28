import javax.imageio.IIOException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class socketLoop implements Runnable {

    private int sockNum;
    private ServerSocket socket;
    private String kind;

    public socketLoop(int num) throws IOException {
        this.sockNum = num;
        socket = new ServerSocket(num);
        kind = readKind();
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
                    String path = "";
                    if(sockNum == 6000)
                        path = "src//audio/"+kind1+"/"+time + kind1 + ".txt";
                    else
                        path = "src/gyro/"+time + kind1 + ".txt";

                    FileWriter file = new FileWriter(path);
                    String length = reader.readLine();
                    while (length != null) {
                        file.write(length + "\n");
                        length = reader.readLine();
                    }

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
