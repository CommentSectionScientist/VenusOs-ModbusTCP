package de.ossi.modbustcp.data.unit;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Type {

	INT16("int16"), 
	INT32("int32"), 
	UINT16("uint16"), 
	UINT32("uint32"), 
	STRING7("string[7]"), 
	STRING6("string[6]");

	@Getter
	String name;

	public static Type from(String name) {
		return Arrays.stream(Type.values()).filter(a -> a.getName().equals(name)).findFirst().orElse(null);
	}

	public boolean isUnsigned() {
		return name.startsWith("u");
	}
}
