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
package org.thingml.xtext.thingML.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.thingml.xtext.thingML.RFR;
import org.thingml.xtext.thingML.ThingMLFactory;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * This is the item provider adapter for a {@link org.thingml.xtext.thingML.RFR} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RFRItemProvider extends machine_learning_ModelAlgorithmItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RFRItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addCriterionPropertyDescriptor(object);
			addMin_weight_fraction_leafPropertyDescriptor(object);
			addMax_featuresPropertyDescriptor(object);
			addMin_impurity_decreasePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Criterion feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCriterionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RFR_criterion_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RFR_criterion_feature", "_UI_RFR_type"),
				 ThingMLPackage.eINSTANCE.getRFR_Criterion(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min weight fraction leaf feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMin_weight_fraction_leafPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RFR_min_weight_fraction_leaf_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RFR_min_weight_fraction_leaf_feature", "_UI_RFR_type"),
				 ThingMLPackage.eINSTANCE.getRFR_Min_weight_fraction_leaf(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max features feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMax_featuresPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RFR_max_features_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RFR_max_features_feature", "_UI_RFR_type"),
				 ThingMLPackage.eINSTANCE.getRFR_Max_features(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min impurity decrease feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMin_impurity_decreasePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RFR_min_impurity_decrease_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RFR_min_impurity_decrease_feature", "_UI_RFR_type"),
				 ThingMLPackage.eINSTANCE.getRFR_Min_impurity_decrease(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_N_estimators());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Max_depth());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Min_samples_split());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Min_samples_leaf());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Max_leaf_nodes());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Bootstrap());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Oob_score());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_N_jobs());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Random_state());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Verbose());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getRFR_Warm_start());
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns RFR.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/RFR"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((RFR)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_RFR_type") :
			getString("_UI_RFR_type") + " " + label;
	}


	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(RFR.class)) {
			case ThingMLPackage.RFR__CRITERION:
			case ThingMLPackage.RFR__MIN_WEIGHT_FRACTION_LEAF:
			case ThingMLPackage.RFR__MAX_FEATURES:
			case ThingMLPackage.RFR__MIN_IMPURITY_DECREASE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ThingMLPackage.RFR__NESTIMATORS:
			case ThingMLPackage.RFR__MAX_DEPTH:
			case ThingMLPackage.RFR__MIN_SAMPLES_SPLIT:
			case ThingMLPackage.RFR__MIN_SAMPLES_LEAF:
			case ThingMLPackage.RFR__MAX_LEAF_NODES:
			case ThingMLPackage.RFR__BOOTSTRAP:
			case ThingMLPackage.RFR__OOB_SCORE:
			case ThingMLPackage.RFR__NJOBS:
			case ThingMLPackage.RFR__RANDOM_STATE:
			case ThingMLPackage.RFR__VERBOSE:
			case ThingMLPackage.RFR__WARM_START:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_N_estimators(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Max_depth(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Min_samples_split(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Min_samples_leaf(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Max_leaf_nodes(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Bootstrap(),
				 ThingMLFactory.eINSTANCE.createBooleanLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Oob_score(),
				 ThingMLFactory.eINSTANCE.createBooleanLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_N_jobs(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Random_state(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Verbose(),
				 ThingMLFactory.eINSTANCE.createBooleanLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getRFR_Warm_start(),
				 ThingMLFactory.eINSTANCE.createBooleanLiteral()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == ThingMLPackage.eINSTANCE.getRFR_N_estimators() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Max_depth() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Min_samples_split() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Min_samples_leaf() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Max_leaf_nodes() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_N_jobs() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Random_state() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Bootstrap() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Oob_score() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Verbose() ||
			childFeature == ThingMLPackage.eINSTANCE.getRFR_Warm_start();

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
