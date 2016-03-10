package infx141asst3;/**
 * Created by blenz on 3/8/16.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

public class Browser extends Application
{

    Stage window;
    Scene scene;
    Button searchButton;
    Indexer indexer;
    TextField searchTextField;
    WebView webView;
    WebEngine webEngine;
    javafx.scene.control.Label label;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        indexer = new Indexer();
        indexer.read("index.ser");

        window = stage;
        window.setTitle("Browser");

        //Layout
        BorderPane border = new BorderPane();
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(20, 20, 20, 20));
        border.setTop(hBox);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        border.setCenter(vBox);

        //Search Bar / Button
        searchTextField = new TextField();
        searchButton = new Button("Search");
        searchTextField.setPrefWidth(1000);
        searchButton.setOnAction(event -> search());
        label = new Label("Status: ");
        label.setPadding(new Insets(20, 20, 20, 20));
        border.setBottom(label);

        //WebView
        webView = new WebView();
        webEngine = webView.getEngine();
        webView.isVisible();


        vBox.getChildren().addAll(webView);
        hBox.getChildren().addAll(searchTextField, searchButton);
        scene = new Scene(border, 1200, 600);
        window.setScene(scene);
        stage.setResizable(false);
        window.show();

        System.out.println(searchTextField.getText());
    }

    public void search()
    {
        try
        {
            ArrayList<Pair> searchQuery = indexer.searchResults(searchTextField.getText().split(" "));
            String URL = Indexer.docIdToURL(searchQuery.get(0).getKey().toString());
            searchTextField.setText(URL);
            label.setText("Status: FOUND PAGE");
            label.setTextFill(javafx.scene.paint.Paint.valueOf("Green"));
            webEngine.load(URL);
        }
        catch (Exception e)
        {
            label.setText("Status: NOTHING FOUND");
            label.setTextFill(javafx.scene.paint.Paint.valueOf("Red"));
        }
    }
}
