package jp.kusumotolab.kgenprog.project.jdt;

import static jp.kusumotolab.kgenprog.project.jdt.ASTNodeAssert.assertThat;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Test;
import jp.kusumotolab.kgenprog.project.ASTLocations;
import jp.kusumotolab.kgenprog.project.ProductSourcePath;

public class JDTASTCrossoverLocationTest {

  @Test
  public void testLocateForTheSameAst01() {
    final String source = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    i = 1;" // target
        + "    i = 2;"
        + "  }"
        + "}";
    final GeneratedJDTAST<ProductSourcePath> ast = createAst(source);

    // extract target location
    final JDTASTLocation location = getLocation(ast, 1);
    final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
    assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

    // try locate() for the same ast root
    final ASTNode node = cLocation.locate(ast.getRoot());

    // of course, target node and located node are the same
    assertThat(node).isSameSourceCodeAs("i=1;");
    assertThat(node.getRoot()).isSameRootClassAs(ast.getRoot());
  }

  @Test
  public void testLocateForTheSameAst02() {
    final String source = "class A {"
        + "  public void a(int i) {"
        + "    i = 1;" // target
        + "    i = 1;"
        + "    i = 1;"
        + "  }"
        + "}";
    final GeneratedJDTAST<ProductSourcePath> ast = createAst(source);

    // extract target location
    final JDTASTLocation location = getLocation(ast, 0);
    final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
    assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

    // try locate() for the same ast root
    final ASTNode node = cLocation.locate(ast.getRoot());

    // of course, target node and located node are the same instance
    assertThat(node).isSameAs(location.getNode());
    assertThat(node.getRoot()).isSameRootClassAs(ast.getRoot());
  }

  @Test
  public void testLocateForTheSameAst03() {
    final String source = "class A {"
        + "  public void a(int i) {"
        + "    i = 1;"
        + "    i = 1;" // target
        + "    i = 1;"
        + "  }"
        + "}";
    final GeneratedJDTAST<ProductSourcePath> ast = createAst(source);

    // extract target location
    final JDTASTLocation location = getLocation(ast, 1);
    final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
    assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

    // try locate() for the same ast root
    final ASTNode node = cLocation.locate(ast.getRoot());

    // there are the same nodes as target node, so located node is not target node but first one
    assertThat(node).isNotSameAs(location.getNode());
    assertThat(node).isSameAs(getLocation(ast, 0).getNode());
    assertThat(node.getRoot()).isSameRootClassAs(ast.getRoot());
  }

  @Test
  public void testLocateForTheSameAst04() {
    final String source = "class A {"
        + "  public void a(int i) {"
        + "    if (true) {"
        + "      i = 1;" // target
        + "      if (true) {"
        + "        i = 1;" // target
        + "      } else {"
        + "        i = 1;" // target
        + "      }"
        + "    } else {"
        + "      i = 1;" // target
        + "      if (true) {"
        + "        i = 1;" // target
        + "      } else {"
        + "        i = 1;" // target
        + "      }"
        + "    }"
        + "}";
    final GeneratedJDTAST<ProductSourcePath> ast = createAst(source);

    final List<Integer> ids = Arrays.asList(1, 2, 3, 6, 7, 8); // These nodes have i=1

    for (int i : ids) {
      // extract target location
      final JDTASTLocation location = getLocation(ast, i);
      final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
      assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

      // try locate() for the same ast root
      final ASTNode node = cLocation.locate(ast.getRoot());

      // located node is the same instance as target node
      assertThat(node).isSameAs(location.getNode());
      assertThat(node.getRoot()).isSameRootClassAs(ast.getRoot());
    }
  }

  @Test
  public void testLocateForSameContentAst() {
    final String source = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    i = 1;" // target
        + "    i = 2;"
        + "  }"
        + "}";

    // generate two asts (contents are the same but they are different instances)
    final GeneratedJDTAST<ProductSourcePath> ast1 = createAst(source);
    final GeneratedJDTAST<ProductSourcePath> ast2 = createAst(source);

    // extract target location
    final JDTASTLocation location = getLocation(ast1, 1);
    final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
    assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

    // try loc1.locate()
    final ASTNode node = cLocation.locate(ast2.getRoot());

    // located node is the same as target node
    assertThat(node).isSameSourceCodeAs("i=1;");
    assertThat(node).isSameRootClassAs(ast2.getRoot());
  }

