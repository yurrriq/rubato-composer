package org.rubato.rubettes.bigbang.view.subview;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.rubato.math.yoneda.ColimitForm;
import org.rubato.math.yoneda.Form;
import org.rubato.rubettes.bigbang.view.View;
import org.rubato.rubettes.bigbang.view.controller.ViewController;
import org.rubato.rubettes.bigbang.view.model.DisplayObject;
import org.rubato.rubettes.bigbang.view.model.LayerState;
import org.rubato.rubettes.bigbang.view.model.LayerStates;
import org.rubato.rubettes.util.DenotatorPath;
import org.rubato.rubettes.util.DenotatorValueFinder;

public class DisplayObjectList extends TreeSet<DisplayObject> implements View {
	
	private Form baseForm;
	private int selectedObject;
	private List<Integer> selectedColimitCoordinates;
	private List<Form> objects;
	private Map<Form,DenotatorPath> objectsAndPaths;
	private boolean allowsForSatellites;
	//TODO: make objectcolimits!!!!!
	private List<ColimitForm> topDenotatorColimits;
	private Map<ColimitForm,DenotatorPath> topDenotatorColimitsAndPaths;
	private List<String> valueNames;
	//TODO: paths not used anymore!!! since they can be different in every object!!! refactor
	private Map<String,DenotatorPath> valueNamesAndPaths;
	private Set<DisplayObject> selectedNotes;
	private DisplayObject selectedAnchorNote;
	
	public DisplayObjectList(ViewController controller, Form baseForm) {
		controller.addView(this);
		this.baseForm = baseForm;
		this.objects = new ArrayList<Form>();
		this.objectsAndPaths = new TreeMap<Form,DenotatorPath>();
		this.topDenotatorColimits = new ArrayList<ColimitForm>();
		this.topDenotatorColimitsAndPaths = new TreeMap<ColimitForm,DenotatorPath>();
		this.selectedNotes = new TreeSet<DisplayObject>();
		this.valueNames = new ArrayList<String>();
		this.valueNamesAndPaths = new TreeMap<String,DenotatorPath>();
		this.initSelectedColimitCoordinates(0);
	}
	
	private void initSelectedColimitCoordinates(int numberOfColimits) {
		this.selectedColimitCoordinates = new ArrayList<Integer>();
		for (int i = 0; i < numberOfColimits; i++) {
			this.selectedColimitCoordinates.add(0);
		}
	}
	
	public void setSelectedObject(int selectedObject) {
		this.selectedObject = selectedObject;
	}
	
	public int getSelectedObject() {
		return this.selectedObject;
	}
	
	public void setSelectedColimitCoordinates(List<Integer> selectedColimitCoordinates) {
		this.selectedColimitCoordinates = selectedColimitCoordinates;
	}
	
	public List<Integer> getSelectedColimitCoordinates() {
		return this.selectedColimitCoordinates;
	}
	
	public void setSelectedColimitCoordinate(Integer colimitIndex, Integer coordinateIndex) {
		List<ColimitForm> topDenotatorColimits = this.getTopDenotatorColimits();
		if (coordinateIndex >= 0 && topDenotatorColimits.size() > colimitIndex && topDenotatorColimits.get(colimitIndex).getForms().size() >= coordinateIndex) {
			this.selectedColimitCoordinates.set(colimitIndex, coordinateIndex);
			//set all ColimitForms impossible to reach to -1
			//TODO: does not account for forms that contain the same colimit several times
			Form coordinateForm = topDenotatorColimits.get(colimitIndex).getForm(coordinateIndex);
			List<ColimitForm> subColimits = new DenotatorValueFinder(coordinateForm, false).getColimitsInFoundOrder();
			for (int i = colimitIndex+1; i < topDenotatorColimits.size(); i++) {
				if (!subColimits.contains(topDenotatorColimits.get(i))) {
					this.selectedColimitCoordinates.set(i, -1);
				} else if (this.selectedColimitCoordinates.get(i) == -1) {
					this.selectedColimitCoordinates.set(i, 0);
				}
			}
		}
	}
	
	public void setValueNames(List<String> valueNames) {
		this.valueNames = valueNames;
	}
	
	public List<String> getValueNames() {
		return this.valueNames;
	}
	
	public Form getBaseForm() {
		return this.baseForm;
	}
	
	public void setAllowsForSatellites(boolean allowsForSatellites) {
		this.allowsForSatellites = allowsForSatellites;
	}
	
	public boolean allowsForSatellites() {
		return this.allowsForSatellites;
	}
	
	public void setValueNamesAndPaths(Map<String,DenotatorPath> valuesNamesAndPaths) {
		this.valueNamesAndPaths = valuesNamesAndPaths;
	}
	
