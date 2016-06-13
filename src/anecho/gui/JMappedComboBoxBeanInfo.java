/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package anecho.gui;

import java.beans.*;

/**
 *
 * @author jeffnik
 */
public class JMappedComboBoxBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( anecho.gui.JMappedComboBox.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor
    // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor


    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_accessibleContext = 0;
    private static final int PROPERTY_action = 1;
    private static final int PROPERTY_actionCommand = 2;
    private static final int PROPERTY_actionListeners = 3;
    private static final int PROPERTY_actionMap = 4;
    private static final int PROPERTY_alignmentX = 5;
    private static final int PROPERTY_alignmentY = 6;
    private static final int PROPERTY_ancestorListeners = 7;
    private static final int PROPERTY_autoscrolls = 8;
    private static final int PROPERTY_background = 9;
    private static final int PROPERTY_backgroundSet = 10;
    private static final int PROPERTY_baselineResizeBehavior = 11;
    private static final int PROPERTY_border = 12;
    private static final int PROPERTY_bounds = 13;
    private static final int PROPERTY_colorModel = 14;
    private static final int PROPERTY_component = 15;
    private static final int PROPERTY_componentCount = 16;
    private static final int PROPERTY_componentListeners = 17;
    private static final int PROPERTY_componentOrientation = 18;
    private static final int PROPERTY_componentPopupMenu = 19;
    private static final int PROPERTY_components = 20;
    private static final int PROPERTY_containerListeners = 21;
    private static final int PROPERTY_cursor = 22;
    private static final int PROPERTY_cursorSet = 23;
    private static final int PROPERTY_debugGraphicsOptions = 24;
    private static final int PROPERTY_displayable = 25;
    private static final int PROPERTY_doubleBuffered = 26;
    private static final int PROPERTY_dropTarget = 27;
    private static final int PROPERTY_editable = 28;
    private static final int PROPERTY_editor = 29;
    private static final int PROPERTY_enabled = 30;
    private static final int PROPERTY_focusable = 31;
    private static final int PROPERTY_focusCycleRoot = 32;
    private static final int PROPERTY_focusCycleRootAncestor = 33;
    private static final int PROPERTY_focusListeners = 34;
    private static final int PROPERTY_focusOwner = 35;
    private static final int PROPERTY_focusTraversable = 36;
    private static final int PROPERTY_focusTraversalKeys = 37;
    private static final int PROPERTY_focusTraversalKeysEnabled = 38;
    private static final int PROPERTY_focusTraversalPolicy = 39;
    private static final int PROPERTY_focusTraversalPolicyProvider = 40;
    private static final int PROPERTY_focusTraversalPolicySet = 41;
    private static final int PROPERTY_font = 42;
    private static final int PROPERTY_fontSet = 43;
    private static final int PROPERTY_foreground = 44;
    private static final int PROPERTY_foregroundSet = 45;
    private static final int PROPERTY_graphics = 46;
    private static final int PROPERTY_graphicsConfiguration = 47;
    private static final int PROPERTY_height = 48;
    private static final int PROPERTY_hierarchyBoundsListeners = 49;
    private static final int PROPERTY_hierarchyListeners = 50;
    private static final int PROPERTY_ignoreRepaint = 51;
    private static final int PROPERTY_inheritsPopupMenu = 52;
    private static final int PROPERTY_inputContext = 53;
    private static final int PROPERTY_inputMap = 54;
    private static final int PROPERTY_inputMethodListeners = 55;
    private static final int PROPERTY_inputMethodRequests = 56;
    private static final int PROPERTY_inputVerifier = 57;
    private static final int PROPERTY_insets = 58;
    private static final int PROPERTY_itemAt = 59;
    private static final int PROPERTY_itemCount = 60;
    private static final int PROPERTY_itemListeners = 61;
    private static final int PROPERTY_keyListeners = 62;
    private static final int PROPERTY_keySelectionManager = 63;
    private static final int PROPERTY_layout = 64;
    private static final int PROPERTY_lightweight = 65;
    private static final int PROPERTY_lightWeightPopupEnabled = 66;
    private static final int PROPERTY_locale = 67;
    private static final int PROPERTY_location = 68;
    private static final int PROPERTY_locationOnScreen = 69;
    private static final int PROPERTY_managingFocus = 70;
    private static final int PROPERTY_mapAt = 71;
    private static final int PROPERTY_maximumRowCount = 72;
    private static final int PROPERTY_maximumSize = 73;
    private static final int PROPERTY_maximumSizeSet = 74;
    private static final int PROPERTY_minimumSize = 75;
    private static final int PROPERTY_minimumSizeSet = 76;
    private static final int PROPERTY_model = 77;
    private static final int PROPERTY_mouseListeners = 78;
    private static final int PROPERTY_mouseMotionListeners = 79;
    private static final int PROPERTY_mousePosition = 80;
    private static final int PROPERTY_mouseWheelListeners = 81;
    private static final int PROPERTY_name = 82;
    private static final int PROPERTY_nextFocusableComponent = 83;
    private static final int PROPERTY_opaque = 84;
    private static final int PROPERTY_optimizedDrawingEnabled = 85;
    private static final int PROPERTY_paintingForPrint = 86;
    private static final int PROPERTY_paintingTile = 87;
    private static final int PROPERTY_parent = 88;
    private static final int PROPERTY_peer = 89;
    private static final int PROPERTY_popupMenuListeners = 90;
    private static final int PROPERTY_popupVisible = 91;
    private static final int PROPERTY_preferredSize = 92;
    private static final int PROPERTY_preferredSizeSet = 93;
    private static final int PROPERTY_propertyChangeListeners = 94;
    private static final int PROPERTY_prototypeDisplayValue = 95;
    private static final int PROPERTY_registeredKeyStrokes = 96;
    private static final int PROPERTY_renderer = 97;
    private static final int PROPERTY_requestFocusEnabled = 98;
    private static final int PROPERTY_rootPane = 99;
    private static final int PROPERTY_selectedIndex = 100;
    private static final int PROPERTY_selectedItem = 101;
    private static final int PROPERTY_selectedMap = 102;
    private static final int PROPERTY_selectedObjects = 103;
    private static final int PROPERTY_showing = 104;
    private static final int PROPERTY_size = 105;
    private static final int PROPERTY_toolkit = 106;
    private static final int PROPERTY_toolTipText = 107;
    private static final int PROPERTY_topLevelAncestor = 108;
    private static final int PROPERTY_transferHandler = 109;
    private static final int PROPERTY_treeLock = 110;
    private static final int PROPERTY_UI = 111;
    private static final int PROPERTY_UIClassID = 112;
    private static final int PROPERTY_valid = 113;
    private static final int PROPERTY_validateRoot = 114;
    private static final int PROPERTY_verifyInputWhenFocusTarget = 115;
    private static final int PROPERTY_vetoableChangeListeners = 116;
    private static final int PROPERTY_visible = 117;
    private static final int PROPERTY_visibleRect = 118;
    private static final int PROPERTY_width = 119;
    private static final int PROPERTY_x = 120;
    private static final int PROPERTY_y = 121;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[122];
    
        try {
            properties[PROPERTY_accessibleContext] = new PropertyDescriptor ( "accessibleContext", anecho.gui.JMappedComboBox.class, "getAccessibleContext", null ); // NOI18N
            properties[PROPERTY_action] = new PropertyDescriptor ( "action", anecho.gui.JMappedComboBox.class, "getAction", "setAction" ); // NOI18N
            properties[PROPERTY_actionCommand] = new PropertyDescriptor ( "actionCommand", anecho.gui.JMappedComboBox.class, "getActionCommand", "setActionCommand" ); // NOI18N
            properties[PROPERTY_actionListeners] = new PropertyDescriptor ( "actionListeners", anecho.gui.JMappedComboBox.class, "getActionListeners", null ); // NOI18N
            properties[PROPERTY_actionMap] = new PropertyDescriptor ( "actionMap", anecho.gui.JMappedComboBox.class, "getActionMap", "setActionMap" ); // NOI18N
            properties[PROPERTY_alignmentX] = new PropertyDescriptor ( "alignmentX", anecho.gui.JMappedComboBox.class, "getAlignmentX", "setAlignmentX" ); // NOI18N
            properties[PROPERTY_alignmentY] = new PropertyDescriptor ( "alignmentY", anecho.gui.JMappedComboBox.class, "getAlignmentY", "setAlignmentY" ); // NOI18N
            properties[PROPERTY_ancestorListeners] = new PropertyDescriptor ( "ancestorListeners", anecho.gui.JMappedComboBox.class, "getAncestorListeners", null ); // NOI18N
            properties[PROPERTY_autoscrolls] = new PropertyDescriptor ( "autoscrolls", anecho.gui.JMappedComboBox.class, "getAutoscrolls", "setAutoscrolls" ); // NOI18N
            properties[PROPERTY_background] = new PropertyDescriptor ( "background", anecho.gui.JMappedComboBox.class, "getBackground", "setBackground" ); // NOI18N
            properties[PROPERTY_backgroundSet] = new PropertyDescriptor ( "backgroundSet", anecho.gui.JMappedComboBox.class, "isBackgroundSet", null ); // NOI18N
            properties[PROPERTY_baselineResizeBehavior] = new PropertyDescriptor ( "baselineResizeBehavior", anecho.gui.JMappedComboBox.class, "getBaselineResizeBehavior", null ); // NOI18N
            properties[PROPERTY_border] = new PropertyDescriptor ( "border", anecho.gui.JMappedComboBox.class, "getBorder", "setBorder" ); // NOI18N
            properties[PROPERTY_bounds] = new PropertyDescriptor ( "bounds", anecho.gui.JMappedComboBox.class, "getBounds", "setBounds" ); // NOI18N
            properties[PROPERTY_colorModel] = new PropertyDescriptor ( "colorModel", anecho.gui.JMappedComboBox.class, "getColorModel", null ); // NOI18N
            properties[PROPERTY_component] = new IndexedPropertyDescriptor ( "component", anecho.gui.JMappedComboBox.class, null, null, "getComponent", null ); // NOI18N
            properties[PROPERTY_componentCount] = new PropertyDescriptor ( "componentCount", anecho.gui.JMappedComboBox.class, "getComponentCount", null ); // NOI18N
            properties[PROPERTY_componentListeners] = new PropertyDescriptor ( "componentListeners", anecho.gui.JMappedComboBox.class, "getComponentListeners", null ); // NOI18N
            properties[PROPERTY_componentOrientation] = new PropertyDescriptor ( "componentOrientation", anecho.gui.JMappedComboBox.class, "getComponentOrientation", "setComponentOrientation" ); // NOI18N
            properties[PROPERTY_componentPopupMenu] = new PropertyDescriptor ( "componentPopupMenu", anecho.gui.JMappedComboBox.class, "getComponentPopupMenu", "setComponentPopupMenu" ); // NOI18N
            properties[PROPERTY_components] = new PropertyDescriptor ( "components", anecho.gui.JMappedComboBox.class, "getComponents", null ); // NOI18N
            properties[PROPERTY_containerListeners] = new PropertyDescriptor ( "containerListeners", anecho.gui.JMappedComboBox.class, "getContainerListeners", null ); // NOI18N
            properties[PROPERTY_cursor] = new PropertyDescriptor ( "cursor", anecho.gui.JMappedComboBox.class, "getCursor", "setCursor" ); // NOI18N
            properties[PROPERTY_cursorSet] = new PropertyDescriptor ( "cursorSet", anecho.gui.JMappedComboBox.class, "isCursorSet", null ); // NOI18N
            properties[PROPERTY_debugGraphicsOptions] = new PropertyDescriptor ( "debugGraphicsOptions", anecho.gui.JMappedComboBox.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions" ); // NOI18N
            properties[PROPERTY_displayable] = new PropertyDescriptor ( "displayable", anecho.gui.JMappedComboBox.class, "isDisplayable", null ); // NOI18N
            properties[PROPERTY_doubleBuffered] = new PropertyDescriptor ( "doubleBuffered", anecho.gui.JMappedComboBox.class, "isDoubleBuffered", "setDoubleBuffered" ); // NOI18N
            properties[PROPERTY_dropTarget] = new PropertyDescriptor ( "dropTarget", anecho.gui.JMappedComboBox.class, "getDropTarget", "setDropTarget" ); // NOI18N
            properties[PROPERTY_editable] = new PropertyDescriptor ( "editable", anecho.gui.JMappedComboBox.class, "isEditable", "setEditable" ); // NOI18N
            properties[PROPERTY_editor] = new PropertyDescriptor ( "editor", anecho.gui.JMappedComboBox.class, "getEditor", "setEditor" ); // NOI18N
            properties[PROPERTY_enabled] = new PropertyDescriptor ( "enabled", anecho.gui.JMappedComboBox.class, "isEnabled", "setEnabled" ); // NOI18N
            properties[PROPERTY_focusable] = new PropertyDescriptor ( "focusable", anecho.gui.JMappedComboBox.class, "isFocusable", "setFocusable" ); // NOI18N
            properties[PROPERTY_focusCycleRoot] = new PropertyDescriptor ( "focusCycleRoot", anecho.gui.JMappedComboBox.class, "isFocusCycleRoot", "setFocusCycleRoot" ); // NOI18N
            properties[PROPERTY_focusCycleRootAncestor] = new PropertyDescriptor ( "focusCycleRootAncestor", anecho.gui.JMappedComboBox.class, "getFocusCycleRootAncestor", null ); // NOI18N
            properties[PROPERTY_focusListeners] = new PropertyDescriptor ( "focusListeners", anecho.gui.JMappedComboBox.class, "getFocusListeners", null ); // NOI18N
            properties[PROPERTY_focusOwner] = new PropertyDescriptor ( "focusOwner", anecho.gui.JMappedComboBox.class, "isFocusOwner", null ); // NOI18N
            properties[PROPERTY_focusTraversable] = new PropertyDescriptor ( "focusTraversable", anecho.gui.JMappedComboBox.class, "isFocusTraversable", null ); // NOI18N
            properties[PROPERTY_focusTraversalKeys] = new IndexedPropertyDescriptor ( "focusTraversalKeys", anecho.gui.JMappedComboBox.class, null, null, null, "setFocusTraversalKeys" ); // NOI18N
            properties[PROPERTY_focusTraversalKeysEnabled] = new PropertyDescriptor ( "focusTraversalKeysEnabled", anecho.gui.JMappedComboBox.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled" ); // NOI18N
            properties[PROPERTY_focusTraversalPolicy] = new PropertyDescriptor ( "focusTraversalPolicy", anecho.gui.JMappedComboBox.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy" ); // NOI18N
            properties[PROPERTY_focusTraversalPolicyProvider] = new PropertyDescriptor ( "focusTraversalPolicyProvider", anecho.gui.JMappedComboBox.class, "isFocusTraversalPolicyProvider", "setFocusTraversalPolicyProvider" ); // NOI18N
            properties[PROPERTY_focusTraversalPolicySet] = new PropertyDescriptor ( "focusTraversalPolicySet", anecho.gui.JMappedComboBox.class, "isFocusTraversalPolicySet", null ); // NOI18N
            properties[PROPERTY_font] = new PropertyDescriptor ( "font", anecho.gui.JMappedComboBox.class, "getFont", "setFont" ); // NOI18N
            properties[PROPERTY_fontSet] = new PropertyDescriptor ( "fontSet", anecho.gui.JMappedComboBox.class, "isFontSet", null ); // NOI18N
            properties[PROPERTY_foreground] = new PropertyDescriptor ( "foreground", anecho.gui.JMappedComboBox.class, "getForeground", "setForeground" ); // NOI18N
            properties[PROPERTY_foregroundSet] = new PropertyDescriptor ( "foregroundSet", anecho.gui.JMappedComboBox.class, "isForegroundSet", null ); // NOI18N
            properties[PROPERTY_graphics] = new PropertyDescriptor ( "graphics", anecho.gui.JMappedComboBox.class, "getGraphics", null ); // NOI18N
            properties[PROPERTY_graphicsConfiguration] = new PropertyDescriptor ( "graphicsConfiguration", anecho.gui.JMappedComboBox.class, "getGraphicsConfiguration", null ); // NOI18N
            properties[PROPERTY_height] = new PropertyDescriptor ( "height", anecho.gui.JMappedComboBox.class, "getHeight", null ); // NOI18N
            properties[PROPERTY_hierarchyBoundsListeners] = new PropertyDescriptor ( "hierarchyBoundsListeners", anecho.gui.JMappedComboBox.class, "getHierarchyBoundsListeners", null ); // NOI18N
            properties[PROPERTY_hierarchyListeners] = new PropertyDescriptor ( "hierarchyListeners", anecho.gui.JMappedComboBox.class, "getHierarchyListeners", null ); // NOI18N
            properties[PROPERTY_ignoreRepaint] = new PropertyDescriptor ( "ignoreRepaint", anecho.gui.JMappedComboBox.class, "getIgnoreRepaint", "setIgnoreRepaint" ); // NOI18N
            properties[PROPERTY_inheritsPopupMenu] = new PropertyDescriptor ( "inheritsPopupMenu", anecho.gui.JMappedComboBox.class, "getInheritsPopupMenu", "setInheritsPopupMenu" ); // NOI18N
            properties[PROPERTY_inputContext] = new PropertyDescriptor ( "inputContext", anecho.gui.JMappedComboBox.class, "getInputContext", null ); // NOI18N
            properties[PROPERTY_inputMap] = new PropertyDescriptor ( "inputMap", anecho.gui.JMappedComboBox.class, "getInputMap", null ); // NOI18N
            properties[PROPERTY_inputMethodListeners] = new PropertyDescriptor ( "inputMethodListeners", anecho.gui.JMappedComboBox.class, "getInputMethodListeners", null ); // NOI18N
            properties[PROPERTY_inputMethodRequests] = new PropertyDescriptor ( "inputMethodRequests", anecho.gui.JMappedComboBox.class, "getInputMethodRequests", null ); // NOI18N
            properties[PROPERTY_inputVerifier] = new PropertyDescriptor ( "inputVerifier", anecho.gui.JMappedComboBox.class, "getInputVerifier", "setInputVerifier" ); // NOI18N
            properties[PROPERTY_insets] = new PropertyDescriptor ( "insets", anecho.gui.JMappedComboBox.class, "getInsets", null ); // NOI18N
            properties[PROPERTY_itemAt] = new IndexedPropertyDescriptor ( "itemAt", anecho.gui.JMappedComboBox.class, null, null, "getItemAt", null ); // NOI18N
            properties[PROPERTY_itemCount] = new PropertyDescriptor ( "itemCount", anecho.gui.JMappedComboBox.class, "getItemCount", null ); // NOI18N
            properties[PROPERTY_itemListeners] = new PropertyDescriptor ( "itemListeners", anecho.gui.JMappedComboBox.class, "getItemListeners", null ); // NOI18N
            properties[PROPERTY_keyListeners] = new PropertyDescriptor ( "keyListeners", anecho.gui.JMappedComboBox.class, "getKeyListeners", null ); // NOI18N
            properties[PROPERTY_keySelectionManager] = new PropertyDescriptor ( "keySelectionManager", anecho.gui.JMappedComboBox.class, "getKeySelectionManager", "setKeySelectionManager" ); // NOI18N
            properties[PROPERTY_layout] = new PropertyDescriptor ( "layout", anecho.gui.JMappedComboBox.class, "getLayout", "setLayout" ); // NOI18N
            properties[PROPERTY_lightweight] = new PropertyDescriptor ( "lightweight", anecho.gui.JMappedComboBox.class, "isLightweight", null ); // NOI18N
            properties[PROPERTY_lightWeightPopupEnabled] = new PropertyDescriptor ( "lightWeightPopupEnabled", anecho.gui.JMappedComboBox.class, "isLightWeightPopupEnabled", "setLightWeightPopupEnabled" ); // NOI18N
            properties[PROPERTY_locale] = new PropertyDescriptor ( "locale", anecho.gui.JMappedComboBox.class, "getLocale", "setLocale" ); // NOI18N
            properties[PROPERTY_location] = new PropertyDescriptor ( "location", anecho.gui.JMappedComboBox.class, "getLocation", "setLocation" ); // NOI18N
            properties[PROPERTY_locationOnScreen] = new PropertyDescriptor ( "locationOnScreen", anecho.gui.JMappedComboBox.class, "getLocationOnScreen", null ); // NOI18N
            properties[PROPERTY_managingFocus] = new PropertyDescriptor ( "managingFocus", anecho.gui.JMappedComboBox.class, "isManagingFocus", null ); // NOI18N
            properties[PROPERTY_mapAt] = new IndexedPropertyDescriptor ( "mapAt", anecho.gui.JMappedComboBox.class, null, null, "getMapAt", null ); // NOI18N
            properties[PROPERTY_maximumRowCount] = new PropertyDescriptor ( "maximumRowCount", anecho.gui.JMappedComboBox.class, "getMaximumRowCount", "setMaximumRowCount" ); // NOI18N
            properties[PROPERTY_maximumSize] = new PropertyDescriptor ( "maximumSize", anecho.gui.JMappedComboBox.class, "getMaximumSize", "setMaximumSize" ); // NOI18N
            properties[PROPERTY_maximumSizeSet] = new PropertyDescriptor ( "maximumSizeSet", anecho.gui.JMappedComboBox.class, "isMaximumSizeSet", null ); // NOI18N
            properties[PROPERTY_minimumSize] = new PropertyDescriptor ( "minimumSize", anecho.gui.JMappedComboBox.class, "getMinimumSize", "setMinimumSize" ); // NOI18N
            properties[PROPERTY_minimumSizeSet] = new PropertyDescriptor ( "minimumSizeSet", anecho.gui.JMappedComboBox.class, "isMinimumSizeSet", null ); // NOI18N
            properties[PROPERTY_model] = new PropertyDescriptor ( "model", anecho.gui.JMappedComboBox.class, "getModel", "setModel" ); // NOI18N
            properties[PROPERTY_mouseListeners] = new PropertyDescriptor ( "mouseListeners", anecho.gui.JMappedComboBox.class, "getMouseListeners", null ); // NOI18N
            properties[PROPERTY_mouseMotionListeners] = new PropertyDescriptor ( "mouseMotionListeners", anecho.gui.JMappedComboBox.class, "getMouseMotionListeners", null ); // NOI18N
            properties[PROPERTY_mousePosition] = new PropertyDescriptor ( "mousePosition", anecho.gui.JMappedComboBox.class, "getMousePosition", null ); // NOI18N
            properties[PROPERTY_mouseWheelListeners] = new PropertyDescriptor ( "mouseWheelListeners", anecho.gui.JMappedComboBox.class, "getMouseWheelListeners", null ); // NOI18N
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", anecho.gui.JMappedComboBox.class, "getName", "setName" ); // NOI18N
            properties[PROPERTY_nextFocusableComponent] = new PropertyDescriptor ( "nextFocusableComponent", anecho.gui.JMappedComboBox.class, "getNextFocusableComponent", "setNextFocusableComponent" ); // NOI18N
            properties[PROPERTY_opaque] = new PropertyDescriptor ( "opaque", anecho.gui.JMappedComboBox.class, "isOpaque", "setOpaque" ); // NOI18N
            properties[PROPERTY_optimizedDrawingEnabled] = new PropertyDescriptor ( "optimizedDrawingEnabled", anecho.gui.JMappedComboBox.class, "isOptimizedDrawingEnabled", null ); // NOI18N
            properties[PROPERTY_paintingForPrint] = new PropertyDescriptor ( "paintingForPrint", anecho.gui.JMappedComboBox.class, "isPaintingForPrint", null ); // NOI18N
            properties[PROPERTY_paintingTile] = new PropertyDescriptor ( "paintingTile", anecho.gui.JMappedComboBox.class, "isPaintingTile", null ); // NOI18N
            properties[PROPERTY_parent] = new PropertyDescriptor ( "parent", anecho.gui.JMappedComboBox.class, "getParent", null ); // NOI18N
            properties[PROPERTY_peer] = new PropertyDescriptor ( "peer", anecho.gui.JMappedComboBox.class, "getPeer", null ); // NOI18N
            properties[PROPERTY_popupMenuListeners] = new PropertyDescriptor ( "popupMenuListeners", anecho.gui.JMappedComboBox.class, "getPopupMenuListeners", null ); // NOI18N
            properties[PROPERTY_popupVisible] = new PropertyDescriptor ( "popupVisible", anecho.gui.JMappedComboBox.class, "isPopupVisible", "setPopupVisible" ); // NOI18N
            properties[PROPERTY_preferredSize] = new PropertyDescriptor ( "preferredSize", anecho.gui.JMappedComboBox.class, "getPreferredSize", "setPreferredSize" ); // NOI18N
            properties[PROPERTY_preferredSizeSet] = new PropertyDescriptor ( "preferredSizeSet", anecho.gui.JMappedComboBox.class, "isPreferredSizeSet", null ); // NOI18N
            properties[PROPERTY_propertyChangeListeners] = new PropertyDescriptor ( "propertyChangeListeners", anecho.gui.JMappedComboBox.class, "getPropertyChangeListeners", null ); // NOI18N
            properties[PROPERTY_prototypeDisplayValue] = new PropertyDescriptor ( "prototypeDisplayValue", anecho.gui.JMappedComboBox.class, "getPrototypeDisplayValue", "setPrototypeDisplayValue" ); // NOI18N
            properties[PROPERTY_registeredKeyStrokes] = new PropertyDescriptor ( "registeredKeyStrokes", anecho.gui.JMappedComboBox.class, "getRegisteredKeyStrokes", null ); // NOI18N
            properties[PROPERTY_renderer] = new PropertyDescriptor ( "renderer", anecho.gui.JMappedComboBox.class, "getRenderer", "setRenderer" ); // NOI18N
            properties[PROPERTY_requestFocusEnabled] = new PropertyDescriptor ( "requestFocusEnabled", anecho.gui.JMappedComboBox.class, "isRequestFocusEnabled", "setRequestFocusEnabled" ); // NOI18N
            properties[PROPERTY_rootPane] = new PropertyDescriptor ( "rootPane", anecho.gui.JMappedComboBox.class, "getRootPane", null ); // NOI18N
            properties[PROPERTY_selectedIndex] = new PropertyDescriptor ( "selectedIndex", anecho.gui.JMappedComboBox.class, "getSelectedIndex", "setSelectedIndex" ); // NOI18N
            properties[PROPERTY_selectedItem] = new PropertyDescriptor ( "selectedItem", anecho.gui.JMappedComboBox.class, "getSelectedItem", "setSelectedItem" ); // NOI18N
            properties[PROPERTY_selectedMap] = new PropertyDescriptor ( "selectedMap", anecho.gui.JMappedComboBox.class, "getSelectedMap", "setSelectedMap" ); // NOI18N
            properties[PROPERTY_selectedObjects] = new PropertyDescriptor ( "selectedObjects", anecho.gui.JMappedComboBox.class, "getSelectedObjects", null ); // NOI18N
            properties[PROPERTY_showing] = new PropertyDescriptor ( "showing", anecho.gui.JMappedComboBox.class, "isShowing", null ); // NOI18N
            properties[PROPERTY_size] = new PropertyDescriptor ( "size", anecho.gui.JMappedComboBox.class, "getSize", "setSize" ); // NOI18N
            properties[PROPERTY_toolkit] = new PropertyDescriptor ( "toolkit", anecho.gui.JMappedComboBox.class, "getToolkit", null ); // NOI18N
            properties[PROPERTY_toolTipText] = new PropertyDescriptor ( "toolTipText", anecho.gui.JMappedComboBox.class, "getToolTipText", "setToolTipText" ); // NOI18N
            properties[PROPERTY_topLevelAncestor] = new PropertyDescriptor ( "topLevelAncestor", anecho.gui.JMappedComboBox.class, "getTopLevelAncestor", null ); // NOI18N
            properties[PROPERTY_transferHandler] = new PropertyDescriptor ( "transferHandler", anecho.gui.JMappedComboBox.class, "getTransferHandler", "setTransferHandler" ); // NOI18N
            properties[PROPERTY_treeLock] = new PropertyDescriptor ( "treeLock", anecho.gui.JMappedComboBox.class, "getTreeLock", null ); // NOI18N
            properties[PROPERTY_UI] = new PropertyDescriptor ( "UI", anecho.gui.JMappedComboBox.class, "getUI", "setUI" ); // NOI18N
            properties[PROPERTY_UIClassID] = new PropertyDescriptor ( "UIClassID", anecho.gui.JMappedComboBox.class, "getUIClassID", null ); // NOI18N
            properties[PROPERTY_valid] = new PropertyDescriptor ( "valid", anecho.gui.JMappedComboBox.class, "isValid", null ); // NOI18N
            properties[PROPERTY_validateRoot] = new PropertyDescriptor ( "validateRoot", anecho.gui.JMappedComboBox.class, "isValidateRoot", null ); // NOI18N
            properties[PROPERTY_verifyInputWhenFocusTarget] = new PropertyDescriptor ( "verifyInputWhenFocusTarget", anecho.gui.JMappedComboBox.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget" ); // NOI18N
            properties[PROPERTY_vetoableChangeListeners] = new PropertyDescriptor ( "vetoableChangeListeners", anecho.gui.JMappedComboBox.class, "getVetoableChangeListeners", null ); // NOI18N
            properties[PROPERTY_visible] = new PropertyDescriptor ( "visible", anecho.gui.JMappedComboBox.class, "isVisible", "setVisible" ); // NOI18N
            properties[PROPERTY_visibleRect] = new PropertyDescriptor ( "visibleRect", anecho.gui.JMappedComboBox.class, "getVisibleRect", null ); // NOI18N
            properties[PROPERTY_width] = new PropertyDescriptor ( "width", anecho.gui.JMappedComboBox.class, "getWidth", null ); // NOI18N
            properties[PROPERTY_x] = new PropertyDescriptor ( "x", anecho.gui.JMappedComboBox.class, "getX", null ); // NOI18N
            properties[PROPERTY_y] = new PropertyDescriptor ( "y", anecho.gui.JMappedComboBox.class, "getY", null ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties
    // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties

    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_actionListener = 0;
    private static final int EVENT_ancestorListener = 1;
    private static final int EVENT_componentListener = 2;
    private static final int EVENT_containerListener = 3;
    private static final int EVENT_focusListener = 4;
    private static final int EVENT_hierarchyBoundsListener = 5;
    private static final int EVENT_hierarchyListener = 6;
    private static final int EVENT_inputMethodListener = 7;
    private static final int EVENT_itemListener = 8;
    private static final int EVENT_keyListener = 9;
    private static final int EVENT_mouseListener = 10;
    private static final int EVENT_mouseMotionListener = 11;
    private static final int EVENT_mouseWheelListener = 12;
    private static final int EVENT_popupMenuListener = 13;
    private static final int EVENT_propertyChangeListener = 14;
    private static final int EVENT_vetoableChangeListener = 15;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[16];
    
        try {
            eventSets[EVENT_actionListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "actionListener", java.awt.event.ActionListener.class, new String[] {"actionPerformed"}, "addActionListener", "removeActionListener" ); // NOI18N
            eventSets[EVENT_ancestorListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] {"ancestorAdded", "ancestorRemoved", "ancestorMoved"}, "addAncestorListener", "removeAncestorListener" ); // NOI18N
            eventSets[EVENT_componentListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "componentListener", java.awt.event.ComponentListener.class, new String[] {"componentResized", "componentMoved", "componentShown", "componentHidden"}, "addComponentListener", "removeComponentListener" ); // NOI18N
            eventSets[EVENT_containerListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "containerListener", java.awt.event.ContainerListener.class, new String[] {"componentAdded", "componentRemoved"}, "addContainerListener", "removeContainerListener" ); // NOI18N
            eventSets[EVENT_focusListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "focusListener", java.awt.event.FocusListener.class, new String[] {"focusGained", "focusLost"}, "addFocusListener", "removeFocusListener" ); // NOI18N
            eventSets[EVENT_hierarchyBoundsListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] {"ancestorMoved", "ancestorResized"}, "addHierarchyBoundsListener", "removeHierarchyBoundsListener" ); // NOI18N
            eventSets[EVENT_hierarchyListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] {"hierarchyChanged"}, "addHierarchyListener", "removeHierarchyListener" ); // NOI18N
            eventSets[EVENT_inputMethodListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] {"inputMethodTextChanged", "caretPositionChanged"}, "addInputMethodListener", "removeInputMethodListener" ); // NOI18N
            eventSets[EVENT_itemListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "itemListener", java.awt.event.ItemListener.class, new String[] {"itemStateChanged"}, "addItemListener", "removeItemListener" ); // NOI18N
            eventSets[EVENT_keyListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "keyListener", java.awt.event.KeyListener.class, new String[] {"keyTyped", "keyPressed", "keyReleased"}, "addKeyListener", "removeKeyListener" ); // NOI18N
            eventSets[EVENT_mouseListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "mouseListener", java.awt.event.MouseListener.class, new String[] {"mouseClicked", "mousePressed", "mouseReleased", "mouseEntered", "mouseExited"}, "addMouseListener", "removeMouseListener" ); // NOI18N
            eventSets[EVENT_mouseMotionListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] {"mouseDragged", "mouseMoved"}, "addMouseMotionListener", "removeMouseMotionListener" ); // NOI18N
            eventSets[EVENT_mouseWheelListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] {"mouseWheelMoved"}, "addMouseWheelListener", "removeMouseWheelListener" ); // NOI18N
            eventSets[EVENT_popupMenuListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "popupMenuListener", javax.swing.event.PopupMenuListener.class, new String[] {"popupMenuWillBecomeVisible", "popupMenuWillBecomeInvisible", "popupMenuCanceled"}, "addPopupMenuListener", "removePopupMenuListener" ); // NOI18N
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener", "removePropertyChangeListener" ); // NOI18N
            eventSets[EVENT_vetoableChangeListener] = new EventSetDescriptor ( anecho.gui.JMappedComboBox.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] {"vetoableChange"}, "addVetoableChangeListener", "removeVetoableChangeListener" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Events
    // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_action0 = 0;
    private static final int METHOD_actionPerformed1 = 1;
    private static final int METHOD_add2 = 2;
    private static final int METHOD_add3 = 3;
    private static final int METHOD_add4 = 4;
    private static final int METHOD_add5 = 5;
    private static final int METHOD_add6 = 6;
    private static final int METHOD_add7 = 7;
    private static final int METHOD_addItem8 = 8;
    private static final int METHOD_addItem9 = 9;
    private static final int METHOD_addNotify10 = 10;
    private static final int METHOD_addPropertyChangeListener11 = 11;
    private static final int METHOD_applyComponentOrientation12 = 12;
    private static final int METHOD_areFocusTraversalKeysSet13 = 13;
    private static final int METHOD_bounds14 = 14;
    private static final int METHOD_checkImage15 = 15;
    private static final int METHOD_checkImage16 = 16;
    private static final int METHOD_computeVisibleRect17 = 17;
    private static final int METHOD_configureEditor18 = 18;
    private static final int METHOD_contains19 = 19;
    private static final int METHOD_contains20 = 20;
    private static final int METHOD_containsItem21 = 21;
    private static final int METHOD_contentsChanged22 = 22;
    private static final int METHOD_countComponents23 = 23;
    private static final int METHOD_createImage24 = 24;
    private static final int METHOD_createImage25 = 25;
    private static final int METHOD_createToolTip26 = 26;
    private static final int METHOD_createVolatileImage27 = 27;
    private static final int METHOD_createVolatileImage28 = 28;
    private static final int METHOD_deliverEvent29 = 29;
    private static final int METHOD_disable30 = 30;
    private static final int METHOD_dispatchEvent31 = 31;
    private static final int METHOD_doLayout32 = 32;
    private static final int METHOD_enable33 = 33;
    private static final int METHOD_enable34 = 34;
    private static final int METHOD_enableInputMethods35 = 35;
    private static final int METHOD_findComponentAt36 = 36;
    private static final int METHOD_findComponentAt37 = 37;
    private static final int METHOD_firePopupMenuCanceled38 = 38;
    private static final int METHOD_firePopupMenuWillBecomeInvisible39 = 39;
    private static final int METHOD_firePopupMenuWillBecomeVisible40 = 40;
    private static final int METHOD_firePropertyChange41 = 41;
    private static final int METHOD_firePropertyChange42 = 42;
    private static final int METHOD_firePropertyChange43 = 43;
    private static final int METHOD_firePropertyChange44 = 44;
    private static final int METHOD_firePropertyChange45 = 45;
    private static final int METHOD_firePropertyChange46 = 46;
    private static final int METHOD_firePropertyChange47 = 47;
    private static final int METHOD_firePropertyChange48 = 48;
    private static final int METHOD_getActionForKeyStroke49 = 49;
    private static final int METHOD_getBaseline50 = 50;
    private static final int METHOD_getBounds51 = 51;
    private static final int METHOD_getClientProperty52 = 52;
    private static final int METHOD_getComponentAt53 = 53;
    private static final int METHOD_getComponentAt54 = 54;
    private static final int METHOD_getComponentZOrder55 = 55;
    private static final int METHOD_getConditionForKeyStroke56 = 56;
    private static final int METHOD_getDefaultLocale57 = 57;
    private static final int METHOD_getFocusTraversalKeys58 = 58;
    private static final int METHOD_getFontMetrics59 = 59;
    private static final int METHOD_getInsets60 = 60;
    private static final int METHOD_getItemIndex61 = 61;
    private static final int METHOD_getItemIndex62 = 62;
    private static final int METHOD_getListeners63 = 63;
    private static final int METHOD_getLocation64 = 64;
    private static final int METHOD_getMapIndex65 = 65;
    private static final int METHOD_getMapIndex66 = 66;
    private static final int METHOD_getMappedObject67 = 67;
    private static final int METHOD_getMousePosition68 = 68;
    private static final int METHOD_getPopupLocation69 = 69;
    private static final int METHOD_getPropertyChangeListeners70 = 70;
    private static final int METHOD_getSize71 = 71;
    private static final int METHOD_getToolTipLocation72 = 72;
    private static final int METHOD_getToolTipText73 = 73;
    private static final int METHOD_gotFocus74 = 74;
    private static final int METHOD_grabFocus75 = 75;
    private static final int METHOD_handleEvent76 = 76;
    private static final int METHOD_hasFocus77 = 77;
    private static final int METHOD_hide78 = 78;
    private static final int METHOD_hidePopup79 = 79;
    private static final int METHOD_imageUpdate80 = 80;
    private static final int METHOD_insertItemAt81 = 81;
    private static final int METHOD_insertItemAt82 = 82;
    private static final int METHOD_insets83 = 83;
    private static final int METHOD_inside84 = 84;
    private static final int METHOD_intervalAdded85 = 85;
    private static final int METHOD_intervalRemoved86 = 86;
    private static final int METHOD_invalidate87 = 87;
    private static final int METHOD_isAncestorOf88 = 88;
    private static final int METHOD_isFocusCycleRoot89 = 89;
    private static final int METHOD_isLightweightComponent90 = 90;
    private static final int METHOD_keyDown91 = 91;
    private static final int METHOD_keyUp92 = 92;
    private static final int METHOD_layout93 = 93;
    private static final int METHOD_list94 = 94;
    private static final int METHOD_list95 = 95;
    private static final int METHOD_list96 = 96;
    private static final int METHOD_list97 = 97;
    private static final int METHOD_list98 = 98;
    private static final int METHOD_locate99 = 99;
    private static final int METHOD_location100 = 100;
    private static final int METHOD_lostFocus101 = 101;
    private static final int METHOD_minimumSize102 = 102;
    private static final int METHOD_mouseDown103 = 103;
    private static final int METHOD_mouseDrag104 = 104;
    private static final int METHOD_mouseEnter105 = 105;
    private static final int METHOD_mouseExit106 = 106;
    private static final int METHOD_mouseMove107 = 107;
    private static final int METHOD_mouseUp108 = 108;
    private static final int METHOD_move109 = 109;
    private static final int METHOD_nextFocus110 = 110;
    private static final int METHOD_paint111 = 111;
    private static final int METHOD_paintAll112 = 112;
    private static final int METHOD_paintComponents113 = 113;
    private static final int METHOD_paintImmediately114 = 114;
    private static final int METHOD_paintImmediately115 = 115;
    private static final int METHOD_postEvent116 = 116;
    private static final int METHOD_preferredSize117 = 117;
    private static final int METHOD_prepareImage118 = 118;
    private static final int METHOD_prepareImage119 = 119;
    private static final int METHOD_print120 = 120;
    private static final int METHOD_printAll121 = 121;
    private static final int METHOD_printComponents122 = 122;
    private static final int METHOD_processKeyEvent123 = 123;
    private static final int METHOD_putClientProperty124 = 124;
    private static final int METHOD_registerKeyboardAction125 = 125;
    private static final int METHOD_registerKeyboardAction126 = 126;
    private static final int METHOD_remove127 = 127;
    private static final int METHOD_remove128 = 128;
    private static final int METHOD_remove129 = 129;
    private static final int METHOD_removeAll130 = 130;
    private static final int METHOD_removeAllItems131 = 131;
    private static final int METHOD_removeItem132 = 132;
    private static final int METHOD_removeItemAt133 = 133;
    private static final int METHOD_removeNotify134 = 134;
    private static final int METHOD_removePropertyChangeListener135 = 135;
    private static final int METHOD_repaint136 = 136;
    private static final int METHOD_repaint137 = 137;
    private static final int METHOD_repaint138 = 138;
    private static final int METHOD_repaint139 = 139;
    private static final int METHOD_repaint140 = 140;
    private static final int METHOD_requestDefaultFocus141 = 141;
    private static final int METHOD_requestFocus142 = 142;
    private static final int METHOD_requestFocus143 = 143;
    private static final int METHOD_requestFocusInWindow144 = 144;
    private static final int METHOD_resetKeyboardActions145 = 145;
    private static final int METHOD_reshape146 = 146;
    private static final int METHOD_resize147 = 147;
    private static final int METHOD_resize148 = 148;
    private static final int METHOD_revalidate149 = 149;
    private static final int METHOD_scrollRectToVisible150 = 150;
    private static final int METHOD_selectWithKeyChar151 = 151;
    private static final int METHOD_setBounds152 = 152;
    private static final int METHOD_setComponentZOrder153 = 153;
    private static final int METHOD_setDefaultLocale154 = 154;
    private static final int METHOD_show155 = 155;
    private static final int METHOD_show156 = 156;
    private static final int METHOD_showPopup157 = 157;
    private static final int METHOD_size158 = 158;
    private static final int METHOD_toString159 = 159;
    private static final int METHOD_transferFocus160 = 160;
    private static final int METHOD_transferFocusBackward161 = 161;
    private static final int METHOD_transferFocusDownCycle162 = 162;
    private static final int METHOD_transferFocusUpCycle163 = 163;
    private static final int METHOD_unregisterKeyboardAction164 = 164;
    private static final int METHOD_update165 = 165;
    private static final int METHOD_updateUI166 = 166;
    private static final int METHOD_validate167 = 167;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[168];
    
        try {
            methods[METHOD_action0] = new MethodDescriptor(java.awt.Component.class.getMethod("action", new Class[] {java.awt.Event.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_action0].setDisplayName ( "" );
            methods[METHOD_actionPerformed1] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("actionPerformed", new Class[] {java.awt.event.ActionEvent.class})); // NOI18N
            methods[METHOD_actionPerformed1].setDisplayName ( "" );
            methods[METHOD_add2] = new MethodDescriptor(java.awt.Component.class.getMethod("add", new Class[] {java.awt.PopupMenu.class})); // NOI18N
            methods[METHOD_add2].setDisplayName ( "" );
            methods[METHOD_add3] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_add3].setDisplayName ( "" );
            methods[METHOD_add4] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] {java.lang.String.class, java.awt.Component.class})); // NOI18N
            methods[METHOD_add4].setDisplayName ( "" );
            methods[METHOD_add5] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] {java.awt.Component.class, int.class})); // NOI18N
            methods[METHOD_add5].setDisplayName ( "" );
            methods[METHOD_add6] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] {java.awt.Component.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_add6].setDisplayName ( "" );
            methods[METHOD_add7] = new MethodDescriptor(java.awt.Container.class.getMethod("add", new Class[] {java.awt.Component.class, java.lang.Object.class, int.class})); // NOI18N
            methods[METHOD_add7].setDisplayName ( "" );
            methods[METHOD_addItem8] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("addItem", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_addItem8].setDisplayName ( "" );
            methods[METHOD_addItem9] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("addItem", new Class[] {java.lang.Object.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_addItem9].setDisplayName ( "" );
            methods[METHOD_addNotify10] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("addNotify", new Class[] {})); // NOI18N
            methods[METHOD_addNotify10].setDisplayName ( "" );
            methods[METHOD_addPropertyChangeListener11] = new MethodDescriptor(java.awt.Container.class.getMethod("addPropertyChangeListener", new Class[] {java.lang.String.class, java.beans.PropertyChangeListener.class})); // NOI18N
            methods[METHOD_addPropertyChangeListener11].setDisplayName ( "" );
            methods[METHOD_applyComponentOrientation12] = new MethodDescriptor(java.awt.Container.class.getMethod("applyComponentOrientation", new Class[] {java.awt.ComponentOrientation.class})); // NOI18N
            methods[METHOD_applyComponentOrientation12].setDisplayName ( "" );
            methods[METHOD_areFocusTraversalKeysSet13] = new MethodDescriptor(java.awt.Container.class.getMethod("areFocusTraversalKeysSet", new Class[] {int.class})); // NOI18N
            methods[METHOD_areFocusTraversalKeysSet13].setDisplayName ( "" );
            methods[METHOD_bounds14] = new MethodDescriptor(java.awt.Component.class.getMethod("bounds", new Class[] {})); // NOI18N
            methods[METHOD_bounds14].setDisplayName ( "" );
            methods[METHOD_checkImage15] = new MethodDescriptor(java.awt.Component.class.getMethod("checkImage", new Class[] {java.awt.Image.class, java.awt.image.ImageObserver.class})); // NOI18N
            methods[METHOD_checkImage15].setDisplayName ( "" );
            methods[METHOD_checkImage16] = new MethodDescriptor(java.awt.Component.class.getMethod("checkImage", new Class[] {java.awt.Image.class, int.class, int.class, java.awt.image.ImageObserver.class})); // NOI18N
            methods[METHOD_checkImage16].setDisplayName ( "" );
            methods[METHOD_computeVisibleRect17] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("computeVisibleRect", new Class[] {java.awt.Rectangle.class})); // NOI18N
            methods[METHOD_computeVisibleRect17].setDisplayName ( "" );
            methods[METHOD_configureEditor18] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("configureEditor", new Class[] {javax.swing.ComboBoxEditor.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_configureEditor18].setDisplayName ( "" );
            methods[METHOD_contains19] = new MethodDescriptor(java.awt.Component.class.getMethod("contains", new Class[] {java.awt.Point.class})); // NOI18N
            methods[METHOD_contains19].setDisplayName ( "" );
            methods[METHOD_contains20] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("contains", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_contains20].setDisplayName ( "" );
            methods[METHOD_containsItem21] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("containsItem", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_containsItem21].setDisplayName ( "" );
            methods[METHOD_contentsChanged22] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("contentsChanged", new Class[] {javax.swing.event.ListDataEvent.class})); // NOI18N
            methods[METHOD_contentsChanged22].setDisplayName ( "" );
            methods[METHOD_countComponents23] = new MethodDescriptor(java.awt.Container.class.getMethod("countComponents", new Class[] {})); // NOI18N
            methods[METHOD_countComponents23].setDisplayName ( "" );
            methods[METHOD_createImage24] = new MethodDescriptor(java.awt.Component.class.getMethod("createImage", new Class[] {java.awt.image.ImageProducer.class})); // NOI18N
            methods[METHOD_createImage24].setDisplayName ( "" );
            methods[METHOD_createImage25] = new MethodDescriptor(java.awt.Component.class.getMethod("createImage", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_createImage25].setDisplayName ( "" );
            methods[METHOD_createToolTip26] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("createToolTip", new Class[] {})); // NOI18N
            methods[METHOD_createToolTip26].setDisplayName ( "" );
            methods[METHOD_createVolatileImage27] = new MethodDescriptor(java.awt.Component.class.getMethod("createVolatileImage", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_createVolatileImage27].setDisplayName ( "" );
            methods[METHOD_createVolatileImage28] = new MethodDescriptor(java.awt.Component.class.getMethod("createVolatileImage", new Class[] {int.class, int.class, java.awt.ImageCapabilities.class})); // NOI18N
            methods[METHOD_createVolatileImage28].setDisplayName ( "" );
            methods[METHOD_deliverEvent29] = new MethodDescriptor(java.awt.Container.class.getMethod("deliverEvent", new Class[] {java.awt.Event.class})); // NOI18N
            methods[METHOD_deliverEvent29].setDisplayName ( "" );
            methods[METHOD_disable30] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("disable", new Class[] {})); // NOI18N
            methods[METHOD_disable30].setDisplayName ( "" );
            methods[METHOD_dispatchEvent31] = new MethodDescriptor(java.awt.Component.class.getMethod("dispatchEvent", new Class[] {java.awt.AWTEvent.class})); // NOI18N
            methods[METHOD_dispatchEvent31].setDisplayName ( "" );
            methods[METHOD_doLayout32] = new MethodDescriptor(java.awt.Container.class.getMethod("doLayout", new Class[] {})); // NOI18N
            methods[METHOD_doLayout32].setDisplayName ( "" );
            methods[METHOD_enable33] = new MethodDescriptor(java.awt.Component.class.getMethod("enable", new Class[] {boolean.class})); // NOI18N
            methods[METHOD_enable33].setDisplayName ( "" );
            methods[METHOD_enable34] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("enable", new Class[] {})); // NOI18N
            methods[METHOD_enable34].setDisplayName ( "" );
            methods[METHOD_enableInputMethods35] = new MethodDescriptor(java.awt.Component.class.getMethod("enableInputMethods", new Class[] {boolean.class})); // NOI18N
            methods[METHOD_enableInputMethods35].setDisplayName ( "" );
            methods[METHOD_findComponentAt36] = new MethodDescriptor(java.awt.Container.class.getMethod("findComponentAt", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_findComponentAt36].setDisplayName ( "" );
            methods[METHOD_findComponentAt37] = new MethodDescriptor(java.awt.Container.class.getMethod("findComponentAt", new Class[] {java.awt.Point.class})); // NOI18N
            methods[METHOD_findComponentAt37].setDisplayName ( "" );
            methods[METHOD_firePopupMenuCanceled38] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("firePopupMenuCanceled", new Class[] {})); // NOI18N
            methods[METHOD_firePopupMenuCanceled38].setDisplayName ( "" );
            methods[METHOD_firePopupMenuWillBecomeInvisible39] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("firePopupMenuWillBecomeInvisible", new Class[] {})); // NOI18N
            methods[METHOD_firePopupMenuWillBecomeInvisible39].setDisplayName ( "" );
            methods[METHOD_firePopupMenuWillBecomeVisible40] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("firePopupMenuWillBecomeVisible", new Class[] {})); // NOI18N
            methods[METHOD_firePopupMenuWillBecomeVisible40].setDisplayName ( "" );
            methods[METHOD_firePropertyChange41] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, byte.class, byte.class})); // NOI18N
            methods[METHOD_firePropertyChange41].setDisplayName ( "" );
            methods[METHOD_firePropertyChange42] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, short.class, short.class})); // NOI18N
            methods[METHOD_firePropertyChange42].setDisplayName ( "" );
            methods[METHOD_firePropertyChange43] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, long.class, long.class})); // NOI18N
            methods[METHOD_firePropertyChange43].setDisplayName ( "" );
            methods[METHOD_firePropertyChange44] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, float.class, float.class})); // NOI18N
            methods[METHOD_firePropertyChange44].setDisplayName ( "" );
            methods[METHOD_firePropertyChange45] = new MethodDescriptor(java.awt.Component.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, double.class, double.class})); // NOI18N
            methods[METHOD_firePropertyChange45].setDisplayName ( "" );
            methods[METHOD_firePropertyChange46] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, boolean.class, boolean.class})); // NOI18N
            methods[METHOD_firePropertyChange46].setDisplayName ( "" );
            methods[METHOD_firePropertyChange47] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, int.class, int.class})); // NOI18N
            methods[METHOD_firePropertyChange47].setDisplayName ( "" );
            methods[METHOD_firePropertyChange48] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, char.class, char.class})); // NOI18N
            methods[METHOD_firePropertyChange48].setDisplayName ( "" );
            methods[METHOD_getActionForKeyStroke49] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getActionForKeyStroke", new Class[] {javax.swing.KeyStroke.class})); // NOI18N
            methods[METHOD_getActionForKeyStroke49].setDisplayName ( "" );
            methods[METHOD_getBaseline50] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getBaseline", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_getBaseline50].setDisplayName ( "" );
            methods[METHOD_getBounds51] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getBounds", new Class[] {java.awt.Rectangle.class})); // NOI18N
            methods[METHOD_getBounds51].setDisplayName ( "" );
            methods[METHOD_getClientProperty52] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getClientProperty", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_getClientProperty52].setDisplayName ( "" );
            methods[METHOD_getComponentAt53] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentAt", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_getComponentAt53].setDisplayName ( "" );
            methods[METHOD_getComponentAt54] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentAt", new Class[] {java.awt.Point.class})); // NOI18N
            methods[METHOD_getComponentAt54].setDisplayName ( "" );
            methods[METHOD_getComponentZOrder55] = new MethodDescriptor(java.awt.Container.class.getMethod("getComponentZOrder", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_getComponentZOrder55].setDisplayName ( "" );
            methods[METHOD_getConditionForKeyStroke56] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getConditionForKeyStroke", new Class[] {javax.swing.KeyStroke.class})); // NOI18N
            methods[METHOD_getConditionForKeyStroke56].setDisplayName ( "" );
            methods[METHOD_getDefaultLocale57] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getDefaultLocale", new Class[] {})); // NOI18N
            methods[METHOD_getDefaultLocale57].setDisplayName ( "" );
            methods[METHOD_getFocusTraversalKeys58] = new MethodDescriptor(java.awt.Container.class.getMethod("getFocusTraversalKeys", new Class[] {int.class})); // NOI18N
            methods[METHOD_getFocusTraversalKeys58].setDisplayName ( "" );
            methods[METHOD_getFontMetrics59] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getFontMetrics", new Class[] {java.awt.Font.class})); // NOI18N
            methods[METHOD_getFontMetrics59].setDisplayName ( "" );
            methods[METHOD_getInsets60] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getInsets", new Class[] {java.awt.Insets.class})); // NOI18N
            methods[METHOD_getInsets60].setDisplayName ( "" );
            methods[METHOD_getItemIndex61] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("getItemIndex", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_getItemIndex61].setDisplayName ( "" );
            methods[METHOD_getItemIndex62] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("getItemIndex", new Class[] {java.lang.Object.class, int.class})); // NOI18N
            methods[METHOD_getItemIndex62].setDisplayName ( "" );
            methods[METHOD_getListeners63] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getListeners", new Class[] {java.lang.Class.class})); // NOI18N
            methods[METHOD_getListeners63].setDisplayName ( "" );
            methods[METHOD_getLocation64] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getLocation", new Class[] {java.awt.Point.class})); // NOI18N
            methods[METHOD_getLocation64].setDisplayName ( "" );
            methods[METHOD_getMapIndex65] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("getMapIndex", new Class[] {java.lang.Object.class, int.class})); // NOI18N
            methods[METHOD_getMapIndex65].setDisplayName ( "" );
            methods[METHOD_getMapIndex66] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("getMapIndex", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_getMapIndex66].setDisplayName ( "" );
            methods[METHOD_getMappedObject67] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("getMappedObject", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_getMappedObject67].setDisplayName ( "" );
            methods[METHOD_getMousePosition68] = new MethodDescriptor(java.awt.Container.class.getMethod("getMousePosition", new Class[] {boolean.class})); // NOI18N
            methods[METHOD_getMousePosition68].setDisplayName ( "" );
            methods[METHOD_getPopupLocation69] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getPopupLocation", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_getPopupLocation69].setDisplayName ( "" );
            methods[METHOD_getPropertyChangeListeners70] = new MethodDescriptor(java.awt.Component.class.getMethod("getPropertyChangeListeners", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getPropertyChangeListeners70].setDisplayName ( "" );
            methods[METHOD_getSize71] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getSize", new Class[] {java.awt.Dimension.class})); // NOI18N
            methods[METHOD_getSize71].setDisplayName ( "" );
            methods[METHOD_getToolTipLocation72] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getToolTipLocation", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_getToolTipLocation72].setDisplayName ( "" );
            methods[METHOD_getToolTipText73] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("getToolTipText", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_getToolTipText73].setDisplayName ( "" );
            methods[METHOD_gotFocus74] = new MethodDescriptor(java.awt.Component.class.getMethod("gotFocus", new Class[] {java.awt.Event.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_gotFocus74].setDisplayName ( "" );
            methods[METHOD_grabFocus75] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("grabFocus", new Class[] {})); // NOI18N
            methods[METHOD_grabFocus75].setDisplayName ( "" );
            methods[METHOD_handleEvent76] = new MethodDescriptor(java.awt.Component.class.getMethod("handleEvent", new Class[] {java.awt.Event.class})); // NOI18N
            methods[METHOD_handleEvent76].setDisplayName ( "" );
            methods[METHOD_hasFocus77] = new MethodDescriptor(java.awt.Component.class.getMethod("hasFocus", new Class[] {})); // NOI18N
            methods[METHOD_hasFocus77].setDisplayName ( "" );
            methods[METHOD_hide78] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("hide", new Class[] {})); // NOI18N
            methods[METHOD_hide78].setDisplayName ( "" );
            methods[METHOD_hidePopup79] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("hidePopup", new Class[] {})); // NOI18N
            methods[METHOD_hidePopup79].setDisplayName ( "" );
            methods[METHOD_imageUpdate80] = new MethodDescriptor(java.awt.Component.class.getMethod("imageUpdate", new Class[] {java.awt.Image.class, int.class, int.class, int.class, int.class, int.class})); // NOI18N
            methods[METHOD_imageUpdate80].setDisplayName ( "" );
            methods[METHOD_insertItemAt81] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("insertItemAt", new Class[] {java.lang.Object.class, int.class})); // NOI18N
            methods[METHOD_insertItemAt81].setDisplayName ( "" );
            methods[METHOD_insertItemAt82] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("insertItemAt", new Class[] {java.lang.Object.class, int.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_insertItemAt82].setDisplayName ( "" );
            methods[METHOD_insets83] = new MethodDescriptor(java.awt.Container.class.getMethod("insets", new Class[] {})); // NOI18N
            methods[METHOD_insets83].setDisplayName ( "" );
            methods[METHOD_inside84] = new MethodDescriptor(java.awt.Component.class.getMethod("inside", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_inside84].setDisplayName ( "" );
            methods[METHOD_intervalAdded85] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("intervalAdded", new Class[] {javax.swing.event.ListDataEvent.class})); // NOI18N
            methods[METHOD_intervalAdded85].setDisplayName ( "" );
            methods[METHOD_intervalRemoved86] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("intervalRemoved", new Class[] {javax.swing.event.ListDataEvent.class})); // NOI18N
            methods[METHOD_intervalRemoved86].setDisplayName ( "" );
            methods[METHOD_invalidate87] = new MethodDescriptor(java.awt.Container.class.getMethod("invalidate", new Class[] {})); // NOI18N
            methods[METHOD_invalidate87].setDisplayName ( "" );
            methods[METHOD_isAncestorOf88] = new MethodDescriptor(java.awt.Container.class.getMethod("isAncestorOf", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_isAncestorOf88].setDisplayName ( "" );
            methods[METHOD_isFocusCycleRoot89] = new MethodDescriptor(java.awt.Container.class.getMethod("isFocusCycleRoot", new Class[] {java.awt.Container.class})); // NOI18N
            methods[METHOD_isFocusCycleRoot89].setDisplayName ( "" );
            methods[METHOD_isLightweightComponent90] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("isLightweightComponent", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_isLightweightComponent90].setDisplayName ( "" );
            methods[METHOD_keyDown91] = new MethodDescriptor(java.awt.Component.class.getMethod("keyDown", new Class[] {java.awt.Event.class, int.class})); // NOI18N
            methods[METHOD_keyDown91].setDisplayName ( "" );
            methods[METHOD_keyUp92] = new MethodDescriptor(java.awt.Component.class.getMethod("keyUp", new Class[] {java.awt.Event.class, int.class})); // NOI18N
            methods[METHOD_keyUp92].setDisplayName ( "" );
            methods[METHOD_layout93] = new MethodDescriptor(java.awt.Container.class.getMethod("layout", new Class[] {})); // NOI18N
            methods[METHOD_layout93].setDisplayName ( "" );
            methods[METHOD_list94] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[] {})); // NOI18N
            methods[METHOD_list94].setDisplayName ( "" );
            methods[METHOD_list95] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[] {java.io.PrintStream.class})); // NOI18N
            methods[METHOD_list95].setDisplayName ( "" );
            methods[METHOD_list96] = new MethodDescriptor(java.awt.Component.class.getMethod("list", new Class[] {java.io.PrintWriter.class})); // NOI18N
            methods[METHOD_list96].setDisplayName ( "" );
            methods[METHOD_list97] = new MethodDescriptor(java.awt.Container.class.getMethod("list", new Class[] {java.io.PrintStream.class, int.class})); // NOI18N
            methods[METHOD_list97].setDisplayName ( "" );
            methods[METHOD_list98] = new MethodDescriptor(java.awt.Container.class.getMethod("list", new Class[] {java.io.PrintWriter.class, int.class})); // NOI18N
            methods[METHOD_list98].setDisplayName ( "" );
            methods[METHOD_locate99] = new MethodDescriptor(java.awt.Container.class.getMethod("locate", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_locate99].setDisplayName ( "" );
            methods[METHOD_location100] = new MethodDescriptor(java.awt.Component.class.getMethod("location", new Class[] {})); // NOI18N
            methods[METHOD_location100].setDisplayName ( "" );
            methods[METHOD_lostFocus101] = new MethodDescriptor(java.awt.Component.class.getMethod("lostFocus", new Class[] {java.awt.Event.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_lostFocus101].setDisplayName ( "" );
            methods[METHOD_minimumSize102] = new MethodDescriptor(java.awt.Container.class.getMethod("minimumSize", new Class[] {})); // NOI18N
            methods[METHOD_minimumSize102].setDisplayName ( "" );
            methods[METHOD_mouseDown103] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseDown", new Class[] {java.awt.Event.class, int.class, int.class})); // NOI18N
            methods[METHOD_mouseDown103].setDisplayName ( "" );
            methods[METHOD_mouseDrag104] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseDrag", new Class[] {java.awt.Event.class, int.class, int.class})); // NOI18N
            methods[METHOD_mouseDrag104].setDisplayName ( "" );
            methods[METHOD_mouseEnter105] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseEnter", new Class[] {java.awt.Event.class, int.class, int.class})); // NOI18N
            methods[METHOD_mouseEnter105].setDisplayName ( "" );
            methods[METHOD_mouseExit106] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseExit", new Class[] {java.awt.Event.class, int.class, int.class})); // NOI18N
            methods[METHOD_mouseExit106].setDisplayName ( "" );
            methods[METHOD_mouseMove107] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseMove", new Class[] {java.awt.Event.class, int.class, int.class})); // NOI18N
            methods[METHOD_mouseMove107].setDisplayName ( "" );
            methods[METHOD_mouseUp108] = new MethodDescriptor(java.awt.Component.class.getMethod("mouseUp", new Class[] {java.awt.Event.class, int.class, int.class})); // NOI18N
            methods[METHOD_mouseUp108].setDisplayName ( "" );
            methods[METHOD_move109] = new MethodDescriptor(java.awt.Component.class.getMethod("move", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_move109].setDisplayName ( "" );
            methods[METHOD_nextFocus110] = new MethodDescriptor(java.awt.Component.class.getMethod("nextFocus", new Class[] {})); // NOI18N
            methods[METHOD_nextFocus110].setDisplayName ( "" );
            methods[METHOD_paint111] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paint", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_paint111].setDisplayName ( "" );
            methods[METHOD_paintAll112] = new MethodDescriptor(java.awt.Component.class.getMethod("paintAll", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_paintAll112].setDisplayName ( "" );
            methods[METHOD_paintComponents113] = new MethodDescriptor(java.awt.Container.class.getMethod("paintComponents", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_paintComponents113].setDisplayName ( "" );
            methods[METHOD_paintImmediately114] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paintImmediately", new Class[] {int.class, int.class, int.class, int.class})); // NOI18N
            methods[METHOD_paintImmediately114].setDisplayName ( "" );
            methods[METHOD_paintImmediately115] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("paintImmediately", new Class[] {java.awt.Rectangle.class})); // NOI18N
            methods[METHOD_paintImmediately115].setDisplayName ( "" );
            methods[METHOD_postEvent116] = new MethodDescriptor(java.awt.Component.class.getMethod("postEvent", new Class[] {java.awt.Event.class})); // NOI18N
            methods[METHOD_postEvent116].setDisplayName ( "" );
            methods[METHOD_preferredSize117] = new MethodDescriptor(java.awt.Container.class.getMethod("preferredSize", new Class[] {})); // NOI18N
            methods[METHOD_preferredSize117].setDisplayName ( "" );
            methods[METHOD_prepareImage118] = new MethodDescriptor(java.awt.Component.class.getMethod("prepareImage", new Class[] {java.awt.Image.class, java.awt.image.ImageObserver.class})); // NOI18N
            methods[METHOD_prepareImage118].setDisplayName ( "" );
            methods[METHOD_prepareImage119] = new MethodDescriptor(java.awt.Component.class.getMethod("prepareImage", new Class[] {java.awt.Image.class, int.class, int.class, java.awt.image.ImageObserver.class})); // NOI18N
            methods[METHOD_prepareImage119].setDisplayName ( "" );
            methods[METHOD_print120] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("print", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_print120].setDisplayName ( "" );
            methods[METHOD_printAll121] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("printAll", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_printAll121].setDisplayName ( "" );
            methods[METHOD_printComponents122] = new MethodDescriptor(java.awt.Container.class.getMethod("printComponents", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_printComponents122].setDisplayName ( "" );
            methods[METHOD_processKeyEvent123] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("processKeyEvent", new Class[] {java.awt.event.KeyEvent.class})); // NOI18N
            methods[METHOD_processKeyEvent123].setDisplayName ( "" );
            methods[METHOD_putClientProperty124] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("putClientProperty", new Class[] {java.lang.Object.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_putClientProperty124].setDisplayName ( "" );
            methods[METHOD_registerKeyboardAction125] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("registerKeyboardAction", new Class[] {java.awt.event.ActionListener.class, java.lang.String.class, javax.swing.KeyStroke.class, int.class})); // NOI18N
            methods[METHOD_registerKeyboardAction125].setDisplayName ( "" );
            methods[METHOD_registerKeyboardAction126] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("registerKeyboardAction", new Class[] {java.awt.event.ActionListener.class, javax.swing.KeyStroke.class, int.class})); // NOI18N
            methods[METHOD_registerKeyboardAction126].setDisplayName ( "" );
            methods[METHOD_remove127] = new MethodDescriptor(java.awt.Component.class.getMethod("remove", new Class[] {java.awt.MenuComponent.class})); // NOI18N
            methods[METHOD_remove127].setDisplayName ( "" );
            methods[METHOD_remove128] = new MethodDescriptor(java.awt.Container.class.getMethod("remove", new Class[] {int.class})); // NOI18N
            methods[METHOD_remove128].setDisplayName ( "" );
            methods[METHOD_remove129] = new MethodDescriptor(java.awt.Container.class.getMethod("remove", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_remove129].setDisplayName ( "" );
            methods[METHOD_removeAll130] = new MethodDescriptor(java.awt.Container.class.getMethod("removeAll", new Class[] {})); // NOI18N
            methods[METHOD_removeAll130].setDisplayName ( "" );
            methods[METHOD_removeAllItems131] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("removeAllItems", new Class[] {})); // NOI18N
            methods[METHOD_removeAllItems131].setDisplayName ( "" );
            methods[METHOD_removeItem132] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("removeItem", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_removeItem132].setDisplayName ( "" );
            methods[METHOD_removeItemAt133] = new MethodDescriptor(anecho.gui.JMappedComboBox.class.getMethod("removeItemAt", new Class[] {int.class})); // NOI18N
            methods[METHOD_removeItemAt133].setDisplayName ( "" );
            methods[METHOD_removeNotify134] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("removeNotify", new Class[] {})); // NOI18N
            methods[METHOD_removeNotify134].setDisplayName ( "" );
            methods[METHOD_removePropertyChangeListener135] = new MethodDescriptor(java.awt.Component.class.getMethod("removePropertyChangeListener", new Class[] {java.lang.String.class, java.beans.PropertyChangeListener.class})); // NOI18N
            methods[METHOD_removePropertyChangeListener135].setDisplayName ( "" );
            methods[METHOD_repaint136] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[] {})); // NOI18N
            methods[METHOD_repaint136].setDisplayName ( "" );
            methods[METHOD_repaint137] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[] {long.class})); // NOI18N
            methods[METHOD_repaint137].setDisplayName ( "" );
            methods[METHOD_repaint138] = new MethodDescriptor(java.awt.Component.class.getMethod("repaint", new Class[] {int.class, int.class, int.class, int.class})); // NOI18N
            methods[METHOD_repaint138].setDisplayName ( "" );
            methods[METHOD_repaint139] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("repaint", new Class[] {long.class, int.class, int.class, int.class, int.class})); // NOI18N
            methods[METHOD_repaint139].setDisplayName ( "" );
            methods[METHOD_repaint140] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("repaint", new Class[] {java.awt.Rectangle.class})); // NOI18N
            methods[METHOD_repaint140].setDisplayName ( "" );
            methods[METHOD_requestDefaultFocus141] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestDefaultFocus", new Class[] {})); // NOI18N
            methods[METHOD_requestDefaultFocus141].setDisplayName ( "" );
            methods[METHOD_requestFocus142] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocus", new Class[] {})); // NOI18N
            methods[METHOD_requestFocus142].setDisplayName ( "" );
            methods[METHOD_requestFocus143] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocus", new Class[] {boolean.class})); // NOI18N
            methods[METHOD_requestFocus143].setDisplayName ( "" );
            methods[METHOD_requestFocusInWindow144] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("requestFocusInWindow", new Class[] {})); // NOI18N
            methods[METHOD_requestFocusInWindow144].setDisplayName ( "" );
            methods[METHOD_resetKeyboardActions145] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("resetKeyboardActions", new Class[] {})); // NOI18N
            methods[METHOD_resetKeyboardActions145].setDisplayName ( "" );
            methods[METHOD_reshape146] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("reshape", new Class[] {int.class, int.class, int.class, int.class})); // NOI18N
            methods[METHOD_reshape146].setDisplayName ( "" );
            methods[METHOD_resize147] = new MethodDescriptor(java.awt.Component.class.getMethod("resize", new Class[] {int.class, int.class})); // NOI18N
            methods[METHOD_resize147].setDisplayName ( "" );
            methods[METHOD_resize148] = new MethodDescriptor(java.awt.Component.class.getMethod("resize", new Class[] {java.awt.Dimension.class})); // NOI18N
            methods[METHOD_resize148].setDisplayName ( "" );
            methods[METHOD_revalidate149] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("revalidate", new Class[] {})); // NOI18N
            methods[METHOD_revalidate149].setDisplayName ( "" );
            methods[METHOD_scrollRectToVisible150] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("scrollRectToVisible", new Class[] {java.awt.Rectangle.class})); // NOI18N
            methods[METHOD_scrollRectToVisible150].setDisplayName ( "" );
            methods[METHOD_selectWithKeyChar151] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("selectWithKeyChar", new Class[] {char.class})); // NOI18N
            methods[METHOD_selectWithKeyChar151].setDisplayName ( "" );
            methods[METHOD_setBounds152] = new MethodDescriptor(java.awt.Component.class.getMethod("setBounds", new Class[] {int.class, int.class, int.class, int.class})); // NOI18N
            methods[METHOD_setBounds152].setDisplayName ( "" );
            methods[METHOD_setComponentZOrder153] = new MethodDescriptor(java.awt.Container.class.getMethod("setComponentZOrder", new Class[] {java.awt.Component.class, int.class})); // NOI18N
            methods[METHOD_setComponentZOrder153].setDisplayName ( "" );
            methods[METHOD_setDefaultLocale154] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("setDefaultLocale", new Class[] {java.util.Locale.class})); // NOI18N
            methods[METHOD_setDefaultLocale154].setDisplayName ( "" );
            methods[METHOD_show155] = new MethodDescriptor(java.awt.Component.class.getMethod("show", new Class[] {})); // NOI18N
            methods[METHOD_show155].setDisplayName ( "" );
            methods[METHOD_show156] = new MethodDescriptor(java.awt.Component.class.getMethod("show", new Class[] {boolean.class})); // NOI18N
            methods[METHOD_show156].setDisplayName ( "" );
            methods[METHOD_showPopup157] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("showPopup", new Class[] {})); // NOI18N
            methods[METHOD_showPopup157].setDisplayName ( "" );
            methods[METHOD_size158] = new MethodDescriptor(java.awt.Component.class.getMethod("size", new Class[] {})); // NOI18N
            methods[METHOD_size158].setDisplayName ( "" );
            methods[METHOD_toString159] = new MethodDescriptor(java.awt.Component.class.getMethod("toString", new Class[] {})); // NOI18N
            methods[METHOD_toString159].setDisplayName ( "" );
            methods[METHOD_transferFocus160] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocus", new Class[] {})); // NOI18N
            methods[METHOD_transferFocus160].setDisplayName ( "" );
            methods[METHOD_transferFocusBackward161] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocusBackward", new Class[] {})); // NOI18N
            methods[METHOD_transferFocusBackward161].setDisplayName ( "" );
            methods[METHOD_transferFocusDownCycle162] = new MethodDescriptor(java.awt.Container.class.getMethod("transferFocusDownCycle", new Class[] {})); // NOI18N
            methods[METHOD_transferFocusDownCycle162].setDisplayName ( "" );
            methods[METHOD_transferFocusUpCycle163] = new MethodDescriptor(java.awt.Component.class.getMethod("transferFocusUpCycle", new Class[] {})); // NOI18N
            methods[METHOD_transferFocusUpCycle163].setDisplayName ( "" );
            methods[METHOD_unregisterKeyboardAction164] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("unregisterKeyboardAction", new Class[] {javax.swing.KeyStroke.class})); // NOI18N
            methods[METHOD_unregisterKeyboardAction164].setDisplayName ( "" );
            methods[METHOD_update165] = new MethodDescriptor(javax.swing.JComponent.class.getMethod("update", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_update165].setDisplayName ( "" );
            methods[METHOD_updateUI166] = new MethodDescriptor(javax.swing.JComboBox.class.getMethod("updateUI", new Class[] {})); // NOI18N
            methods[METHOD_updateUI166].setDisplayName ( "" );
            methods[METHOD_validate167] = new MethodDescriptor(java.awt.Container.class.getMethod("validate", new Class[] {})); // NOI18N
            methods[METHOD_validate167].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods
    // Here you can add code for customizing the methods array.

        return methods;     }//GEN-LAST:Methods

    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons

    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx


