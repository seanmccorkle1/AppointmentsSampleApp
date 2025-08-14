
module com.example.qam2sampleapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
//    requires mysql.connector.j;

    opens com.example.qam2sampleapp to javafx.fxml;
    exports com.example.qam2sampleapp;
}

//    exports com.example.qam2sampleapp;
//module com.example.qam2sampleapp {
//jetbrains://idea/navigate/reference?project=QAM2SampleApp&path=module-info.java
//    exports QAM2SampleApp27Dec.src.main.java.com.example.qam2sampleapp;
//module QAM2SampleApp27Dec.src.main.java.com.example.qam2sampleapp {