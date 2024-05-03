import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        System.out.println("Connected to server");

        JFrame jFrame = new JFrame("Client");
        jFrame.setSize(400, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon imageIcon = new ImageIcon("E:\\Self Study\\Image Sharing - Networking\\Images\\Teddy-Bear.jpg");

        JLabel jLabelPic = new JLabel(imageIcon);
        JButton jButton = new JButton("Send Image to Server");

        jFrame.add(jLabelPic);
        jFrame.add(jButton);

        jFrame.setVisible(true);
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

                    Image image = imageIcon.getImage();
                    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

                    Graphics graphics = bufferedImage.createGraphics();
                    graphics.drawImage(image, 0, 0, null);
                    graphics.dispose();

                    ImageIO.write(bufferedImage, "jpg",bufferedOutputStream);
                    bufferedOutputStream.close();
                    socket.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        );
    }
}
