

package planarviewer.client;


class GPlanarPanoramicZoomLevel {

   public static final int TILE_WIDTH  = 256;
   public static final int TILE_HEIGHT = 256;


   private int             _level;

   private int             _width;

   private int             _height;

   private int             _widthInTiles;

   private int             _heightInTiles;


   GPlanarPanoramicZoomLevel() {
      // empty constructor for GSON deserialization
   }


   //   GPlanarPanoramicZoomLevel(final int level,
   //                             final int width,
   //                             final int height,
   //                             final int tileWidth,
   //                             final int tileHeight) {
   //      _level = level;
   //
   //      _width = width;
   //      _height = height;
   //
   //      int widthInTiles = _width / tileWidth;
   //      if ((widthInTiles * tileWidth) < _width) {
   //         widthInTiles++;
   //      }
   //      _widthInTiles = widthInTiles;
   //
   //      int heightInTiles = _height / tileHeight;
   //      if ((heightInTiles * tileHeight) < _height) {
   //         heightInTiles++;
   //      }
   //      _heightInTiles = heightInTiles;
   //   }


   GPlanarPanoramicZoomLevel(final int level,
                             final int width,
                             final int height,
                             final int widthInTiles,
                             final int heightInTiles) {
      _level = level;

      _width = width;
      _height = height;

      _widthInTiles = widthInTiles;
      _heightInTiles = heightInTiles;
   }


   public int getLevel() {
      return _level;
   }


   public int getWidth() {
      return _width;
   }


   public int getHeight() {
      return _height;
   }


   public int getWidthInTiles() {
      return _widthInTiles;
   }


   public int getHeightInTiles() {
      return _heightInTiles;
   }


   @Override
   public String toString() {
      return "Level=" + _level + ", Pixels=" + _width + "x" + _height + ", " + ((float) (_width * _height) / 1024 / 1024)
             + "Mpx, Tiles=" + _widthInTiles + "x" + _heightInTiles;
   }


}