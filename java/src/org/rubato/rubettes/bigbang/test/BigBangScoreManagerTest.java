package org.rubato.rubettes.bigbang.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.rubato.base.RubatoException;
import org.rubato.math.matrix.RMatrix;
import org.rubato.math.module.morphism.ModuleMorphism;
import org.rubato.math.module.morphism.RFreeAffineMorphism;
import org.rubato.math.yoneda.Denotator;
import org.rubato.math.yoneda.LimitDenotator;
import org.rubato.math.yoneda.PowerDenotator;
import org.rubato.rubettes.bigbang.controller.BigBangController;
import org.rubato.rubettes.bigbang.model.BigBangScoreManager;
import org.rubato.rubettes.bigbang.model.BigBangTransformation;
import org.rubato.rubettes.bigbang.model.TransformationProperties;
import org.rubato.rubettes.util.DenotatorPath;

public class BigBangScoreManagerTest extends TestCase {
	
	private final int[][] NODE_COORDINATE_PATHS = new int[][]{{0,0},{0,1},{0,0},{0,1}};
	private final int[][] NOTE_COORDINATE_PATHS = new int[][]{{0},{1},{0},{1}};
	
	private BigBangScoreManager scoreManager;
	private TestObjects objects;
	
	protected void setUp() {
		this.objects = new TestObjects();
		this.scoreManager = this.objects.scoreManager;
	}
	
	/*TODO: adapt!!! public void testAddNote() {
		DenotatorPath nodePath = this.scoreManager.addObject(this.objects.NOTE0_VALUES);
		TestCase.assertEquals(nodePath, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0}));
		nodePath = this.scoreManager.addObject(this.objects.NOTE2_ABSOLUTE_VALUES);
		TestCase.assertEquals(nodePath, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1,0}));
		nodePath = this.scoreManager.addObject(this.objects.NOTE1_ABSOLUTE_VALUES);
		TestCase.assertEquals(nodePath, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1,0}));
		PowerDenotator composition = (PowerDenotator)this.scoreManager.getComposition();
		TestCase.assertTrue(composition.getFactorCount() == 3);
	}*/
	
