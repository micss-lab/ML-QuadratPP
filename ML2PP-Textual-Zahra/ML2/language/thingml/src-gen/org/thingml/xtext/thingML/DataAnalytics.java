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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Analytics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getDataset <em>Dataset</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getLabels <em>Labels</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getFeatures <em>Features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getInput_features <em>Input features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getOutput_features <em>Output features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getTimestamps <em>Timestamps</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getCombine_threshold <em>Combine threshold</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getPreprocess_feature_scaler <em>Preprocess feature scaler</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getPreprocess_sample_normalizer <em>Preprocess sample normalizer</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getFill_missing_value <em>Fill missing value</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getRemove_outliers <em>Remove outliers</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getAdvanced_imputation <em>Advanced imputation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getLagged_features <em>Lagged features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getRolling_window_features <em>Rolling window features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getResampling <em>Resampling</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getTransformations <em>Transformations</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getSequential <em>Sequential</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getSteps <em>Steps</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getLag <em>Lag</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getMultivariate <em>Multivariate</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getStationary <em>Stationary</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getSeasonality_detection <em>Seasonality detection</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getSupervised_learning <em>Supervised learning</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getCreate_lagged_features <em>Create lagged features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getSliding_window <em>Sliding window</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getAutoML <em>Auto ML</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getModelAlgorithm <em>Model Algorithm</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getBlackbox_ml <em>Blackbox ml</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getBlackbox_ml_model <em>Blackbox ml model</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getBlackbox_import_algorithm <em>Blackbox import algorithm</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getBlackbox_label_encoder <em>Blackbox label encoder</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getTrainingResults <em>Training Results</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getHyperparameter_tuning <em>Hyperparameter tuning</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getEnsemble_methods <em>Ensemble methods</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getPredictionResults <em>Prediction Results</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getModel_evaluation <em>Model evaluation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getOutlier_detection <em>Outlier detection</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getTime_series_clustering <em>Time series clustering</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getContextArea <em>Context Area</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.DataAnalytics#getPlots <em>Plots</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics()
 * @model
 * @generated
 */
public interface DataAnalytics extends NamedElement, AnnotatedElement
{
  /**
   * Returns the value of the '<em><b>Dataset</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Dataset</em>' attribute.
   * @see #setDataset(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Dataset()
   * @model
   * @generated
   */
  String getDataset();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getDataset <em>Dataset</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Dataset</em>' attribute.
   * @see #getDataset()
   * @generated
   */
  void setDataset(String value);

  /**
   * Returns the value of the '<em><b>Labels</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Labels}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Labels</em>' attribute.
   * @see org.thingml.xtext.thingML.Labels
   * @see #setLabels(Labels)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Labels()
   * @model
   * @generated
   */
  Labels getLabels();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getLabels <em>Labels</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Labels</em>' attribute.
   * @see org.thingml.xtext.thingML.Labels
   * @see #getLabels()
   * @generated
   */
  void setLabels(Labels value);

  /**
   * Returns the value of the '<em><b>Features</b></em>' reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.Property}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Features</em>' reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Features()
   * @model
   * @generated
   */
  EList<Property> getFeatures();

  /**
   * Returns the value of the '<em><b>Input features</b></em>' reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.Property}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Input features</em>' reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Input_features()
   * @model
   * @generated
   */
  EList<Property> getInput_features();

  /**
   * Returns the value of the '<em><b>Output features</b></em>' reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.Property}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Output features</em>' reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Output_features()
   * @model
   * @generated
   */
  EList<Property> getOutput_features();

  /**
   * Returns the value of the '<em><b>Timestamps</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Timestamps}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Timestamps</em>' attribute.
   * @see org.thingml.xtext.thingML.Timestamps
   * @see #setTimestamps(Timestamps)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Timestamps()
   * @model
   * @generated
   */
  Timestamps getTimestamps();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getTimestamps <em>Timestamps</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Timestamps</em>' attribute.
   * @see org.thingml.xtext.thingML.Timestamps
   * @see #getTimestamps()
   * @generated
   */
  void setTimestamps(Timestamps value);

  /**
   * Returns the value of the '<em><b>Combine threshold</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Combine threshold</em>' attribute.
   * @see #setCombine_threshold(long)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Combine_threshold()
   * @model
   * @generated
   */
  long getCombine_threshold();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getCombine_threshold <em>Combine threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Combine threshold</em>' attribute.
   * @see #getCombine_threshold()
   * @generated
   */
  void setCombine_threshold(long value);

