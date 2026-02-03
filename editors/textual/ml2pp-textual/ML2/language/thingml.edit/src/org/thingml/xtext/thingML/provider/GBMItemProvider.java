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

import org.thingml.xtext.thingML.GBM;
import org.thingml.xtext.thingML.ThingMLFactory;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * This is the item provider adapter for a {@link org.thingml.xtext.thingML.GBM} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class GBMItemProvider extends machine_learning_ModelAlgorithmItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GBMItemProvider(AdapterFactory adapterFactory) {
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

			addLossPropertyDescriptor(object);
			addLearning_ratePropertyDescriptor(object);
			addSubsamplePropertyDescriptor(object);
			addCriterionPropertyDescriptor(object);
			addMin_weight_fraction_leafPropertyDescriptor(object);
			addMin_impurity_decreasePropertyDescriptor(object);
			addInitPropertyDescriptor(object);
			addMax_featuresPropertyDescriptor(object);
			addAlphaPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Loss feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLossPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GBM_loss_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_loss_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Loss(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Learning rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLearning_ratePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GBM_learning_rate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_learning_rate_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Learning_rate(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Subsample feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSubsamplePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GBM_subsample_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_subsample_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Subsample(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
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
				 getString("_UI_GBM_criterion_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_criterion_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Criterion(),
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
				 getString("_UI_GBM_min_weight_fraction_leaf_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_min_weight_fraction_leaf_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Min_weight_fraction_leaf(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
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
				 getString("_UI_GBM_min_impurity_decrease_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_min_impurity_decrease_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Min_impurity_decrease(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Init feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GBM_init_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_init_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Init(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
				 getString("_UI_GBM_max_features_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_max_features_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Max_features(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Alpha feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAlphaPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_GBM_alpha_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_GBM_alpha_feature", "_UI_GBM_type"),
				 ThingMLPackage.eINSTANCE.getGBM_Alpha(),
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
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_N_estimators());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_Min_samples_split());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_Min_samples_leaf());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_Max_depth());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_Random_state());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_Verbose());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_Max_leaf_nodes());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_Warm_start());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getGBM_Presort());
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
	 * This returns GBM.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/GBM"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((GBM)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_GBM_type") :
			getString("_UI_GBM_type") + " " + label;
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

		switch (notification.getFeatureID(GBM.class)) {
			case ThingMLPackage.GBM__LOSS:
			case ThingMLPackage.GBM__LEARNING_RATE:
			case ThingMLPackage.GBM__SUBSAMPLE:
			case ThingMLPackage.GBM__CRITERION:
			case ThingMLPackage.GBM__MIN_WEIGHT_FRACTION_LEAF:
			case ThingMLPackage.GBM__MIN_IMPURITY_DECREASE:
			case ThingMLPackage.GBM__INIT:
			case ThingMLPackage.GBM__MAX_FEATURES:
			case ThingMLPackage.GBM__ALPHA:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ThingMLPackage.GBM__NESTIMATORS:
			case ThingMLPackage.GBM__MIN_SAMPLES_SPLIT:
			case ThingMLPackage.GBM__MIN_SAMPLES_LEAF:
			case ThingMLPackage.GBM__MAX_DEPTH:
			case ThingMLPackage.GBM__RANDOM_STATE:
			case ThingMLPackage.GBM__VERBOSE:
			case ThingMLPackage.GBM__MAX_LEAF_NODES:
			case ThingMLPackage.GBM__WARM_START:
			case ThingMLPackage.GBM__PRESORT:
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
				(ThingMLPackage.eINSTANCE.getGBM_N_estimators(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getGBM_Min_samples_split(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getGBM_Min_samples_leaf(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getGBM_Max_depth(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getGBM_Random_state(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getGBM_Verbose(),
				 ThingMLFactory.eINSTANCE.createBooleanLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getGBM_Max_leaf_nodes(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getGBM_Warm_start(),
				 ThingMLFactory.eINSTANCE.createBooleanLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getGBM_Presort(),
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
			childFeature == ThingMLPackage.eINSTANCE.getGBM_N_estimators() ||
			childFeature == ThingMLPackage.eINSTANCE.getGBM_Min_samples_split() ||
			childFeature == ThingMLPackage.eINSTANCE.getGBM_Min_samples_leaf() ||
			childFeature == ThingMLPackage.eINSTANCE.getGBM_Max_depth() ||
			childFeature == ThingMLPackage.eINSTANCE.getGBM_Random_state() ||
			childFeature == ThingMLPackage.eINSTANCE.getGBM_Max_leaf_nodes() ||
			childFeature == ThingMLPackage.eINSTANCE.getGBM_Verbose() ||
			childFeature == ThingMLPackage.eINSTANCE.getGBM_Warm_start() ||
			childFeature == ThingMLPackage.eINSTANCE.getGBM_Presort();

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
