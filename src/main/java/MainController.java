import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class MainController {
    @FXML
    public TextArea fileContent;

    @FXML
    public TextField searchContent;

    public File file;
    boolean save = false;

    void save() {
        if (file == null || !file.exists()){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose to save");
            file = fileChooser.showSaveDialog(null);
            if (file != null && file.exists()){
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(fileContent.getText().getBytes());
                    out.flush();
                    out.close();
                    save = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(fileContent.getText().getBytes());
                out.flush();
                out.close();
                save = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onMenuNew(javafx.event.ActionEvent actionEvent) throws Exception {
        if (!save && !fileContent.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Tip");
            alert.setHeaderText("You have not save this file. ");
            alert.setContentText("Do you want to save? ");
            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeTwo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                save();
            } else if (result.get() == buttonTypeTwo) {
                fileContent.setText("");
                file = null;
            }
        }else {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
            Scene scene = new Scene(root, 600, 500);
            Stage newStage = new Stage();
            DateFormat date = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
            EventHandler<ActionEvent> eventHandler = e -> {
                newStage.setTitle(date.format(new Date()));
            };
            Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.play();
            newStage.setScene(scene);
            newStage.show();
            file = null;
        }
    }

    @FXML
    public void onMenuOpen(ActionEvent actionEvent) {
        FileChooser fileChooser  = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        if (file != null && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] content = new byte[(int)file.length()];
                fileInputStream.read(content);
                fileContent.setText(new String(content));
                fileInputStream.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Your action was wrong!" ,"error",  JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    @FXML
    public void onMenuSave(ActionEvent actionEvent) {
        save();
    }

    @FXML
    public void onMenuPrint(ActionEvent actionEvent) {
        PrinterJob job = PrinterJob.getPrinterJob();
        try {
            boolean a = job.printDialog();
            if(a) {
                job.setPrintable(new Printable() {
                    @Override
                    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                        if(pageIndex>0){return Printable.NO_SUCH_PAGE;}
                        return Printable.PAGE_EXISTS;}
                });
                job.print();
            }else{job.cancel();}
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onMenuAbout(javafx.event.ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(null, "NZ194 Zi Wei\nNZ194 Xilin Xu" ,"About",  JOptionPane.PLAIN_MESSAGE);
    }

    @FXML
    public void onMenuExit(javafx.event.ActionEvent actionEvent) {
        System.exit(0);
    }

    public int index;
    public String content;
    public String search_content;
    @FXML
    public void onButtonSearch(ActionEvent actionEvent) {
        content = fileContent.getText();
        search_content = searchContent.getText();
        index = content.indexOf(search_content);
        fileContent.selectRange(index, index+search_content.length());
        index = index + search_content.length();
    }

    @FXML
    public void onButtonNext(ActionEvent actionEvent) {
        search_content = searchContent.getText();
        String content1 = content.substring(index);
        int index1 = content1.indexOf(search_content);
        index = index + index1;
        fileContent.selectRange(index, index+search_content.length());
        index = index + search_content.length();
    }
}
