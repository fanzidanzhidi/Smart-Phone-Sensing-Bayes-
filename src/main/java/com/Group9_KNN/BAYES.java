package com.Group9_KNN;

import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class BAYES {
    public static NaiveBayes bayes = new NaiveBayes();

    public static Instances getDataSet(String fileName) throws IOException {
        /*
         * we can set the file i.e., loader.setFile("finename") to load the data
         */
//        int classIdx = 4;
        /* the arffloader to load the arff file */
        ArffLoader loader = new ArffLoader();
        /*load the traing data */
        loader.setSource(KNN.class.getResourceAsStream("/" + fileName));
        /*
         * we can also set the file like loader3.setFile(new
         * File("test-confused.arff"));
         */
        //loader.setFile(new File(fileName));
        Instances dataSet = loader.getDataSet();
        /* set the index based on the data given in the arff files */
//        dataSet.setClassIndex(classIdx);
        return dataSet;
    }


    public static void process(Instances data) throws Exception {

//        data.setClassIndex(data.numAttributes() - 1);

        // Classifier for Bayes
        bayes.setUseSupervisedDiscretization(true);
        bayes.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(bayes, data);
//        // The Cell Prediction Output


        System.out.println("** KNN Analysis  **");

        System.out.println(eval.toSummaryString());
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toMatrixString());

    }

    public static String locate_bayes_String(Instances data, Instance test) throws Exception {
        double result = bayes.classifyInstance(test);

//        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//        System.out.print("result = ");
//        System.out.println(result);
//        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(data.classAttribute().value((int) result));
        return data.classAttribute().value((int) result);
    }

    public static double locate_bayes_double (Instance test) throws Exception {
        return bayes.classifyInstance(test);
    }
}

