package jp.kusumotolab.kgenprog.project;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import jp.kusumotolab.kgenprog.project.jdt.JDTLocation;

public class GeneratedSourceCodeTest {
  private static class GeneratedASTMock implements GeneratedAST {
    private final SourceFile file;
    private final List<Location> locations;

    public GeneratedASTMock(final SourceFile file, final List<Location> locations) {
      this.file = file;
      this.locations = locations;
    }

    @Override
    public String getSourceCode() {
      return null;
    }

    @Override
    public String getPrimaryClassName() {
      return null;
    }

    @Override
    public SourceFile getSourceFile() {
      return file;
    }

    @Override
    public List<Location> inferLocations(int lineNumber) {
      return null;
    }

    @Override
    public List<Location> getAllLocations() {
      return locations;
    }

  }

  @Test
  public void testGetAllLocations() {
    final Location l0 = new JDTLocation(null, null);
    final Location l1 = new JDTLocation(null, null);
    final Location l2 = new JDTLocation(null, null);
    final Location l3 = new JDTLocation(null, null);
    final Location l4 = new JDTLocation(null, null);

    final GeneratedAST ast1 =
        new GeneratedASTMock(new TargetSourceFile(Paths.get("a")), Arrays.asList(l0, l1));
    final GeneratedAST ast2 =
        new GeneratedASTMock(new TargetSourceFile(Paths.get("b")), Arrays.asList(l2, l3, l4));
    final GeneratedSourceCode generatedSourceCode =
        new GeneratedSourceCode(Arrays.asList(ast1, ast2));

    final List<Location> locations = generatedSourceCode.getAllLocations();

    assertThat(locations, hasSize(5));
    assertThat(locations.get(0), is(l0));
    assertThat(locations.get(1), is(l1));
    assertThat(locations.get(2), is(l2));
    assertThat(locations.get(3), is(l3));
    assertThat(locations.get(4), is(l4));

  }

}