	public void testAddNotes() {
		//addNodes
		this.scoreManager.setComposition(this.objects.multiLevelMacroScore);
		List<DenotatorPath> anchorPaths = new ArrayList<DenotatorPath>();
		anchorPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{}));
		anchorPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0}));
		anchorPaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,0}));
		List<Denotator> notes = new ArrayList<Denotator>();
		notes.add(this.objects.node1Absolute);
		notes.add(this.objects.node2Absolute);
		notes.add(this.objects.node0);
		List<DenotatorPath> satellitePaths = this.scoreManager.addObjects(notes, anchorPaths);
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), satellitePaths.get(0));
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,1}), satellitePaths.get(1));
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0}), satellitePaths.get(2));
	}
	
	public void testMapNodesFlat() {
		this.scoreManager.setComposition(this.objects.flatMacroScore);
		BigBangTransformation translation = this.makeTranslation(-1,-2, this.NODE_COORDINATE_PATHS);
		List<DenotatorPath> notePaths = this.makeNotePaths(new int[]{0}, new int[]{2});
		List<DenotatorPath> newPaths = this.scoreManager.mapNodes(notePaths, translation, false);
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), newPaths.get(0));
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), newPaths.get(1));
	}
	
	public void testMapNodesFlatSequential() {
		this.scoreManager.setComposition(this.objects.flatMacroScore);
		BigBangTransformation translation = this.makeTranslation(3,5, this.NODE_COORDINATE_PATHS);
		List<DenotatorPath> notePaths = this.makeNotePaths(new int[]{0}, new int[]{2});
		/*List<List<LimitDenotator>> notes = this.scoreManager.getNotes(notePaths);
		TestCase.assertTrue(notes.size() == 2);
		List<NotePath> retrievedPaths = this.scoreManager.getNotePaths(notes);
		System.out.println(retrievedPaths);*/
		List<DenotatorPath> newPaths = new ArrayList<DenotatorPath>(this.scoreManager.mapNodes(notePaths, translation, false));
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), newPaths.get(0));
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2}), newPaths.get(1));
		/*retrievedPaths = this.scoreManager.getNotePaths(notes);
		System.out.println(retrievedPaths);
		TestCase.assertTrue(retrievedPaths.equals(newPaths));*/
	}
	
	public void testMapNodesMultiLevel() throws RubatoException {
		this.objects.multiLevelMacroScore.appendFactor(this.objects.generator.createNodeDenotator(this.objects.note2Absolute));
		this.scoreManager.setComposition(this.objects.multiLevelMacroScore);
		BigBangTransformation translation = this.makeTranslation(-2, -1, this.NODE_COORDINATE_PATHS);
		List<DenotatorPath> nodePaths = this.makeNotePaths(new int[]{1}, new int[]{0,1,0});
		List<DenotatorPath> newPaths = new ArrayList<DenotatorPath>(this.scoreManager.mapNodes(nodePaths, translation, false));
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), newPaths.get(0));
		TestCase.assertEquals(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1,1,0}), newPaths.get(1));
		LimitDenotator expectedNode = this.objects.generator.createNodeDenotator(new double[]{-1,2,-4,0,0,0});
		this.assertEqualNodes(expectedNode, this.scoreManager.getComposition().get(new int[]{1,1,0}));
		
		nodePaths = this.makeNotePaths(new int[]{0}, new int[]{1,1,0});
		newPaths = new ArrayList<DenotatorPath>(this.scoreManager.mapNodes(nodePaths, translation, false));
		TestCase.assertEquals(newPaths.get(0), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		TestCase.assertEquals(newPaths.get(1), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1,1,0}));
		expectedNode = this.objects.generator.createNodeDenotator(new double[]{-3,1,-4,0,0,0});
		this.assertEqualNodes(expectedNode, this.scoreManager.getComposition().get(new int[]{1,1,0}));
	}
	
	public void testMapModulators() throws RubatoException {
		this.scoreManager.setComposition(this.objects.flatMacroScore);
		List<DenotatorPath> paths = new ArrayList<DenotatorPath>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		//build modulator structure
		this.scoreManager.moveObjectsToParent(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), 1);
		this.scoreManager.moveObjectsToParent(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0,6,0}), 0);
		
		BigBangTransformation translation = this.makeTranslation(-2, -1, this.NOTE_COORDINATE_PATHS);
		List<DenotatorPath> nodePaths = this.makeNotePaths(new int[]{0,0,6,0,6,0});
		List<DenotatorPath> newPaths = this.scoreManager.mapNodes(nodePaths, translation, false);
		TestCase.assertEquals(newPaths.get(0), new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0,6,0,6,0}));
		LimitDenotator expectedNote = this.objects.generator.createNoteDenotator(new double[]{-1,-4,5,0,1,0});
		this.assertEqualNotes(expectedNote, this.scoreManager.getComposition().get(new int[]{0,0,6,0,6,0}));
	}
	
	public void testCopyAndMapDenotator() {
		this.scoreManager.setComposition(this.objects.flatMacroScore);
		TreeSet<DenotatorPath> nodePaths = new TreeSet<DenotatorPath>();
		nodePaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}));
		nodePaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		int[][] coordinatePaths = new int[][]{{0},{1},{0},{1}};
		/*Set<DenotatorPath> copyPaths = this.score.copyAndMapNotes(nodePaths, translation, coordinatePaths);
		TestCase.assertTrue(copyPaths.size() == 2);
		//TestCase.assertTrue(copyPaths.first().equals(new DenotatorPath(new int[]{3})));
		//TestCase.assertTrue(copyPaths.last().equals(new DenotatorPath(new int[]{4})));
		TestCase.assertTrue(this.score.getComposition().getFactorCount() == 5);
		//TestCase.assertEquals(this.score.getComposition().get(new int[]{0}), this.node0);
		//this.printNode(this.score.getComposition().get(new int[]{4}));
		//TestCase.assertEquals(this.score.getComposition().get(new int[]{1}), this.node1Absolute);
		//this.checkEquals(this.score.getComposition().get(new int[]{2}), this.node2Absolute);
		//this.checkEquals(this.score.getComposition().get(new int[]{3}), this.node0);
		//this.checkEquals(this.score.getComposition().get(new int[]{4}), this.node2Absolute);
		*/
		/*?????LimitDenotator node = this.node2Absolute = this.generator.createNodeDenotator(new double[]{2,60,121,1,1,0});
		LimitDenotator node2 = this.generator.createNodeDenotator(new double[]{2,60,121,1,1,0});
		TestCase.assertTrue(!node.equals(node2));
		PowerDenotator ms = this.generator.createEmptyScore();
		ms.appendFactor(node);
		ms.appendFactor(node2);
		TestCase.assertTrue(ms.getFactorCount() == 2);
		ms = ms.copy();
		TestCase.assertTrue(ms.getFactorCount() == 2);*/
	}
	
	public void testGetAbsoluteNode() {
		this.scoreManager.setComposition(this.objects.multiLevelMacroScore);
		DenotatorPath nodePath = new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0});
		//LimitDenotator absoluteNode = this.scoreManager.getComposition().getAbsoluteNode(nodePath);
		//TestCase.assertEquals(absoluteNode, this.node2Absolute);
	}
	
	public void testBuildSatellites() throws RubatoException {
		this.scoreManager.setComposition(this.objects.flatMacroScore);
		List<DenotatorPath> paths = new ArrayList<DenotatorPath>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1,0}));
		//build first satellite and check if it's there
		this.scoreManager.moveObjectsToParent(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), 0);
		this.assertEqualNodes(this.objects.node1Relative, this.scoreManager.getComposition().get(new int[]{0,1,0}));
		//build second satellite and check if it's there
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,1,0,1})).getFactorCount() == 0);
		List<DenotatorPath> satellitePaths = this.scoreManager.moveObjectsToParent(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0}), 0);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,1,0,1})).getFactorCount() == 1);
		this.assertEqualNodes(this.objects.node2Relative, this.scoreManager.getComposition().get(new int[]{0,1,0,1,0}));
		//undo and check if original is there again
		this.scoreManager.undoMoveToParent(new ArrayList<DenotatorPath>(satellitePaths), paths);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,1,0,1})).getFactorCount() == 0);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition()).getFactorCount() == 2);
		this.assertEqualNodes(this.objects.node0, this.scoreManager.getComposition().get(new int[]{0}));
		this.assertEqualNodes(this.objects.node2Absolute, this.scoreManager.getComposition().get(new int[]{1}));
		this.assertEqualNodes(this.objects.node1Relative, this.scoreManager.getComposition().get(new int[]{0,1,0}));
	}
	
	public void testBuildModulators() throws RubatoException {
		this.scoreManager.setComposition(this.objects.flatMacroScore);
		List<DenotatorPath> paths = new ArrayList<DenotatorPath>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}));
		//build first modulator and check if it's there
		this.scoreManager.moveObjectsToParent(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0}), 1);
		this.assertEqualNotes(this.objects.note1Relative, this.scoreManager.getComposition().get(new int[]{0,0,6,0}));
		//build second modulator and check if it's there
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,0,6,0,6})).getFactorCount() == 0);
		List<DenotatorPath> modulatorPaths = this.scoreManager.moveObjectsToParent(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0,6,0}), 0);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,0,6,0,6})).getFactorCount() == 1);
		Denotator addedModulator = this.scoreManager.getComposition().get(new int[]{0,0,6,0,6,0});
		TestCase.assertEquals(this.objects.generator.SOUND_NOTE_FORM, addedModulator.getForm());
		this.assertEqualNotes(this.objects.note2Relative, this.scoreManager.getComposition().get(new int[]{0,0,6,0,6,0}));
		//undo and check if original is there again
		this.scoreManager.undoMoveToParent(new ArrayList<DenotatorPath>(modulatorPaths), paths);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,0,6,0,6})).getFactorCount() == 0);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition()).getFactorCount() == 2);
		this.assertEqualNodes(this.objects.node0, this.scoreManager.getComposition().get(new int[]{0}));
		this.assertEqualNodes(this.objects.node2Absolute, this.scoreManager.getComposition().get(new int[]{1}));
		this.assertEqualNotes(this.objects.note1Relative, this.scoreManager.getComposition().get(new int[]{0,0,6,0}));
		//try to add a note with a modulator as a satellite
		paths = new ArrayList<DenotatorPath>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0}));
		this.scoreManager.moveObjectsToParent(paths, new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1}), 0);
		this.assertEqualNodes(this.objects.node2Absolute, this.scoreManager.getComposition().get(new int[]{0}));
		this.assertEqualNodes(this.objects.generator.createNodeDenotator(new double[]{-2,0,-1,0,-1,0}), this.scoreManager.getComposition().get(new int[]{0,1,0}));
		this.assertEqualNotes(this.objects.note1Relative, this.scoreManager.getComposition().get(new int[]{0,1,0,0,6,0}));
	}
	
	public void testFlatten() throws RubatoException {
		this.objects = new TestObjects();
		this.scoreManager = new BigBangScoreManager(new BigBangController());
		this.scoreManager.setComposition(this.objects.multiLevelMacroScore);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,1})).getFactorCount() == 1);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,1,0,1})).getFactorCount() == 1);
		TreeSet<DenotatorPath> paths = new TreeSet<DenotatorPath>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0,1,0,0}));
		this.scoreManager.flattenNotes(paths);
		TestCase.assertTrue(((PowerDenotator)this.scoreManager.getComposition().get(new int[]{0,1})).getFactorCount() == 2);
	}
	
	public void testFlatten2() throws RubatoException {
		this.scoreManager.setComposition(this.objects.multiLevelMacroScore);
		TreeSet<DenotatorPath> paths = new TreeSet<DenotatorPath>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,1,0}));
		//flatten the first path
		Map<DenotatorPath,DenotatorPath> newAndOldPaths = this.scoreManager.flattenNotes(paths);
		this.assertEqualNodes(this.objects.node0, this.scoreManager.getComposition().get(new int[]{0}));
		this.assertEqualNodes(this.objects.node1Absolute, this.scoreManager.getComposition().get(new int[]{1}));
		this.assertEqualNodes(this.objects.node2Relative, this.scoreManager.getComposition().get(new int[]{1,1,0}));
		this.scoreManager.unflattenNotes(newAndOldPaths);
		this.assertEqualNodes(this.objects.node0, this.scoreManager.getComposition().get(new int[]{0}));
		this.assertEqualNodes(this.objects.node1Relative, this.scoreManager.getComposition().get(new int[]{0,1,0}));
		this.assertEqualNodes(this.objects.node2Relative, this.scoreManager.getComposition().get(new int[]{0,1,0,1,0}));
	}
	
	public void testShapeNotes() throws RubatoException {
		this.scoreManager.setComposition(this.objects.flatMacroScore);
		Set<DenotatorPath> paths = new TreeSet<DenotatorPath>();
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{0,0}));
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{1,0}));
		paths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, new int[]{2,0}));
		int[][] elementPaths = new int[][]{{0},{1}};
		TransformationProperties properties = new TransformationProperties(paths, elementPaths, false, false, false);
		TreeMap<Double,Double> shapingLocations = new TreeMap<Double,Double>();
		shapingLocations.put(-0.3, 69.0);
		shapingLocations.put(0.5, 71.0);
		shapingLocations.put(4.0, 99.0);
		this.scoreManager.shapeNotes(properties, shapingLocations);
		
		LimitDenotator expectedNote = this.objects.generator.createNodeDenotator(new double[]{0,69,120,1,0,0});
		this.assertEqualNodes(expectedNote, this.scoreManager.getComposition().get(new int[]{0}));
		expectedNote = this.objects.generator.createNodeDenotator(new double[]{1,71,116,1,0,0});
		this.assertEqualNodes(expectedNote, this.scoreManager.getComposition().get(new int[]{1}));
		expectedNote = this.objects.generator.createNodeDenotator(new double[]{2,60,121,1,1,0});
		this.assertEqualNodes(expectedNote, this.scoreManager.getComposition().get(new int[]{2}));
	}
	
	private BigBangTransformation makeTranslation(int x, int y, int[][] paths) {
		RMatrix identity = new RMatrix(new double[][]{{1,0},{0,1}});
		ModuleMorphism translation = RFreeAffineMorphism.make(identity, new double[]{x, y});
		return new BigBangTransformation(translation, paths, false, null);
	}
	
	private List<DenotatorPath> makeNotePaths(int[]... intNotePaths) {
		List<DenotatorPath> notePaths = new ArrayList<DenotatorPath>();
		for (int[] currentPath: intNotePaths) {
			notePaths.add(new DenotatorPath(this.objects.SOUND_SCORE_FORM, currentPath));
		}
		return notePaths;
	}
	
	private void assertEqualNodes(Denotator node1, Denotator node2) throws RubatoException {
		for (int i = 0; i < 6; i++) {
			TestCase.assertEquals(node1.get(new int[]{0,i}).getCoordinate().getMap(), node2.get(new int[]{0,i}).getCoordinate().getMap());
		}
	}
	
	private void assertEqualNotes(Denotator note1, Denotator note2) throws RubatoException {
		for (int i = 0; i < 6; i++) {
			TestCase.assertEquals(note1.get(new int[]{i}).getCoordinate().getMap(), note2.get(new int[]{i}).getCoordinate().getMap());
		}
	}
	
	@SuppressWarnings("unused")
    private void printNote(Denotator note) throws RubatoException {
		for (int i = 0; i < 7; i++) {
			System.out.println(note.getElement(new int[]{i,0}));
		}
	}

}
