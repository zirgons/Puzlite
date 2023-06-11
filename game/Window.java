/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package game15;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author tkazaks
 */
public class Window extends javax.swing.JPanel {

    int rows = 4;
    int columns = 4;
    BufferedImage imgs[][];
    int board[][];
    int subimage_Width;
    int subimage_Height;
    int removedX;
    int removedY;
    double aspectRatio;
    
    
    
    public Window(int rows, int columns, String imgPath) {
        initComponents();
        try {
            createGrid(rows, columns, imgPath);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createGrid(int rows, int columns, String imgPath) throws FileNotFoundException, IOException{
        this.rows = rows;
        this.columns = columns;
        removedX = columns-1;
        removedY = rows-1;
        File file = new File(imgPath);
        FileInputStream sourceFile = new FileInputStream(file);
        BufferedImage image = ImageIO.read(sourceFile);
        aspectRatio = image.getWidth()/(double)image.getHeight();
        imgs = new BufferedImage[columns][rows];
        board = new int[columns][rows];
        subimage_Width = image.getWidth() / columns;
        subimage_Height = image.getHeight() / rows;
        
        int currImage = 0;
        // iterating over rows and columns for each sub-image
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                
                // Creating sub image
                imgs[j][i] = new BufferedImage(subimage_Width, subimage_Height, image.getType());
                board[j][i] = currImage;
                Graphics2D img_creator = imgs[j][i].createGraphics();

                // coordinates of source image
                int src_first_x = subimage_Width * j;
                int src_first_y = subimage_Height * i;

                // coordinates of sub-image
                int dst_corner_x = subimage_Width * j + subimage_Width;
                int dst_corner_y = subimage_Height * i + subimage_Height;
                
                img_creator.drawImage(image, 0, 0, subimage_Width, subimage_Height, src_first_x, src_first_y, dst_corner_x, dst_corner_y, null);
                currImage++;
            }
        }
    }
    
    public void render(Graphics g){
        
        
        
        int width = getWidth()/rows;
        int height = getHeight()/columns;
        if(getWidth()<getHeight()*aspectRatio){
            width = getWidth()/columns;
            height=(int)(getWidth()/aspectRatio/rows);
        }else{
            height = (int)(getHeight()*aspectRatio/rows);
            width=(int)(getHeight()*aspectRatio/columns);
        }
        
        int xOffset = (getWidth()-width*columns)/2;
        int yOffset = (getHeight()-height*rows)/2;
        
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if(removedX==j && removedY ==i)
                    continue;
                g.drawImage(imgs[j][i], xOffset+j*width, yOffset+i*height, width-1, height-1, this);
            }
        }
        
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        render(g);
    }
    
    public boolean moveRight(){
        if(!(removedX>0)) return true;
        
        switchImage(removedX, removedY, removedX-1, removedY);
        removedX-=1;
        repaint();
        return true;
        
    }
    
    public boolean moveLeft(){
        if(!(removedX<columns-1))return false;
        
        switchImage(removedX, removedY, removedX+1, removedY);
        removedX+=1;
        repaint();
        return true;
    }
    
    public boolean moveDown(){
        if(!(removedY>0))return false;
        
        switchImage(removedX, removedY, removedX, removedY-1);
        removedY-=1;
        
        repaint();
        return true;
    }
    
    public boolean moveUp(){
        if(!(removedY<rows-1))return false;
        
        switchImage(removedX, removedY, removedX, removedY+1);
        removedY+=1;
        
        repaint();
        return true;
    }
    
    public void switchImage(int x1, int y1, int x2, int y2){
        BufferedImage image = imgs[x1][y1];
        int num = board[x1][y1];
        imgs[x1][y1] = imgs[x2][y2];
        board[x1][y1] = board[x2][y2];
        imgs[x2][y2] = image;
        board[x2][y2] = num;
        
    }

    public void shuffle(){
        int n = rows*50*columns*50;
        Random rand = new Random(); 
        for(int i=0;i<n;i++){
            switch(rand.nextInt(4)){
                case 0:
                    if (!moveLeft()) i--;
                    break;
                case 1:
                    if (!moveRight()) i--;
                    break;
                case 2:
                    if (!moveUp()) i--;
                    break;
                case 3:
                    if (!moveDown()) i--;
                    break;
            }
        }
        for(int i=0;i<rows; i++){
            moveUp();
        }
        for(int i=0;i<columns; i++){
            moveLeft();
        }
        if(isSolved()) shuffle();
    }
    
    public boolean isSolved(){
        int currNum = 0;
        for(int i =0; i<rows;i++){
            for(int j = 0; j<columns;j++){
                if(board[j][i] == currNum){
                    currNum++;
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName(""); // NOI18N
        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
