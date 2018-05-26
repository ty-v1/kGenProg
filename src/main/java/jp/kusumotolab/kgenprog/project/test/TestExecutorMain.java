package jp.kusumotolab.kgenprog.project.test;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public final class TestExecutorMain {

	@Option(name = "-b", aliases = "--bindir", required = true, usage = "Specify a bin directory")
	private String binDir;

	@Option(name = "-s", aliases = "--src", required = true, usage = "Specify source classes")
	private String sourceClass;

	@Option(name = "-t", aliases = "--test", required = true, usage = "Specify executed test classes")
	private String testClass;

	public static final String SEPARATOR = File.pathSeparator;

	/**
	 * Application entry point <br>
	 * usage: $ java Main -s jp.kusu.TargetClass jp.kusu.TargetClassTest
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		final TestExecutorMain main = new TestExecutorMain();
		final CmdLineParser parser = new CmdLineParser(main);
		parser.parseArgument(args);

		final URL binUrl = Paths.get(main.binDir).toUri().toURL();
		final TestExecutor executor = new TestExecutor(new URL[] { binUrl });

		final TestResults testResults = executor.exec(createFQNs(main.sourceClass), createFQNs(main.testClass));
		TestResults.serialize(testResults);

	}

	private static List<FullyQualifiedName> createFQNs(String names) {
		return Arrays.asList(names.split(SEPARATOR)).stream().map(n -> new FullyQualifiedName(n))
				.collect(Collectors.toList());
	}
}