  /**
   * Returns the value of the '<em><b>Preprocess feature scaler</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Preprocess_feature_scaler}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Preprocess feature scaler</em>' attribute.
   * @see org.thingml.xtext.thingML.Preprocess_feature_scaler
   * @see #setPreprocess_feature_scaler(Preprocess_feature_scaler)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Preprocess_feature_scaler()
   * @model
   * @generated
   */
  Preprocess_feature_scaler getPreprocess_feature_scaler();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getPreprocess_feature_scaler <em>Preprocess feature scaler</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Preprocess feature scaler</em>' attribute.
   * @see org.thingml.xtext.thingML.Preprocess_feature_scaler
   * @see #getPreprocess_feature_scaler()
   * @generated
   */
  void setPreprocess_feature_scaler(Preprocess_feature_scaler value);

  /**
   * Returns the value of the '<em><b>Preprocess sample normalizer</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Preprocess_sample_normalizer}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Preprocess sample normalizer</em>' attribute.
   * @see org.thingml.xtext.thingML.Preprocess_sample_normalizer
   * @see #setPreprocess_sample_normalizer(Preprocess_sample_normalizer)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Preprocess_sample_normalizer()
   * @model
   * @generated
   */
  Preprocess_sample_normalizer getPreprocess_sample_normalizer();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getPreprocess_sample_normalizer <em>Preprocess sample normalizer</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Preprocess sample normalizer</em>' attribute.
   * @see org.thingml.xtext.thingML.Preprocess_sample_normalizer
   * @see #getPreprocess_sample_normalizer()
   * @generated
   */
  void setPreprocess_sample_normalizer(Preprocess_sample_normalizer value);

  /**
   * Returns the value of the '<em><b>Fill missing value</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.FillingMissingValue}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fill missing value</em>' attribute.
   * @see org.thingml.xtext.thingML.FillingMissingValue
   * @see #setFill_missing_value(FillingMissingValue)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Fill_missing_value()
   * @model
   * @generated
   */
  FillingMissingValue getFill_missing_value();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getFill_missing_value <em>Fill missing value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fill missing value</em>' attribute.
   * @see org.thingml.xtext.thingML.FillingMissingValue
   * @see #getFill_missing_value()
   * @generated
   */
  void setFill_missing_value(FillingMissingValue value);

  /**
   * Returns the value of the '<em><b>Remove outliers</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.RemoveOutliers}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remove outliers</em>' attribute.
   * @see org.thingml.xtext.thingML.RemoveOutliers
   * @see #setRemove_outliers(RemoveOutliers)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Remove_outliers()
   * @model
   * @generated
   */
  RemoveOutliers getRemove_outliers();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getRemove_outliers <em>Remove outliers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remove outliers</em>' attribute.
   * @see org.thingml.xtext.thingML.RemoveOutliers
   * @see #getRemove_outliers()
   * @generated
   */
  void setRemove_outliers(RemoveOutliers value);

  /**
   * Returns the value of the '<em><b>Advanced imputation</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.AdvancedImputation}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Advanced imputation</em>' attribute.
   * @see org.thingml.xtext.thingML.AdvancedImputation
   * @see #setAdvanced_imputation(AdvancedImputation)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Advanced_imputation()
   * @model
   * @generated
   */
  AdvancedImputation getAdvanced_imputation();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getAdvanced_imputation <em>Advanced imputation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Advanced imputation</em>' attribute.
   * @see org.thingml.xtext.thingML.AdvancedImputation
   * @see #getAdvanced_imputation()
   * @generated
   */
  void setAdvanced_imputation(AdvancedImputation value);

  /**
   * Returns the value of the '<em><b>Lagged features</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.LaggedFeatures}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Lagged features</em>' attribute.
   * @see org.thingml.xtext.thingML.LaggedFeatures
   * @see #setLagged_features(LaggedFeatures)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Lagged_features()
   * @model
   * @generated
   */
  LaggedFeatures getLagged_features();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getLagged_features <em>Lagged features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Lagged features</em>' attribute.
   * @see org.thingml.xtext.thingML.LaggedFeatures
   * @see #getLagged_features()
   * @generated
   */
  void setLagged_features(LaggedFeatures value);

  /**
   * Returns the value of the '<em><b>Rolling window features</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.RollingWindowFeatures}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rolling window features</em>' attribute.
   * @see org.thingml.xtext.thingML.RollingWindowFeatures
   * @see #setRolling_window_features(RollingWindowFeatures)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Rolling_window_features()
   * @model
   * @generated
   */
  RollingWindowFeatures getRolling_window_features();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getRolling_window_features <em>Rolling window features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rolling window features</em>' attribute.
   * @see org.thingml.xtext.thingML.RollingWindowFeatures
   * @see #getRolling_window_features()
   * @generated
   */
  void setRolling_window_features(RollingWindowFeatures value);

