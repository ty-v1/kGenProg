package jp.kusumotolab.kgenprog.ga.crossover;

import java.util.Random;
import jp.kusumotolab.kgenprog.ga.variant.Variant;

public class SecondVariantTestSimilarityBasedSelection
    extends SecondVariantSimilarityBasedSelection {

  public SecondVariantTestSimilarityBasedSelection(final Random random) {
    super(random);
  }

  @Override
  protected double calculateSimilarity(final Variant variant1, final Variant variant2) {
    return SimilarityCalculator.exec(variant1, variant2, v -> v.getTestResults()
        .getFailedTestFQNs());
  }
}
