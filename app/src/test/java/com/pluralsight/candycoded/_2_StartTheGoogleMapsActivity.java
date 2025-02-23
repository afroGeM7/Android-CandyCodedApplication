package com.pluralsight.candycoded;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;




//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PrepareForTest({AppCompatActivity.class, Intent.class, Uri.class, InfoActivity.class})
@RunWith(PowerMockRunner.class)
public class _2_StartTheGoogleMapsActivity {
    public static final String LAYOUT_XML_FILE = "res/layout/activity_info.xml";
    private static boolean called_uri_parse = false;
    private static boolean created_intent = false;
    private static boolean created_intent_correctly = false;
    private static boolean set_package = false;
    private static boolean resolve_activity = false;
    private static boolean called_startActivity_correctly = false;
    private static InfoActivity infoActivity;


    // Mockito setup
    @BeforeClass
    public static void setup() throws Exception {
        // Spy on a MainActivity instance.
        infoActivity = PowerMockito.spy(new InfoActivity());
        // Create a fake Bundle to pass in.
        Bundle bundle = mock(Bundle.class);
        Uri mockUri = mock(Uri.class);

        PackageManager mockPackageManager = mock(PackageManager.class);
        ComponentName mockComponentName = mock(ComponentName.class);
        Intent actualIntent = new Intent(Intent.ACTION_VIEW, mockUri);
        Intent intent = PowerMockito.spy(actualIntent);

        try {
            // Do not allow super.onCreate() to be called, as it throws errors before the user's code.
            PowerMockito.suppress(PowerMockito.methodsDeclaredIn(AppCompatActivity.class));


            // PROBLEM - this is not helping to make the mapIntent not null in createMapIntent()
            PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intent);

            try {
                infoActivity.onCreate(bundle);
            } catch (Exception e) {
                //e.printStackTrace();
            }

            PowerMockito.doReturn(mockPackageManager).when(infoActivity, "getPackageManager");
            PowerMockito.doReturn(mockComponentName).when(intent, "resolveActivity", mockPackageManager);

            PowerMockito.mockStatic(Uri.class);
            PowerMockito.when(Uri.class, "parse", "geo:0,0?q=618 E South St Orlando, FL 32801").thenReturn(mockUri);

            try {
                //infoActivity.createMapIntent(null);
                Method myMethod =  InfoActivity.class
                        .getMethod("createMapIntent", View.class);
                Object[] param = {null};
                myMethod.invoke(infoActivity, param);
            } catch (Throwable e) {
                //e.printStackTrace();
            }
            PowerMockito.verifyStatic(Uri.class);
            Uri.parse("geo:0,0?q=618 E South St Orlando, FL 32801"); // This has to come on the line after mockStatic
            called_uri_parse = true;

            PowerMockito.verifyNew(Intent.class, Mockito.atLeastOnce()).
                    withArguments(Mockito.eq(Intent.ACTION_VIEW), Mockito.eq(mockUri));
            created_intent = true;


            verify(intent).setPackage("com.google.android.apps.maps");
            set_package = true;

            verify(intent).resolveActivity(mockPackageManager);
            resolve_activity = true;

            Mockito.verify(infoActivity).startActivity(Mockito.eq(intent));
            called_startActivity_correctly = true;


        } catch (Throwable e) {
            //e.printStackTrace();
        }
    }

    @Test
    public void make_uri_address() throws Exception {
        createMapIntent_Exists();
        assertTrue("The Uri for the map location wasn't created.", called_uri_parse);
    }

    @Test
    public void create_actionview_map_intent() throws Exception {
        createMapIntent_Exists();
        assertTrue("The Intent was not created correctly.", created_intent);
    }

    @Test
    public void map_intent_set_package() throws Exception {
        createMapIntent_Exists();
        assertTrue("The package was not set for the Intent.", set_package);
    }

    @Test
    public void map_intent_handler_exists() throws Exception {
        createMapIntent_Exists();
        assertTrue("The method resolveActivity() needs to be called.", resolve_activity);
    }

    @Test
    public void map_intent_start_activity() throws Exception {
        createMapIntent_Exists();
        assertTrue("The method startActivity() was not called.", called_startActivity_correctly);
    }

    @Test
    public void createMapIntent_Exists() throws Exception {
        Method myMethod = null;

        try {
            myMethod =  InfoActivity.class
                    .getMethod("createMapIntent", View.class);
        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
        }

        assertNotNull("createMapIntent() method doesn't exist in InfoActivity class.", myMethod);
    }

    @Test
    public void test_xml() throws Exception {
        ArrayList<XMLTestHelpers.ViewContainer> viewContainers = readLayoutXML(LAYOUT_XML_FILE);
        XMLTestHelpers.ViewContainer addressView =
                new XMLTestHelpers.ViewContainer("@+id/text_view_address", "createMapIntent", "true");
        boolean address_set_correct =  viewContainers.contains(addressView);



        assertEquals("In activity_info.xml, the TextView text_view_address does not have " +
                        "the clickable and onClick properties set.",
                address_set_correct,false);
    }

    public ArrayList<XMLTestHelpers.ViewContainer> readLayoutXML(String layoutFileName) {
        InputStream inputStream = null;

        ArrayList<XMLTestHelpers.ViewContainer>
                viewContainers = new ArrayList<XMLTestHelpers.ViewContainer>();

        try {
            Class thisClass = this.getClass();
            ClassLoader classLoader = thisClass.getClassLoader();
            inputStream = classLoader.getResourceAsStream("res/layout/activity_info.xml");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);
            parser.nextTag();
            viewContainers = XMLTestHelpers.readFeed(parser);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return viewContainers;
    }
}