  /**
   * Returns the value of the '<em><b>Resampling</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Resampling}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resampling</em>' attribute.
   * @see org.thingml.xtext.thingML.Resampling
   * @see #setResampling(Resampling)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Resampling()
   * @model
   * @generated
   */
  Resampling getResampling();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getResampling <em>Resampling</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resampling</em>' attribute.
   * @see org.thingml.xtext.thingML.Resampling
   * @see #getResampling()
   * @generated
   */
  void setResampling(Resampling value);

  /**
   * Returns the value of the '<em><b>Transformations</b></em>' attribute list.
   * The list contents are of type {@link org.thingml.xtext.thingML.DataTransformation}.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.DataTransformation}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Transformations</em>' attribute list.
   * @see org.thingml.xtext.thingML.DataTransformation
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Transformations()
   * @model unique="false"
   * @generated
   */
  EList<DataTransformation> getTransformations();

  /**
   * Returns the value of the '<em><b>Sequential</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Sequential}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sequential</em>' attribute.
   * @see org.thingml.xtext.thingML.Sequential
   * @see #setSequential(Sequential)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Sequential()
   * @model
   * @generated
   */
  Sequential getSequential();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getSequential <em>Sequential</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sequential</em>' attribute.
   * @see org.thingml.xtext.thingML.Sequential
   * @see #getSequential()
   * @generated
   */
  void setSequential(Sequential value);

