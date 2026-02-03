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
package org.thingml.xtext.thingML;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ML2 Model Algorithm</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.ML2_ModelAlgorithm#getPrediction_plots <em>Prediction plots</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getML2_ModelAlgorithm()
 * @model
 * @generated
 */
public interface ML2_ModelAlgorithm extends DataAnalyticsModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Prediction plots</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.PredictionPlots}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prediction plots</em>' attribute.
   * @see org.thingml.xtext.thingML.PredictionPlots
   * @see #setPrediction_plots(PredictionPlots)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getML2_ModelAlgorithm_Prediction_plots()
   * @model
   * @generated
   */
  PredictionPlots getPrediction_plots();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ML2_ModelAlgorithm#getPrediction_plots <em>Prediction plots</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prediction plots</em>' attribute.
   * @see org.thingml.xtext.thingML.PredictionPlots
   * @see #getPrediction_plots()
   * @generated
   */
  void setPrediction_plots(PredictionPlots value);

} // ML2_ModelAlgorithm
