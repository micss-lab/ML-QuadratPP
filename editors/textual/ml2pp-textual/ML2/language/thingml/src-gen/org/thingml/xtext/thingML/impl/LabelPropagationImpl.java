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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.thingml.xtext.thingML.DoubleLiteral;
import org.thingml.xtext.thingML.IntegerLiteral;
import org.thingml.xtext.thingML.LabelPropagation;
import org.thingml.xtext.thingML.Label_Propagation_Kernel;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Label Propagation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.LabelPropagationImpl#getKernel <em>Kernel</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.LabelPropagationImpl#getGamma <em>Gamma</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.LabelPropagationImpl#getN_neighbors <em>Nneighbors</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.LabelPropagationImpl#getMax_iter <em>Max iter</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.LabelPropagationImpl#getTol <em>Tol</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.LabelPropagationImpl#getN_jobs <em>Njobs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LabelPropagationImpl extends ML2_ModelAlgorithmImpl implements LabelPropagation
{
  /**
   * The default value of the '{@link #getKernel() <em>Kernel</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKernel()
   * @generated
   * @ordered
   */
  protected static final Label_Propagation_Kernel KERNEL_EDEFAULT = Label_Propagation_Kernel.NOT_SET;

  /**
   * The cached value of the '{@link #getKernel() <em>Kernel</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKernel()
   * @generated
   * @ordered
   */
  protected Label_Propagation_Kernel kernel = KERNEL_EDEFAULT;

  /**
   * The cached value of the '{@link #getGamma() <em>Gamma</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGamma()
   * @generated
   * @ordered
   */
  protected DoubleLiteral gamma;

  /**
   * The cached value of the '{@link #getN_neighbors() <em>Nneighbors</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_neighbors()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_neighbors;

  /**
   * The cached value of the '{@link #getMax_iter() <em>Max iter</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax_iter()
   * @generated
   * @ordered
   */
  protected IntegerLiteral max_iter;

  /**
   * The cached value of the '{@link #getTol() <em>Tol</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTol()
   * @generated
   * @ordered
   */
  protected DoubleLiteral tol;

  /**
   * The cached value of the '{@link #getN_jobs() <em>Njobs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_jobs()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_jobs;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LabelPropagationImpl()
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
    return ThingMLPackage.eINSTANCE.getLabelPropagation();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Label_Propagation_Kernel getKernel()
  {
    return kernel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setKernel(Label_Propagation_Kernel newKernel)
  {
    Label_Propagation_Kernel oldKernel = kernel;
    kernel = newKernel == null ? KERNEL_EDEFAULT : newKernel;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__KERNEL, oldKernel, kernel));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getGamma()
  {
    return gamma;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetGamma(DoubleLiteral newGamma, NotificationChain msgs)
  {
    DoubleLiteral oldGamma = gamma;
    gamma = newGamma;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__GAMMA, oldGamma, newGamma);
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
  public void setGamma(DoubleLiteral newGamma)
  {
    if (newGamma != gamma)
    {
      NotificationChain msgs = null;
      if (gamma != null)
        msgs = ((InternalEObject)gamma).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__GAMMA, null, msgs);
      if (newGamma != null)
        msgs = ((InternalEObject)newGamma).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__GAMMA, null, msgs);
      msgs = basicSetGamma(newGamma, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__GAMMA, newGamma, newGamma));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getN_neighbors()
  {
    return n_neighbors;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetN_neighbors(IntegerLiteral newN_neighbors, NotificationChain msgs)
  {
    IntegerLiteral oldN_neighbors = n_neighbors;
    n_neighbors = newN_neighbors;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS, oldN_neighbors, newN_neighbors);
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
  public void setN_neighbors(IntegerLiteral newN_neighbors)
  {
    if (newN_neighbors != n_neighbors)
    {
      NotificationChain msgs = null;
      if (n_neighbors != null)
        msgs = ((InternalEObject)n_neighbors).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS, null, msgs);
      if (newN_neighbors != null)
        msgs = ((InternalEObject)newN_neighbors).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS, null, msgs);
      msgs = basicSetN_neighbors(newN_neighbors, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS, newN_neighbors, newN_neighbors));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getMax_iter()
  {
    return max_iter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMax_iter(IntegerLiteral newMax_iter, NotificationChain msgs)
  {
    IntegerLiteral oldMax_iter = max_iter;
    max_iter = newMax_iter;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__MAX_ITER, oldMax_iter, newMax_iter);
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
  public void setMax_iter(IntegerLiteral newMax_iter)
  {
    if (newMax_iter != max_iter)
    {
      NotificationChain msgs = null;
      if (max_iter != null)
        msgs = ((InternalEObject)max_iter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__MAX_ITER, null, msgs);
      if (newMax_iter != null)
        msgs = ((InternalEObject)newMax_iter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__MAX_ITER, null, msgs);
      msgs = basicSetMax_iter(newMax_iter, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__MAX_ITER, newMax_iter, newMax_iter));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getTol()
  {
    return tol;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTol(DoubleLiteral newTol, NotificationChain msgs)
  {
    DoubleLiteral oldTol = tol;
    tol = newTol;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__TOL, oldTol, newTol);
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
  public void setTol(DoubleLiteral newTol)
  {
    if (newTol != tol)
    {
      NotificationChain msgs = null;
      if (tol != null)
        msgs = ((InternalEObject)tol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__TOL, null, msgs);
      if (newTol != null)
        msgs = ((InternalEObject)newTol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__TOL, null, msgs);
      msgs = basicSetTol(newTol, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__TOL, newTol, newTol));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getN_jobs()
  {
    return n_jobs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetN_jobs(IntegerLiteral newN_jobs, NotificationChain msgs)
  {
    IntegerLiteral oldN_jobs = n_jobs;
    n_jobs = newN_jobs;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__NJOBS, oldN_jobs, newN_jobs);
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
  public void setN_jobs(IntegerLiteral newN_jobs)
  {
    if (newN_jobs != n_jobs)
    {
      NotificationChain msgs = null;
      if (n_jobs != null)
        msgs = ((InternalEObject)n_jobs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__NJOBS, null, msgs);
      if (newN_jobs != null)
        msgs = ((InternalEObject)newN_jobs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.LABEL_PROPAGATION__NJOBS, null, msgs);
      msgs = basicSetN_jobs(newN_jobs, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.LABEL_PROPAGATION__NJOBS, newN_jobs, newN_jobs));
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
      case ThingMLPackage.LABEL_PROPAGATION__GAMMA:
        return basicSetGamma(null, msgs);
      case ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS:
        return basicSetN_neighbors(null, msgs);
      case ThingMLPackage.LABEL_PROPAGATION__MAX_ITER:
        return basicSetMax_iter(null, msgs);
      case ThingMLPackage.LABEL_PROPAGATION__TOL:
        return basicSetTol(null, msgs);
      case ThingMLPackage.LABEL_PROPAGATION__NJOBS:
        return basicSetN_jobs(null, msgs);
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
      case ThingMLPackage.LABEL_PROPAGATION__KERNEL:
        return getKernel();
      case ThingMLPackage.LABEL_PROPAGATION__GAMMA:
        return getGamma();
      case ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS:
        return getN_neighbors();
      case ThingMLPackage.LABEL_PROPAGATION__MAX_ITER:
        return getMax_iter();
      case ThingMLPackage.LABEL_PROPAGATION__TOL:
        return getTol();
      case ThingMLPackage.LABEL_PROPAGATION__NJOBS:
        return getN_jobs();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ThingMLPackage.LABEL_PROPAGATION__KERNEL:
        setKernel((Label_Propagation_Kernel)newValue);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__GAMMA:
        setGamma((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS:
        setN_neighbors((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__MAX_ITER:
        setMax_iter((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__TOL:
        setTol((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__NJOBS:
        setN_jobs((IntegerLiteral)newValue);
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
      case ThingMLPackage.LABEL_PROPAGATION__KERNEL:
        setKernel(KERNEL_EDEFAULT);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__GAMMA:
        setGamma((DoubleLiteral)null);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS:
        setN_neighbors((IntegerLiteral)null);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__MAX_ITER:
        setMax_iter((IntegerLiteral)null);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__TOL:
        setTol((DoubleLiteral)null);
        return;
      case ThingMLPackage.LABEL_PROPAGATION__NJOBS:
        setN_jobs((IntegerLiteral)null);
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
      case ThingMLPackage.LABEL_PROPAGATION__KERNEL:
        return kernel != KERNEL_EDEFAULT;
      case ThingMLPackage.LABEL_PROPAGATION__GAMMA:
        return gamma != null;
      case ThingMLPackage.LABEL_PROPAGATION__NNEIGHBORS:
        return n_neighbors != null;
      case ThingMLPackage.LABEL_PROPAGATION__MAX_ITER:
        return max_iter != null;
      case ThingMLPackage.LABEL_PROPAGATION__TOL:
        return tol != null;
      case ThingMLPackage.LABEL_PROPAGATION__NJOBS:
        return n_jobs != null;
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
    result.append(" (kernel: ");
    result.append(kernel);
    result.append(')');
    return result.toString();
  }

} //LabelPropagationImpl
