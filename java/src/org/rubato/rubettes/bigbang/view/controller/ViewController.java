package org.rubato.rubettes.bigbang.view.controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.TreeMap;

import org.rubato.rubettes.bigbang.controller.Controller;
import org.rubato.rubettes.bigbang.view.View;
import org.rubato.rubettes.bigbang.view.controller.mode.DisplayModeAdapter;
import org.rubato.rubettes.bigbang.view.model.DisplayNote;
import org.rubato.rubettes.bigbang.view.model.ZoomChange;
import org.rubato.rubettes.bigbang.view.model.tools.DisplayTool;
import org.rubato.rubettes.bigbang.view.model.tools.SelectionTool;

public class ViewController extends Controller {
	
	//modes
	public static final String DISPLAY_MODE = "setDisplayMode";
	
	//gui
	public static final String ADD_WINDOW = "addNewWindow";
	public static final String MAIN_OPTIONS_VISIBLE = "toggleMainOptionsVisible";
	public static final String VIEW_PARAMETERS_VISIBLE = "toggleViewParametersVisible";
	public static final String SHOW_WINDOW_PREFERENCES = "showWindowPreferences";
	
	//view manipulation
	public static final String VIEW_PARAMETERS = "ViewParameters";
	public static final String SELECTED_VIEW_PARAMETERS = "SelectedViewParameters";
	public static final String MANUAL_DENOTATOR_LIMITS = "setManualDenotatorLimits";
	public static final String PARAMETER_MIN_MAX = "setParameterMinAndMax";
	public static final String ZOOM_FACTORS = "changeZoomFactors";
	public static final String DISPLAY_POSITION = "changeDisplayPosition";
	public static final String SATELLITES_CONNECTED = "SatellitesConnected";
	public static final String LAYERS = "changeLayerState";
	public static final String TOGGLE_MOD_FILTER = "toggleModFilter";
	public static final String MOD_FILTER_VALUES = "changeModFilter";
	
	//general functionality
	public static final String UNDO = "undo";
	public static final String REDO = "redo";
	public static final String TRANSFORMATIONS = "transformations";
	public static final String INPUT_ACTIVE = "InputActive";
	
	//score manipulation - display
	public static final String DISPLAY_NOTES = "DisplayNotes";
	public static final String NOTE_SELECTION = "toggleNoteSelection";
	public static final String ANCHOR_NOTE_SELECTION = "toggleAnchorNoteSelection";
	public static final String SELECT_NOTES = "selectNotes";
	public static final String DISPLAY_TOOL = "setDisplayTool";
	public static final String CLEAR_DISPLAY_TOOL = "clearDisplayTool";
	
	//score manipulation - denotators
	public static final String ADD_NOTE = "addNote";
	public static final String DELETE_NOTES = "deleteSelectedNotes";
	public static final String COPY_NOTES = "copySelectedNotesTo";
	public static final String COPY_NOTES_NEW = "copySelectedNotesToNewLayer";
	public static final String MOVE_NOTES = "moveSelectedNotesTo";
	public static final String MOVE_NOTES_NEW = "moveSelectedNotesToNewLayer";
	public static final String SATELLITE_NOTES = "addSelectedNotesAsSatellitesTo";
	public static final String FLATTEN_NOTES = "flattenSelectedNotes";
	public static final String MODULATOR_NOTES = "addSelectedNotesAsModulatorsTo";
	public static final String REMOVE_NOTES_FROM_CARRIER = "removeSelectedNotesFromCarrier";
	
	//score manipulation - transformations
	public static final String TRANSLATE_NOTES = "translateSelectedNotes";
	public static final String ROTATE_NOTES = "rotateSelectedNotes";
	public static final String SCALE_NOTES = "scaleSelectedNotes";
	public static final String REFLECT_NOTES = "reflectSelectedNotes";
	public static final String SHEAR_NOTES = "shearSelectedNotes";
	public static final String SHAPE_NOTES = "shapeSelectedNotes";
	public static final String AFFINE_TRANSFORM_NOTES = "affineTransformSelectedNotes";
	
