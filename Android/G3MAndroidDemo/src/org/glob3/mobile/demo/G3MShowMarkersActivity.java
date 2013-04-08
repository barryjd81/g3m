

package org.glob3.mobile.demo;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.JSONParser_Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;


public class G3MShowMarkersActivity
         extends
            Activity {

   MarksRenderer             _weatherMarkers = new MarksRenderer(false);
   private G3MWidget_Android _widgetAndroid;
   private boolean           _WeatherMarkerIsDone;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.bar_glob3_template);
      final G3MBuilder_Android builder = new G3MBuilder_Android(getApplicationContext());
      builder.setInitializationTask(getWeatherMarkerLayersTask());
      builder.addRenderer(_weatherMarkers);
      //Always after setting params
      _widgetAndroid = builder.createWidget();
      final LinearLayout g3mLayout = (LinearLayout) findViewById(R.id.glob3);
      g3mLayout.addView(_widgetAndroid);

   }


   private GInitializationTask getWeatherMarkerLayersTask() {
      final GInitializationTask initializationTask = new GInitializationTask() {

         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new IBufferDownloadListener() {


               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer) {

                  final String response = buffer.getAsString();
                  final IJSONParser parser = new JSONParser_Android();
                  final JSONBaseObject jsonObject = parser.parse(response);
                  final JSONObject object = jsonObject.asObject();
                  final JSONArray list = object.getAsArray("list");
                  for (int i = 0; i < list.size(); i++) {

                     final JSONObject city = list.getAsObject(i);

                     final JSONObject coords = city.getAsObject("coord");
                     final Geodetic2D position = new Geodetic2D(Angle.fromDegrees(coords.getAsNumber("lat").value()),
                              Angle.fromDegrees(coords.getAsNumber("lon").value()));
                     final JSONArray weather = city.getAsArray("weather");
                     final JSONObject weatherObject = weather.getAsObject(0);


                     String icon = "";
                     if (weatherObject.getAsString("icon", "DOUBLE").equals("DOUBLE")) {
                        icon = "" + (int) weatherObject.getAsNumber("icon").value() + "d.png";
                        if (icon.length() < 7) {
                           icon = "0" + icon;
                        }
                     }
                     else {
                        icon = weatherObject.getAsString("icon", "DOUBLE") + ".png";
                     }


                     _weatherMarkers.addMark(new Mark( //
                              city.getAsString("name", ""), //
                              new URL("http://openweathermap.org/img/w/" + icon, false), //
                              new Geodetic3D(position, 0), //
                              0, //
                              true, //
                              14));
                  }


                  _WeatherMarkerIsDone = true;
               }


               @Override
               public void onError(final URL url) {
                  Toast.makeText(getApplicationContext(), "Error retrieving  weather data", Toast.LENGTH_SHORT).show();

               }


               @Override
               public void onCancel(final URL url) {
                  //DO Nothing
               }


               @Override
               public void onCanceledDownload(final URL url,
                                              final IByteBuffer data) {
                  //Do Nothing
               }
            };

            downloader.requestBuffer(new URL("http://openweathermap.org/data/2.1/find/city?bbox=-80,-180,80,180,4&cluster=yes",
                     false), 0, TimeInterval.fromHours(1.0), listener, false);
         }


         @Override
         public boolean isDone(final G3MContext context) {
            if (_WeatherMarkerIsDone) {
               _widgetAndroid.setAnimatedCameraPosition(new Geodetic3D(Angle.fromDegrees(45d), Angle.fromDegrees(0.d), 3000000),
                        TimeInterval.fromSeconds(3));
               return true;
            }
            return false;
         }
      };
      return initializationTask;
   }
}