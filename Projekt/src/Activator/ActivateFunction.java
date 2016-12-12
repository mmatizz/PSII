package Activator;

/**
 * Created by mateu on 05.12.2016.
 */
public interface ActivateFunction {
    double activate(double weightedSum);
    double derivative(double weightedSum);
    ActivateFunction copy();
}