	//score manipulation - wallpaper
	public static final String START_WALLPAPER = "startWallpaper";
	public static final String ADD_WP_DIMENSION = "addWallpaperDimension";
	public static final String END_WALLPAPER = "endWallpaper";
	public static final String RANGE = "setRange";
	
	//score manipulation - alteration
	public static final String ALTERATION_COMPOSITION = "setAlterationComposition";
	
	
	public void changeDisplayMode(DisplayModeAdapter newMode) {
		this.callModelMethod(ViewController.DISPLAY_MODE, newMode);
	}
	
	public void addNewWindow() {
		this.callModelMethod(ViewController.ADD_WINDOW);
	}
	
	public void toggleMainOptionsVisible() {
		this.callModelMethod(ViewController.MAIN_OPTIONS_VISIBLE);
	}
	
	public void changeLayerState(int layerIndex) {
		this.callModelMethod(ViewController.LAYERS, layerIndex);
	}
	
	public void toggleModFilter() {
		this.callModelMethod(ViewController.TOGGLE_MOD_FILTER);
	}
	
	public void changeModFilter(int modLevel, int modNumber) {
		this.callModelMethod(ViewController.MOD_FILTER_VALUES, modLevel, modNumber);
	}
	
	public void toggleViewParametersVisible() {
		this.callModelMethod(ViewController.VIEW_PARAMETERS_VISIBLE);
	}
	
	public void changeViewParameters(int[] newViewParameters) {
		this.setModelProperty(ViewController.SELECTED_VIEW_PARAMETERS, newViewParameters);
	}
	
	public void showWindowPreferences() {
		this.callModelMethod(ViewController.SHOW_WINDOW_PREFERENCES);
	}
	
	public void changeDenotatorMinAndMax(int index, boolean manual, double min, double max) {
		this.callModelMethod(ViewController.MANUAL_DENOTATOR_LIMITS, index, manual, min, max);
	}
	
	public void changeParameterMinAndMax(int index, boolean relative, double min, double max, boolean cyclic) {
		this.callModelMethod(ViewController.PARAMETER_MIN_MAX, index, relative, min, max, cyclic);
	}
	
	public void changeZoomFactors(ZoomChange zoomChange) {
		this.callModelMethod(ViewController.ZOOM_FACTORS, zoomChange);
	}
	
	public void setZoomFactors(double zoomFactor) {
		this.callModelMethod("setZoomFactors", zoomFactor, zoomFactor);
	}
	
	public void changeDisplayPosition(Dimension difference) {
		this.callModelMethod(ViewController.DISPLAY_POSITION, difference);
	}
	
	public void setDisplayPosition(Point center) {
		this.callModelMethod("setDisplayPosition", center);
	}
	
	public void toggleNoteSelection(Point location) {
		this.callModelMethod(ViewController.NOTE_SELECTION, location);
	}
	
	public void toggleAnchorNoteSelection(Point location) {
		this.callModelMethod(ViewController.ANCHOR_NOTE_SELECTION, location);
	}
	
	public void selectNotes(SelectionTool tool, boolean stillSelecting) {
		this.callModelMethod(ViewController.SELECT_NOTES, tool, stillSelecting);
	}
	
	public void changeDisplayTool(DisplayTool tool) {
		this.callModelMethod(ViewController.DISPLAY_TOOL, tool);
	}
	
	public void clearDisplayTool() {
		this.callModelMethod(ViewController.CLEAR_DISPLAY_TOOL);
	}
	
	public void translateSelectedNotes(Dimension difference, boolean copyAndTranslate, boolean previewMode) {
		this.callModelMethod(ViewController.TRANSLATE_NOTES, difference, copyAndTranslate, previewMode);
	}
	