  @Test
  public void testLocateForDifferentAst01() {
    final String source1 = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    i = 1;" // target
        + "    i = 2;"
        + "  }"
        + "}";
    final String source2 = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    i = 2;"
        + "    i = 1;" // target
        + "  }"
        + "}";

    // generate two asts
    final GeneratedJDTAST<ProductSourcePath> ast1 = createAst(source1);
    final GeneratedJDTAST<ProductSourcePath> ast2 = createAst(source2);

    // extract target location
    final JDTASTLocation location = getLocation(ast1, 1);
    final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
    assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

    // try loc1.locate()
    final ASTNode node = cLocation.locate(ast2.getRoot());

    // located node is the same as target node
    assertThat(node).isSameSourceCodeAs("i=1;");
    assertThat(node).isSameRootClassAs(ast2.getRoot());
  }

  @Test
  public void testLocateForDifferentAst02() {
    final String source1 = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    i = 1;" // target
        + "    i = 2;"
        + "  }"
        + "}";
    final String source2 = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    i = 1;" // target
        + "    if (true) {"
        + "      i = 2;"
        + "    }"
        + "  }"
        + "}";

    // generate two asts
    final GeneratedJDTAST<ProductSourcePath> ast1 = createAst(source1);
    final GeneratedJDTAST<ProductSourcePath> ast2 = createAst(source2);

    // extract target location
    final JDTASTLocation location = getLocation(ast1, 1);
    final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
    assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

    // try loc1.locate()
    final ASTNode node = cLocation.locate(ast2.getRoot());

    // located node is the same as target node
    assertThat(node).isSameSourceCodeAs("i=1;");
    assertThat(node).isSameRootClassAs(ast2.getRoot());
  }

  @Test
  public void testLocateForDifferentAst03() {
    final String source1 = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    i = 1;" // target
        + "    i = 2;"
        + "  }"
        + "}";
    final String source2 = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    if (true) {"
        + "      i = 1;" // target
        + "    }"
        + "    i = 2;"
        + "  }"
        + "}";

    // generate two asts
    final GeneratedJDTAST<ProductSourcePath> ast1 = createAst(source1);
    final GeneratedJDTAST<ProductSourcePath> ast2 = createAst(source2);

    // extract target location
    final JDTASTLocation location = getLocation(ast1, 1);
    final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
    assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

    // try loc1.locate()
    final ASTNode node = cLocation.locate(ast2.getRoot());

    // failed to locate
    assertThat(node).isNull();
  }

  @Test
  public void testLocateForDifferentAst04() {
    final String source1 = "class A {"
        + "  public void a(int i) {"
        + "    i = 0;"
        + "    i = 1;" // target
        + "    i = 2;"
        + "  }"
        + "}";
    final String source2 = "class A {"
        + "  public void a(int i) {"
        + "  }"
        + "}";

    // generate two asts
    final GeneratedJDTAST<ProductSourcePath> ast1 = createAst(source1);
    final GeneratedJDTAST<ProductSourcePath> ast2 = createAst(source2);

    // extract target location
    final JDTASTLocation location = getLocation(ast1, 1);
    final JDTASTCrossoverLocation cLocation = new JDTASTCrossoverLocation(location);
    assertThat(cLocation.getNode()).isSameSourceCodeAs("i=1;");

    // try loc1.locate()
    final ASTNode node = cLocation.locate(ast2.getRoot());

    // failed to locate
    assertThat(node).isNull();
  }

  private GeneratedJDTAST<ProductSourcePath> createAst(final String source) {
    final String fname = source.hashCode() + ".java"; // dummy file name
    final ProductSourcePath path = new ProductSourcePath(Paths.get("."), Paths.get(fname));
    final JDTASTConstruction constructor = new JDTASTConstruction();
    return constructor.constructAST(path, source);
  }

  private JDTASTLocation getLocation(GeneratedJDTAST<ProductSourcePath> ast, int idx) {
    final ASTLocations locs = ast.createLocations();
    return (JDTASTLocation) locs.getAll()
        .get(idx);
  }
}