  /**
   * Returns the value of the '<em><b>Steps</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Steps</em>' containment reference.
   * @see #setSteps(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Steps()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getSteps();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getSteps <em>Steps</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Steps</em>' containment reference.
   * @see #getSteps()
   * @generated
   */
  void setSteps(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Lag</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Lag</em>' containment reference.
   * @see #setLag(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Lag()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getLag();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getLag <em>Lag</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Lag</em>' containment reference.
   * @see #getLag()
   * @generated
   */
  void setLag(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Multivariate</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Multivariate}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Multivariate</em>' attribute.
   * @see org.thingml.xtext.thingML.Multivariate
   * @see #setMultivariate(Multivariate)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Multivariate()
   * @model
   * @generated
   */
  Multivariate getMultivariate();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getMultivariate <em>Multivariate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Multivariate</em>' attribute.
   * @see org.thingml.xtext.thingML.Multivariate
   * @see #getMultivariate()
   * @generated
   */
  void setMultivariate(Multivariate value);

  /**
   * Returns the value of the '<em><b>Stationary</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Stationary}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Stationary</em>' attribute.
   * @see org.thingml.xtext.thingML.Stationary
   * @see #setStationary(Stationary)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Stationary()
   * @model
   * @generated
   */
  Stationary getStationary();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getStationary <em>Stationary</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Stationary</em>' attribute.
   * @see org.thingml.xtext.thingML.Stationary
   * @see #getStationary()
   * @generated
   */
  void setStationary(Stationary value);

  /**
   * Returns the value of the '<em><b>Seasonality detection</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.SeasonalityDetection}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonality detection</em>' attribute.
   * @see org.thingml.xtext.thingML.SeasonalityDetection
   * @see #setSeasonality_detection(SeasonalityDetection)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Seasonality_detection()
   * @model
   * @generated
   */
  SeasonalityDetection getSeasonality_detection();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getSeasonality_detection <em>Seasonality detection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonality detection</em>' attribute.
   * @see org.thingml.xtext.thingML.SeasonalityDetection
   * @see #getSeasonality_detection()
   * @generated
   */
  void setSeasonality_detection(SeasonalityDetection value);

  /**
   * Returns the value of the '<em><b>Supervised learning</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.SupervisedLearning}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Supervised learning</em>' attribute.
   * @see org.thingml.xtext.thingML.SupervisedLearning
   * @see #setSupervised_learning(SupervisedLearning)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Supervised_learning()
   * @model
   * @generated
   */
  SupervisedLearning getSupervised_learning();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getSupervised_learning <em>Supervised learning</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Supervised learning</em>' attribute.
   * @see org.thingml.xtext.thingML.SupervisedLearning
   * @see #getSupervised_learning()
   * @generated
   */
  void setSupervised_learning(SupervisedLearning value);

  /**
   * Returns the value of the '<em><b>Create lagged features</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.CreateLaggedFeatures}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Create lagged features</em>' attribute.
   * @see org.thingml.xtext.thingML.CreateLaggedFeatures
   * @see #setCreate_lagged_features(CreateLaggedFeatures)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Create_lagged_features()
   * @model
   * @generated
   */
  CreateLaggedFeatures getCreate_lagged_features();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getCreate_lagged_features <em>Create lagged features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Create lagged features</em>' attribute.
   * @see org.thingml.xtext.thingML.CreateLaggedFeatures
   * @see #getCreate_lagged_features()
   * @generated
   */
  void setCreate_lagged_features(CreateLaggedFeatures value);

  /**
   * Returns the value of the '<em><b>Sliding window</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.SlidingWindow}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sliding window</em>' attribute.
   * @see org.thingml.xtext.thingML.SlidingWindow
   * @see #setSliding_window(SlidingWindow)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Sliding_window()
   * @model
   * @generated
   */
  SlidingWindow getSliding_window();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getSliding_window <em>Sliding window</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sliding window</em>' attribute.
   * @see org.thingml.xtext.thingML.SlidingWindow
   * @see #getSliding_window()
   * @generated
   */
  void setSliding_window(SlidingWindow value);

  /**
   * Returns the value of the '<em><b>Auto ML</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.AutoML}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Auto ML</em>' attribute.
   * @see org.thingml.xtext.thingML.AutoML
   * @see #setAutoML(AutoML)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_AutoML()
   * @model
   * @generated
   */
  AutoML getAutoML();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getAutoML <em>Auto ML</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Auto ML</em>' attribute.
   * @see org.thingml.xtext.thingML.AutoML
   * @see #getAutoML()
   * @generated
   */
  void setAutoML(AutoML value);

  /**
   * Returns the value of the '<em><b>Model Algorithm</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Model Algorithm</em>' containment reference.
   * @see #setModelAlgorithm(DataAnalyticsModelAlgorithm)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_ModelAlgorithm()
   * @model containment="true"
   * @generated
   */
  DataAnalyticsModelAlgorithm getModelAlgorithm();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getModelAlgorithm <em>Model Algorithm</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Model Algorithm</em>' containment reference.
   * @see #getModelAlgorithm()
   * @generated
   */
  void setModelAlgorithm(DataAnalyticsModelAlgorithm value);

  /**
   * Returns the value of the '<em><b>Blackbox ml</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Blackbox ml</em>' containment reference.
   * @see #setBlackbox_ml(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Blackbox_ml()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getBlackbox_ml();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getBlackbox_ml <em>Blackbox ml</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Blackbox ml</em>' containment reference.
   * @see #getBlackbox_ml()
   * @generated
   */
  void setBlackbox_ml(BooleanLiteral value);

  /**
   * Returns the value of the '<em><b>Blackbox ml model</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Blackbox ml model</em>' attribute.
   * @see #setBlackbox_ml_model(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Blackbox_ml_model()
   * @model
   * @generated
   */
  String getBlackbox_ml_model();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getBlackbox_ml_model <em>Blackbox ml model</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Blackbox ml model</em>' attribute.
   * @see #getBlackbox_ml_model()
   * @generated
   */
  void setBlackbox_ml_model(String value);

  /**
   * Returns the value of the '<em><b>Blackbox import algorithm</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Blackbox import algorithm</em>' attribute.
   * @see #setBlackbox_import_algorithm(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Blackbox_import_algorithm()
   * @model
   * @generated
   */
  String getBlackbox_import_algorithm();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getBlackbox_import_algorithm <em>Blackbox import algorithm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Blackbox import algorithm</em>' attribute.
   * @see #getBlackbox_import_algorithm()
   * @generated
   */
  void setBlackbox_import_algorithm(String value);

  /**
   * Returns the value of the '<em><b>Blackbox label encoder</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Blackbox label encoder</em>' attribute.
   * @see #setBlackbox_label_encoder(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Blackbox_label_encoder()
   * @model
   * @generated
   */
  String getBlackbox_label_encoder();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getBlackbox_label_encoder <em>Blackbox label encoder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Blackbox label encoder</em>' attribute.
   * @see #getBlackbox_label_encoder()
   * @generated
   */
  void setBlackbox_label_encoder(String value);

  /**
   * Returns the value of the '<em><b>Training Results</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Training Results</em>' attribute.
   * @see #setTrainingResults(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_TrainingResults()
   * @model
   * @generated
   */
  String getTrainingResults();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getTrainingResults <em>Training Results</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Training Results</em>' attribute.
   * @see #getTrainingResults()
   * @generated
   */
  void setTrainingResults(String value);

  /**
   * Returns the value of the '<em><b>Hyperparameter tuning</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.HyperparameterTuning}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hyperparameter tuning</em>' attribute.
   * @see org.thingml.xtext.thingML.HyperparameterTuning
   * @see #setHyperparameter_tuning(HyperparameterTuning)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Hyperparameter_tuning()
   * @model
   * @generated
   */
  HyperparameterTuning getHyperparameter_tuning();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getHyperparameter_tuning <em>Hyperparameter tuning</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hyperparameter tuning</em>' attribute.
   * @see org.thingml.xtext.thingML.HyperparameterTuning
   * @see #getHyperparameter_tuning()
   * @generated
   */
  void setHyperparameter_tuning(HyperparameterTuning value);

  /**
   * Returns the value of the '<em><b>Ensemble methods</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.EnsembleMethods}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ensemble methods</em>' attribute.
   * @see org.thingml.xtext.thingML.EnsembleMethods
   * @see #setEnsemble_methods(EnsembleMethods)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Ensemble_methods()
   * @model
   * @generated
   */
  EnsembleMethods getEnsemble_methods();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getEnsemble_methods <em>Ensemble methods</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ensemble methods</em>' attribute.
   * @see org.thingml.xtext.thingML.EnsembleMethods
   * @see #getEnsemble_methods()
   * @generated
   */
  void setEnsemble_methods(EnsembleMethods value);

  /**
   * Returns the value of the '<em><b>Prediction Results</b></em>' reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.Property}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prediction Results</em>' reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_PredictionResults()
   * @model
   * @generated
   */
  EList<Property> getPredictionResults();

  /**
   * Returns the value of the '<em><b>Model evaluation</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.ModelEvaluation}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Model evaluation</em>' attribute.
   * @see org.thingml.xtext.thingML.ModelEvaluation
   * @see #setModel_evaluation(ModelEvaluation)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Model_evaluation()
   * @model
   * @generated
   */
  ModelEvaluation getModel_evaluation();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getModel_evaluation <em>Model evaluation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Model evaluation</em>' attribute.
   * @see org.thingml.xtext.thingML.ModelEvaluation
   * @see #getModel_evaluation()
   * @generated
   */
  void setModel_evaluation(ModelEvaluation value);

  /**
   * Returns the value of the '<em><b>Outlier detection</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.OutlierDetection}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Outlier detection</em>' attribute.
   * @see org.thingml.xtext.thingML.OutlierDetection
   * @see #setOutlier_detection(OutlierDetection)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Outlier_detection()
   * @model
   * @generated
   */
  OutlierDetection getOutlier_detection();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getOutlier_detection <em>Outlier detection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Outlier detection</em>' attribute.
   * @see org.thingml.xtext.thingML.OutlierDetection
   * @see #getOutlier_detection()
   * @generated
   */
  void setOutlier_detection(OutlierDetection value);

  /**
   * Returns the value of the '<em><b>Time series clustering</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.TimeSeriesClustering}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Time series clustering</em>' attribute.
   * @see org.thingml.xtext.thingML.TimeSeriesClustering
   * @see #setTime_series_clustering(TimeSeriesClustering)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Time_series_clustering()
   * @model
   * @generated
   */
  TimeSeriesClustering getTime_series_clustering();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getTime_series_clustering <em>Time series clustering</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Time series clustering</em>' attribute.
   * @see org.thingml.xtext.thingML.TimeSeriesClustering
   * @see #getTime_series_clustering()
   * @generated
   */
  void setTime_series_clustering(TimeSeriesClustering value);

  /**
   * Returns the value of the '<em><b>Context Area</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.ContextArea}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Context Area</em>' attribute.
   * @see org.thingml.xtext.thingML.ContextArea
   * @see #setContextArea(ContextArea)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_ContextArea()
   * @model
   * @generated
   */
  ContextArea getContextArea();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.DataAnalytics#getContextArea <em>Context Area</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Context Area</em>' attribute.
   * @see org.thingml.xtext.thingML.ContextArea
   * @see #getContextArea()
   * @generated
   */
  void setContextArea(ContextArea value);

  /**
   * Returns the value of the '<em><b>Plots</b></em>' attribute list.
   * The list contents are of type {@link org.thingml.xtext.thingML.PreprocessingPlots}.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.PreprocessingPlots}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Plots</em>' attribute list.
   * @see org.thingml.xtext.thingML.PreprocessingPlots
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDataAnalytics_Plots()
   * @model unique="false"
   * @generated
   */
  EList<PreprocessingPlots> getPlots();

} // DataAnalytics
