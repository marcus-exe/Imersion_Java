import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class StickerGenerator {
    public void create(InputStream inputStream, String fileName, String text, InputStream inputStream2) throws Exception{
        //read image

        BufferedImage originalImage = ImageIO.read(inputStream);
        BufferedImage emoji = ImageIO.read(inputStream2);
        
        //create new image
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int newHeight = height + 200;
        BufferedImage newImage = new BufferedImage(width, newHeight, BufferedImage.TRANSLUCENT);


        //copy the new image to image in memory
        Graphics2D graphics = (Graphics2D) newImage.getGraphics();
        graphics.drawImage(originalImage, 0, 0, null);
        int YPosition = newHeight - emoji.getHeight();
        graphics.drawImage(emoji, 0, YPosition, null);

        //Set Font
        var font = new Font("Impact", Font.BOLD, 64);
        graphics.setColor(Color.YELLOW);
        graphics.setFont(font);



        //write a comic text   
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle2D rectangle = fontMetrics.getStringBounds(text, graphics);
        int textWidth = (int) rectangle.getWidth();
        int positionX = ((width - textWidth)/ 2);
        int positionY = newHeight - 100;
        graphics.drawString(text, positionX, positionY);
        
        //change font and Add Outline
        FontRenderContext fontRenderContext = graphics.getFontRenderContext();
        var textLayout = new TextLayout(text, font, fontRenderContext);
        Shape outline = textLayout.getOutline(null);

        AffineTransform transform = graphics.getTransform();
        transform.translate(positionY, positionX);
        graphics.setTransform(transform);

        BasicStroke basicStroke = new BasicStroke(width * 0.01f);
        graphics.setStroke(basicStroke);
        graphics.setColor(Color.BLACK);
        graphics.draw(outline);
        graphics.setClip(outline);

        //write the image inside a file
        ImageIO.write(newImage, "png", new File("AluraStickers/output/" + fileName + ".png"));

    };

}