	public void rotateSelectedNotes(Point2D.Double center, double angle, boolean copyAndTranslate, boolean previewMode) {
		this.callModelMethod(ViewController.ROTATE_NOTES, center, angle, copyAndTranslate, previewMode);
	}
	
	public void scaleSelectedNotes(Point2D.Double center, double[] scaleFactors, boolean copyAndTranslate, boolean previewMode) {
		this.callModelMethod(ViewController.SCALE_NOTES, center, scaleFactors, copyAndTranslate, previewMode);
	}
	
	public void reflectSelectedNotes(Point2D.Double center, double[] reflectionVector, boolean copyAndTranslate, boolean previewMode) {
		this.callModelMethod(ViewController.REFLECT_NOTES, center, reflectionVector, copyAndTranslate, previewMode);
	}
	
	public void shearSelectedNotes(Point2D.Double center, double[] shearingFactors, boolean copyAndTranslate, boolean previewMode) {
		this.callModelMethod(ViewController.SHEAR_NOTES, center, shearingFactors, copyAndTranslate, previewMode);
	}
	
	public void shapeSelectedNotes(TreeMap<Integer,Integer> location, boolean copyAndTransform, boolean previewMode) {
		this.callModelMethod(ViewController.SHAPE_NOTES, location, copyAndTransform, previewMode);
	}
	
	public void affineTransformSelectedNotes(Point2D.Double center, double[] shift, double angle, double[] scaleFactors, boolean copyAndTransform, boolean previewMode) {
		this.callModelMethod(ViewController.AFFINE_TRANSFORM_NOTES, center, shift, angle, scaleFactors, copyAndTransform, previewMode);
	}
	
	public void addNote(Point2D.Double location) {
		this.callModelMethod(ViewController.ADD_NOTE, location);
	}
	
	public void deleteSelectedNotes() {
		this.callModelMethod(ViewController.DELETE_NOTES);
	}
	
	public void copySelectedNotesTo(int layerIndex) {
		this.callModelMethod(ViewController.COPY_NOTES, layerIndex);
	}
	
	public void copySelectedNotesToNewLayer() {
		this.callModelMethod(ViewController.COPY_NOTES_NEW);
	}
	
	public void moveSelectedNotesTo(int layerIndex) {
		this.callModelMethod(ViewController.MOVE_NOTES, layerIndex);
	}
	
	public void moveSelectedNotesToNewLayer() {
		this.callModelMethod(ViewController.MOVE_NOTES_NEW);
	}
	
	public void addSelectedNotesAsSatellitesTo(DisplayNote parentNote) {
		this.callModelMethod(ViewController.SATELLITE_NOTES, parentNote);
	}
	
	public void flattenSelectedNotes() {
		this.callModelMethod(ViewController.FLATTEN_NOTES);
	}
	
	public void addSelectedNotesAsModulatorsTo(DisplayNote parentNote) {
		this.callModelMethod(ViewController.MODULATOR_NOTES, parentNote);
	}
	
	public void removeSelectedNotesFromCarrier() {
		this.callModelMethod(ViewController.REMOVE_NOTES_FROM_CARRIER);
	}
	
	public void addWallpaperDimension() {
		this.callModelMethod(ViewController.ADD_WP_DIMENSION);
	}
	
	public void stopWallpaper() {
		this.callModelMethod(ViewController.END_WALLPAPER);
	}
	
	public void changeWallpaperRange(int dimension, boolean rangeTo, int value) {
		this.callModelMethod(ViewController.RANGE, dimension, rangeTo, value);
	}
	
	public void changeAlterationComposition(int index) {
		this.callModelMethod(ViewController.ALTERATION_COMPOSITION, index);
	}
	
	public void undo() {
		this.callModelMethod(ViewController.UNDO);
	}
	
	public void redo() {
		this.callModelMethod(ViewController.REDO);
	}
	
	public void changeInputActive(boolean inputActive) {
		this.setModelProperty(ViewController.INPUT_ACTIVE, inputActive);
	}
	
	public List<View> getViews() {
		return this.registeredViews;
	}

}