	public List<DenotatorPath> getValuePaths() {
		List<DenotatorPath> paths = new ArrayList<DenotatorPath>();
		for (String currentValueName : this.valueNames) {
			paths.add(this.valueNamesAndPaths.get(currentValueName));
		}
		return paths;
	}
	
	public DenotatorPath getObjectValuePathAt(int valueIndex) {
		return this.getObjectValueSubPath(this.valueNames.get(valueIndex));
	}
	
	private DenotatorPath getPathOfValueAt(int valueIndex) {
		return this.valueNamesAndPaths.get(this.valueNames.get(valueIndex));
	}
	
	public DisplayObject getClosestObject(int valueIndex, double value, DenotatorPath powersetPath) {
		String valueName = this.valueNames.get(valueIndex);
		DenotatorPath valuePath = this.getPathOfValueAt(valueIndex);
		DisplayObject closestObject = null;
		double shortestDistance = Double.MAX_VALUE;
		if (this.selectedObject > 0) {
			for (DisplayObject currentObject : this) {
				//has to be same type of object. TODO: lenghth of course is not the deciding thing!!!!
				if (currentObject.getTopDenotatorPath().size() == powersetPath.getTopPath().size()) {
					Double currentValue = currentObject.getValue(valueName);
					if (currentValue != null) {
						double currentDistance = Math.abs(currentValue-value);
						if (currentDistance < shortestDistance) {
							shortestDistance = currentDistance;
							closestObject = currentObject;
						}
					}
				}
			}
			return closestObject;
		}
		return null;
	}
	
	public void setObjects(List<Form> objects) {
		this.objects = objects;
	}
	
	public void setObjectsAndPaths(Map<Form,DenotatorPath> objectsAndPaths) {
		this.objectsAndPaths = objectsAndPaths;
	}
	
	public List<Form> getObjects() {
		return this.objects;
	}
	
	public DenotatorPath getSelectedObjectPath() {
		return this.objectsAndPaths.get(this.objects.get(this.selectedObject));
	}
	
	public void setTopDenotatorColimits(List<ColimitForm> colimits) {
		this.topDenotatorColimits = colimits;
		this.initSelectedColimitCoordinates(colimits.size());
	}
	
	public List<ColimitForm> getTopDenotatorColimits() {
		return this.topDenotatorColimits;
	}
	
	public void setTopDenotatorColimitsAndPaths(Map<ColimitForm,DenotatorPath> colimitsAndPaths) {
		this.topDenotatorColimitsAndPaths = colimitsAndPaths;
	}
	
	public List<DenotatorPath> getTopDenotatorColimitPaths(List<Integer> colimitCoordinates) {
		List<DenotatorPath> colimitCoordinatePaths = new ArrayList<DenotatorPath>();
		for (int i = 0; i < colimitCoordinates.size(); i++) {
			int currentSelectedCoordinate = colimitCoordinates.get(i);
			if (currentSelectedCoordinate >= 0) {
				colimitCoordinatePaths.add(this.topDenotatorColimitsAndPaths.get(this.topDenotatorColimits.get(i)).getChildPath(currentSelectedCoordinate));
			}
		}
		return colimitCoordinatePaths;
	}
	
	/**
	 * @return the top denotator standard values under assumption that the given value
	 * is selected in a colimit. if it is not in a colimit, just returns the standard values
	 */
	public Map<DenotatorPath,Double> getObjectStandardValues(Map<String,Double> standardDenotatorValues) {
		DenotatorPath selectedObjectPath = this.getSelectedObjectPath();
		Map<DenotatorPath,Double> objectStandardValues = new TreeMap<DenotatorPath,Double>();
		List<DenotatorPath> selectedColimitCoordinatePaths = this.getTopDenotatorColimitPaths(this.selectedColimitCoordinates);
		for (String currentName : this.valueNamesAndPaths.keySet()) {
			if (standardDenotatorValues.containsKey(currentName)) {
				//if (this.inAllowedColimitBranch(currentPath, selectedColimitCoordinatePaths)) {
				DenotatorPath valuePath = this.getObjectValueSubPath(currentName);
				if (valuePath != null) {
					objectStandardValues.put(valuePath, standardDenotatorValues.get(currentName));
				}
				//}
			}
			/*DenotatorPath currentPath = this.valueNamesAndPaths.get(currentName);
			if (currentPath.isPartOfSameObjectAs(selectedObjectPath) && standardDenotatorValues.containsKey(currentName)) {
				if (this.inAllowedColimitBranch(currentPath, selectedColimitCoordinatePaths)) {
					objectStandardValues.put(this.getObjectValueSubPath(currentName), standardDenotatorValues.get(currentName));
					System.out.println(currentName + " " + this.getObjectValueSubPath(currentName) + " " + standardDenotatorValues.get(currentName));
				}
			}*/
		}
		return objectStandardValues;
	}
	
