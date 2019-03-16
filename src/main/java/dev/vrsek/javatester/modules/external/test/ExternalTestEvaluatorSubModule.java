package dev.vrsek.javatester.modules.external.test;

import com.google.common.collect.Iterables;
import dev.vrsek.javatester.modules.RootEvaluationContext;
import dev.vrsek.javatester.modules.external.test.configuration.model.ExternalTestEvaluation;
import dev.vrsek.utils.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

// TODO: implements interface SubModuleEvaluator
public class ExternalTestEvaluatorSubModule {
	public void evaluate(Class externalClass, ExternalTestEvaluation configuration, RootEvaluationContext context) {
		Class evaluatedClass = context.getEvaluatedClass();

		if (!inheritsFromInterface(evaluatedClass, configuration.getResultType())
				&& !inheritsFromClass(evaluatedClass, configuration.getResultType())) {
			Logger.log("- Tovarní třida nedědí od " + configuration.getResultType());

			return;
		}
		// TODO: Error handling


		Object externalClassInstance = null, evaluatedClassInstance = null;
		try {
			evaluatedClassInstance = evaluatedClass.getConstructor().newInstance();
			externalClassInstance = externalClass.getConstructor().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		Method[] methods = externalClass.getMethods();
		Method testMethod = Arrays.stream(methods).filter(x -> x.getName() == "test").findFirst().get();

		try {
			testMethod.invoke(externalClassInstance, evaluatedClassInstance);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private boolean inheritsFromInterface(Class evaluatedClass, String interfaceType) {
		List<Class> interfaces = Arrays.asList(evaluatedClass.getInterfaces());

		return Iterables.any(interfaces, x -> x.getTypeName().equals(interfaceType));
	}

	private boolean inheritsFromClass(Class evaluatedClass, String classType) {
		Class superClass = evaluatedClass.getSuperclass();

		return superClass.getTypeName().equals(classType);
	}
}