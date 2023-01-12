/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.strasbourg.zerr.projetEnchere.FX.composants;

import fr.insa.strasbourg.zerr.projetEnchere.FX.JavaFXUtils;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.imageio.ImageIO;

/**
 *
 * @author jules
 */
public class VueImage extends BorderPane {

    private ImageView contentImage;
    private StackPane contentPane;
    private double width;
    private Image image;

    public VueImage() throws IOException {
        this.setId("vue-image");
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.width = gd.getDisplayMode().getWidth() / 2;
        int height = gd.getDisplayMode().getHeight() / 2;
        this.contentPane = new StackPane();
        this.contentPane.setPrefSize(width, width);
        this.getChildren().size();
        this.setCenter(this.contentPane);
        try {
            setDefautImage();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VueImage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public StackPane getContentPane() {
        return contentPane;
    }

    public void setContentPane(StackPane contentPane) {
        this.contentPane = contentPane;
    }

    void addImage(Image i, StackPane pane) {

        contentImage = new ImageView();
        contentImage.setImage(i);
        contentImage.setFitHeight(width);
        contentImage.setFitWidth(width);
        pane.getChildren().add(contentImage);
    }
    void addDefautImage(Image i, StackPane pane) throws IOException {

        contentImage = new ImageView();
        contentImage.setImage(i);
        contentImage.setFitHeight(200);
        contentImage.setFitWidth(200);
        pane.getChildren().add(contentImage);
    }
    
    public void mouseDragDropped(final DragEvent e) {
        Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            // Only get the first file from the list
            final File file = db.getFiles().get(0);
            Platform.runLater(new Runnable() {
                private Image image;
                @Override
                public void run() {
                    System.out.println(file.getAbsolutePath());
                    try {
                        if (!contentPane.getChildren().isEmpty()) {
                            contentPane.getChildren().remove(0);
                        }
                        
                        Image img = new Image(new FileInputStream(file.getAbsolutePath())); 
                        setImage(img);
                        System.out.println("on est la");
                        addImage(img, contentPane);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(VueImage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(VueImage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        e.setDropCompleted(success);
        e.consume();
    }
    public void mouseClicked(File file) {
            // Only get the first file from the list
            Platform.runLater(new Runnable() {
                private Image image;
                @Override
                public void run() {
                    System.out.println(file.getAbsolutePath());
                    try {
                        if (!contentPane.getChildren().isEmpty()) {
                            contentPane.getChildren().remove(0);
                        }
                        
                        Image img = new Image(new FileInputStream(file.getAbsolutePath())); 
                        setImage(img);
                        System.out.println("on est la");
                        addImage(img, contentPane);
                        this.image = img;
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(VueImage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(VueImage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
  
    }
    public Image getImage(){
        return this.image;
    }
     public static Image texteEnImage(String img) throws IOException {
        byte[] result = Base64.getUrlDecoder().decode(img);
        ByteArrayInputStream bis = new ByteArrayInputStream(result);
        BufferedImage bImage2 = ImageIO.read(bis);
        Image Final = SwingFXUtils.toFXImage(bImage2, null);
        return Final;
    }

    public void mouseDragOver(final DragEvent e) {
        Dragboard db = e.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

        if (db.hasFiles()) {
            if (isAccepted) {
                contentPane.setStyle("-fx-border-color: red;"
                        + "-fx-border-width: 5;"
                        + "-fx-background-color: #C6C6C6;"
                        + "-fx-border-style: solid;");
                e.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            e.consume();
        }
    }

    public static String ImageEnTexte(Image img) {

        BufferedImage bufi = SwingFXUtils.fromFXImage(img, null);

        System.out.println("1");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufi, "jpg", baos);
            System.out.println("2");
        } catch (IOException ex) {
            throw new Error("pb conv image ; ne devrait pas arriver");
        }
        byte[] bytes = baos.toByteArray();
        System.out.println("3");
        String ImageTexte = Base64.getUrlEncoder().encodeToString(bytes);
        return ImageTexte;
    }


    private void setDefautImage() throws FileNotFoundException, IOException {
        //FileInputStream input = new FileInputStream("addImage.png");
        InputStream is = this.getClass().getResourceAsStream("ressources/addImage.png");
        Image image = new Image(is);
        addDefautImage(image, contentPane);
    }
    

    public void setImage(Image image) {
        this.image = image;
    }

    public void setOnMouseClicked() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