	private DenotatorPath getObjectValueSubPath(String valueName) {
		DenotatorPath selectedObjectPath = this.getSelectedObjectPath();
		DenotatorPath objectValuePath = new DenotatorValueFinder(selectedObjectPath.getForm(), true).getValueNamesAndPaths().get(valueName);
		return objectValuePath;
	}
	
	public boolean pathInAllowedColimitBranch(DenotatorPath path) {
		return this.inAllowedColimitBranch(path, this.getTopDenotatorColimitPaths(this.selectedColimitCoordinates));
	}
	
	private boolean inAllowedColimitBranch(DenotatorPath path, List<DenotatorPath> selectedColimitCoordinatePaths) {
		for (DenotatorPath currentColimitPath : path.getParentColimitPaths()) {
			boolean containedInSelectedPaths = false;
			for (DenotatorPath currentSelectedPath : selectedColimitCoordinatePaths) {
				if (currentColimitPath.equals(currentSelectedPath.getParentPath())) {
					containedInSelectedPaths = true;
					if (!path.subPath(0, currentColimitPath.size()+1).equals(currentSelectedPath)) {
						return false;
					}
				}
			}
			if (!containedInSelectedPaths) {
				if (path.subPath(0, currentColimitPath.size()+1).getLastIndex() != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean inConflictingColimitPositions(int valueIndex1, int valueIndex2) {
		return this.getPathOfValueAt(valueIndex1).inConflictingColimitPositions(this.getPathOfValueAt(valueIndex2));
	}
	
	public void tempSelectNotes(Rectangle2D.Double area) {
		for (DisplayObject currentNote: this) {
			if (!this.selectedNotes.contains(currentNote)) {
				currentNote.setSelected(currentNote.intersects(area));
			}
		}
	}
	
	public int selectNotes(Rectangle2D.Double area) {
		for (DisplayObject currentNote: this) {
			if (currentNote.intersects(area)) {
				this.selectNote(currentNote);
			}
		}
		return this.selectedNotes.size();
	}
	
	private void toggleSelected(DisplayObject note) {
		if (note.isSelected()) {
			this.deselectNote(note);
		} else {
			this.selectNote(note);
		}
	}
	
	public void selectNote(DisplayObject note) {
		if (this.isNotSelectedAnchorNote(note)) {
			note.setSelected(true);
			if (!this.selectedNotes.contains(note) && note.isActive()) {
				this.selectedNotes.add(note);
				this.deselectParents(note);
				this.deselectChildren(note);
			}
		}
	}
	
	private void deselectNote(DisplayObject note) {
		note.setSelected(false);
		this.selectedNotes.remove(note);
	}
	
	private boolean isNotSelectedAnchorNote(DisplayObject note) {
		return (this.selectedAnchorNote != null && !this.selectedAnchorNote.equals(note))
			|| this.selectedAnchorNote == null;
	}
	
	private void deselectParents(DisplayObject note) {
		DisplayObject parent = note.getParent(); 
		if (parent != null) {
			this.deselectNote(parent);
			this.deselectParents(parent);
		}
	}
	
	private void deselectChildren(DisplayObject note) {
		for (DisplayObject currentChild: note.getChildren()) {
			this.deselectNote(currentChild);
			this.deselectChildren(currentChild);
		}
	}
	
	public int selectTopOrDeselectAllNotes(Point location) {
		//notes are saved from bottom to top... just takes one note
		for (DisplayObject currentNote: this) {
			if (currentNote.getRectangle().contains(location)) {
				this.toggleSelected(currentNote);
				return this.selectedNotes.size();
			}
		}
		this.deselectAllNotes();
		return this.selectedNotes.size();
	}
	
	private void deselectAllNotes() {
		for (DisplayObject currentNote: this.selectedNotes) {
			currentNote.setSelected(false);
		}
		this.selectedNotes = new TreeSet<DisplayObject>();
	}
	
	public DisplayObject getNoteAt(Point location) {
		return this.getNoteAt(location, this);
	}
	
	public boolean hasSelectedNoteAt(Point location) {
		return this.getNoteAt(location, this.selectedNotes) != null;
	}
	
	private DisplayObject getNoteAt(Point location, Set<DisplayObject> notes) {
		for (DisplayObject currentNote : notes) {
			if (currentNote.getRectangle().contains(location)) {
				return currentNote;
			}
		}
		return null;
	}
	
	public void selectOrDeselectAnchorNote(Point location) {
		DisplayObject noteInLocation = this.getNoteAt(location);
		if (noteInLocation != null) {
			if (noteInLocation.equals(this.selectedAnchorNote)) {
				this.selectedAnchorNote = null;
			} else if (noteInLocation.hasChildren()) {
				this.setSelectedAnchorNote(noteInLocation);
			}
		} else {
			this.selectedAnchorNote = null;
		}
	}
	
	public void setSelectedAnchorNote(DisplayObject note) {
		this.selectedAnchorNote = note;
		this.selectedNotes.remove(note);
	}
	
	public DenotatorPath getSelectedAnchorNodePath() {
		if (this.selectedAnchorNote != null) {
			return this.selectedAnchorNote.getTopDenotatorPath();
		}
		return null;
	}
	
	public Point2D.Double getSelectedAnchorNodeCenter() {
		if (this.selectedAnchorNote != null) {
			return this.selectedAnchorNote.getLocation();
		}
		return null;
	}
	
	public Set<DenotatorPath> getSelectedObjectPaths() {
		TreeSet<DenotatorPath> objectPaths = new TreeSet<DenotatorPath>();
		for (DisplayObject currentObject : this.selectedNotes) {
			//nodePaths.add(new DenotatorPath(currentNote.getOriginalPath()));
			objectPaths.add(currentObject.getTopDenotatorPath());
		}
		return objectPaths;
	}
	
	private void makeAllModulatorsVisible() {
		for (DisplayObject currentNote: this) {
			currentNote.setVisibility(LayerState.active);
		}
	}
	
	public void updateModulatorVisibility(int modLevel, int siblingNumber) {
		/*TODO:for (DisplayObject currentNote: this) {
			if (currentNote.getValue(5) == modLevel
					&& (siblingNumber == -1 || currentNote.getValue(7) == siblingNumber)) {
				currentNote.setVisibility(LayerState.active);
			} else {
				currentNote.setVisibility(LayerState.invisible);
				this.deselectNote(currentNote);
			}
		}*/
	}
	
	private void updateVisibility(LayerStates states) {
		for (DisplayObject currentNote: this) {
			LayerState currentState = states.get(currentNote.getLayer());
			currentNote.setVisibility(currentState);
			if (!currentState.equals(LayerState.active)) {
				this.deselectNote(currentNote);
			}
		}
		
		//deselect or select notes!!!
	}
	
	public void updateBounds(double xZoomFactor, double yZoomFactor, int xPosition, int yPosition) {
		for (DisplayObject currentNote : this) {
			currentNote.updateBounds(xZoomFactor, yZoomFactor, xPosition, yPosition);
		}
	}
	
	public void paint(AbstractPainter painter) {
		this.paintConnectors(painter, this);
		this.paintInactiveNotes(painter);
		this.paintActiveNotes(painter);
		//leads to some flipping problems, but necessary for clearness
		this.paintSelectedNotes(painter);
		this.paintSelectedAnchorNote(painter);
	}
	
	public void paintSelectedNotesConnectors(AbstractPainter painter, int parentX, int parentY) {
		for (DisplayObject currentNote : this.selectedNotes) {
			currentNote.paintConnectors(painter, parentX, parentY);
		}
	}
	
	private void paintConnectors(AbstractPainter painter, Set<DisplayObject> notes) {
		for (DisplayObject currentNote : notes) {
			currentNote.paintConnectors(painter);
		}
	}
	
	private void paintInactiveNotes(AbstractPainter painter) {
		for (DisplayObject currentNote : this) {
			if (!currentNote.isActive()) {
				currentNote.paint(painter);
			}
		}
	}
	
	private void paintActiveNotes(AbstractPainter painter) {
		for (DisplayObject currentNote : this) {
			if (currentNote.isActive()) {
				currentNote.paint(painter);
			}
		}
	}
	
	public void paintSelectedNotes(AbstractPainter painter) {
		for (DisplayObject currentNote : this.selectedNotes) {
			currentNote.paint(painter);
		}
	}
	
	private void paintSelectedAnchorNote(AbstractPainter painter) {
		if (this.selectedAnchorNote != null) {
			this.selectedAnchorNote.paintAnchorSelection(painter);
		}
	}

	public void modelPropertyChange(PropertyChangeEvent event) {
		String propertyName = event.getPropertyName();
		if (propertyName.equals(ViewController.LAYERS)) {
			this.updateVisibility((LayerStates)event.getNewValue());
		} else if (propertyName.equals(ViewController.TOGGLE_MOD_FILTER)) {
			boolean filterOff = !(Boolean)event.getNewValue();
			if (filterOff) {
				this.makeAllModulatorsVisible();
			}
		} else if (propertyName.equals(ViewController.MOD_FILTER_VALUES)) {
			int[] selectedMods = (int[])event.getNewValue();
			this.updateModulatorVisibility(selectedMods[0], selectedMods[1]);
		}
	}

}
