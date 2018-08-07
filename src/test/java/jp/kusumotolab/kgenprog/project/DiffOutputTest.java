package jp.kusumotolab.kgenprog.project;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Test;
import jp.kusumotolab.kgenprog.ga.Base;
import jp.kusumotolab.kgenprog.ga.SimpleGene;
import jp.kusumotolab.kgenprog.ga.Variant;
import jp.kusumotolab.kgenprog.project.factory.TargetProject;
import jp.kusumotolab.kgenprog.project.factory.TargetProjectFactory;
import jp.kusumotolab.kgenprog.project.jdt.DeleteOperation;
import jp.kusumotolab.kgenprog.project.jdt.GeneratedJDTAST;
import jp.kusumotolab.kgenprog.project.jdt.InsertOperation;
import jp.kusumotolab.kgenprog.project.jdt.JDTASTLocation;
import jp.kusumotolab.kgenprog.project.jdt.ReplaceOperation;

public class DiffOutputTest {


  @Test
  public void testDiffOutput1() throws IOException {
    Path basePath = Paths.get("example/BuildSuccess01");
    final Path outdirPath = basePath.resolve("modified");
    DiffOutput diffOutput = new DiffOutput(outdirPath);

    String expected = "package example;\n" + "public class Foo {\n" + "  public int foo(  int n){\n"
        + "    return n;\n" + "  }\n" + "}\n\n";

    TargetProject project = TargetProjectFactory.create(basePath);
    Variant originVariant = project.getInitialVariant();
    GeneratedJDTAST ast = (GeneratedJDTAST) originVariant.getGeneratedSourceCode()
        .getAsts()
        .get(0);

    // 削除位置の Location 作成
    TypeDeclaration type = (TypeDeclaration) ast.getRoot()
        .types()
        .get(0);
    MethodDeclaration method = type.getMethods()[0];
    Statement statement = (Statement) method.getBody()
        .statements()
        .get(0);
    DeleteOperation operation = new DeleteOperation();
    JDTASTLocation location = new JDTASTLocation(
        new ProductSourcePath(basePath.resolve("src/example/Foo.java")), statement);

    GeneratedSourceCode code = operation.apply(originVariant.getGeneratedSourceCode(), location);
    List<Variant> modVariant = new ArrayList<Variant>(Arrays.asList(
        new Variant(new SimpleGene(Arrays.asList(new Base(location, operation))), null, code)));

    diffOutput.outputResult(project, modVariant);

    String modSource =
        new String(Files.readAllBytes(outdirPath.resolve("variant01/example.Foo.java")));

    FileUtils.deleteDirectory(outdirPath.toFile());

    assertThat(modSource).isEqualToNormalizingNewlines(expected);
  }

  @Test
  public void testDiffOutput2() throws IOException {
    Path basePath = Paths.get("example/BuildSuccess03");
    final Path outdirPath = basePath.resolve("modified");
    DiffOutput diffOutput = new DiffOutput(outdirPath);

    String expected = new StringBuilder().append("")
        .append("package example;\n")
        .append("public class Bar {\n")
        .append("  public static int bar1(  int n){\n")
        // .append(" return n + 1;\n")
        .append("  }\n")
        .append("  public static int bar2(  int n){\n")
        .append("    return n - 1;\n")
        .append("  }\n")
        .append("  public static void bar3(){\n")
        .append("    new String();\n")
        .append("  }\n")
        .append("}\n\n")
        .toString();

    TargetProject project = TargetProjectFactory.create(basePath);
    Variant originVariant = project.getInitialVariant();
    GeneratedJDTAST ast = (GeneratedJDTAST) originVariant.getGeneratedSourceCode()
        .getAsts()
        .get(0);

    // 削除位置の Location 作成
    TypeDeclaration type = (TypeDeclaration) ast.getRoot()
        .types()
        .get(0);
    MethodDeclaration method = type.getMethods()[0];
    Statement statement = (Statement) method.getBody()
        .statements()
        .get(0);
    DeleteOperation operation = new DeleteOperation();
    JDTASTLocation location = new JDTASTLocation(
        new ProductSourcePath(basePath.resolve("src/example/Bar.java")), statement);

    GeneratedSourceCode code = operation.apply(originVariant.getGeneratedSourceCode(), location);
    List<Variant> modVariant = new ArrayList<Variant>(Arrays.asList(
        new Variant(new SimpleGene(Arrays.asList(new Base(location, operation))), null, code)));

    diffOutput.outputResult(project, modVariant);

    String modSource =
        new String(Files.readAllBytes(outdirPath.resolve("variant01/example.Bar.java")));

    FileUtils.deleteDirectory(outdirPath.toFile());

    assertThat(modSource).isEqualToNormalizingNewlines(expected);
  }

