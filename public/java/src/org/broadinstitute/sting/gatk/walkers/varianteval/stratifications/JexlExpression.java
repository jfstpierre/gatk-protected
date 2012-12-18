package org.broadinstitute.sting.gatk.walkers.varianteval.stratifications;

import org.broadinstitute.sting.gatk.contexts.ReferenceContext;
import org.broadinstitute.sting.gatk.refdata.RefMetaDataTracker;
import org.broadinstitute.sting.gatk.walkers.varianteval.util.SortableJexlVCMatchExp;
import org.broadinstitute.variant.variantcontext.VariantContext;
import org.broadinstitute.variant.variantcontext.VariantContextUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * Stratifies the eval RODs by user-supplied JEXL expressions
 *
 * See http://gatkforums.broadinstitute.org/discussion/1255/what-are-jexl-expressions-and-how-can-i-use-them-with-the-gatk for more details
 */
public class JexlExpression extends VariantStratifier implements StandardStratification {
    // needs to know the jexl expressions
    private Set<SortableJexlVCMatchExp> jexlExpressions;

    @Override
    public void initialize() {
        jexlExpressions = getVariantEvalWalker().getJexlExpressions();

        states.add("none");
        for ( SortableJexlVCMatchExp jexlExpression : jexlExpressions ) {
            states.add(jexlExpression.name);
        }
    }

    public List<Object> getRelevantStates(ReferenceContext ref, RefMetaDataTracker tracker, VariantContext comp, String compName, VariantContext eval, String evalName, String sampleName) {
        ArrayList<Object> relevantStates = new ArrayList<Object>();
        relevantStates.add("none");

        for ( SortableJexlVCMatchExp jexlExpression : jexlExpressions ) {
            if (eval != null && VariantContextUtils.match(eval, jexlExpression)) {
                relevantStates.add(jexlExpression.name);
            }
        }

        return relevantStates;
    }
}
