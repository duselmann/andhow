package org.yarnandtail.andhow.compile;

import com.sun.source.util.Trees;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.*;
import javax.lang.model.element.*;

import static javax.lang.model.element.ElementKind.*;

import javax.tools.FileObject;
import org.yarnandtail.andhow.GlobalPropertyGroup;

/**
 *
 * Note: check to ensure that Props are not referenced in static init blocks b/c
 * we may need to load the class (and run its init) before andHow init can
 * complete, causing a circular init loop.
 *
 * @author ericeverman
 */
@SupportedAnnotationTypes("*")
public class AndHowCompileProcessor extends AbstractProcessor {

	public static final String GENERATED_CLASS_PREFIX = "$GlobalPropGrpStub";
	public static final String GENERATED_CLASS_NESTED_SEP = "$";

	private static final String SERVICES_PACKAGE = "";
	private static final String RELATIVE_NAME = "META-INF/services/org.yarnandtail.andhow.GlobalPropertyGroupStub";

	//Static to insure all generated classes have the same timestamp
	private static Calendar runDate;

	private Trees trees;

	public AndHowCompileProcessor() {
		//required by Processor API
		runDate = new GregorianCalendar();
	}


	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		Filer filer = this.processingEnv.getFiler();


		StringBuffer existingServiceFileContent = new StringBuffer();
		StringBuffer newServiceFileContent = new StringBuffer();

		try {

			FileObject groupFile = filer.getResource(
					javax.tools.StandardLocation.SOURCE_OUTPUT,
					SERVICES_PACKAGE, RELATIVE_NAME);

			if (groupFile != null) {
				existingServiceFileContent.append(groupFile.getCharContent(true));
			}

		} catch (IOException ex) {
			//Ignore - This just means the file doesn't exist
		}

		//
		//Scan all the Compilation units (i.e. class files) for AndHow Properties
		Iterator<? extends Element> it = roundEnv.getRootElements().iterator();
		for (Element e : roundEnv.getRootElements()) {


			TypeElement te = (TypeElement) e;
			AndHowElementScanner7 st = new AndHowElementScanner7(this.processingEnv);
			CompileUnit ret = st.scan(e);

			if (ret.hasRegistrations()) {
				for (PropertyRegistration p : ret.getRegistrations()) {

					System.out.println("Found Property '" + p.getCanonicalPropertyName() + "'in : " + p.getCanonicalRootName() + " parent class: " + p.getJavaCanonicalParentName());

				}
			}

			for (String err : ret.getErrors()) {
				System.out.println("Found Error: " + err);
			}

		}

		/*
		for (Element e : globalGroups) {

			if (e.getKind().equals(ElementKind.INTERFACE)) {
				TypeElement ie = (TypeElement) e;

				GroupModel groupModel = new GroupModel(ie);

				trace("Writing proxy class for " + ie.getQualifiedName());

				writeClassFile(filer, groupModel);

				newServiceFileContent.append(groupModel.buildNameModel().getQualifiedName());
				newServiceFileContent.append(System.lineSeparator());

			} else {
				throw new RuntimeException("Not able to handle anything other than an interface currently");
			}

		}

		if (existingServiceFileContent.length() == 0) {
			trace("New " + RELATIVE_NAME + " file created");
			existingServiceFileContent.append("# GENERATED BY THE AndHow AndHowCompileProcessor.");
		}

		try {

			FileObject groupFile = filer.createResource(javax.tools.StandardLocation.SOURCE_OUTPUT,
					SERVICES_PACKAGE, RELATIVE_NAME, globalGroups.toArray(new Element[globalGroups.size()]));

			try (Writer writer = groupFile.openWriter()) {
				writer.write(existingServiceFileContent.toString());
				writer.write(System.lineSeparator());
				writer.write(newServiceFileContent.toString());
			}

		} catch (IOException ex) {
			System.err.println("FAILED TO RUN COMPILE PROCESSOR");
			Logger.getLogger(AndHowCompileProcessor.class.getName()).log(Level.SEVERE, null, ex);
		}

		*/

		return false;

	}
//
//	public String generateClassString(String pkgName, String genSimpleName, String causeSimpleName) {
//		String templatePath
//				= "/" + this.getClass().getCanonicalName().replace(".", "/") + "_Template.txt";
//
//		try {
//
//			InputStream in = AndHowCompileProcessor.class.getResourceAsStream(templatePath);
//			if (in == null) {
//				throw new Exception("resource not found: " + templatePath);
//			}
//
//			StringBuffer buf = new StringBuffer();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//			String line = reader.readLine();
//			while (line != null) {
//				buf.append(line).append(System.lineSeparator());
//				line = reader.readLine();
//			}
//
//			reader.close();
//
//			//String template = new String(Files.readAllBytes(Paths.get(getClass().getResource(templatePath).toURI())));
//			String source = String.format(buf.toString(),
//					pkgName, genSimpleName,
//					causeSimpleName,
//					GlobalPropertyGroupStub.class.getCanonicalName(),
//					this.getClass().getCanonicalName(), runDate,
//					"Build a 2d array here..."
//			);
//			return source;
//		} catch (Exception ex) {
//			error("Error finding or formatting '" + templatePath + "'", ex);
//			return null;	//error throws a runtime
//		}
//
//	}
//
//	public void writeClassFile(Filer filer, GroupModel causingElement) {
//
//		String genFullName = "";
//
//		try {
//
//			String causeFullName = causingElement.getLeafElement().getQualifiedName().toString();
//			String causeSimpleName = causingElement.getLeafElement().getSimpleName().toString();
//			String pkgName = causingElement.getPackage().getQualifiedName().toString();
//			String genSimpleName = causingElement.buildNameModel().getClassName();
//			genFullName = (pkgName.length() > 0) ? pkgName + "." + genSimpleName : genSimpleName;
//
//			String classContent = this.generateClassString(pkgName, genSimpleName, causeSimpleName);
//
//			trace("Writing " + genFullName + " as a generated source file");
//
//			FileObject classFile = filer.createSourceFile(
//					genFullName, new Element[]{causingElement.getLeafElement()});
//
//			try (Writer writer = classFile.openWriter()) {
//				writer.write(classContent);
//			}
//		} catch (IOException ex) {
//			error("Error attempting to write the generated class file: " + genFullName, ex);
//		}
//	}

	public List<List<String>> buildGroupList(Element start) {
		ElementKind kind = start.getKind();

		switch (kind) {
			case INTERFACE:
			case CLASS:
				break;
			case FIELD:
			case LOCAL_VARIABLE:
				break;
			default:
			//ignore
		}

		return null;
	}

	public static void trace(String msg) {
		System.out.println("AndHowCompileProcessor: " + msg);
	}

	public static void error(String msg) {
		error(msg, null);
	}

	public static void error(String msg, Exception e) {
		System.err.println("AndHowCompileProcessor: " + msg);
		throw new RuntimeException(msg, e);
	}

}
