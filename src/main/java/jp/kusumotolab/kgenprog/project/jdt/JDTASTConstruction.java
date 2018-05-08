package jp.kusumotolab.kgenprog.project.jdt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import jp.kusumotolab.kgenprog.project.GeneratedAST;
import jp.kusumotolab.kgenprog.project.SourceFile;
import jp.kusumotolab.kgenprog.project.TargetProject;

public class JDTASTConstruction {
	public List<GeneratedAST> constructAST(TargetProject project) {
		return constructAST(project.getSourceFiles());
	}
	
	public List<GeneratedAST> constructAST(List<SourceFile> sourceFiles){
		String[] filePaths = sourceFiles.stream()
			.map(file -> file.path)
			.toArray(String[]::new);

		ASTParser parser = ASTParser.newParser(AST.JLS10);
		// TODO: Bindingが必要か検討
		parser.setResolveBindings(false);
		parser.setBindingsRecovery(false);
		parser.setEnvironment(null, null, null, true);

		Map<String, SourceFile> pathToSourceFile = sourceFiles.stream()
			.collect(Collectors.toMap(file -> file.path, file -> file));

		List<GeneratedAST> asts = new ArrayList<>();

		FileASTRequestor requestor = new FileASTRequestor() {
			@Override
			public void acceptAST(String sourceFilePath, CompilationUnit ast) {
				SourceFile file = pathToSourceFile.get(sourceFilePath);
				if(file != null){
					asts.add(new GeneratedJDTAST(file, ast));
				}
			}
		};

		parser.createASTs(filePaths, null, new String[]{}, requestor, null);

		return asts;
	}
	
	public GeneratedAST constructAST(SourceFile file, char[] data){
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_10);

		parser.setCompilerOptions(options);
		parser.setSource(data);

		return new GeneratedJDTAST(file, (CompilationUnit)parser.createAST(null));
	}
}