/*
   Copyright 2011 frank asseg

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.osgi.snps.base.de.congrace.exp4j;

import java.util.Map;
import java.util.Stack;

/**
 * A {@link Token} for representing variables
 * 
 * @author fas
 */
class VariableToken extends CalculationToken {
	/**
	 * construct a new {@link VariableToken}
	 * 
	 * @param value
	 *            the value of the token
	 */
	VariableToken(String value) {
		super(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VariableToken) {
			return super.getValue().equals(((VariableToken) obj).getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.getValue().hashCode();
	}

	@Override
	void mutateStackForCalculation(Stack<Double> stack, Map<String, Double> variableValues) {
		double value = variableValues.get(this.getValue());
		stack.push(value);
	}

	@Override
	void mutateStackForInfixTranslation(Stack<Token> operatorStack, StringBuilder output) {
		output.append(this.getValue()).append(" ");
	}
}