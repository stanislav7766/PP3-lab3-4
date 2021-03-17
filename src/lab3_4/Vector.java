package lab3_4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Arrays;

public class Vector {
    private int size = 10;
    private double[] _Vector = new double[size];
    private boolean filled = false;
    private String path;

    public Vector(String path) {
        this.path = path;
    }

    public double[] get_Vector() {
        return _Vector;
    }

    private static double kahanSum(double... fa) {
        double sum = 0.0;
        double c = 0.0;
        for (double f : fa) {
            double y = f - c;
            double t = sum + y;
            c = (t - sum) - y;
            sum = t;
        }
        return sum;
    }

    public Vector filledWithRandomValues() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            for (int i = 0; i < size; i++) {
                double value = 1000.0 * new Random().nextDouble();
                _Vector[i] = value;
                bw.write(String.valueOf(value) + ", ");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            filled = false;
        }
        filled = true;
        return this;
    }

    public Vector saveToFile(String newpath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(newpath));
            for (int i = 0; i < size; i++) {
                bw.write(String.valueOf(_Vector[i]) + ", ");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
        }
        return this;
    }

    public Vector sort() {
        if (!filled)
            throw new Error("Спочатку необхідно викликати filledWithRandomValues");

        Arrays.sort(_Vector);
        return this;
    }

    public double findMin() {
        if (!filled)
            throw new Error("Спочатку необхідно викликати filledWithRandomValues");

        double min = Arrays.stream(_Vector).reduce(_Vector[0], (x, y) -> x > y ? y : x);
        return min;
    }

    public Vector multiplyWithMatrix(Matrix matrix) {
        if (!filled)
            throw new Error("Спочатку необхідно викликати filledWithRandomValues");

        double[] resVector = new double[size];
        double[][] currentMatrix = matrix.get_Matrix();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                resVector[i] = kahanSum(resVector[i], _Vector[j] * currentMatrix[j][i]);
            }
        }
        _Vector = resVector;
        return this;
    }

    public Vector multiplyWithDouble(double d) {
        if (!filled)
            throw new Error("Спочатку необхідно викликати filledWithRandomValues");

        double[] resVector = new double[size];
        for (int i = 0; i < size; i++) {
            resVector[i] = _Vector[i] * d;
        }
        _Vector = resVector;
        return this;
    }

    public Vector sumWithVector(Vector vector) {
        if (!filled)
            throw new Error("Спочатку необхідно викликати filledWithRandomValues");

        double[] resVector = new double[size];
        double[] currentVector = vector.get_Vector();
        for (int i = 0; i < size; i++) {
            resVector[i] = _Vector[i] + currentVector[i];
        }
        _Vector = resVector;
        return this;
    }

    public Vector printToConsole() {
        System.out.println(Arrays.toString(_Vector));
        return this;
    }
}