//GEN-FIRST:Superclass
    // Here you can add code for customizing the Superclass BeanInfo.

//GEN-LAST:Superclass
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return BeanDescriptor describing the editable properties of this bean.
     * May return null if the information should be obtained by automatic
     * analysis.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean. May return null if the information
     * should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will belong
     * to the IndexedPropertyDescriptor subclass of PropertyDescriptor. A client
     * of getPropertyDescriptors can use "instanceof" to check if a given
     * PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return An array of EventSetDescriptors describing the kinds of events
     * fired by this bean. May return null if the information should be obtained
     * by automatic analysis.
     */
    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return An array of MethodDescriptors describing the methods implemented
     * by this bean. May return null if the information should be obtained by
     * automatic analysis.
     */
    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are
     * customizing the bean.
     *
     * @return Index of default property in the PropertyDescriptor array
     * returned by getPropertyDescriptors.
     * <P>
     * Returns -1 if there is no default property.
     */
    @Override
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will mostly
     * commonly be used by human's when using the bean.
     *
     * @return Index of default event in the EventSetDescriptor array returned
     * by getEventSetDescriptors.
     * <P>
     * Returns -1 if there is no default event.
     */
    @Override
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to represent the
     * bean in toolboxes, toolbars, etc. Icon images will typically be GIFs, but
     * may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from this
     * method.
     * <p>
     * There are four possible flavors of icons (16x16 color, 32x32 color, 16x16
     * mono, 32x32 mono). If a bean choses to only support a single icon we
     * recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background so they can be
     * rendered onto an existing background.
     *
     * @param iconKind The kind of icon requested. This should be one of the
     * constant values ICON_COLOR_16x16, ICON_COLOR_32x32, ICON_MONO_16x16, or
     * ICON_MONO_32x32.
     * @return An image object representing the requested icon. May return null
     * if no suitable icon is available.
     */
    @Override
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case ICON_COLOR_16x16:
                if (iconNameC16 == null) {
                    return null;
                } else {
                    if (iconColor16 == null) {
                        iconColor16 = loadImage(iconNameC16);
                    }
                    return iconColor16;
                }
            case ICON_COLOR_32x32:
                if (iconNameC32 == null) {
                    return null;
                } else {
                    if (iconColor32 == null) {
                        iconColor32 = loadImage(iconNameC32);
                    }
                    return iconColor32;
                }
            case ICON_MONO_16x16:
                if (iconNameM16 == null) {
                    return null;
                } else {
                    if (iconMono16 == null) {
                        iconMono16 = loadImage(iconNameM16);
                    }
                    return iconMono16;
                }
            case ICON_MONO_32x32:
                if (iconNameM32 == null) {
                    return null;
                } else {
                    if (iconMono32 == null) {
                        iconMono32 = loadImage(iconNameM32);
                    }
                    return iconMono32;
                }
            default:
                return null;
        }
    }
    
}
