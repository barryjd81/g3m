

package org.glob3.mobile.specific;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.InitializationContext;
import org.glob3.mobile.generated.URL;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;


public final class Downloader_WebGL
         extends
            IDownloader {

   final private int                                _maxConcurrentOperationCount;
   private final Map<URL, Downloader_WebGL_Handler> _downloadingHandlers;
   private final Map<URL, Downloader_WebGL_Handler> _queuedHandlers;
   private final Timer                              _timer;
   private final int                                _delayMillis;

   private final String                             _proxy;

   private long                                     _requestIdCounter;
   private long                                     _requestsCounter;
   private long                                     _cancelsCounter;


   public Downloader_WebGL(final int maxConcurrentOperationCount,
                           final int delayMillis,
                           final String proxy) {
      _maxConcurrentOperationCount = maxConcurrentOperationCount;
      _requestIdCounter = 1;
      _requestsCounter = 0;
      _cancelsCounter = 0;
      _downloadingHandlers = new HashMap<URL, Downloader_WebGL_Handler>();
      _queuedHandlers = new HashMap<URL, Downloader_WebGL_Handler>();
      _delayMillis = delayMillis;
      _proxy = proxy;

      final Downloader_WebGL thisDownloader = this;
      _timer = new Timer() {
         @Override
         public void run() {
            if (_downloadingHandlers.size() < _maxConcurrentOperationCount) {
               final Downloader_WebGL_Handler handler = getHandlerToRun();

               if (handler != null) {
                  handler.runWithDownloader(thisDownloader);
               }
            }
            sendRequest();
         }
      };
   }


   @Override
   public void start() {
      sendRequest();
   }


   @Override
   public void stop() {
      _timer.cancel();
   }


   private void sendRequest() {
      _timer.schedule(_delayMillis);
   }


   @Override
   public long requestBuffer(final URL url,
                             final long priority,
                             final IBufferDownloadListener listener,
                             final boolean deleteListener) {

      final long requestId;
      Downloader_WebGL_Handler handler = null;
      final URL proxyUrl = new URL(_proxy + url.getPath(), false);

      _requestsCounter++;
      requestId = _requestIdCounter++;
      handler = _downloadingHandlers.get(proxyUrl);

      if ((handler != null) && !handler.isRequestingImage()) {
         // the URL is being downloaded, just add the new listener
         handler.addListener(listener, priority, requestId);
      }
      else {
         handler = _queuedHandlers.get(proxyUrl);
         if ((handler != null) && !handler.isRequestingImage()) {
            // the URL is queued for future download, just add the new listener
            handler.addListener(listener, priority, requestId);
         }
         else {
            // new handler, queue it
            //            handler = new Downloader_WebGL_HandlerImpl(proxyUrl, listener, priority, requestId);
            handler = GWT.create(Downloader_WebGL_Handler.class);
            handler.init(proxyUrl, listener, priority, requestId);
            _queuedHandlers.put(proxyUrl, handler);
         }
      }

      return requestId;
   }


   @Override
   public long requestImage(final URL url,
                            final long priority,
                            final IImageDownloadListener listener,
                            final boolean deleteListener) {
      final long requestId;
      Downloader_WebGL_Handler handler = null;
      final URL proxyUrl = new URL(_proxy + url.getPath(), false);

      _requestsCounter++;
      requestId = _requestIdCounter++;
      handler = _downloadingHandlers.get(proxyUrl);

      if ((handler != null) && handler.isRequestingImage()) {
         // the URL is being downloaded, just add the new listener
         handler.addListener(listener, priority, requestId);
      }
      else {
         handler = _queuedHandlers.get(proxyUrl);
         if ((handler != null) && handler.isRequestingImage()) {
            // the URL is queued for future download, just add the new listener
            handler.addListener(listener, priority, requestId);
         }
         else {
            // new handler, queue it
            //            handler = new Downloader_WebGL_HandlerImpl(proxyUrl, listener, priority, requestId);
            handler = GWT.create(Downloader_WebGL_Handler.class);
            handler.init(proxyUrl, listener, priority, requestId);
            _queuedHandlers.put(proxyUrl, handler);
         }
      }

      return requestId;
   }


   @Override
   public void cancelRequest(final long requestId) {
      if (requestId < 0) {
         return;
      }

      _cancelsCounter++;

      boolean found = false;
      Iterator<Map.Entry<URL, Downloader_WebGL_Handler>> iter = _queuedHandlers.entrySet().iterator();

      while (iter.hasNext() && !found) {
         final Map.Entry<URL, Downloader_WebGL_Handler> e = iter.next();
         final Downloader_WebGL_Handler handler = e.getValue();

         if (handler.removeListenerForRequestId(requestId)) {
            if (!handler.hasListener()) {
               iter.remove();
            }
            found = true;
         }
      }

      if (!found) {
         iter = _downloadingHandlers.entrySet().iterator();

         while (iter.hasNext() && !found) {
            final Map.Entry<URL, Downloader_WebGL_Handler> e = iter.next();
            final Downloader_WebGL_Handler handler = e.getValue();

            if (handler.cancelListenerForRequestId(requestId)) {
               found = true;
            }
         }
      }

   }


   @Override
   public String statistics() {
      final StringBuilder_WebGL sb = new StringBuilder_WebGL();

      sb.addString("Downloader_WebGL(downloading=");
      sb.addInt(_downloadingHandlers.size());
      sb.addString(", queued=");
      sb.addInt(_queuedHandlers.size());
      sb.addString(", totalRequests=");
      sb.addLong(_requestsCounter);
      sb.addString(", totalCancels=");
      sb.addLong(_cancelsCounter);

      return sb.getString();
   }


   public void removeDownloadingHandlerForUrl(final URL url) {
      _downloadingHandlers.remove(url);
   }


   public Downloader_WebGL_Handler getHandlerToRun() {
      long selectedPriority = -100000000; // TODO: LONG_MAX_VALUE;
      Downloader_WebGL_Handler selectedHandler = null;
      URL selectedURL = null;

      final Iterator<Map.Entry<URL, Downloader_WebGL_Handler>> it = _queuedHandlers.entrySet().iterator();

      while (it.hasNext()) {
         final Map.Entry<URL, Downloader_WebGL_Handler> e = it.next();
         final URL url = e.getKey();
         final Downloader_WebGL_Handler handler = e.getValue();
         final long priority = handler.getPriority();

         if (priority > selectedPriority) {
            selectedPriority = priority;
            selectedHandler = handler;
            selectedURL = url;
         }
      }

      if (selectedHandler != null) {
         // move the selected handler to _downloadingHandlers collection
         _queuedHandlers.remove(selectedURL);
         _downloadingHandlers.put(selectedURL, selectedHandler);
      }

      return selectedHandler;
   }


   @Override
   public void onResume(final InitializationContext ic) {

   }


   @Override
   public void onPause(final InitializationContext ic) {

   }

}
