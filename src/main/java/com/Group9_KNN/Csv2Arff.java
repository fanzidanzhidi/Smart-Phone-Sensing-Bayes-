package com.Group9_KNN;

import androidx.appcompat.app.AppCompatActivity;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class Csv2Arff extends AppCompatActivity {
    /**
     * takes 2 arguments:
     * - CSV input file
     * - ARFF output file
     */
    public static void transfer(String csv, String arff) throws Exception {

        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csv));
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(arff));
        saver.setDestination(new File(arff));
        saver.writeBatch();
    }
}