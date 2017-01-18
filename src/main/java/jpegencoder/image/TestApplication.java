package jpegencoder.image;

import jpegencoder.image.colors.ColorChannel;
import jpegencoder.image.colors.rgb.RGBImage;
import jpegencoder.image.colors.ycbcr.YCbCrImage;
import jpegencoder.image.colors.ColorChannels;
import jpegencoder.image.colors.rgb.RGB;
import jpegencoder.image.colors.ycbcr.YCbCr;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.*;

/**
 * Created by Long Bui on 26.10.16.
 * E-Mail: giaolong.bui@student.fhws.de
 */
public class TestApplication extends Application
{
    public static void main(String[] args)
    {
        launch();
    }

    public void start(Stage primaryStage) throws Exception
    {
        BufferedImage img = readPpmAndConvertToYcbcr();
        Image image = SwingFXUtils.toFXImage(img, null);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(900);
        VBox verticalBox = new VBox();
        verticalBox.getChildren().add(imageView);
        Scene scene = new Scene(verticalBox, image.getWidth(), image.getHeight());
        primaryStage.setTitle("Test Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BufferedImage readPpmAndConvertToYcbcr()
    {
        RGBImage testPicture = null;
        try
        {
            testPicture = RGBImage.RGBImageBuilder.from(new FileInputStream(new File("test-pic.ppm"))).build();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        YCbCrImage yCbCrImage = ColorChannels.RGBToYCbCr(testPicture);
        yCbCrImage.reduce(1);
        long start = System.currentTimeMillis();
        BufferedImage img = new BufferedImage(yCbCrImage.getWidth(),
                                              yCbCrImage.getHeight(),
                                              BufferedImage.TYPE_BYTE_GRAY);
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < yCbCrImage.getHeight(); i++)
        {
            for (int j = 0; j < yCbCrImage.getWidth(); j++)
            {
                YCbCr pixel = yCbCrImage.getPixelAt(j, i);
                pixels[j + i * yCbCrImage.getWidth()] = (byte) pixel.getLuminanceChannel();
            }
        }
        System.out.println("Built image in "
                                   + ((System.currentTimeMillis() - start) / 1000d)
                                   + " seconds");
        return img;
    }

    // Aufgabe 1d) Demo
    private BufferedImage readPpmAndRender()
    {
        RGBImage testPicture = null;
        try
        {
            testPicture = RGBImage.RGBImageBuilder.from(new FileInputStream(new File("100.ppm"))).build();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        BufferedImage img = new BufferedImage(testPicture.getWidth(),
                                              testPicture.getHeight(),
                                              BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < testPicture.getHeight(); i++)
        {
            for (int j = 0; j < testPicture.getWidth(); j++)
            {
                RGB pixel = testPicture.getRGBAt(j, i);
                pixels[j + i * testPicture.getWidth()] = pixel.getRed() << 16
                        | pixel.getGreen() << 8
                        | pixel.getBlue();
            }
        }
        System.out.println("Built image in "
                                   + ((System.currentTimeMillis() - start) / 1000d)
                                   + " seconds");
        return img;
    }

    private BufferedImage blackAndWhiteColorChannel()
    {
        ColorChannel channel = new ColorChannel(1024, 1024);
        for (int y = 0; y < channel.getHeight(); y++)
        {
            for (int x = 0; x < channel.getWidth(); x++)
            {
                int value;
                value = (x + (y * 8)) % channel.getWidth();
                channel.setPixel(x, y, value);
            }
        }
        BufferedImage img = new BufferedImage(channel.getWidth(),
                                              channel.getHeight(),
                                              BufferedImage.TYPE_BYTE_GRAY);
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < channel.getHeight(); i++)
        {
            for (int j = 0; j < channel.getWidth(); j++)
            {
                double pixel = channel.getPixel(j, i);
                pixels[j + i * channel.getWidth()] = (byte) (int) pixel;
            }
        }
        return img;
    }
}