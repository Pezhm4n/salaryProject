module com.example.salaryproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.salaryproject to javafx.fxml;
    exports com.example.salaryproject;
}