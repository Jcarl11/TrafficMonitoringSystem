package Utilities;

import com.jfoenix.controls.JFXSnackbar;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class GlobalObjects 
{
    private GlobalObjects(){}
    private static GlobalObjects instance = null;
    public static GlobalObjects getInstance()
    {
        if(instance == null)
            instance = new GlobalObjects();
        return instance;
    }
    public ScheduledExecutorService timer;
    public VideoCapture videoCapture;
    public Image mat2Image(Mat frame)
    {
        try
        {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        }
        catch (Exception e)
        {
            System.err.println("Cannot convert the Mat obejct: " + e);
            return null;
        }
    }
    public BufferedImage matToBufferedImage(Mat original)
    {
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1)
        {
                image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        }
        else
        {
                image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }
    public void stopCamera(VideoCapture videoCapture)
    {
        if(videoCapture != null)
        {
            if (videoCapture.isOpened())
                videoCapture.release();
        }
        
    }
    public void shutdownScheduledExecutor(ScheduledExecutorService timer)
    {
        if (timer!=null && !timer.isShutdown())
        {
            try
            {
                timer.shutdown();
                timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e)
            {
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }
    }
    
    public String showChooserDialog(String... acceptableFileTypes)
    {
        String path = new String();
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new FileNameExtensionFilter("Files",acceptableFileTypes));
        chooser.showOpenDialog(null);
        File selectedFile = chooser.getSelectedFile();
        if(selectedFile != null)
        {
            path = selectedFile.getAbsolutePath();
        }
        return path;
    }
    
    public void showMessage(String message,Pane container)
    {
        JFXSnackbar snackbar = new JFXSnackbar(container);
        snackbar.show(message.trim(), 3000);
    }
}
