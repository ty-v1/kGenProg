package jp.kusumotolab.kgenprog.ga.crossover;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import jp.kusumotolab.kgenprog.ga.variant.Variant;

public abstract class SecondVariantSimilarityBasedSelection
    implements SecondVariantSelectionStrategy {


  private final Random random;

  protected SecondVariantSimilarityBasedSelection(final Random random) {
    this.random = random;
  }

  @Override
  public Variant exec(final List<Variant> variants, final Variant firstVariant)
      throws CrossoverInfeasibleException {

    double minSimilarity = 1.0d;

    // secondVariantの初期値を，一つ目の親以外のバリアントからランダムに選択
    final List<Variant> secondVariantCandidates = variants.stream()
        .filter(v -> !v.equals(firstVariant))
        .collect(Collectors.toList());
    if (secondVariantCandidates.isEmpty()) { // 候補リストが空の時は例外を投げる
      throw new CrossoverInfeasibleException("no variant for second parent");
    }
    Variant secondVariant =
        secondVariantCandidates.get(random.nextInt(secondVariantCandidates.size()));

    for (final Variant variant : variants) {
      final double similarity = calculateSimilarity(firstVariant, variant);
      if (similarity < minSimilarity) {
        minSimilarity = similarity;
        secondVariant = variant;
      }
    }

    return secondVariant;
  }

  protected abstract double calculateSimilarity(final Variant variant1, final Variant variant2);
}
