

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileRendererBuilder;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.generated.WidgetUserData;
import org.glob3.mobile.specific.Downloader_Android;
import org.glob3.mobile.specific.G3MBaseActivity;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.SQLiteStorage_Android;
import org.glob3.mobile.specific.ThreadUtils_Android;
import org.glob3.mobile.specific.TileVisitorCache_Android;

import android.os.Bundle;


public class G3MSimpleTileCacheExampleActivity
    extends
      G3MBaseActivity {

  private G3MWidget_Android _widgetAndroid = null;


  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    _widgetAndroid = new G3MWidget_Android(this);
    final IStorage storage = new SQLiteStorage_Android("g3m.cache", this);

    final TimeInterval connectTimeout = TimeInterval.fromSeconds(20);
    final TimeInterval readTimeout = TimeInterval.fromSeconds(30);
    final boolean saveInBackground = true;

    final IDownloader downloader = new CachedDownloader( //
        new Downloader_Android(8, connectTimeout, readTimeout, this), //
        storage, //
        saveInBackground);

    final IThreadUtils threadUtils = new ThreadUtils_Android(_widgetAndroid);

    final Planet planet = Planet.createEarth();

    final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
    cameraConstraints.add(new SimpleCameraConstrainer());

    final CameraRenderer cameraRenderer = new CameraRenderer();
    final boolean useInertia = true;
    cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
    final boolean processRotation = true;
    final boolean processZoom = true;
    cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation,
        processZoom));
    cameraRenderer.addHandler(new CameraRotationHandler());
    cameraRenderer.addHandler(new CameraDoubleTapHandler());

    final CompositeRenderer mainRenderer = new CompositeRenderer();

    // Añadir todas las capas que se quieren cachear
    final LayerSet layerSet = new LayerSet();
    final WMSLayer osm = new WMSLayer( //
        "osm_auto:all", //
        new URL("http://129.206.228.72/cached/osm", false), //
        WMSServerVersion.WMS_1_1_0, //
        Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
        "image/jpeg", //
        "EPSG:4326", //
        "", //
        false, //
        null, //
        TimeInterval.fromDays(30));
    layerSet.addLayer(osm);

    final TileRendererBuilder tlBuilder = new TileRendererBuilder();
    tlBuilder.setLayerSet(layerSet);
    final TileRenderer tileRenderer = tlBuilder.create();
    mainRenderer.addRenderer(tileRenderer);

    final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();

    final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2,
        1);

    final boolean logFPS = true;

    final boolean logDownloaderStatistics = false;

    // PRECACHING
    final GInitializationTask initializationTask = new GInitializationTask() {
      @Override
      public void run(final G3MContext ctx) {
        // Especificamos el tipo de cacheo que se quiere hacer, indicando el
        // ancho y alto de los tiles
        tileRenderer.acceptTileVisitor(new TileVisitorCache_Android(ctx,
            layerSet, 256, 256));

        // Cacheamos los 2 primeros niveles de todas las capas
        tileRenderer.visitTilesTouchesWith(Sector.fullSphere(), 0, 2);

        // Cacheamos el sector que nos interese de todas las capas
        tileRenderer.visitTilesTouchesWith(
            new Sector(new Geodetic2D(Angle.fromDegrees(39.31),
                Angle.fromDegrees(-6.72)), new Geodetic2D(
                Angle.fromDegrees(39.38), Angle.fromDegrees(-6.64))), 2, 14);


        ctx.getLogger().logInfo(
            "SE HA TERMINADO DE CACHEAR LOS SECTORES ESPECIFICADOS.");
      }


      @Override
      public boolean isDone(final G3MContext context1) {
        return true;
      }
    };

    final boolean autoDeleteInitializationTask = true;

    final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

    final WidgetUserData userData = null;

    _widgetAndroid.initWidget(//
        storage, //
        downloader, //
        threadUtils, //
        planet, //
        cameraConstraints, //
        cameraRenderer, //
        mainRenderer, //
        busyRenderer, //
        backgroundColor, //
        logFPS, //
        logDownloaderStatistics, //
        initializationTask, //
        autoDeleteInitializationTask, //
        periodicalTasks, //
        userData);
    setContentView(_widgetAndroid);
  }


  @Override
  protected G3MWidget_Android getWidgetAndroid() {
    return _widgetAndroid;
  }
}
