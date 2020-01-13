package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FifthHomework {

    private static class Classifier implements Comparable<Classifier> {
        double sepalLength;
        double sepalWidth;
        double petalLength;
        double petalWidth;
        String classifier;
        double distance;

        public Classifier(double sepalLength, double sepalWidth, double petalLength, double petalWidth) {
            this.sepalLength = sepalLength;
            this.sepalWidth = sepalWidth;
            this.petalLength = petalLength;
            this.petalWidth = petalWidth;
        }

        public Classifier(String[] line) {
            this.sepalLength = Double.parseDouble(line[0]);
            this.sepalWidth = Double.parseDouble(line[1]);
            this.petalLength = Double.parseDouble(line[2]);
            this.petalWidth = Double.parseDouble(line[3]);
            this.classifier = line[4];
        }


        @Override
        public int compareTo(Classifier o) {
            if (this.distance < o.distance)
                return -1;
            else if (o.distance < this.distance)
                return 1;
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Classifier that = (Classifier) o;
            return Double.compare(that.distance, distance) == 0 &&
                    Objects.equals(sepalLength, that.sepalLength) &&
                    Objects.equals(sepalWidth, that.sepalWidth) &&
                    Objects.equals(petalLength, that.petalLength) &&
                    Objects.equals(petalWidth, that.petalWidth) &&
                    Objects.equals(classifier, that.classifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sepalLength, sepalWidth, petalLength, petalWidth, classifier, distance);
        }
    }

    public static double ed(Classifier c1, Classifier c2, int length) {
        double distance = 0;
        for (int i = 0; i < length; i++) {
            distance += ((c1.petalLength - c2.petalLength) * (c1.petalLength - c2.petalLength) +
                    (c1.petalWidth - c2.petalWidth) * (c1.petalWidth - c2.petalWidth));
        }
        return Math.sqrt(distance);
    }

    public static String classifyAPoint(Classifier[] arr, int k, Classifier c) {
        for (int i = 0; i < arr.length; i++) {
            arr[i].distance = ed(arr[i], c, 4);
        }

        Arrays.sort(arr);

        // three groups
        int freqSetosa = 0;
        int freqVersicolor = 0;
        int freqVirginica = 0;

        for (int i = 0; i < k; i++) {
            if (arr[i].classifier.equals("Iris-setosa")){
                freqSetosa++;
            } else if (arr[i].classifier.equals("Iris-versicolor")) {
                freqVersicolor++;
            } else if (arr[i].classifier.equals("Iris-virginica")) {
                freqVirginica++;
            }
        }

        if (freqSetosa >= freqVersicolor && freqSetosa >= freqVirginica) {
            return "Iris-setosa";
        } else if (freqVersicolor >= freqSetosa && freqVersicolor >= freqVirginica) {
            return "Iris-versicolor";
        } else {
            return "Iris-virginica";
        }
    }

    public static void main(String[] args) {
        boolean startReading = false;
        List<Classifier> classifiers = new ArrayList<>();
        try {
            BufferedReader readbuffer = new BufferedReader(new FileReader(new File("iris.arff")));
            String availalbe;
            while ((availalbe = readbuffer.readLine()) != null) {
                if (availalbe.equals("@DATA")) {
                    startReading = true;
                    continue;
                }
                if (startReading) {
                    String[] line = availalbe.split(",");
                    Classifier classifier = new Classifier(line);
                    classifiers.add(classifier);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object[] cl = classifiers.toArray();
        Classifier[] arrOfClassifiers = new Classifier[cl.length];

        for (int i = 0; i < cl.length; i++) {
            Classifier s = (Classifier) cl[i];
            arrOfClassifiers[i] = s;
        }
        System.out.println(classifyAPoint(arrOfClassifiers, 10, new Classifier(5.1,2.2,4.0,1.0)));
    }
}
