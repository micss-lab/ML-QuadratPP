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
package org.thingml.xtext.thingML.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.thingml.xtext.thingML.AdvancedImputation;
import org.thingml.xtext.thingML.AnnotatedElement;
import org.thingml.xtext.thingML.AutoML;
import org.thingml.xtext.thingML.BooleanLiteral;
import org.thingml.xtext.thingML.ContextArea;
import org.thingml.xtext.thingML.CreateLaggedFeatures;
import org.thingml.xtext.thingML.DataAnalytics;
import org.thingml.xtext.thingML.DataAnalyticsModelAlgorithm;
import org.thingml.xtext.thingML.DataTransformation;
import org.thingml.xtext.thingML.EnsembleMethods;
import org.thingml.xtext.thingML.FillingMissingValue;
import org.thingml.xtext.thingML.HyperparameterTuning;
import org.thingml.xtext.thingML.IntegerLiteral;
import org.thingml.xtext.thingML.Labels;
import org.thingml.xtext.thingML.LaggedFeatures;
import org.thingml.xtext.thingML.ModelEvaluation;
import org.thingml.xtext.thingML.Multivariate;
import org.thingml.xtext.thingML.OutlierDetection;
import org.thingml.xtext.thingML.PlatformAnnotation;
import org.thingml.xtext.thingML.Preprocess_feature_scaler;
import org.thingml.xtext.thingML.Preprocess_sample_normalizer;
import org.thingml.xtext.thingML.PreprocessingPlots;
import org.thingml.xtext.thingML.Property;
import org.thingml.xtext.thingML.RemoveOutliers;
import org.thingml.xtext.thingML.Resampling;
import org.thingml.xtext.thingML.RollingWindowFeatures;
import org.thingml.xtext.thingML.SeasonalityDetection;
import org.thingml.xtext.thingML.Sequential;
import org.thingml.xtext.thingML.SlidingWindow;
import org.thingml.xtext.thingML.Stationary;
import org.thingml.xtext.thingML.SupervisedLearning;
import org.thingml.xtext.thingML.ThingMLPackage;
import org.thingml.xtext.thingML.TimeSeriesClustering;
import org.thingml.xtext.thingML.Timestamps;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Analytics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getAnnotations <em>Annotations</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getDataset <em>Dataset</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getLabels <em>Labels</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getFeatures <em>Features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getInput_features <em>Input features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getOutput_features <em>Output features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getTimestamps <em>Timestamps</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getCombine_threshold <em>Combine threshold</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getPreprocess_feature_scaler <em>Preprocess feature scaler</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getPreprocess_sample_normalizer <em>Preprocess sample normalizer</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getFill_missing_value <em>Fill missing value</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getRemove_outliers <em>Remove outliers</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getAdvanced_imputation <em>Advanced imputation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getLagged_features <em>Lagged features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getRolling_window_features <em>Rolling window features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getResampling <em>Resampling</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getTransformations <em>Transformations</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getSequential <em>Sequential</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getSteps <em>Steps</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getLag <em>Lag</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getMultivariate <em>Multivariate</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getStationary <em>Stationary</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getSeasonality_detection <em>Seasonality detection</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getSupervised_learning <em>Supervised learning</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getCreate_lagged_features <em>Create lagged features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getSliding_window <em>Sliding window</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getAutoML <em>Auto ML</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getModelAlgorithm <em>Model Algorithm</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getBlackbox_ml <em>Blackbox ml</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getBlackbox_ml_model <em>Blackbox ml model</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getBlackbox_import_algorithm <em>Blackbox import algorithm</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getBlackbox_label_encoder <em>Blackbox label encoder</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getTrainingResults <em>Training Results</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getHyperparameter_tuning <em>Hyperparameter tuning</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getEnsemble_methods <em>Ensemble methods</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getPredictionResults <em>Prediction Results</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getModel_evaluation <em>Model evaluation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getOutlier_detection <em>Outlier detection</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getTime_series_clustering <em>Time series clustering</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getContextArea <em>Context Area</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.DataAnalyticsImpl#getPlots <em>Plots</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataAnalyticsImpl extends NamedElementImpl implements DataAnalytics
{
  /**
   * The cached value of the '{@link #getAnnotations() <em>Annotations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAnnotations()
   * @generated
   * @ordered
   */
  protected EList<PlatformAnnotation> annotations;

  /**
   * The default value of the '{@link #getDataset() <em>Dataset</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDataset()
   * @generated
   * @ordered
   */
  protected static final String DATASET_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDataset() <em>Dataset</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDataset()
   * @generated
   * @ordered
   */
  protected String dataset = DATASET_EDEFAULT;

  /**
   * The default value of the '{@link #getLabels() <em>Labels</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabels()
   * @generated
   * @ordered
   */
  protected static final Labels LABELS_EDEFAULT = Labels.NOT_SET;

  /**
   * The cached value of the '{@link #getLabels() <em>Labels</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabels()
   * @generated
   * @ordered
   */
  protected Labels labels = LABELS_EDEFAULT;

  /**
   * The cached value of the '{@link #getFeatures() <em>Features</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFeatures()
   * @generated
   * @ordered
   */
  protected EList<Property> features;

  /**
   * The cached value of the '{@link #getInput_features() <em>Input features</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInput_features()
   * @generated
   * @ordered
   */
  protected EList<Property> input_features;

  /**
   * The cached value of the '{@link #getOutput_features() <em>Output features</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutput_features()
   * @generated
   * @ordered
   */
  protected EList<Property> output_features;

  /**
   * The default value of the '{@link #getTimestamps() <em>Timestamps</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimestamps()
   * @generated
   * @ordered
   */
  protected static final Timestamps TIMESTAMPS_EDEFAULT = Timestamps.NOT_SET;

  /**
   * The cached value of the '{@link #getTimestamps() <em>Timestamps</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimestamps()
   * @generated
   * @ordered
   */
  protected Timestamps timestamps = TIMESTAMPS_EDEFAULT;

  /**
   * The default value of the '{@link #getCombine_threshold() <em>Combine threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCombine_threshold()
   * @generated
   * @ordered
   */
  protected static final long COMBINE_THRESHOLD_EDEFAULT = 0L;

  /**
   * The cached value of the '{@link #getCombine_threshold() <em>Combine threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCombine_threshold()
   * @generated
   * @ordered
   */
  protected long combine_threshold = COMBINE_THRESHOLD_EDEFAULT;

  /**
   * The default value of the '{@link #getPreprocess_feature_scaler() <em>Preprocess feature scaler</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreprocess_feature_scaler()
   * @generated
   * @ordered
   */
  protected static final Preprocess_feature_scaler PREPROCESS_FEATURE_SCALER_EDEFAULT = Preprocess_feature_scaler.NOT_SET;

  /**
   * The cached value of the '{@link #getPreprocess_feature_scaler() <em>Preprocess feature scaler</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreprocess_feature_scaler()
   * @generated
   * @ordered
   */
  protected Preprocess_feature_scaler preprocess_feature_scaler = PREPROCESS_FEATURE_SCALER_EDEFAULT;

  /**
   * The default value of the '{@link #getPreprocess_sample_normalizer() <em>Preprocess sample normalizer</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreprocess_sample_normalizer()
   * @generated
   * @ordered
   */
  protected static final Preprocess_sample_normalizer PREPROCESS_SAMPLE_NORMALIZER_EDEFAULT = Preprocess_sample_normalizer.NOT_SET;

  /**
   * The cached value of the '{@link #getPreprocess_sample_normalizer() <em>Preprocess sample normalizer</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreprocess_sample_normalizer()
   * @generated
   * @ordered
   */
  protected Preprocess_sample_normalizer preprocess_sample_normalizer = PREPROCESS_SAMPLE_NORMALIZER_EDEFAULT;

  /**
   * The default value of the '{@link #getFill_missing_value() <em>Fill missing value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFill_missing_value()
   * @generated
   * @ordered
   */
  protected static final FillingMissingValue FILL_MISSING_VALUE_EDEFAULT = FillingMissingValue.NOT_SET;

  /**
   * The cached value of the '{@link #getFill_missing_value() <em>Fill missing value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFill_missing_value()
   * @generated
   * @ordered
   */
  protected FillingMissingValue fill_missing_value = FILL_MISSING_VALUE_EDEFAULT;

  /**
   * The default value of the '{@link #getRemove_outliers() <em>Remove outliers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemove_outliers()
   * @generated
   * @ordered
   */
  protected static final RemoveOutliers REMOVE_OUTLIERS_EDEFAULT = RemoveOutliers.NOT_SET;

  /**
   * The cached value of the '{@link #getRemove_outliers() <em>Remove outliers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemove_outliers()
   * @generated
   * @ordered
   */
  protected RemoveOutliers remove_outliers = REMOVE_OUTLIERS_EDEFAULT;

  /**
   * The default value of the '{@link #getAdvanced_imputation() <em>Advanced imputation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAdvanced_imputation()
   * @generated
   * @ordered
   */
  protected static final AdvancedImputation ADVANCED_IMPUTATION_EDEFAULT = AdvancedImputation.OFF;

  /**
   * The cached value of the '{@link #getAdvanced_imputation() <em>Advanced imputation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAdvanced_imputation()
   * @generated
   * @ordered
   */
  protected AdvancedImputation advanced_imputation = ADVANCED_IMPUTATION_EDEFAULT;

  /**
   * The default value of the '{@link #getLagged_features() <em>Lagged features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLagged_features()
   * @generated
   * @ordered
   */
  protected static final LaggedFeatures LAGGED_FEATURES_EDEFAULT = LaggedFeatures.OFF;

  /**
   * The cached value of the '{@link #getLagged_features() <em>Lagged features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLagged_features()
   * @generated
   * @ordered
   */
  protected LaggedFeatures lagged_features = LAGGED_FEATURES_EDEFAULT;

  /**
   * The default value of the '{@link #getRolling_window_features() <em>Rolling window features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRolling_window_features()
   * @generated
   * @ordered
   */
  protected static final RollingWindowFeatures ROLLING_WINDOW_FEATURES_EDEFAULT = RollingWindowFeatures.OFF;

  /**
   * The cached value of the '{@link #getRolling_window_features() <em>Rolling window features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRolling_window_features()
   * @generated
   * @ordered
   */
  protected RollingWindowFeatures rolling_window_features = ROLLING_WINDOW_FEATURES_EDEFAULT;

  /**
   * The default value of the '{@link #getResampling() <em>Resampling</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResampling()
   * @generated
   * @ordered
   */
  protected static final Resampling RESAMPLING_EDEFAULT = Resampling.NOT_SET;

  /**
   * The cached value of the '{@link #getResampling() <em>Resampling</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResampling()
   * @generated
   * @ordered
   */
  protected Resampling resampling = RESAMPLING_EDEFAULT;

  /**
   * The cached value of the '{@link #getTransformations() <em>Transformations</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTransformations()
   * @generated
   * @ordered
   */
  protected EList<DataTransformation> transformations;

  /**
   * The default value of the '{@link #getSequential() <em>Sequential</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSequential()
   * @generated
   * @ordered
   */
  protected static final Sequential SEQUENTIAL_EDEFAULT = Sequential.NOT_SET;

  /**
   * The cached value of the '{@link #getSequential() <em>Sequential</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSequential()
   * @generated
   * @ordered
   */
  protected Sequential sequential = SEQUENTIAL_EDEFAULT;

  /**
   * The cached value of the '{@link #getSteps() <em>Steps</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSteps()
   * @generated
   * @ordered
   */
  protected IntegerLiteral steps;

  /**
   * The cached value of the '{@link #getLag() <em>Lag</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLag()
   * @generated
   * @ordered
   */
  protected IntegerLiteral lag;

  /**
   * The default value of the '{@link #getMultivariate() <em>Multivariate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMultivariate()
   * @generated
   * @ordered
   */
  protected static final Multivariate MULTIVARIATE_EDEFAULT = Multivariate.NOT_SET;

  /**
   * The cached value of the '{@link #getMultivariate() <em>Multivariate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMultivariate()
   * @generated
   * @ordered
   */
  protected Multivariate multivariate = MULTIVARIATE_EDEFAULT;

  /**
   * The default value of the '{@link #getStationary() <em>Stationary</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStationary()
   * @generated
   * @ordered
   */
  protected static final Stationary STATIONARY_EDEFAULT = Stationary.NOT_SET;

  /**
   * The cached value of the '{@link #getStationary() <em>Stationary</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStationary()
   * @generated
   * @ordered
   */
  protected Stationary stationary = STATIONARY_EDEFAULT;

  /**
   * The default value of the '{@link #getSeasonality_detection() <em>Seasonality detection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSeasonality_detection()
   * @generated
   * @ordered
   */
  protected static final SeasonalityDetection SEASONALITY_DETECTION_EDEFAULT = SeasonalityDetection.NOT_SET;

  /**
   * The cached value of the '{@link #getSeasonality_detection() <em>Seasonality detection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSeasonality_detection()
   * @generated
   * @ordered
   */
  protected SeasonalityDetection seasonality_detection = SEASONALITY_DETECTION_EDEFAULT;

  /**
   * The default value of the '{@link #getSupervised_learning() <em>Supervised learning</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSupervised_learning()
   * @generated
   * @ordered
   */
  protected static final SupervisedLearning SUPERVISED_LEARNING_EDEFAULT = SupervisedLearning.NOT_SET;

  /**
   * The cached value of the '{@link #getSupervised_learning() <em>Supervised learning</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSupervised_learning()
   * @generated
   * @ordered
   */
  protected SupervisedLearning supervised_learning = SUPERVISED_LEARNING_EDEFAULT;

  /**
   * The default value of the '{@link #getCreate_lagged_features() <em>Create lagged features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCreate_lagged_features()
   * @generated
   * @ordered
   */
  protected static final CreateLaggedFeatures CREATE_LAGGED_FEATURES_EDEFAULT = CreateLaggedFeatures.NOT_SET;

  /**
   * The cached value of the '{@link #getCreate_lagged_features() <em>Create lagged features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCreate_lagged_features()
   * @generated
   * @ordered
   */
  protected CreateLaggedFeatures create_lagged_features = CREATE_LAGGED_FEATURES_EDEFAULT;

  /**
   * The default value of the '{@link #getSliding_window() <em>Sliding window</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSliding_window()
   * @generated
   * @ordered
   */
  protected static final SlidingWindow SLIDING_WINDOW_EDEFAULT = SlidingWindow.NOT_SET;

  /**
   * The cached value of the '{@link #getSliding_window() <em>Sliding window</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSliding_window()
   * @generated
   * @ordered
   */
  protected SlidingWindow sliding_window = SLIDING_WINDOW_EDEFAULT;

  /**
   * The default value of the '{@link #getAutoML() <em>Auto ML</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAutoML()
   * @generated
   * @ordered
   */
  protected static final AutoML AUTO_ML_EDEFAULT = AutoML.NOT_SET;

  /**
   * The cached value of the '{@link #getAutoML() <em>Auto ML</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAutoML()
   * @generated
   * @ordered
   */
  protected AutoML autoML = AUTO_ML_EDEFAULT;

  /**
   * The cached value of the '{@link #getModelAlgorithm() <em>Model Algorithm</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getModelAlgorithm()
   * @generated
   * @ordered
   */
  protected DataAnalyticsModelAlgorithm modelAlgorithm;

  /**
   * The cached value of the '{@link #getBlackbox_ml() <em>Blackbox ml</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlackbox_ml()
   * @generated
   * @ordered
   */
  protected BooleanLiteral blackbox_ml;

  /**
   * The default value of the '{@link #getBlackbox_ml_model() <em>Blackbox ml model</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlackbox_ml_model()
   * @generated
   * @ordered
   */
  protected static final String BLACKBOX_ML_MODEL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getBlackbox_ml_model() <em>Blackbox ml model</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlackbox_ml_model()
   * @generated
   * @ordered
   */
  protected String blackbox_ml_model = BLACKBOX_ML_MODEL_EDEFAULT;

  /**
   * The default value of the '{@link #getBlackbox_import_algorithm() <em>Blackbox import algorithm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlackbox_import_algorithm()
   * @generated
   * @ordered
   */
  protected static final String BLACKBOX_IMPORT_ALGORITHM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getBlackbox_import_algorithm() <em>Blackbox import algorithm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlackbox_import_algorithm()
   * @generated
   * @ordered
   */
  protected String blackbox_import_algorithm = BLACKBOX_IMPORT_ALGORITHM_EDEFAULT;

  /**
   * The default value of the '{@link #getBlackbox_label_encoder() <em>Blackbox label encoder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlackbox_label_encoder()
   * @generated
   * @ordered
   */
  protected static final String BLACKBOX_LABEL_ENCODER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getBlackbox_label_encoder() <em>Blackbox label encoder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlackbox_label_encoder()
   * @generated
   * @ordered
   */
  protected String blackbox_label_encoder = BLACKBOX_LABEL_ENCODER_EDEFAULT;

  /**
   * The default value of the '{@link #getTrainingResults() <em>Training Results</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTrainingResults()
   * @generated
   * @ordered
   */
  protected static final String TRAINING_RESULTS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTrainingResults() <em>Training Results</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTrainingResults()
   * @generated
   * @ordered
   */
  protected String trainingResults = TRAINING_RESULTS_EDEFAULT;

  /**
   * The default value of the '{@link #getHyperparameter_tuning() <em>Hyperparameter tuning</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHyperparameter_tuning()
   * @generated
   * @ordered
   */
  protected static final HyperparameterTuning HYPERPARAMETER_TUNING_EDEFAULT = HyperparameterTuning.OFF;

  /**
   * The cached value of the '{@link #getHyperparameter_tuning() <em>Hyperparameter tuning</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHyperparameter_tuning()
   * @generated
   * @ordered
   */
  protected HyperparameterTuning hyperparameter_tuning = HYPERPARAMETER_TUNING_EDEFAULT;

  /**
   * The default value of the '{@link #getEnsemble_methods() <em>Ensemble methods</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnsemble_methods()
   * @generated
   * @ordered
   */
  protected static final EnsembleMethods ENSEMBLE_METHODS_EDEFAULT = EnsembleMethods.OFF;

  /**
   * The cached value of the '{@link #getEnsemble_methods() <em>Ensemble methods</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnsemble_methods()
   * @generated
   * @ordered
   */
  protected EnsembleMethods ensemble_methods = ENSEMBLE_METHODS_EDEFAULT;

  /**
   * The cached value of the '{@link #getPredictionResults() <em>Prediction Results</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPredictionResults()
   * @generated
   * @ordered
   */
  protected EList<Property> predictionResults;

  /**
   * The default value of the '{@link #getModel_evaluation() <em>Model evaluation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getModel_evaluation()
   * @generated
   * @ordered
   */
  protected static final ModelEvaluation MODEL_EVALUATION_EDEFAULT = ModelEvaluation.RMSE;

  /**
   * The cached value of the '{@link #getModel_evaluation() <em>Model evaluation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getModel_evaluation()
   * @generated
   * @ordered
   */
  protected ModelEvaluation model_evaluation = MODEL_EVALUATION_EDEFAULT;

  /**
   * The default value of the '{@link #getOutlier_detection() <em>Outlier detection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutlier_detection()
   * @generated
   * @ordered
   */
  protected static final OutlierDetection OUTLIER_DETECTION_EDEFAULT = OutlierDetection.OFF;

  /**
   * The cached value of the '{@link #getOutlier_detection() <em>Outlier detection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutlier_detection()
   * @generated
   * @ordered
   */
  protected OutlierDetection outlier_detection = OUTLIER_DETECTION_EDEFAULT;

  /**
   * The default value of the '{@link #getTime_series_clustering() <em>Time series clustering</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTime_series_clustering()
   * @generated
   * @ordered
   */
  protected static final TimeSeriesClustering TIME_SERIES_CLUSTERING_EDEFAULT = TimeSeriesClustering.OFF;

  /**
   * The cached value of the '{@link #getTime_series_clustering() <em>Time series clustering</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTime_series_clustering()
   * @generated
   * @ordered
   */
  protected TimeSeriesClustering time_series_clustering = TIME_SERIES_CLUSTERING_EDEFAULT;

  /**
   * The default value of the '{@link #getContextArea() <em>Context Area</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContextArea()
   * @generated
   * @ordered
   */
  protected static final ContextArea CONTEXT_AREA_EDEFAULT = ContextArea.RIVER_FLOW;

  /**
   * The cached value of the '{@link #getContextArea() <em>Context Area</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContextArea()
   * @generated
   * @ordered
   */
  protected ContextArea contextArea = CONTEXT_AREA_EDEFAULT;

  /**
   * The cached value of the '{@link #getPlots() <em>Plots</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPlots()
   * @generated
   * @ordered
   */
  protected EList<PreprocessingPlots> plots;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DataAnalyticsImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ThingMLPackage.eINSTANCE.getDataAnalytics();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<PlatformAnnotation> getAnnotations()
  {
    if (annotations == null)
    {
      annotations = new EObjectContainmentEList<PlatformAnnotation>(PlatformAnnotation.class, this, ThingMLPackage.DATA_ANALYTICS__ANNOTATIONS);
    }
    return annotations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getDataset()
  {
    return dataset;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setDataset(String newDataset)
  {
    String oldDataset = dataset;
    dataset = newDataset;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__DATASET, oldDataset, dataset));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Labels getLabels()
  {
    return labels;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setLabels(Labels newLabels)
  {
    Labels oldLabels = labels;
    labels = newLabels == null ? LABELS_EDEFAULT : newLabels;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__LABELS, oldLabels, labels));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Property> getFeatures()
  {
    if (features == null)
    {
      features = new EObjectResolvingEList<Property>(Property.class, this, ThingMLPackage.DATA_ANALYTICS__FEATURES);
    }
    return features;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Property> getInput_features()
  {
    if (input_features == null)
    {
      input_features = new EObjectResolvingEList<Property>(Property.class, this, ThingMLPackage.DATA_ANALYTICS__INPUT_FEATURES);
    }
    return input_features;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Property> getOutput_features()
  {
    if (output_features == null)
    {
      output_features = new EObjectResolvingEList<Property>(Property.class, this, ThingMLPackage.DATA_ANALYTICS__OUTPUT_FEATURES);
    }
    return output_features;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Timestamps getTimestamps()
  {
    return timestamps;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTimestamps(Timestamps newTimestamps)
  {
    Timestamps oldTimestamps = timestamps;
    timestamps = newTimestamps == null ? TIMESTAMPS_EDEFAULT : newTimestamps;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__TIMESTAMPS, oldTimestamps, timestamps));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public long getCombine_threshold()
  {
    return combine_threshold;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCombine_threshold(long newCombine_threshold)
  {
    long oldCombine_threshold = combine_threshold;
    combine_threshold = newCombine_threshold;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__COMBINE_THRESHOLD, oldCombine_threshold, combine_threshold));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Preprocess_feature_scaler getPreprocess_feature_scaler()
  {
    return preprocess_feature_scaler;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setPreprocess_feature_scaler(Preprocess_feature_scaler newPreprocess_feature_scaler)
  {
    Preprocess_feature_scaler oldPreprocess_feature_scaler = preprocess_feature_scaler;
    preprocess_feature_scaler = newPreprocess_feature_scaler == null ? PREPROCESS_FEATURE_SCALER_EDEFAULT : newPreprocess_feature_scaler;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__PREPROCESS_FEATURE_SCALER, oldPreprocess_feature_scaler, preprocess_feature_scaler));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Preprocess_sample_normalizer getPreprocess_sample_normalizer()
  {
    return preprocess_sample_normalizer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setPreprocess_sample_normalizer(Preprocess_sample_normalizer newPreprocess_sample_normalizer)
  {
    Preprocess_sample_normalizer oldPreprocess_sample_normalizer = preprocess_sample_normalizer;
    preprocess_sample_normalizer = newPreprocess_sample_normalizer == null ? PREPROCESS_SAMPLE_NORMALIZER_EDEFAULT : newPreprocess_sample_normalizer;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__PREPROCESS_SAMPLE_NORMALIZER, oldPreprocess_sample_normalizer, preprocess_sample_normalizer));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FillingMissingValue getFill_missing_value()
  {
    return fill_missing_value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setFill_missing_value(FillingMissingValue newFill_missing_value)
  {
    FillingMissingValue oldFill_missing_value = fill_missing_value;
    fill_missing_value = newFill_missing_value == null ? FILL_MISSING_VALUE_EDEFAULT : newFill_missing_value;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__FILL_MISSING_VALUE, oldFill_missing_value, fill_missing_value));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RemoveOutliers getRemove_outliers()
  {
    return remove_outliers;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRemove_outliers(RemoveOutliers newRemove_outliers)
  {
    RemoveOutliers oldRemove_outliers = remove_outliers;
    remove_outliers = newRemove_outliers == null ? REMOVE_OUTLIERS_EDEFAULT : newRemove_outliers;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__REMOVE_OUTLIERS, oldRemove_outliers, remove_outliers));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public AdvancedImputation getAdvanced_imputation()
  {
    return advanced_imputation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setAdvanced_imputation(AdvancedImputation newAdvanced_imputation)
  {
    AdvancedImputation oldAdvanced_imputation = advanced_imputation;
    advanced_imputation = newAdvanced_imputation == null ? ADVANCED_IMPUTATION_EDEFAULT : newAdvanced_imputation;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__ADVANCED_IMPUTATION, oldAdvanced_imputation, advanced_imputation));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LaggedFeatures getLagged_features()
  {
    return lagged_features;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setLagged_features(LaggedFeatures newLagged_features)
  {
    LaggedFeatures oldLagged_features = lagged_features;
    lagged_features = newLagged_features == null ? LAGGED_FEATURES_EDEFAULT : newLagged_features;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__LAGGED_FEATURES, oldLagged_features, lagged_features));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RollingWindowFeatures getRolling_window_features()
  {
    return rolling_window_features;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRolling_window_features(RollingWindowFeatures newRolling_window_features)
  {
    RollingWindowFeatures oldRolling_window_features = rolling_window_features;
    rolling_window_features = newRolling_window_features == null ? ROLLING_WINDOW_FEATURES_EDEFAULT : newRolling_window_features;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__ROLLING_WINDOW_FEATURES, oldRolling_window_features, rolling_window_features));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Resampling getResampling()
  {
    return resampling;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setResampling(Resampling newResampling)
  {
    Resampling oldResampling = resampling;
    resampling = newResampling == null ? RESAMPLING_EDEFAULT : newResampling;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__RESAMPLING, oldResampling, resampling));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<DataTransformation> getTransformations()
  {
    if (transformations == null)
    {
      transformations = new EDataTypeEList<DataTransformation>(DataTransformation.class, this, ThingMLPackage.DATA_ANALYTICS__TRANSFORMATIONS);
    }
    return transformations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Sequential getSequential()
  {
    return sequential;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSequential(Sequential newSequential)
  {
    Sequential oldSequential = sequential;
    sequential = newSequential == null ? SEQUENTIAL_EDEFAULT : newSequential;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__SEQUENTIAL, oldSequential, sequential));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getSteps()
  {
    return steps;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSteps(IntegerLiteral newSteps, NotificationChain msgs)
  {
    IntegerLiteral oldSteps = steps;
    steps = newSteps;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__STEPS, oldSteps, newSteps);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSteps(IntegerLiteral newSteps)
  {
    if (newSteps != steps)
    {
      NotificationChain msgs = null;
      if (steps != null)
        msgs = ((InternalEObject)steps).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.DATA_ANALYTICS__STEPS, null, msgs);
      if (newSteps != null)
        msgs = ((InternalEObject)newSteps).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.DATA_ANALYTICS__STEPS, null, msgs);
      msgs = basicSetSteps(newSteps, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__STEPS, newSteps, newSteps));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getLag()
  {
    return lag;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetLag(IntegerLiteral newLag, NotificationChain msgs)
  {
    IntegerLiteral oldLag = lag;
    lag = newLag;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__LAG, oldLag, newLag);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setLag(IntegerLiteral newLag)
  {
    if (newLag != lag)
    {
      NotificationChain msgs = null;
      if (lag != null)
        msgs = ((InternalEObject)lag).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.DATA_ANALYTICS__LAG, null, msgs);
      if (newLag != null)
        msgs = ((InternalEObject)newLag).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.DATA_ANALYTICS__LAG, null, msgs);
      msgs = basicSetLag(newLag, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__LAG, newLag, newLag));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Multivariate getMultivariate()
  {
    return multivariate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMultivariate(Multivariate newMultivariate)
  {
    Multivariate oldMultivariate = multivariate;
    multivariate = newMultivariate == null ? MULTIVARIATE_EDEFAULT : newMultivariate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__MULTIVARIATE, oldMultivariate, multivariate));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Stationary getStationary()
  {
    return stationary;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setStationary(Stationary newStationary)
  {
    Stationary oldStationary = stationary;
    stationary = newStationary == null ? STATIONARY_EDEFAULT : newStationary;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__STATIONARY, oldStationary, stationary));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SeasonalityDetection getSeasonality_detection()
  {
    return seasonality_detection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSeasonality_detection(SeasonalityDetection newSeasonality_detection)
  {
    SeasonalityDetection oldSeasonality_detection = seasonality_detection;
    seasonality_detection = newSeasonality_detection == null ? SEASONALITY_DETECTION_EDEFAULT : newSeasonality_detection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__SEASONALITY_DETECTION, oldSeasonality_detection, seasonality_detection));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SupervisedLearning getSupervised_learning()
  {
    return supervised_learning;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSupervised_learning(SupervisedLearning newSupervised_learning)
  {
    SupervisedLearning oldSupervised_learning = supervised_learning;
    supervised_learning = newSupervised_learning == null ? SUPERVISED_LEARNING_EDEFAULT : newSupervised_learning;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__SUPERVISED_LEARNING, oldSupervised_learning, supervised_learning));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CreateLaggedFeatures getCreate_lagged_features()
  {
    return create_lagged_features;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCreate_lagged_features(CreateLaggedFeatures newCreate_lagged_features)
  {
    CreateLaggedFeatures oldCreate_lagged_features = create_lagged_features;
    create_lagged_features = newCreate_lagged_features == null ? CREATE_LAGGED_FEATURES_EDEFAULT : newCreate_lagged_features;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__CREATE_LAGGED_FEATURES, oldCreate_lagged_features, create_lagged_features));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SlidingWindow getSliding_window()
  {
    return sliding_window;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSliding_window(SlidingWindow newSliding_window)
  {
    SlidingWindow oldSliding_window = sliding_window;
    sliding_window = newSliding_window == null ? SLIDING_WINDOW_EDEFAULT : newSliding_window;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__SLIDING_WINDOW, oldSliding_window, sliding_window));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public AutoML getAutoML()
  {
    return autoML;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setAutoML(AutoML newAutoML)
  {
    AutoML oldAutoML = autoML;
    autoML = newAutoML == null ? AUTO_ML_EDEFAULT : newAutoML;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__AUTO_ML, oldAutoML, autoML));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DataAnalyticsModelAlgorithm getModelAlgorithm()
  {
    return modelAlgorithm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetModelAlgorithm(DataAnalyticsModelAlgorithm newModelAlgorithm, NotificationChain msgs)
  {
    DataAnalyticsModelAlgorithm oldModelAlgorithm = modelAlgorithm;
    modelAlgorithm = newModelAlgorithm;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM, oldModelAlgorithm, newModelAlgorithm);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setModelAlgorithm(DataAnalyticsModelAlgorithm newModelAlgorithm)
  {
    if (newModelAlgorithm != modelAlgorithm)
    {
      NotificationChain msgs = null;
      if (modelAlgorithm != null)
        msgs = ((InternalEObject)modelAlgorithm).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM, null, msgs);
      if (newModelAlgorithm != null)
        msgs = ((InternalEObject)newModelAlgorithm).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM, null, msgs);
      msgs = basicSetModelAlgorithm(newModelAlgorithm, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM, newModelAlgorithm, newModelAlgorithm));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BooleanLiteral getBlackbox_ml()
  {
    return blackbox_ml;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBlackbox_ml(BooleanLiteral newBlackbox_ml, NotificationChain msgs)
  {
    BooleanLiteral oldBlackbox_ml = blackbox_ml;
    blackbox_ml = newBlackbox_ml;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML, oldBlackbox_ml, newBlackbox_ml);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setBlackbox_ml(BooleanLiteral newBlackbox_ml)
  {
    if (newBlackbox_ml != blackbox_ml)
    {
      NotificationChain msgs = null;
      if (blackbox_ml != null)
        msgs = ((InternalEObject)blackbox_ml).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML, null, msgs);
      if (newBlackbox_ml != null)
        msgs = ((InternalEObject)newBlackbox_ml).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML, null, msgs);
      msgs = basicSetBlackbox_ml(newBlackbox_ml, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML, newBlackbox_ml, newBlackbox_ml));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getBlackbox_ml_model()
  {
    return blackbox_ml_model;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setBlackbox_ml_model(String newBlackbox_ml_model)
  {
    String oldBlackbox_ml_model = blackbox_ml_model;
    blackbox_ml_model = newBlackbox_ml_model;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML_MODEL, oldBlackbox_ml_model, blackbox_ml_model));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getBlackbox_import_algorithm()
  {
    return blackbox_import_algorithm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setBlackbox_import_algorithm(String newBlackbox_import_algorithm)
  {
    String oldBlackbox_import_algorithm = blackbox_import_algorithm;
    blackbox_import_algorithm = newBlackbox_import_algorithm;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__BLACKBOX_IMPORT_ALGORITHM, oldBlackbox_import_algorithm, blackbox_import_algorithm));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getBlackbox_label_encoder()
  {
    return blackbox_label_encoder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setBlackbox_label_encoder(String newBlackbox_label_encoder)
  {
    String oldBlackbox_label_encoder = blackbox_label_encoder;
    blackbox_label_encoder = newBlackbox_label_encoder;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__BLACKBOX_LABEL_ENCODER, oldBlackbox_label_encoder, blackbox_label_encoder));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getTrainingResults()
  {
    return trainingResults;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTrainingResults(String newTrainingResults)
  {
    String oldTrainingResults = trainingResults;
    trainingResults = newTrainingResults;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__TRAINING_RESULTS, oldTrainingResults, trainingResults));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public HyperparameterTuning getHyperparameter_tuning()
  {
    return hyperparameter_tuning;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setHyperparameter_tuning(HyperparameterTuning newHyperparameter_tuning)
  {
    HyperparameterTuning oldHyperparameter_tuning = hyperparameter_tuning;
    hyperparameter_tuning = newHyperparameter_tuning == null ? HYPERPARAMETER_TUNING_EDEFAULT : newHyperparameter_tuning;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__HYPERPARAMETER_TUNING, oldHyperparameter_tuning, hyperparameter_tuning));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EnsembleMethods getEnsemble_methods()
  {
    return ensemble_methods;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setEnsemble_methods(EnsembleMethods newEnsemble_methods)
  {
    EnsembleMethods oldEnsemble_methods = ensemble_methods;
    ensemble_methods = newEnsemble_methods == null ? ENSEMBLE_METHODS_EDEFAULT : newEnsemble_methods;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__ENSEMBLE_METHODS, oldEnsemble_methods, ensemble_methods));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Property> getPredictionResults()
  {
    if (predictionResults == null)
    {
      predictionResults = new EObjectResolvingEList<Property>(Property.class, this, ThingMLPackage.DATA_ANALYTICS__PREDICTION_RESULTS);
    }
    return predictionResults;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ModelEvaluation getModel_evaluation()
  {
    return model_evaluation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setModel_evaluation(ModelEvaluation newModel_evaluation)
  {
    ModelEvaluation oldModel_evaluation = model_evaluation;
    model_evaluation = newModel_evaluation == null ? MODEL_EVALUATION_EDEFAULT : newModel_evaluation;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__MODEL_EVALUATION, oldModel_evaluation, model_evaluation));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public OutlierDetection getOutlier_detection()
  {
    return outlier_detection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setOutlier_detection(OutlierDetection newOutlier_detection)
  {
    OutlierDetection oldOutlier_detection = outlier_detection;
    outlier_detection = newOutlier_detection == null ? OUTLIER_DETECTION_EDEFAULT : newOutlier_detection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__OUTLIER_DETECTION, oldOutlier_detection, outlier_detection));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TimeSeriesClustering getTime_series_clustering()
  {
    return time_series_clustering;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTime_series_clustering(TimeSeriesClustering newTime_series_clustering)
  {
    TimeSeriesClustering oldTime_series_clustering = time_series_clustering;
    time_series_clustering = newTime_series_clustering == null ? TIME_SERIES_CLUSTERING_EDEFAULT : newTime_series_clustering;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__TIME_SERIES_CLUSTERING, oldTime_series_clustering, time_series_clustering));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ContextArea getContextArea()
  {
    return contextArea;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setContextArea(ContextArea newContextArea)
  {
    ContextArea oldContextArea = contextArea;
    contextArea = newContextArea == null ? CONTEXT_AREA_EDEFAULT : newContextArea;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.DATA_ANALYTICS__CONTEXT_AREA, oldContextArea, contextArea));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<PreprocessingPlots> getPlots()
  {
    if (plots == null)
    {
      plots = new EDataTypeEList<PreprocessingPlots>(PreprocessingPlots.class, this, ThingMLPackage.DATA_ANALYTICS__PLOTS);
    }
    return plots;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ThingMLPackage.DATA_ANALYTICS__ANNOTATIONS:
        return ((InternalEList<?>)getAnnotations()).basicRemove(otherEnd, msgs);
      case ThingMLPackage.DATA_ANALYTICS__STEPS:
        return basicSetSteps(null, msgs);
      case ThingMLPackage.DATA_ANALYTICS__LAG:
        return basicSetLag(null, msgs);
      case ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM:
        return basicSetModelAlgorithm(null, msgs);
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML:
        return basicSetBlackbox_ml(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ThingMLPackage.DATA_ANALYTICS__ANNOTATIONS:
        return getAnnotations();
      case ThingMLPackage.DATA_ANALYTICS__DATASET:
        return getDataset();
      case ThingMLPackage.DATA_ANALYTICS__LABELS:
        return getLabels();
      case ThingMLPackage.DATA_ANALYTICS__FEATURES:
        return getFeatures();
      case ThingMLPackage.DATA_ANALYTICS__INPUT_FEATURES:
        return getInput_features();
      case ThingMLPackage.DATA_ANALYTICS__OUTPUT_FEATURES:
        return getOutput_features();
      case ThingMLPackage.DATA_ANALYTICS__TIMESTAMPS:
        return getTimestamps();
      case ThingMLPackage.DATA_ANALYTICS__COMBINE_THRESHOLD:
        return getCombine_threshold();
      case ThingMLPackage.DATA_ANALYTICS__PREPROCESS_FEATURE_SCALER:
        return getPreprocess_feature_scaler();
      case ThingMLPackage.DATA_ANALYTICS__PREPROCESS_SAMPLE_NORMALIZER:
        return getPreprocess_sample_normalizer();
      case ThingMLPackage.DATA_ANALYTICS__FILL_MISSING_VALUE:
        return getFill_missing_value();
      case ThingMLPackage.DATA_ANALYTICS__REMOVE_OUTLIERS:
        return getRemove_outliers();
      case ThingMLPackage.DATA_ANALYTICS__ADVANCED_IMPUTATION:
        return getAdvanced_imputation();
      case ThingMLPackage.DATA_ANALYTICS__LAGGED_FEATURES:
        return getLagged_features();
      case ThingMLPackage.DATA_ANALYTICS__ROLLING_WINDOW_FEATURES:
        return getRolling_window_features();
      case ThingMLPackage.DATA_ANALYTICS__RESAMPLING:
        return getResampling();
      case ThingMLPackage.DATA_ANALYTICS__TRANSFORMATIONS:
        return getTransformations();
      case ThingMLPackage.DATA_ANALYTICS__SEQUENTIAL:
        return getSequential();
      case ThingMLPackage.DATA_ANALYTICS__STEPS:
        return getSteps();
      case ThingMLPackage.DATA_ANALYTICS__LAG:
        return getLag();
      case ThingMLPackage.DATA_ANALYTICS__MULTIVARIATE:
        return getMultivariate();
      case ThingMLPackage.DATA_ANALYTICS__STATIONARY:
        return getStationary();
      case ThingMLPackage.DATA_ANALYTICS__SEASONALITY_DETECTION:
        return getSeasonality_detection();
      case ThingMLPackage.DATA_ANALYTICS__SUPERVISED_LEARNING:
        return getSupervised_learning();
      case ThingMLPackage.DATA_ANALYTICS__CREATE_LAGGED_FEATURES:
        return getCreate_lagged_features();
      case ThingMLPackage.DATA_ANALYTICS__SLIDING_WINDOW:
        return getSliding_window();
      case ThingMLPackage.DATA_ANALYTICS__AUTO_ML:
        return getAutoML();
      case ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM:
        return getModelAlgorithm();
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML:
        return getBlackbox_ml();
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML_MODEL:
        return getBlackbox_ml_model();
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_IMPORT_ALGORITHM:
        return getBlackbox_import_algorithm();
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_LABEL_ENCODER:
        return getBlackbox_label_encoder();
      case ThingMLPackage.DATA_ANALYTICS__TRAINING_RESULTS:
        return getTrainingResults();
      case ThingMLPackage.DATA_ANALYTICS__HYPERPARAMETER_TUNING:
        return getHyperparameter_tuning();
      case ThingMLPackage.DATA_ANALYTICS__ENSEMBLE_METHODS:
        return getEnsemble_methods();
      case ThingMLPackage.DATA_ANALYTICS__PREDICTION_RESULTS:
        return getPredictionResults();
      case ThingMLPackage.DATA_ANALYTICS__MODEL_EVALUATION:
        return getModel_evaluation();
      case ThingMLPackage.DATA_ANALYTICS__OUTLIER_DETECTION:
        return getOutlier_detection();
      case ThingMLPackage.DATA_ANALYTICS__TIME_SERIES_CLUSTERING:
        return getTime_series_clustering();
      case ThingMLPackage.DATA_ANALYTICS__CONTEXT_AREA:
        return getContextArea();
      case ThingMLPackage.DATA_ANALYTICS__PLOTS:
        return getPlots();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ThingMLPackage.DATA_ANALYTICS__ANNOTATIONS:
        getAnnotations().clear();
        getAnnotations().addAll((Collection<? extends PlatformAnnotation>)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__DATASET:
        setDataset((String)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__LABELS:
        setLabels((Labels)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__FEATURES:
        getFeatures().clear();
        getFeatures().addAll((Collection<? extends Property>)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__INPUT_FEATURES:
        getInput_features().clear();
        getInput_features().addAll((Collection<? extends Property>)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__OUTPUT_FEATURES:
        getOutput_features().clear();
        getOutput_features().addAll((Collection<? extends Property>)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__TIMESTAMPS:
        setTimestamps((Timestamps)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__COMBINE_THRESHOLD:
        setCombine_threshold((Long)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__PREPROCESS_FEATURE_SCALER:
        setPreprocess_feature_scaler((Preprocess_feature_scaler)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__PREPROCESS_SAMPLE_NORMALIZER:
        setPreprocess_sample_normalizer((Preprocess_sample_normalizer)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__FILL_MISSING_VALUE:
        setFill_missing_value((FillingMissingValue)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__REMOVE_OUTLIERS:
        setRemove_outliers((RemoveOutliers)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__ADVANCED_IMPUTATION:
        setAdvanced_imputation((AdvancedImputation)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__LAGGED_FEATURES:
        setLagged_features((LaggedFeatures)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__ROLLING_WINDOW_FEATURES:
        setRolling_window_features((RollingWindowFeatures)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__RESAMPLING:
        setResampling((Resampling)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__TRANSFORMATIONS:
        getTransformations().clear();
        getTransformations().addAll((Collection<? extends DataTransformation>)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__SEQUENTIAL:
        setSequential((Sequential)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__STEPS:
        setSteps((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__LAG:
        setLag((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__MULTIVARIATE:
        setMultivariate((Multivariate)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__STATIONARY:
        setStationary((Stationary)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__SEASONALITY_DETECTION:
        setSeasonality_detection((SeasonalityDetection)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__SUPERVISED_LEARNING:
        setSupervised_learning((SupervisedLearning)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__CREATE_LAGGED_FEATURES:
        setCreate_lagged_features((CreateLaggedFeatures)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__SLIDING_WINDOW:
        setSliding_window((SlidingWindow)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__AUTO_ML:
        setAutoML((AutoML)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM:
        setModelAlgorithm((DataAnalyticsModelAlgorithm)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML:
        setBlackbox_ml((BooleanLiteral)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML_MODEL:
        setBlackbox_ml_model((String)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_IMPORT_ALGORITHM:
        setBlackbox_import_algorithm((String)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_LABEL_ENCODER:
        setBlackbox_label_encoder((String)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__TRAINING_RESULTS:
        setTrainingResults((String)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__HYPERPARAMETER_TUNING:
        setHyperparameter_tuning((HyperparameterTuning)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__ENSEMBLE_METHODS:
        setEnsemble_methods((EnsembleMethods)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__PREDICTION_RESULTS:
        getPredictionResults().clear();
        getPredictionResults().addAll((Collection<? extends Property>)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__MODEL_EVALUATION:
        setModel_evaluation((ModelEvaluation)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__OUTLIER_DETECTION:
        setOutlier_detection((OutlierDetection)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__TIME_SERIES_CLUSTERING:
        setTime_series_clustering((TimeSeriesClustering)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__CONTEXT_AREA:
        setContextArea((ContextArea)newValue);
        return;
      case ThingMLPackage.DATA_ANALYTICS__PLOTS:
        getPlots().clear();
        getPlots().addAll((Collection<? extends PreprocessingPlots>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ThingMLPackage.DATA_ANALYTICS__ANNOTATIONS:
        getAnnotations().clear();
        return;
      case ThingMLPackage.DATA_ANALYTICS__DATASET:
        setDataset(DATASET_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__LABELS:
        setLabels(LABELS_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__FEATURES:
        getFeatures().clear();
        return;
      case ThingMLPackage.DATA_ANALYTICS__INPUT_FEATURES:
        getInput_features().clear();
        return;
      case ThingMLPackage.DATA_ANALYTICS__OUTPUT_FEATURES:
        getOutput_features().clear();
        return;
      case ThingMLPackage.DATA_ANALYTICS__TIMESTAMPS:
        setTimestamps(TIMESTAMPS_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__COMBINE_THRESHOLD:
        setCombine_threshold(COMBINE_THRESHOLD_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__PREPROCESS_FEATURE_SCALER:
        setPreprocess_feature_scaler(PREPROCESS_FEATURE_SCALER_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__PREPROCESS_SAMPLE_NORMALIZER:
        setPreprocess_sample_normalizer(PREPROCESS_SAMPLE_NORMALIZER_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__FILL_MISSING_VALUE:
        setFill_missing_value(FILL_MISSING_VALUE_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__REMOVE_OUTLIERS:
        setRemove_outliers(REMOVE_OUTLIERS_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__ADVANCED_IMPUTATION:
        setAdvanced_imputation(ADVANCED_IMPUTATION_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__LAGGED_FEATURES:
        setLagged_features(LAGGED_FEATURES_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__ROLLING_WINDOW_FEATURES:
        setRolling_window_features(ROLLING_WINDOW_FEATURES_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__RESAMPLING:
        setResampling(RESAMPLING_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__TRANSFORMATIONS:
        getTransformations().clear();
        return;
      case ThingMLPackage.DATA_ANALYTICS__SEQUENTIAL:
        setSequential(SEQUENTIAL_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__STEPS:
        setSteps((IntegerLiteral)null);
        return;
      case ThingMLPackage.DATA_ANALYTICS__LAG:
        setLag((IntegerLiteral)null);
        return;
      case ThingMLPackage.DATA_ANALYTICS__MULTIVARIATE:
        setMultivariate(MULTIVARIATE_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__STATIONARY:
        setStationary(STATIONARY_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__SEASONALITY_DETECTION:
        setSeasonality_detection(SEASONALITY_DETECTION_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__SUPERVISED_LEARNING:
        setSupervised_learning(SUPERVISED_LEARNING_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__CREATE_LAGGED_FEATURES:
        setCreate_lagged_features(CREATE_LAGGED_FEATURES_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__SLIDING_WINDOW:
        setSliding_window(SLIDING_WINDOW_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__AUTO_ML:
        setAutoML(AUTO_ML_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM:
        setModelAlgorithm((DataAnalyticsModelAlgorithm)null);
        return;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML:
        setBlackbox_ml((BooleanLiteral)null);
        return;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML_MODEL:
        setBlackbox_ml_model(BLACKBOX_ML_MODEL_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_IMPORT_ALGORITHM:
        setBlackbox_import_algorithm(BLACKBOX_IMPORT_ALGORITHM_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_LABEL_ENCODER:
        setBlackbox_label_encoder(BLACKBOX_LABEL_ENCODER_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__TRAINING_RESULTS:
        setTrainingResults(TRAINING_RESULTS_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__HYPERPARAMETER_TUNING:
        setHyperparameter_tuning(HYPERPARAMETER_TUNING_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__ENSEMBLE_METHODS:
        setEnsemble_methods(ENSEMBLE_METHODS_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__PREDICTION_RESULTS:
        getPredictionResults().clear();
        return;
      case ThingMLPackage.DATA_ANALYTICS__MODEL_EVALUATION:
        setModel_evaluation(MODEL_EVALUATION_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__OUTLIER_DETECTION:
        setOutlier_detection(OUTLIER_DETECTION_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__TIME_SERIES_CLUSTERING:
        setTime_series_clustering(TIME_SERIES_CLUSTERING_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__CONTEXT_AREA:
        setContextArea(CONTEXT_AREA_EDEFAULT);
        return;
      case ThingMLPackage.DATA_ANALYTICS__PLOTS:
        getPlots().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ThingMLPackage.DATA_ANALYTICS__ANNOTATIONS:
        return annotations != null && !annotations.isEmpty();
      case ThingMLPackage.DATA_ANALYTICS__DATASET:
        return DATASET_EDEFAULT == null ? dataset != null : !DATASET_EDEFAULT.equals(dataset);
      case ThingMLPackage.DATA_ANALYTICS__LABELS:
        return labels != LABELS_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__FEATURES:
        return features != null && !features.isEmpty();
      case ThingMLPackage.DATA_ANALYTICS__INPUT_FEATURES:
        return input_features != null && !input_features.isEmpty();
      case ThingMLPackage.DATA_ANALYTICS__OUTPUT_FEATURES:
        return output_features != null && !output_features.isEmpty();
      case ThingMLPackage.DATA_ANALYTICS__TIMESTAMPS:
        return timestamps != TIMESTAMPS_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__COMBINE_THRESHOLD:
        return combine_threshold != COMBINE_THRESHOLD_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__PREPROCESS_FEATURE_SCALER:
        return preprocess_feature_scaler != PREPROCESS_FEATURE_SCALER_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__PREPROCESS_SAMPLE_NORMALIZER:
        return preprocess_sample_normalizer != PREPROCESS_SAMPLE_NORMALIZER_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__FILL_MISSING_VALUE:
        return fill_missing_value != FILL_MISSING_VALUE_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__REMOVE_OUTLIERS:
        return remove_outliers != REMOVE_OUTLIERS_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__ADVANCED_IMPUTATION:
        return advanced_imputation != ADVANCED_IMPUTATION_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__LAGGED_FEATURES:
        return lagged_features != LAGGED_FEATURES_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__ROLLING_WINDOW_FEATURES:
        return rolling_window_features != ROLLING_WINDOW_FEATURES_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__RESAMPLING:
        return resampling != RESAMPLING_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__TRANSFORMATIONS:
        return transformations != null && !transformations.isEmpty();
      case ThingMLPackage.DATA_ANALYTICS__SEQUENTIAL:
        return sequential != SEQUENTIAL_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__STEPS:
        return steps != null;
      case ThingMLPackage.DATA_ANALYTICS__LAG:
        return lag != null;
      case ThingMLPackage.DATA_ANALYTICS__MULTIVARIATE:
        return multivariate != MULTIVARIATE_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__STATIONARY:
        return stationary != STATIONARY_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__SEASONALITY_DETECTION:
        return seasonality_detection != SEASONALITY_DETECTION_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__SUPERVISED_LEARNING:
        return supervised_learning != SUPERVISED_LEARNING_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__CREATE_LAGGED_FEATURES:
        return create_lagged_features != CREATE_LAGGED_FEATURES_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__SLIDING_WINDOW:
        return sliding_window != SLIDING_WINDOW_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__AUTO_ML:
        return autoML != AUTO_ML_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__MODEL_ALGORITHM:
        return modelAlgorithm != null;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML:
        return blackbox_ml != null;
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_ML_MODEL:
        return BLACKBOX_ML_MODEL_EDEFAULT == null ? blackbox_ml_model != null : !BLACKBOX_ML_MODEL_EDEFAULT.equals(blackbox_ml_model);
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_IMPORT_ALGORITHM:
        return BLACKBOX_IMPORT_ALGORITHM_EDEFAULT == null ? blackbox_import_algorithm != null : !BLACKBOX_IMPORT_ALGORITHM_EDEFAULT.equals(blackbox_import_algorithm);
      case ThingMLPackage.DATA_ANALYTICS__BLACKBOX_LABEL_ENCODER:
        return BLACKBOX_LABEL_ENCODER_EDEFAULT == null ? blackbox_label_encoder != null : !BLACKBOX_LABEL_ENCODER_EDEFAULT.equals(blackbox_label_encoder);
      case ThingMLPackage.DATA_ANALYTICS__TRAINING_RESULTS:
        return TRAINING_RESULTS_EDEFAULT == null ? trainingResults != null : !TRAINING_RESULTS_EDEFAULT.equals(trainingResults);
      case ThingMLPackage.DATA_ANALYTICS__HYPERPARAMETER_TUNING:
        return hyperparameter_tuning != HYPERPARAMETER_TUNING_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__ENSEMBLE_METHODS:
        return ensemble_methods != ENSEMBLE_METHODS_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__PREDICTION_RESULTS:
        return predictionResults != null && !predictionResults.isEmpty();
      case ThingMLPackage.DATA_ANALYTICS__MODEL_EVALUATION:
        return model_evaluation != MODEL_EVALUATION_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__OUTLIER_DETECTION:
        return outlier_detection != OUTLIER_DETECTION_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__TIME_SERIES_CLUSTERING:
        return time_series_clustering != TIME_SERIES_CLUSTERING_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__CONTEXT_AREA:
        return contextArea != CONTEXT_AREA_EDEFAULT;
      case ThingMLPackage.DATA_ANALYTICS__PLOTS:
        return plots != null && !plots.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
  {
    if (baseClass == AnnotatedElement.class)
    {
      switch (derivedFeatureID)
      {
        case ThingMLPackage.DATA_ANALYTICS__ANNOTATIONS: return ThingMLPackage.ANNOTATED_ELEMENT__ANNOTATIONS;
        default: return -1;
      }
    }
    return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
  {
    if (baseClass == AnnotatedElement.class)
    {
      switch (baseFeatureID)
      {
        case ThingMLPackage.ANNOTATED_ELEMENT__ANNOTATIONS: return ThingMLPackage.DATA_ANALYTICS__ANNOTATIONS;
        default: return -1;
      }
    }
    return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (dataset: ");
    result.append(dataset);
    result.append(", labels: ");
    result.append(labels);
    result.append(", timestamps: ");
    result.append(timestamps);
    result.append(", combine_threshold: ");
    result.append(combine_threshold);
    result.append(", preprocess_feature_scaler: ");
    result.append(preprocess_feature_scaler);
    result.append(", preprocess_sample_normalizer: ");
    result.append(preprocess_sample_normalizer);
    result.append(", fill_missing_value: ");
    result.append(fill_missing_value);
    result.append(", remove_outliers: ");
    result.append(remove_outliers);
    result.append(", advanced_imputation: ");
    result.append(advanced_imputation);
    result.append(", lagged_features: ");
    result.append(lagged_features);
    result.append(", rolling_window_features: ");
    result.append(rolling_window_features);
    result.append(", resampling: ");
    result.append(resampling);
    result.append(", transformations: ");
    result.append(transformations);
    result.append(", sequential: ");
    result.append(sequential);
    result.append(", multivariate: ");
    result.append(multivariate);
    result.append(", stationary: ");
    result.append(stationary);
    result.append(", seasonality_detection: ");
    result.append(seasonality_detection);
    result.append(", supervised_learning: ");
    result.append(supervised_learning);
    result.append(", create_lagged_features: ");
    result.append(create_lagged_features);
    result.append(", sliding_window: ");
    result.append(sliding_window);
    result.append(", autoML: ");
    result.append(autoML);
    result.append(", blackbox_ml_model: ");
    result.append(blackbox_ml_model);
    result.append(", blackbox_import_algorithm: ");
    result.append(blackbox_import_algorithm);
    result.append(", blackbox_label_encoder: ");
    result.append(blackbox_label_encoder);
    result.append(", trainingResults: ");
    result.append(trainingResults);
    result.append(", hyperparameter_tuning: ");
    result.append(hyperparameter_tuning);
    result.append(", ensemble_methods: ");
    result.append(ensemble_methods);
    result.append(", model_evaluation: ");
    result.append(model_evaluation);
    result.append(", outlier_detection: ");
    result.append(outlier_detection);
    result.append(", time_series_clustering: ");
    result.append(time_series_clustering);
    result.append(", contextArea: ");
    result.append(contextArea);
    result.append(", plots: ");
    result.append(plots);
    result.append(')');
    return result.toString();
  }

} //DataAnalyticsImpl
