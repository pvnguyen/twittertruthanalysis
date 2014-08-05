package analysis;

/**
 * Created by phuong on 5/8/14.
 */

import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.classifiers.Classifier;

public class WekaAdaboostJ48
        extends Classifier {

    /**
     * Returns only the toString() method.
     *
     * @return a string describing the classifier
     */
    public String globalInfo() {
        return toString();
    }

    /**
     * Returns the capabilities of this classifier.
     *
     * @return the capabilities
     */
    public Capabilities getCapabilities() {
        weka.core.Capabilities result = new weka.core.Capabilities(this);

        result.enable(weka.core.Capabilities.Capability.NOMINAL_ATTRIBUTES);
        result.enableDependency(weka.core.Capabilities.Capability.NOMINAL_ATTRIBUTES);
        result.enable(weka.core.Capabilities.Capability.NUMERIC_ATTRIBUTES);
        result.enableDependency(weka.core.Capabilities.Capability.NUMERIC_ATTRIBUTES);
        result.enable(weka.core.Capabilities.Capability.DATE_ATTRIBUTES);
        result.enableDependency(weka.core.Capabilities.Capability.DATE_ATTRIBUTES);
        result.enable(weka.core.Capabilities.Capability.MISSING_VALUES);
        result.enableDependency(weka.core.Capabilities.Capability.MISSING_VALUES);
        result.enable(weka.core.Capabilities.Capability.NOMINAL_CLASS);
        result.enable(weka.core.Capabilities.Capability.MISSING_CLASS_VALUES);
        result.enableDependency(weka.core.Capabilities.Capability.MISSING_CLASS_VALUES);

        result.setMinimumNumberInstances(0);

        return result;
    }

    /**
     * only checks the data against its capabilities.
     *
     * @param i the training data
     */
    public void buildClassifier(Instances i) throws Exception {
        // can classifier handle the data?
        getCapabilities().testWithFail(i);
    }

    /**
     * Classifies the given instance.
     *
     * @param i the instance to classifyStatus
     * @return the classification result
     */
    public double classifyInstance(Instance i) throws Exception {
        Object[] s = new Object[i.numAttributes()];

        for (int j = 0; j < s.length; j++) {
            if (!i.isMissing(j)) {
                if (i.attribute(j).isNominal())
                    s[j] = new String(i.stringValue(j));
                else if (i.attribute(j).isNumeric())
                    s[j] = new Double(i.value(j));
            }
        }

        // set class value to missing
        s[i.classIndex()] = null;

        return WekaClassifier.classify(s);
    }

    /**
     * Returns the revision string.
     *
     * @return        the revision
     */
    public String getRevision() {
        return RevisionUtils.extract("1.0");
    }

    /**
     * Returns only the classnames and what classifier it is based on.
     *
     * @return a short description
     */
    public String toString() {
        return "Auto-generated classifier wrapper, based on weka.classifiers.meta.AdaBoostM1 (generated with Weka 3.6.11).\n" + this.getClass().getName() + "/WekaClassifier";
    }

    /**
     * Runs the classfier from commandline.
     *
     * @param args the commandline arguments
     */
    public static void main(String args[]) {
        runClassifier(new WekaAdaboostJ48(), args);
    }
}

class WekaClassifier {

    public static double classify(Object[] i) throws Exception {
        double [] sums = new double [2];
        sums[(int) WekaClassifier_0.classify(i)] += 3.178053830347946;
        sums[(int) WekaClassifier_1.classify(i)] += 3.850147601710058;
        sums[(int) WekaClassifier_2.classify(i)] += 4.121743536410215;
        sums[(int) WekaClassifier_3.classify(i)] += 3.1640675883732046;
        sums[(int) WekaClassifier_4.classify(i)] += 3.031808330027801;
        sums[(int) WekaClassifier_5.classify(i)] += 3.6595314139742254;
        sums[(int) WekaClassifier_6.classify(i)] += 4.0109372565812205;
        sums[(int) WekaClassifier_7.classify(i)] += 2.4879728542808617;
        sums[(int) WekaClassifier_8.classify(i)] += 5.987010196758601;
        sums[(int) WekaClassifier_9.classify(i)] += 4.219030516293523;
        double maxV = sums[0];
        int maxI = 0;
        for (int j = 1; j < 2; j++) {
            if (sums[j] > maxV) { maxV = sums[j]; maxI = j; }
        }
        return (double) maxI;
    }
}
class WekaClassifier_0 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_0.N45b7003c0(i);
        return p;
    }
    static double N45b7003c0(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 2.0) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() > 2.0) {
            p = 0;
        }
        return p;
    }
}
class WekaClassifier_1 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_1.N717086e51(i);
        return p;
    }
    static double N717086e51(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() <= 20.0) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() > 20.0) {
            p = 1;
        }
        return p;
    }
}
class WekaClassifier_2 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_2.N12a85e922(i);
        return p;
    }
    static double N12a85e922(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 124.0) {
            p = WekaClassifier_2.N6bd8cccf3(i);
        } else if (((Double) i[10]).doubleValue() > 124.0) {
            p = 1;
        }
        return p;
    }
    static double N6bd8cccf3(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 2.0) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() > 2.0) {
            p = 0;
        }
        return p;
    }
}
class WekaClassifier_3 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_3.N666e61594(i);
        return p;
    }
    static double N666e61594(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() <= 1.0) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() > 1.0) {
            p = 0;
        }
        return p;
    }
}
class WekaClassifier_4 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_4.N58b94e985(i);
        return p;
    }
    static double N58b94e985(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 882.0) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() > 882.0) {
            p = WekaClassifier_4.N6dd70aa96(i);
        }
        return p;
    }
    static double N6dd70aa96(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() <= 2074.0) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() > 2074.0) {
            p = 1;
        }
        return p;
    }
}
class WekaClassifier_5 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_5.N40fd15957(i);
        return p;
    }
    static double N40fd15957(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 2053.0) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() > 2053.0) {
            p = 0;
        }
        return p;
    }
}
class WekaClassifier_6 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_6.N27940a888(i);
        return p;
    }
    static double N27940a888(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() <= 20.0) {
            p = WekaClassifier_6.N683f7b049(i);
        } else if (((Double) i[6]).doubleValue() > 20.0) {
            p = 1;
        }
        return p;
    }
    static double N683f7b049(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 769.0) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 769.0) {
            p = 0;
        }
        return p;
    }
}
class WekaClassifier_7 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = 1;
        return p;
    }
}
class WekaClassifier_8 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_8.N1fa5791410(i);
        return p;
    }
    static double N1fa5791410(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 2.0) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() > 2.0) {
            p = WekaClassifier_8.N28b0204711(i);
        }
        return p;
    }
    static double N28b0204711(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 1997.0) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 1997.0) {
            p = 1;
        }
        return p;
    }
}
class WekaClassifier_9 {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier_9.N43172a2f12(i);
        return p;
    }
    static double N43172a2f12(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 1101.0) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() > 1101.0) {
            p = WekaClassifier_9.N25d2961213(i);
        }
        return p;
    }
    static double N25d2961213(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 1997.0) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 1997.0) {
            p = 1;
        }
        return p;
    }
}