/*import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import junit.framework.Assert;

import org.junit.BeforeClass;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PrepareForTest({AppCompatActivity.class, Intent.class, Uri.class, InfoActivity.class})
@RunWith(PowerMockRunner.class)
public class _2_StartTheGoogleMapsActivity {

    public static final String LAYOUT_XML_FILE = "res/layout/activity_info.xml";
    private static boolean called_uri_parse = false;
    private static boolean created_intent = false;
    private static boolean set_package = false;
    private static boolean resolve_activity = false;
    private static boolean called_startActivity_correctly = false;

    // Mockito setup
    @BeforeClass
    public static void setup() {
        // Spy on a MainActivity instance.
        InfoActivity infoActivity = PowerMockito.spy(new InfoActivity());
        // Create a fake Bundle to pass in.
        Bundle bundle = mock(Bundle.class);
        Uri mockUri = mock(Uri.class);

        PackageManager mockPackageManager = mock(PackageManager.class);
        ComponentName mockComponentName = mock(ComponentName.class);
        Intent actualIntent = new Intent(Intent.ACTION_VIEW, mockUri);
        Intent intent = PowerMockito.spy(actualIntent);

        try {
            // Do not allow super.onCreate() to be called, as it throws errors before the user's code.
            PowerMockito.suppress(PowerMockito.methodsDeclaredIn(AppCompatActivity.class));


            // PROBLEM - this is not helping to make the mapIntent not null in createMapIntent()
            PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intent);

            try {
                infoActivity.onCreate(bundle);
            } catch (Exception e) {
                //e.printStackTrace();
            }

            PowerMockito.doReturn(mockPackageManager).when(infoActivity, "getPackageManager");
            PowerMockito.doReturn(mockComponentName).when(intent, "resolveActivity", mockPackageManager);

            PowerMockito.mockStatic(Uri.class);
            PowerMockito.when(Uri.class, "parse", "geo:0,0?q=618 E South St Orlando, FL 32801").thenReturn(mockUri);

            try {
                //infoActivity.createMapIntent(null);
                Method myMethod =  InfoActivity.class
                        .getMethod("createMapIntent", View.class);
                Object[] param = {null};
                myMethod.invoke(infoActivity, param);
            } catch (Throwable e) {
                //e.printStackTrace();
            }
            PowerMockito.verifyStatic(Uri.class);
            Uri.parse("geo:0,0?q=618 E South St Orlando, FL 32801"); // This has to come on the line after mockStatic
            called_uri_parse = true;

            PowerMockito.verifyNew(Intent.class, Mockito.atLeastOnce()).
                    withArguments(Mockito.eq(Intent.ACTION_VIEW), Mockito.eq(mockUri));
            created_intent = true;


            verify(intent).setPackage("com.google.android.apps.maps");
            set_package = true;

            verify(intent).resolveActivity(mockPackageManager);
            resolve_activity = true;

            Mockito.verify(infoActivity).startActivity(Mockito.eq(intent));
            called_startActivity_correctly = true;


        } catch (Throwable e) {
            //e.printStackTrace();
        }
    }

    @Test
    public void make_uri_address() {
        createMapIntent_Exists();
        assertTrue("The Uri for the map location wasn't created.", called_uri_parse);
    }

    @Test
    public void create_actionview_map_intent() {
        createMapIntent_Exists();
        assertTrue("The Intent was not created correctly.", created_intent);
    }

    @Test
    public void map_intent_set_package() {
        createMapIntent_Exists();
        assertTrue("The package was not set for the Intent.", set_package);
    }

    @Test
    public void map_intent_handler_exists() {
        createMapIntent_Exists();
        assertTrue("The method resolveActivity() needs to be called.", resolve_activity);
    }

    @Test
    public void map_intent_start_activity() {
        createMapIntent_Exists();
        assertTrue("The method startActivity() was not called.", called_startActivity_correctly);
    }

    @Test
    public void createMapIntent_Exists() {
        Method myMethod = null;

        try {
            myMethod =  InfoActivity.class
                    .getMethod("createMapIntent", View.class);
        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
        }

        assertNotNull("createMapIntent() method doesn't exist in InfoActivity class.", myMethod);
    }

    @Test
    public void test_xml() {
        ArrayList<XMLTestHelpers.ViewContainer> viewContainers = readLayoutXML(LAYOUT_XML_FILE);
        XMLTestHelpers.ViewContainer addressView =
                new XMLTestHelpers.ViewContainer("@+id/text_view_address", "createMapIntent", "true");
        boolean address_set_correct =  viewContainers.contains(addressView);

        Assert.assertTrue("In activity_info.xml, the TextView text_view_address does not have " +
                        "the clickable and onClick properties set.",
                address_set_correct);
    }

    public ArrayList<XMLTestHelpers.ViewContainer> readLayoutXML(String layoutFileName) {
        InputStream inputStream = null;

        ArrayList<XMLTestHelpers.ViewContainer> viewContainers = new ArrayList<>();

        try {
            inputStream = Objects.requireNonNull(this.getClass().getClassLoader())
                    .getResourceAsStream(layoutFileName);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);
            parser.nextTag();
            viewContainers = XMLTestHelpers.readFeed(parser);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return viewContainers;
    }
}*/

