/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.helper.quantileTransformer;

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

/**
 *
 * 
 */
public class NormalizerTransformer implements ITransformer {

    double min = 0;
    double max = 0;
    double minMax = 0;

    @Override
    public <I> void fit(I x) {
        if (Double[].class.equals(x.getClass())) {
            fit((Double[]) x);
        } else if (Double[].class.equals(x.getClass())) {
            fit(flatten((Double[][]) x));
        } else {
            fit(x);
            //throw new UnsupportedOperationException("The input type " + x.getClass().getName() + " is not supported for this operation");
        }
    }

    private void fit(Double[] x) {
        double[] x2 = ArrayUtils.toPrimitive(x);
        min = StatUtils.min(x2);
        max = StatUtils.max(x2);
        minMax = min - max;
    }

    private static Double[] flatten(Double[][] x) {
        return Arrays.stream(x).toArray(Double[]::new);
    }

    @Override
    public <I, O> O transform(I x, Class<O> returnType, boolean rowMajorInput) {
        if (x == null) {
            return null;
        } else if (Double.class.equals(x.getClass())) {
            return transform((Double) x, returnType);
        } else if (Integer.class.equals(x.getClass())) {
            return transform(((Integer) x).doubleValue(), returnType);
        } else if (Float.class.equals(x.getClass())) {
            return transform(((Float) x).doubleValue(), returnType);
        } else if (Double[].class.equals(x.getClass())) {
            return transform((Double[]) x, returnType);
        } else {
            throw new UnsupportedOperationException("The input type " + x.getClass().getName() + " is not supported for this operation");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T transform(Double x, Class<T> returnType) {
        Double result = (x - min) / (minMax);
        if (Double.class.equals(returnType) || Double.class.equals(returnType)) {
            return (T) result;
        } else if (Float.class.equals(returnType) || float.class.equals(returnType)) {
            return (T) new Float(result.floatValue());
        } else {
            throw new UnsupportedOperationException("The returnType " + returnType.getName() + " is not supported for this operation");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T transform(Double[] x, Class<T> returnType) {
        final int xLen = x.length;
        Double[] result = new Double[xLen];

        for (int i = 0; i < xLen; i++) {
            result[i] = (x[i] - min) / (minMax);
        }

        if (Double[].class.equals(returnType)) {
            return (T) result;
        } else if (float[].class.equals(returnType)) {
            return (T) toFloatArray(result);
        } else {
            throw new UnsupportedOperationException("The returnType " + returnType.getName() + " is not supported for this operation");
        }
    }

    @Override
    public int getColInputCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColOutputCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void shutdown() {
        //To change body of generated methods, choose Tools | Templates.
    }

    private static Float[] toFloatArray(Double[] arr) {
        if (arr == null) {
            return null;
        }
        final int n = arr.length;
        Float[] ret = new Float[n];
        for (int i = 0; i < n; i++) {
            ret[i] = (Float) arr[i].floatValue();
        }
        return ret;
    }
}
