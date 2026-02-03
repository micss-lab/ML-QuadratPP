/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 */
package org.thingml.xtext.thingML.tests;

import junit.textui.TestRunner;

import org.thingml.xtext.thingML.ThingMLFactory;
import org.thingml.xtext.thingML.Time_series_ModelAlgorithm;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Time series Model Algorithm</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class Time_series_ModelAlgorithmTest extends DataAnalyticsModelAlgorithmTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(Time_series_ModelAlgorithmTest.class);
	}

	/**
	 * Constructs a new Time series Model Algorithm test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Time_series_ModelAlgorithmTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Time series Model Algorithm test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected Time_series_ModelAlgorithm getFixture() {
		return (Time_series_ModelAlgorithm)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(ThingMLFactory.eINSTANCE.createTime_series_ModelAlgorithm());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //Time_series_ModelAlgorithmTest
