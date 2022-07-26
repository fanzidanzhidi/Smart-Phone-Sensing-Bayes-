package com.Group9_KNN;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class KNN extends AppCompatActivity {

    public static Instances getDataSet(String fileName) throws IOException {

        ArffLoader loader = new ArffLoader();
        loader.setSource(KNN.class.getResourceAsStream("/" + fileName));

        Instances dataSet = loader.getDataSet();
//        dataSet.setClassIndex(classIdx);
        return dataSet;
    }

    public  static  Classifier ibk;

    public static void process(Instances data) throws Exception {

        // Classifier for KNN()
        ibk = new IBk(3);
        ibk.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(ibk, data);
//        // The Cell Prediction Output


        System.out.println("** KNN Analysis  **");

        System.out.println(eval.toSummaryString());
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toMatrixString());

    }

    public static String test_bayes(Instances data, Instance test) throws Exception {
        double result = ibk.classifyInstance(test);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.print("result = ");
        System.out.println(result);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(data.classAttribute().value((int) result));
        return data.classAttribute().value((int) result);
    }
}
