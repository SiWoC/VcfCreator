/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.siwoc.navigation.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cardme.vcard.VCardImpl;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author niekk
 */
public class VcfCreatorTest {
    
    public VcfCreatorTest() {
    }

    /**
     * Test of setPrefix method, of class VcfCreator.
     */
    @Test
    public void testSetPrefix() {
        System.out.println("setPrefix");
        String prefix = "cb";
        VcfCreator instance = new VcfCreator();
        instance.setPrefix(prefix);
        String result = instance.getPrefix();
        assertEquals(prefix, result);
    }

    /**
     * Test of setRouteName method, of class VcfCreator.
     */
    @Test
    public void testSetRouteName() {
        System.out.println("setRouteName");
        String routeName = "11steden";
        VcfCreator instance = new VcfCreator();
        instance.setRouteName(routeName);
        String result = instance.getRouteName();
        assertEquals(routeName, result);
    }

    /**
     * Test of getVCards method, of class VcfCreator.
     */
    @Test
    public void testGetVCards() {
        System.out.println("getVCards");
        VcfCreator instance = new VcfCreator();
        ArrayList expResult = null;
        ArrayList result = instance.getVCards();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setVCards method, of class VcfCreator.
     */
    @Test
    public void testSetVCards() {
        System.out.println("setVCards");
        ArrayList<VCardImpl> vCards = null;
        VcfCreator instance = new VcfCreator();
        instance.setVCards(vCards);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of export method, of class VcfCreator.
     */
    @Test
    public void testExport_File() {
        System.out.println("export");
        File outputFolder = null;
        VcfCreator instance = new VcfCreator();
        List expResult = null;
        List result = instance.export(outputFolder);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of export method, of class VcfCreator.
     */
    @Test
    public void testExport_List_File() {
        System.out.println("export");
        List<VCardImpl> vCards = null;
        File outputFolder = null;
        VcfCreator instance = new VcfCreator();
        List expResult = null;
        List result = instance.export(vCards, outputFolder);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of export method, of class VcfCreator.
     */
    @Test
    public void testExport_3args() {
        System.out.println("export");
        VCardImpl vCard = null;
        File outputFolder = null;
        String filename = "";
        VcfCreator instance = new VcfCreator();
        List expResult = null;
        List result = instance.export(vCard, outputFolder, filename);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tryParseRte method, of class VcfCreator.
     */
    @Test
    public void testTryParseRte() throws Exception {
        System.out.println("tryParseRte");
        File inputFile = null;
        VcfCreator instance = new VcfCreator();
        ArrayList expResult = null;
        ArrayList result = instance.tryParseRte(inputFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tryParseItn method, of class VcfCreator.
     */
    @Test
    public void testTryParseItn() throws Exception {
        System.out.println("tryParseItn");
        File inputFile = null;
        VcfCreator instance = new VcfCreator();
        ArrayList expResult = null;
        ArrayList result = instance.tryParseItn(inputFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tryParseRte2 method, of class VcfCreator.
     */
    @Test
    public void testTryParseRte2() throws Exception {
        System.out.println("tryParseRte2");
        File inputFile = null;
        VcfCreator instance = new VcfCreator();
        ArrayList expResult = null;
        ArrayList result = instance.tryParseRte2(inputFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tryParseGpx method, of class VcfCreator.
     */
    @Test
    public void testTryParseGpx() throws Exception {
        System.out.println("tryParseGpx");
        File inputFile = null;
        VcfCreator instance = new VcfCreator();
        ArrayList expResult = null;
        ArrayList result = instance.tryParseGpx(inputFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tryParseKml method, of class VcfCreator.
     */
    @Test
    public void testTryParseKml() throws Exception {
        System.out.println("tryParseKml");
        File inputFile = new File("testFiles/DAG3Grandalpes.kml");
        VcfCreator instance = new VcfCreator();
        ArrayList expResult = null;
        ArrayList result = instance.tryParseKml(inputFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
