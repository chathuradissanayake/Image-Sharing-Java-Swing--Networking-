import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
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

        JLabel jLabelPic = new JLabel();
        JButton jButton = new JButton("Send Image to Server");

        jFrame.setLayout(new FlowLayout());
        jFrame.add(jLabelPic);
        jFrame.add(jButton);

        jFrame.setVisible(true);

        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose Image");

                // Set file filter for JPEG and PNG images
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(filter);

                int userSelection = fileChooser.showOpenDialog(jFrame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage bufferedImage = ImageIO.read(selectedFile);

                        int newWidth = 200;
                        int originalWidth = bufferedImage.getWidth();
                        int originalHeight = bufferedImage.getHeight();
                        int newHeight = (int) ((double) newWidth / originalWidth * originalHeight);

                        // Resize the image
                        Image scaledImage = bufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        jLabelPic.setIcon(imageIcon);

                        OutputStream outputStream = socket.getOutputStream();
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

                        // Write image to output stream based on file extension
                        String extension = getFileExtension(selectedFile.getName());
                        ImageIO.write(bufferedImage, extension, bufferedOutputStream);

                        bufferedOutputStream.close();
                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Method to get file extension
    private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0 && index < fileName.length() - 1) {
            return fileName.substring(index + 1).toLowerCase();
        }
        return "";
    }
}
