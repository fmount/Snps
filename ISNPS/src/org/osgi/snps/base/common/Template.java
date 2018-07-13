package org.osgi.snps.base.common;

import java.util.Iterator;
import java.util.List;

import org.osgi.snps.base.de.congrace.exp4j.Calculable;
import org.osgi.snps.base.de.congrace.exp4j.ExpressionBuilder;
import org.osgi.snps.base.json.JSONException;
import org.osgi.snps.base.json.JSONObject;


//import de.congrace.exp4j.Calculable;
//import de.congrace.exp4j.CustomFunction;
//import de.congrace.exp4j.ExpressionBuilder;
//import de.congrace.exp4j.InvalidCustomFunctionException;


public class Template {

	String op;

	public Template(){}
	
	public Template(String op) {
		this.op = op;
	}

	public String process(List<String> datas) {

		if (op.equals("avg")) {
			double sum = 0;
			for (int i = 0; i < datas.size(); i++)
				sum += Double.parseDouble(datas.get(i));
			return String.valueOf(sum / datas.size());
		} else {
			System.out.println("Op not supported!");
			return null;
		}
	}

	public String processExpression(List<String> datas, String expression) {

		double result = 0;
		System.out.println("START PROCESS EXPRESSION");
		if (expression.equals("avg") || expression.equals("none") ) {
			double sum = 0;
			// for(int i=0;i<datas.size();i++)
			// sum+=Double.parseDouble(datas.get(i));
			Iterator<String> it = datas.iterator();
			while (it.hasNext()) {
				String value = it.next();
				try {
					JSONObject json = new JSONObject(value);
					sum += Double.parseDouble(json.getString("value"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return String.valueOf(sum / datas.size());

		} else if (!expression.equals("")) {

			Iterator<String> it = datas.iterator();

			while (it.hasNext()) {
				String value = (String) it.next();
				try {
					JSONObject json = new JSONObject(value);

					expression = expression.replace(json.getString("id"),
							json.getString("value"));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

//			CustomFunction sensorData = null;
//				try {
//					sensorData = new CustomFunction("sgd") {
//
//						@Override
//						public double applyFunction(double[] args) {
//							return args[0];
//						}
//
//					};
//				} catch (InvalidCustomFunctionException e1) {
//					e1.printStackTrace();
//				}
			

			Calculable calc = null;
			try {
				System.out.println("Function calculated" + expression);
				calc = new ExpressionBuilder(expression).build();
				
			} catch (Exception e) {

				return "Invalid expression";
			}

			try {

				result = calc.calculate();

				return String.valueOf(result);

			} catch (Exception e) {

				return "Process Fail";
			}
		}

		else {
			System.out.println("Op not supported!");
			return null;
		}
	}

	// public String processExpression(List<Map<String, String>> datas,
	// String expression) {
	//
	// ArrayList<Map<String, String>> datalist = (ArrayList<Map<String,
	// String>>) datas;
	// double result1;
	// if (expression.equals("avg")) {
	// double sum = 0;
	// // for(int i=0;i<datas.size();i++)
	// // sum+=Double.parseDouble(datas.get(i));
	// Iterator<Map<String, String>> it = datalist.iterator();
	// while (it.hasNext()) {
	// HashMap<String, String> hm = (HashMap<String, String>) it
	// .next();
	// String value = null;
	// for (String key : hm.keySet())
	// value = hm.get(key);
	//
	// sum += Double.parseDouble(value);
	// }
	//
	// return String.valueOf(sum / datas.size());
	// } else if (!expression.equals("")) {
	// CustomFunction sensorData = null;
	// try {
	// sensorData = new CustomFunction("sgd") {
	// public double applyFunction(double[] args) {
	// return args[0];
	// }
	// };
	// } catch (InvalidCustomFunctionException e1) {
	// e1.printStackTrace();
	// }
	//
	// Calculable calc = null;
	// try {
	//
	// calc = new ExpressionBuilder(expression).withCustomFunction(
	// sensorData).build();
	// } catch (Exception e) {
	//
	// return null;
	// }
	//
	//
	// try {
	// result1 = calc.calculate();
	// } catch (Exception e) {
	//
	// return "Process Fail";
	// }
	// }
	//
	// else {
	// System.out.println("Op not supported!");
	// return null;
	// }
	// return String.valueOf(result1);
	// }

}
