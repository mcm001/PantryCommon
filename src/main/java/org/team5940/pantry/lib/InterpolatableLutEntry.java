 package org.team5940.pantry.lib;

 import org.ghrobotics.lib.mathematics.MathExtensionsKt;
 import org.ghrobotics.lib.types.Interpolatable;

 public class InterpolatableLutEntry implements Interpolatable<Double> {

 	Double value;

 	public Double getValue() {
 		return value;
 	}

 	public InterpolatableLutEntry(Double value) {
 		this.value = value;
 	}

 	public InterpolatableLutEntry(double value) {
 		this(Double.valueOf(value));
 	}

 	@Override
 	public Double interpolate(Double otherVal, double arg1) {
 		return MathExtensionsKt.lerp(value, otherVal, arg1);
 	}

 }
