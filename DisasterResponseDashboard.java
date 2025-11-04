import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DisasterResponseDashboard extends Application {
    static class Shelter {
        String name;
        int capacity;
        int peopleAssigned;
        int foodPackets;
        int waterPackets;
        String route;
        Shelter(String name, int capacity, String route) {
            this.name = name;
            this.capacity = capacity;
            this.peopleAssigned = 0;
            this.foodPackets = 0;
            this.waterPackets = 0;
            this.route = route;
        }
        void reset() {
            this.peopleAssigned = 0;
            this.foodPackets = 0;
            this.waterPackets = 0;
        }
    }

    private final List<Shelter> shelters = new ArrayList<>();
    private TextArea publicOutputArea;
    private TextArea governmentLogArea;
    private Label govSuccessLabel;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #D6EAF8, #FCF3CF, #F9EBEA);");

        TabPane tabPane = new TabPane();

        Tab governmentTab = new Tab("Government Dashboard");
        governmentTab.setClosable(false);
        VBox governmentDashboard = createGovernmentDashboard();
        StackPane govWrapper = new StackPane(governmentDashboard);
        govWrapper.setAlignment(Pos.CENTER);
        governmentTab.setContent(govWrapper);

        Tab publicTab = new Tab("Public Dashboard");
        publicTab.setClosable(false);
        publicOutputArea = new TextArea();
        publicOutputArea.setEditable(false);
        publicOutputArea.setPrefHeight(400);
        publicOutputArea.setFont(Font.font("Arial", 16));
        publicOutputArea.setStyle("-fx-background-color: #FDFEFE; -fx-text-fill: #222; -fx-border-radius: 10;");

        Button emergencyBtn = new Button("Emergency");
        emergencyBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        emergencyBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #e53935, #ffb300);" +
            "-fx-text-fill: white; -fx-background-radius: 30;" +
            "-fx-effect: dropshadow(gaussian,rgba(255,0,0,0.8),6,0,0,2);"
        );

        FadeTransition glow = new FadeTransition(Duration.seconds(0.8), emergencyBtn);
        glow.setAutoReverse(true);
        glow.setCycleCount(FadeTransition.INDEFINITE);
        glow.setFromValue(1.0);
        glow.setToValue(0.6);
        glow.play();

        emergencyBtn.setOnAction(e -> {
            final AudioClip[] alarm = {null};
            try {
                AudioClip alarm = new AudioClip(new File("alertsound.mp3").toURI().toString());
    alarm.play();
} catch (Exception ex) {
    System.out.println("Alarm sound file not found");
                } else {
                    System.out.println("Warning: Alarm sound file not found");
                }
            } catch (Exception ex) {
                System.out.println("Warning: Alarm sound file not found");
            }

            Alert alert = new Alert(Alert.AlertType.WARNING,
                "Emergency reported! Officials have been notified for rescue.");
            alert.setHeaderText("Emergency Alert");
            alert.showAndWait();

            String logMsg = "\nEMERGENCY reported! Rescue team notified.\n";
            publicOutputArea.appendText(logMsg);
            governmentLogArea.appendText(logMsg);
        });

        Label assignmentLogLabel = new Label("Assignment Log:");
        assignmentLogLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));

        VBox publicBox = new VBox(18, assignmentLogLabel, publicOutputArea, emergencyBtn);
        publicBox.setPadding(new Insets(18));
        publicBox.setAlignment(Pos.CENTER);
        publicBox.setStyle("-fx-background-color: #F0FFF0; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian,#bbb,10,0,1,2);");

        StackPane publicWrapper = new StackPane(publicBox);
        publicWrapper.setAlignment(Pos.CENTER);
        publicTab.setContent(publicWrapper);

        tabPane.getTabs().addAll(governmentTab, publicTab);
        tabPane.setTabMinWidth(200);

        root.setCenter(tabPane);
        StackPane centerPane = new StackPane(tabPane);
        centerPane.setAlignment(Pos.CENTER);
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 950, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Disaster Response System - Dashboards");
        primaryStage.show();
    }

    private VBox createGovernmentDashboard() {
        VBox govBox = new VBox(20);
        govBox.setPadding(new Insets(30));
        govBox.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 24;");
        govBox.setAlignment(Pos.CENTER);

        Text title = new Text("Intelligent Disaster Response");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        title.setFill(Color.DARKSLATEBLUE);
        title.setEffect(new DropShadow(4, Color.SKYBLUE));

        Label popLabel = new Label("Total Population in Danger Area:");
        popLabel.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 17));

        TextField populationField = new TextField();
        populationField.setPromptText("Enter total population");
        populationField.setFont(Font.font("Segoe UI", 16));
        populationField.setMaxWidth(290);

        Label shelterLabel = new Label("Enter Shelters (one per line): Name,Capacity,Route");
        shelterLabel.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 17));

        TextArea sheltersInputArea = new TextArea();
        sheltersInputArea.setPromptText("Shelters list (Name,Capacity,Route)");
        sheltersInputArea.setFont(Font.font("Segoe UI", 15));
        sheltersInputArea.setMaxWidth(550);
        sheltersInputArea.setPrefRowCount(4);

        Button submitBtn = new Button("Submit Data and Assign");
        submitBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        submitBtn.setStyle("-fx-background-color:linear-gradient(to top,#50d8e8,#66bb6a);-fx-text-fill:white;-fx-background-radius:17;");
        submitBtn.setPrefWidth(250);
        submitBtn.setEffect(new DropShadow(9, Color.DARKGRAY));

        governmentLogArea = new TextArea();
        governmentLogArea.setEditable(false);
        governmentLogArea.setFont(Font.font("Arial", 15));
        governmentLogArea.setPrefHeight(110);
        governmentLogArea.setMaxWidth(550);

        govSuccessLabel = new Label("");
        govSuccessLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        govSuccessLabel.setTextFill(Color.FORESTGREEN);

        Label logLabel = new Label("Operation/Emergency Log:");
        logLabel.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 17));

        submitBtn.setOnAction(e -> {
            String popText = populationField.getText().trim();
            String sheltersText = sheltersInputArea.getText().trim();
            int population;
            if (popText.isEmpty() || sheltersText.isEmpty()) {
                alerts("Please fill both population and shelters data!");
                govSuccessLabel.setText("");
                return;
            }
            try {
                population = Integer.parseInt(popText);
                if (population < 1) throw new NumberFormatException();
                parseShelters(sheltersText);
                if (shelters.isEmpty()) {
                    alerts("No valid shelters found!");
                    govSuccessLabel.setText("");
                    return;
                }
                assignPopulation(population);
                updatePublicDashboard(population);
                govSuccessLabel.setText("Shelters assigned and details sent to Public Dashboard!");
                governmentLogArea.appendText("\nShelters assigned for current operation.\n");
            } catch (NumberFormatException ex) {
                alerts("Please enter a valid positive integer for population!");
                govSuccessLabel.setText("");
            }
        });

        govBox.getChildren().addAll(
                title, popLabel, populationField, shelterLabel,
                sheltersInputArea, submitBtn, govSuccessLabel, logLabel, governmentLogArea
        );
        return govBox;
    }

    private void alerts(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    private void parseShelters(String sheltersText) {
        shelters.clear();
        String[] lines = sheltersText.split("\\r?\\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                try {
                    String name = parts[0].trim();
                    int capacity = Integer.parseInt(parts[1].trim());
                    String route = parts[2].trim();
                    shelters.add(new Shelter(name, capacity, route));
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    private void assignPopulation(int population) {
        for (Shelter s : shelters) s.reset();
        int remaining = population;
        for (Shelter s : shelters) {
            int assign = Math.min(remaining, s.capacity);
            s.peopleAssigned = assign;
            s.foodPackets = assign * 2;
            s.waterPackets = assign * 3;
            remaining -= assign;
            if (remaining <= 0) break;
        }
    }

    private void updatePublicDashboard(int population) {
        publicOutputArea.clear();
        publicOutputArea.appendText("Shelter Assignments:\n\n");
        int totalAssigned = 0;
        for (Shelter s : shelters) {
            publicOutputArea.appendText(String.format(
                    "%s (Route %s): %d people, %d food, %d water. Route: %s\n\n",
                    s.name, s.route, s.peopleAssigned, s.foodPackets, s.waterPackets, s.route));
            totalAssigned += s.peopleAssigned;
        }
        if (totalAssigned < population) {
            publicOutputArea.appendText(String.format(
                    "WARNING: %d people could not be assigned.\n", population - totalAssigned));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