  @Test
  public void testDiffOutput3() throws IOException {
    Path basePath = Paths.get("example/BuildSuccess01");
    final Path outdirPath = basePath.resolve("modified");
    DiffOutput diffOutput = new DiffOutput(outdirPath);

    String expected = "package example;\n" + "public class Foo {\n" + "  public int foo(  int n){\n"
        + "    if (n > 0) {\n" + "      n--;\n" + "    }\n" + " else {\n" + "      n++;\n"
        + "    }\n" + "    a();\n" + "\treturn n;\n" + "  }\n" + "}\n\n";

    TargetProject project = TargetProjectFactory.create(basePath);
    Variant originVariant = project.getInitialVariant();
    GeneratedJDTAST ast = (GeneratedJDTAST) originVariant.getGeneratedSourceCode()
        .getAsts()
        .get(0);

    // 挿入位置のLocation生成
    TypeDeclaration type = (TypeDeclaration) ast.getRoot()
        .types()
        .get(0);
    MethodDeclaration method = type.getMethods()[0];
    Statement statement = (Statement) method.getBody()
        .statements()
        .get(0);
    JDTASTLocation location = new JDTASTLocation(
        new ProductSourcePath(basePath.resolve("src/example/Foo.java")), statement);

    // 挿入対象生成
    AST jdtAST = ast.getRoot()
        .getAST();
    MethodInvocation invocation = jdtAST.newMethodInvocation();
    invocation.setName(jdtAST.newSimpleName("a"));
    Statement insertStatement = jdtAST.newExpressionStatement(invocation);

    InsertOperation operation = new InsertOperation(insertStatement);

    GeneratedSourceCode code = operation.apply(originVariant.getGeneratedSourceCode(), location);

    List<Variant> modVariant = new ArrayList<Variant>(Arrays.asList(
        new Variant(new SimpleGene(Arrays.asList(new Base(location, operation))), null, code)));

    diffOutput.outputResult(project, modVariant);

    String modSource =
        new String(Files.readAllBytes(outdirPath.resolve("variant01/example.Foo.java")));

    FileUtils.deleteDirectory(outdirPath.toFile());

    assertThat(modSource).isEqualToNormalizingNewlines(expected);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testDiffOutput4() throws IOException {
    Path basePath = Paths.get("example/BuildSuccess01/");
    final Path outdirPath = basePath.resolve("modified");
    DiffOutput diffOutput = new DiffOutput(outdirPath);

    String expected = "package example;\n" + "public class Foo {\n" + "  public int foo(  int n){\n"
        + "    {\n" + "\t\ta();\n" + "\t}\n" + "    return n;\n" + "  }\n" + "}\n\n";

    TargetProject project = TargetProjectFactory.create(basePath);
    Variant originVariant = project.getInitialVariant();
    GeneratedJDTAST ast = (GeneratedJDTAST) originVariant.getGeneratedSourceCode()
        .getAsts()
        .get(0);

    // 挿入位置のLocation生成
    TypeDeclaration type = (TypeDeclaration) ast.getRoot()
        .types()
        .get(0);
    MethodDeclaration method = type.getMethods()[0];
    Statement statement = (Statement) method.getBody()
        .statements()
        .get(0);
    JDTASTLocation location = new JDTASTLocation(
        new ProductSourcePath(basePath.resolve("src/example/Foo.java")), statement);

    // 挿入対象生成
    AST jdtAST = ast.getRoot()
        .getAST();
    MethodInvocation invocation = jdtAST.newMethodInvocation();
    invocation.setName(jdtAST.newSimpleName("a"));
    statement = jdtAST.newExpressionStatement(invocation);
    Block replaceBlock = jdtAST.newBlock();
    replaceBlock.statements()
        .add(statement);

    ReplaceOperation operation = new ReplaceOperation(replaceBlock);

    GeneratedSourceCode code = operation.apply(originVariant.getGeneratedSourceCode(), location);
    List<Variant> modVariant = new ArrayList<Variant>(Arrays.asList(
        new Variant(new SimpleGene(Arrays.asList(new Base(location, operation))), null, code)));

    diffOutput.outputResult(project, modVariant);

    String modSource =
        new String(Files.readAllBytes(outdirPath.resolve("variant01/example.Foo.java")));

    FileUtils.deleteDirectory(outdirPath.toFile());

    assertThat(modSource).isEqualToNormalizingNewlines(expected);
  }

}
