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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.thingml.xtext.thingML.Activation;
import org.thingml.xtext.thingML.IntegerLiteral;
import org.thingml.xtext.thingML.Loss;
import org.thingml.xtext.thingML.ThingMLPackage;
import org.thingml.xtext.thingML.Transformer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transformer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getHidden_layers <em>Hidden layers</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getUnits <em>Units</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getNum_layers <em>Num layers</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getD_model <em>Dmodel</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getNum_heads <em>Num heads</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getDff <em>Dff</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getDropout_rate <em>Dropout rate</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getActivation <em>Activation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getLearning_rate <em>Learning rate</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getLoss <em>Loss</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getDense_layers <em>Dense layers</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.TransformerImpl#getNeurons <em>Neurons</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransformerImpl extends Deep_learning_ModelAlgorithmImpl implements Transformer
{
  /**
   * The default value of the '{@link #getHidden_layers() <em>Hidden layers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHidden_layers()
   * @generated
   * @ordered
   */
  protected static final String HIDDEN_LAYERS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHidden_layers() <em>Hidden layers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHidden_layers()
   * @generated
   * @ordered
   */
  protected String hidden_layers = HIDDEN_LAYERS_EDEFAULT;

  /**
   * The cached value of the '{@link #getUnits() <em>Units</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnits()
   * @generated
   * @ordered
   */
  protected EList<IntegerLiteral> units;

  /**
   * The cached value of the '{@link #getNum_layers() <em>Num layers</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNum_layers()
   * @generated
   * @ordered
   */
  protected IntegerLiteral num_layers;

  /**
   * The cached value of the '{@link #getD_model() <em>Dmodel</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getD_model()
   * @generated
   * @ordered
   */
  protected IntegerLiteral d_model;

  /**
   * The cached value of the '{@link #getNum_heads() <em>Num heads</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNum_heads()
   * @generated
   * @ordered
   */
  protected IntegerLiteral num_heads;

  /**
   * The cached value of the '{@link #getDff() <em>Dff</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDff()
   * @generated
   * @ordered
   */
  protected IntegerLiteral dff;

  /**
   * The default value of the '{@link #getDropout_rate() <em>Dropout rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDropout_rate()
   * @generated
   * @ordered
   */
  protected static final double DROPOUT_RATE_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getDropout_rate() <em>Dropout rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDropout_rate()
   * @generated
   * @ordered
   */
  protected double dropout_rate = DROPOUT_RATE_EDEFAULT;

  /**
   * The default value of the '{@link #getActivation() <em>Activation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActivation()
   * @generated
   * @ordered
   */
  protected static final Activation ACTIVATION_EDEFAULT = Activation.NOT_SET;

  /**
   * The cached value of the '{@link #getActivation() <em>Activation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActivation()
   * @generated
   * @ordered
   */
  protected Activation activation = ACTIVATION_EDEFAULT;

  /**
   * The default value of the '{@link #getLearning_rate() <em>Learning rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLearning_rate()
   * @generated
   * @ordered
   */
  protected static final double LEARNING_RATE_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getLearning_rate() <em>Learning rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLearning_rate()
   * @generated
   * @ordered
   */
  protected double learning_rate = LEARNING_RATE_EDEFAULT;

  /**
   * The default value of the '{@link #getLoss() <em>Loss</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLoss()
   * @generated
   * @ordered
   */
  protected static final Loss LOSS_EDEFAULT = Loss.NOT_SET;

  /**
   * The cached value of the '{@link #getLoss() <em>Loss</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLoss()
   * @generated
   * @ordered
   */
  protected Loss loss = LOSS_EDEFAULT;

  /**
   * The default value of the '{@link #getDense_layers() <em>Dense layers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDense_layers()
   * @generated
   * @ordered
   */
  protected static final String DENSE_LAYERS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDense_layers() <em>Dense layers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDense_layers()
   * @generated
   * @ordered
   */
  protected String dense_layers = DENSE_LAYERS_EDEFAULT;

  /**
   * The cached value of the '{@link #getNeurons() <em>Neurons</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNeurons()
   * @generated
   * @ordered
   */
  protected EList<IntegerLiteral> neurons;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TransformerImpl()
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
    return ThingMLPackage.eINSTANCE.getTransformer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getHidden_layers()
  {
    return hidden_layers;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setHidden_layers(String newHidden_layers)
  {
    String oldHidden_layers = hidden_layers;
    hidden_layers = newHidden_layers;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__HIDDEN_LAYERS, oldHidden_layers, hidden_layers));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<IntegerLiteral> getUnits()
  {
    if (units == null)
    {
      units = new EObjectContainmentEList<IntegerLiteral>(IntegerLiteral.class, this, ThingMLPackage.TRANSFORMER__UNITS);
    }
    return units;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getNum_layers()
  {
    return num_layers;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetNum_layers(IntegerLiteral newNum_layers, NotificationChain msgs)
  {
    IntegerLiteral oldNum_layers = num_layers;
    num_layers = newNum_layers;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__NUM_LAYERS, oldNum_layers, newNum_layers);
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
  public void setNum_layers(IntegerLiteral newNum_layers)
  {
    if (newNum_layers != num_layers)
    {
      NotificationChain msgs = null;
      if (num_layers != null)
        msgs = ((InternalEObject)num_layers).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.TRANSFORMER__NUM_LAYERS, null, msgs);
      if (newNum_layers != null)
        msgs = ((InternalEObject)newNum_layers).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.TRANSFORMER__NUM_LAYERS, null, msgs);
      msgs = basicSetNum_layers(newNum_layers, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__NUM_LAYERS, newNum_layers, newNum_layers));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getD_model()
  {
    return d_model;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetD_model(IntegerLiteral newD_model, NotificationChain msgs)
  {
    IntegerLiteral oldD_model = d_model;
    d_model = newD_model;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__DMODEL, oldD_model, newD_model);
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
  public void setD_model(IntegerLiteral newD_model)
  {
    if (newD_model != d_model)
    {
      NotificationChain msgs = null;
      if (d_model != null)
        msgs = ((InternalEObject)d_model).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.TRANSFORMER__DMODEL, null, msgs);
      if (newD_model != null)
        msgs = ((InternalEObject)newD_model).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.TRANSFORMER__DMODEL, null, msgs);
      msgs = basicSetD_model(newD_model, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__DMODEL, newD_model, newD_model));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getNum_heads()
  {
    return num_heads;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetNum_heads(IntegerLiteral newNum_heads, NotificationChain msgs)
  {
    IntegerLiteral oldNum_heads = num_heads;
    num_heads = newNum_heads;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__NUM_HEADS, oldNum_heads, newNum_heads);
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
  public void setNum_heads(IntegerLiteral newNum_heads)
  {
    if (newNum_heads != num_heads)
    {
      NotificationChain msgs = null;
      if (num_heads != null)
        msgs = ((InternalEObject)num_heads).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.TRANSFORMER__NUM_HEADS, null, msgs);
      if (newNum_heads != null)
        msgs = ((InternalEObject)newNum_heads).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.TRANSFORMER__NUM_HEADS, null, msgs);
      msgs = basicSetNum_heads(newNum_heads, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__NUM_HEADS, newNum_heads, newNum_heads));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getDff()
  {
    return dff;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDff(IntegerLiteral newDff, NotificationChain msgs)
  {
    IntegerLiteral oldDff = dff;
    dff = newDff;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__DFF, oldDff, newDff);
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
  public void setDff(IntegerLiteral newDff)
  {
    if (newDff != dff)
    {
      NotificationChain msgs = null;
      if (dff != null)
        msgs = ((InternalEObject)dff).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.TRANSFORMER__DFF, null, msgs);
      if (newDff != null)
        msgs = ((InternalEObject)newDff).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.TRANSFORMER__DFF, null, msgs);
      msgs = basicSetDff(newDff, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__DFF, newDff, newDff));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getDropout_rate()
  {
    return dropout_rate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setDropout_rate(double newDropout_rate)
  {
    double oldDropout_rate = dropout_rate;
    dropout_rate = newDropout_rate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__DROPOUT_RATE, oldDropout_rate, dropout_rate));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Activation getActivation()
  {
    return activation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setActivation(Activation newActivation)
  {
    Activation oldActivation = activation;
    activation = newActivation == null ? ACTIVATION_EDEFAULT : newActivation;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__ACTIVATION, oldActivation, activation));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getLearning_rate()
  {
    return learning_rate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setLearning_rate(double newLearning_rate)
  {
    double oldLearning_rate = learning_rate;
    learning_rate = newLearning_rate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__LEARNING_RATE, oldLearning_rate, learning_rate));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Loss getLoss()
  {
    return loss;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setLoss(Loss newLoss)
  {
    Loss oldLoss = loss;
    loss = newLoss == null ? LOSS_EDEFAULT : newLoss;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__LOSS, oldLoss, loss));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getDense_layers()
  {
    return dense_layers;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setDense_layers(String newDense_layers)
  {
    String oldDense_layers = dense_layers;
    dense_layers = newDense_layers;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TRANSFORMER__DENSE_LAYERS, oldDense_layers, dense_layers));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<IntegerLiteral> getNeurons()
  {
    if (neurons == null)
    {
      neurons = new EObjectContainmentEList<IntegerLiteral>(IntegerLiteral.class, this, ThingMLPackage.TRANSFORMER__NEURONS);
    }
    return neurons;
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
      case ThingMLPackage.TRANSFORMER__UNITS:
        return ((InternalEList<?>)getUnits()).basicRemove(otherEnd, msgs);
      case ThingMLPackage.TRANSFORMER__NUM_LAYERS:
        return basicSetNum_layers(null, msgs);
      case ThingMLPackage.TRANSFORMER__DMODEL:
        return basicSetD_model(null, msgs);
      case ThingMLPackage.TRANSFORMER__NUM_HEADS:
        return basicSetNum_heads(null, msgs);
      case ThingMLPackage.TRANSFORMER__DFF:
        return basicSetDff(null, msgs);
      case ThingMLPackage.TRANSFORMER__NEURONS:
        return ((InternalEList<?>)getNeurons()).basicRemove(otherEnd, msgs);
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
      case ThingMLPackage.TRANSFORMER__HIDDEN_LAYERS:
        return getHidden_layers();
      case ThingMLPackage.TRANSFORMER__UNITS:
        return getUnits();
      case ThingMLPackage.TRANSFORMER__NUM_LAYERS:
        return getNum_layers();
      case ThingMLPackage.TRANSFORMER__DMODEL:
        return getD_model();
      case ThingMLPackage.TRANSFORMER__NUM_HEADS:
        return getNum_heads();
      case ThingMLPackage.TRANSFORMER__DFF:
        return getDff();
      case ThingMLPackage.TRANSFORMER__DROPOUT_RATE:
        return getDropout_rate();
      case ThingMLPackage.TRANSFORMER__ACTIVATION:
        return getActivation();
      case ThingMLPackage.TRANSFORMER__LEARNING_RATE:
        return getLearning_rate();
      case ThingMLPackage.TRANSFORMER__LOSS:
        return getLoss();
      case ThingMLPackage.TRANSFORMER__DENSE_LAYERS:
        return getDense_layers();
      case ThingMLPackage.TRANSFORMER__NEURONS:
        return getNeurons();
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
      case ThingMLPackage.TRANSFORMER__HIDDEN_LAYERS:
        setHidden_layers((String)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__UNITS:
        getUnits().clear();
        getUnits().addAll((Collection<? extends IntegerLiteral>)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__NUM_LAYERS:
        setNum_layers((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__DMODEL:
        setD_model((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__NUM_HEADS:
        setNum_heads((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__DFF:
        setDff((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__DROPOUT_RATE:
        setDropout_rate((Double)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__ACTIVATION:
        setActivation((Activation)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__LEARNING_RATE:
        setLearning_rate((Double)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__LOSS:
        setLoss((Loss)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__DENSE_LAYERS:
        setDense_layers((String)newValue);
        return;
      case ThingMLPackage.TRANSFORMER__NEURONS:
        getNeurons().clear();
        getNeurons().addAll((Collection<? extends IntegerLiteral>)newValue);
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
      case ThingMLPackage.TRANSFORMER__HIDDEN_LAYERS:
        setHidden_layers(HIDDEN_LAYERS_EDEFAULT);
        return;
      case ThingMLPackage.TRANSFORMER__UNITS:
        getUnits().clear();
        return;
      case ThingMLPackage.TRANSFORMER__NUM_LAYERS:
        setNum_layers((IntegerLiteral)null);
        return;
      case ThingMLPackage.TRANSFORMER__DMODEL:
        setD_model((IntegerLiteral)null);
        return;
      case ThingMLPackage.TRANSFORMER__NUM_HEADS:
        setNum_heads((IntegerLiteral)null);
        return;
      case ThingMLPackage.TRANSFORMER__DFF:
        setDff((IntegerLiteral)null);
        return;
      case ThingMLPackage.TRANSFORMER__DROPOUT_RATE:
        setDropout_rate(DROPOUT_RATE_EDEFAULT);
        return;
      case ThingMLPackage.TRANSFORMER__ACTIVATION:
        setActivation(ACTIVATION_EDEFAULT);
        return;
      case ThingMLPackage.TRANSFORMER__LEARNING_RATE:
        setLearning_rate(LEARNING_RATE_EDEFAULT);
        return;
      case ThingMLPackage.TRANSFORMER__LOSS:
        setLoss(LOSS_EDEFAULT);
        return;
      case ThingMLPackage.TRANSFORMER__DENSE_LAYERS:
        setDense_layers(DENSE_LAYERS_EDEFAULT);
        return;
      case ThingMLPackage.TRANSFORMER__NEURONS:
        getNeurons().clear();
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
      case ThingMLPackage.TRANSFORMER__HIDDEN_LAYERS:
        return HIDDEN_LAYERS_EDEFAULT == null ? hidden_layers != null : !HIDDEN_LAYERS_EDEFAULT.equals(hidden_layers);
      case ThingMLPackage.TRANSFORMER__UNITS:
        return units != null && !units.isEmpty();
      case ThingMLPackage.TRANSFORMER__NUM_LAYERS:
        return num_layers != null;
      case ThingMLPackage.TRANSFORMER__DMODEL:
        return d_model != null;
      case ThingMLPackage.TRANSFORMER__NUM_HEADS:
        return num_heads != null;
      case ThingMLPackage.TRANSFORMER__DFF:
        return dff != null;
      case ThingMLPackage.TRANSFORMER__DROPOUT_RATE:
        return dropout_rate != DROPOUT_RATE_EDEFAULT;
      case ThingMLPackage.TRANSFORMER__ACTIVATION:
        return activation != ACTIVATION_EDEFAULT;
      case ThingMLPackage.TRANSFORMER__LEARNING_RATE:
        return learning_rate != LEARNING_RATE_EDEFAULT;
      case ThingMLPackage.TRANSFORMER__LOSS:
        return loss != LOSS_EDEFAULT;
      case ThingMLPackage.TRANSFORMER__DENSE_LAYERS:
        return DENSE_LAYERS_EDEFAULT == null ? dense_layers != null : !DENSE_LAYERS_EDEFAULT.equals(dense_layers);
      case ThingMLPackage.TRANSFORMER__NEURONS:
        return neurons != null && !neurons.isEmpty();
    }
    return super.eIsSet(featureID);
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
    result.append(" (hidden_layers: ");
    result.append(hidden_layers);
    result.append(", dropout_rate: ");
    result.append(dropout_rate);
    result.append(", activation: ");
    result.append(activation);
    result.append(", learning_rate: ");
    result.append(learning_rate);
    result.append(", loss: ");
    result.append(loss);
    result.append(", dense_layers: ");
    result.append(dense_layers);
    result.append(')');
    return result.toString();
  }

} //TransformerImpl
