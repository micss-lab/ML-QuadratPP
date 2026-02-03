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

import org.thingml.xtext.thingML.ML2_ModelAlgorithm;
import org.thingml.xtext.thingML.ThingMLFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>ML2 Model Algorithm</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ML2_ModelAlgorithmTest extends DataAnalyticsModelAlgorithmTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(ML2_ModelAlgorithmTest.class);
	}

	/**
	 * Constructs a new ML2 Model Algorithm test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ML2_ModelAlgorithmTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this ML2 Model Algorithm test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected ML2_ModelAlgorithm getFixture() {
		return (ML2_ModelAlgorithm)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(ThingMLFactory.eINSTANCE.createML2_ModelAlgorithm());
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

} //ML2_ModelAlgorithmTest
