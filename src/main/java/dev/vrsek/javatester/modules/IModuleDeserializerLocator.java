package dev.vrsek.javatester.modules;

import dev.vrsek.javatester.core.configuration.classtest.IModuleDeserializer;

import java.util.Collection;

public interface IModuleDeserializerLocator {
	IModuleDeserializer find(String identifier);

	Collection<IModuleDeserializer> findAll();
}