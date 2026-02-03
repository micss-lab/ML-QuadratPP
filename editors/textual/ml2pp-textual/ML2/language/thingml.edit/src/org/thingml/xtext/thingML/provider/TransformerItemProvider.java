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

import org.thingml.xtext.thingML.ThingMLFactory;
import org.thingml.xtext.thingML.ThingMLPackage;
import org.thingml.xtext.thingML.Transformer;

/**
 * This is the item provider adapter for a {@link org.thingml.xtext.thingML.Transformer} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TransformerItemProvider extends Deep_learning_ModelAlgorithmItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransformerItemProvider(AdapterFactory adapterFactory) {
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

			addHidden_layersPropertyDescriptor(object);
			addDropout_ratePropertyDescriptor(object);
			addActivationPropertyDescriptor(object);
			addLearning_ratePropertyDescriptor(object);
			addLossPropertyDescriptor(object);
			addDense_layersPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Hidden layers feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHidden_layersPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Transformer_hidden_layers_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Transformer_hidden_layers_feature", "_UI_Transformer_type"),
				 ThingMLPackage.eINSTANCE.getTransformer_Hidden_layers(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Dropout rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDropout_ratePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Transformer_dropout_rate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Transformer_dropout_rate_feature", "_UI_Transformer_type"),
				 ThingMLPackage.eINSTANCE.getTransformer_Dropout_rate(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Activation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addActivationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Transformer_activation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Transformer_activation_feature", "_UI_Transformer_type"),
				 ThingMLPackage.eINSTANCE.getTransformer_Activation(),
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
				 getString("_UI_Transformer_learning_rate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Transformer_learning_rate_feature", "_UI_Transformer_type"),
				 ThingMLPackage.eINSTANCE.getTransformer_Learning_rate(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
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
				 getString("_UI_Transformer_loss_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Transformer_loss_feature", "_UI_Transformer_type"),
				 ThingMLPackage.eINSTANCE.getTransformer_Loss(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Dense layers feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDense_layersPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Transformer_dense_layers_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Transformer_dense_layers_feature", "_UI_Transformer_type"),
				 ThingMLPackage.eINSTANCE.getTransformer_Dense_layers(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getTransformer_Units());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getTransformer_Num_layers());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getTransformer_D_model());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getTransformer_Num_heads());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getTransformer_Dff());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getTransformer_Neurons());
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
	 * This returns Transformer.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Transformer"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Transformer)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Transformer_type") :
			getString("_UI_Transformer_type") + " " + label;
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

		switch (notification.getFeatureID(Transformer.class)) {
			case ThingMLPackage.TRANSFORMER__HIDDEN_LAYERS:
			case ThingMLPackage.TRANSFORMER__DROPOUT_RATE:
			case ThingMLPackage.TRANSFORMER__ACTIVATION:
			case ThingMLPackage.TRANSFORMER__LEARNING_RATE:
			case ThingMLPackage.TRANSFORMER__LOSS:
			case ThingMLPackage.TRANSFORMER__DENSE_LAYERS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ThingMLPackage.TRANSFORMER__UNITS:
			case ThingMLPackage.TRANSFORMER__NUM_LAYERS:
			case ThingMLPackage.TRANSFORMER__DMODEL:
			case ThingMLPackage.TRANSFORMER__NUM_HEADS:
			case ThingMLPackage.TRANSFORMER__DFF:
			case ThingMLPackage.TRANSFORMER__NEURONS:
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
				(ThingMLPackage.eINSTANCE.getTransformer_Units(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getTransformer_Num_layers(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getTransformer_D_model(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getTransformer_Num_heads(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getTransformer_Dff(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getTransformer_Neurons(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));
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
			childFeature == ThingMLPackage.eINSTANCE.getDeep_learning_ModelAlgorithm_Batch_size() ||
			childFeature == ThingMLPackage.eINSTANCE.getDeep_learning_ModelAlgorithm_Epochs() ||
			childFeature == ThingMLPackage.eINSTANCE.getTransformer_Units() ||
			childFeature == ThingMLPackage.eINSTANCE.getTransformer_Num_layers() ||
			childFeature == ThingMLPackage.eINSTANCE.getTransformer_D_model() ||
			childFeature == ThingMLPackage.eINSTANCE.getTransformer_Num_heads() ||
			childFeature == ThingMLPackage.eINSTANCE.getTransformer_Dff() ||
			childFeature == ThingMLPackage.eINSTANCE.getTransformer_Neurons();

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
