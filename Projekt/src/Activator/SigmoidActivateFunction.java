package Activator;

import java.io.Serializable;

/**
 * Created by mateu on 05.12.2016.
 */
public class SigmoidActivateFunction implements ActivateFunction, Serializable {
    public double activate(double x) {
        return 1.0 / (1 + Math.exp(-1.0 * x));
    }

    public double derivative(double x) {
        return x * (1.0 - x);
    }

    public SigmoidActivateFunction copy() {
        return new SigmoidActivateFunction();
    }
}

