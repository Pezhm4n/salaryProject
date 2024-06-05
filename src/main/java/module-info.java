module com.example.salaryproject {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.salaryproject to javafx.fxml;
    exports com.example.salaryproject;
}